package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.chuanqi.yz.R;
import java.util.HashMap;
import Constance.constance;
import Utis.SharePre;
import Utis.Utis;
import Utis.UILUtils;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import model.Guide.guidess;
import model.Result;
public class GuideActivity extends BaseActivity {
    private ListView mList;
    private ImageView mImgGuide;
    private RelativeLayout mRtlComplite;
    private TextView mTvNum;
    private int countdown;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (countdown > 1) {
                countdown--;
                mTvNum.setText("" + countdown);
                handler.postDelayed(this, 1000);
            } else {
                handler.removeCallbacks(runnable);
                Complite();
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initview();
        initdate();
        initevent();
    }

    /**
     * 初始化数据
     */
    private void initdate() {
        if(!SharePre.getGuideImgUrl(getApplicationContext()).equals("")){
            UILUtils.displayImageNoAnim(SharePre.getGuideImgUrl(getApplicationContext()),mImgGuide);
        }else {
            HashMap<String,String> map=new HashMap<>();
            OkHttpUtil.getInstance().Get(constance.URL.GUIDE, new OkHttpUtil.FinishListener() {
                @Override
                public void Successfully(boolean IsSuccess, String data, String Msg) {
//                Toast(data.toString());
                    if(IsSuccess){
                        guidess guidess = GsonUtils.parseJSON(data, guidess.class);
                        UILUtils.displayImageNoAnim(guidess.getGgt().get(0).getImgurl(),mImgGuide);
                        if(!SharePre.getGuideImgUrl(getApplicationContext()).equals(guidess.getGgt().get(0).getImgurl())){
                            SharePre.saveGuideImgUrl(getApplicationContext(),guidess.getGgt().get(0).getImgurl());
                        }
                        Log.i("=======引导图",""+guidess.getGgt().get(0).getImgurl());
                    }else {
                        Toast(data.toString());
                    }
                }
            });
        }
    }
    private void initevent() {
        mRtlComplite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                handler.removeCallbacks(runnable);
                Complite();
            }
        });
    }

    private void initview() {
        mImgGuide = (ImageView) findViewById(R.id.img_guide);
        mRtlComplite = (RelativeLayout) findViewById(R.id.rtl_complite);
        mTvNum = (TextView) findViewById(R.id.tv_num);
        countdown=3;
        handler.postDelayed(runnable, 1000);
    }
    public  void Complite(){
        if(!SharePre.getUserId(GuideActivity.this).equals("") ){
                    Intent intent=new Intent(GuideActivity.this,MainActivity.class);
                    startActivity(intent);
                    GuideActivity.this.finish();
        }else {
            IsRegistUser();
        }
    }
    public void IsRegistUser(){
        HashMap<String,String> map=new HashMap<>();
        map.put("udid",""+ Utis.getIMEI(getApplicationContext()));
        OkHttpUtil.getInstance().Post(map, constance.URL.IS_USER, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    Result result = GsonUtils.parseJSON(data, Result.class);
                    if(result.getRun().equals("0")){  //还没注册
                                Intent intent=new Intent(GuideActivity.this,YaoQingActivity.class);
                                startActivity(intent);
                                GuideActivity.this.finish();
                    }else if(result.getRun().equals("1")){    //已经注册
                                Intent intent=new Intent(GuideActivity.this,MainActivity.class);
                                startActivity(intent);
                                GuideActivity.this.finish();
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return false;
    }
}
