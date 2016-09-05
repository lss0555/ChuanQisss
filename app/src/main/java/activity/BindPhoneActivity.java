package activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuanqi.yz.R;

import java.util.HashMap;

import Constance.constance;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Utis.SharePre;
import model.Yzm;

public class BindPhoneActivity extends BaseActivity{
    private Yzm mYzm;
    private TextView mTvSend;
    private EditText mEtPassword;
    private EditText mEtYzm;
    private EditText mEtPhone;
    private int countdown;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (countdown > 1) {
                countdown--;
                mTvSend.setText("重发（" + countdown + "）");
                mTvSend.setBackground(getResources().getDrawable(R.drawable.round_gray_bg));
                handler.postDelayed(this, 1000);
            } else {
                mTvSend.setText("获取验证码");
                mTvSend.setClickable(true);
                mTvSend.setBackground(getResources().getDrawable(R.drawable.round_red_bg));
                handler.removeCallbacks(runnable);
            }
        }
    };
    private RelativeLayout mRtlComplite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_bind);
        initview();
        initevent();
    }

    /**
     * 发送验证码
     */
    private void initevent() {
        mTvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEtPhone.getText().toString().trim().equals("")){
                    showTip("提示:请输入您的手机号码");
                }else {
                    countdown = 60;
                    handler.postDelayed(runnable, 1000);
                    mTvSend.setClickable(false);
                    HashMap<String,String>   map=new HashMap<String, String>();
                    map.put("mobile",mEtPhone.getText().toString().trim());
                    map.put("userid", SharePre.getUserId(getApplicationContext()));
                    OkHttpUtil.getInstance().Post(map, constance.URL.GET_YZM, new OkHttpUtil.FinishListener() {
                        @Override
                        public void Successfully(boolean IsSuccess, String data, String Msg) {
                            mYzm= GsonUtils.parseJSON(data,Yzm.class);

                        }
                    });
                }
            }
        });
        mRtlComplite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 HashMap<String,String> map=new HashMap<String, String>();
                 map.put("userid",SharePre.getUserId(getApplicationContext()));
                 map.put("mobile",mEtPhone.getText().toString().trim());
                 OkHttpUtil.getInstance().Post(map, constance.URL.BIND_PHONE, new OkHttpUtil.FinishListener() {
                     @Override
                     public void Successfully(boolean IsSuccess, String data, String Msg) {
                         if(IsSuccess){
                             Yzm yzm = GsonUtils.parseJSON(data, Yzm.class);
                             if(yzm.getRun().equals("0")){
                                 if(mEtYzm.getText().toString().trim().equals(mYzm.getRun())){
                                     showTip("恭喜您，绑定成功");
                                     finish();
                                 }else {
                                     showErrorTip("抱歉，您的验证码有误");
                                 }
                             }else {
                                 showErrorTip("抱歉，该手机已绑定过");
                             }
                         }else {
                             Toast(data.toString());
                         }
                     }
                 });
            }
        });
    }
    private void initview() {
        mTvSend = (TextView) findViewById(R.id.tv_send);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtYzm = (EditText) findViewById(R.id.et_yzm);
        mRtlComplite = (RelativeLayout) findViewById(R.id.rtl_complite);
    }
}
