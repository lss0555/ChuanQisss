package activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.chuanqi.yz.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import Constance.constance;
import Utis.SharePre;
import Utis.Utis;
import Utis.UILUtils;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import app.BaseApp;
import model.Guide.guidess;
import model.Result;
public class GuideActivity extends BaseActivity {
    private ListView mList;
    private ImageView mImgGuide;
    private RelativeLayout mRtlComplite;
    private TextView mTvNum;
    private static final int READ_PHONE_STATE = 0;
    private int countdown;
    private String guide = "guide.png";
    private Bitmap mBp;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (countdown > 1) {
                countdown--;
                mTvNum.setText("" + countdown+"跳过");
                handler.postDelayed(this, 1000);
            } else {
                handler.removeCallbacks(runnable);
//                Complite();
                IsCanRunning();
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
        if(SharePre.getGuideImgUrl(getApplicationContext())!=null ||!SharePre.getGuideImgUrl(getApplicationContext()).equals("")){
           UILUtils.displayImageNoAnim(SharePre.getGuideImgUrl(getApplicationContext()),mImgGuide);
        }
        getImgUrl();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getImgUrl();
    }

    private void getImgUrl() {
        HashMap<String,String> map=new HashMap<>();
        OkHttpUtil.getInstance().Get(constance.URL.GUIDE, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                Toast(data.toString());
                if(IsSuccess){
                    guidess guidess = GsonUtils.parseJSON(data, guidess.class);
                    String GuiImgUrl=guidess.getGgt().get(0).getImgurl();
                    String GuideCacheImgUrl = SharePre.getGuideImgUrl(getApplicationContext()) ;
//                    File file = new File(BaseApp.getInstance().getBaseFolder() + "/" + guide);
                    if(GuideCacheImgUrl == null || !GuiImgUrl.equalsIgnoreCase(GuiImgUrl) ) {
//                        mBp = returnBitMap(GuiImgUrl);
//                        saveBitmap(mBp);
                        SharePre.saveGuideImgUrl(getApplicationContext(),GuiImgUrl);
                        UILUtils.displayImageNoAnim(GuiImgUrl,mImgGuide);
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
    public Bitmap returnBitMap(final String url) {
        if (url != null && !url.equals("")) {
            URL myFileUrl = null;
            try {
                myFileUrl = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                mBp = BitmapFactory.decodeStream(is);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return mBp;
    }
    /**
     * 保存Bp
     * @param bm
     */
    public void saveBitmap(Bitmap bm) {
        File f = new File(BaseApp.getInstance().getBaseFolder(), guide);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initevent() {
        mRtlComplite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                handler.removeCallbacks(runnable);
                //                Complite();
                IsCanRunning();
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
        if(!SharePre.getUserId(GuideActivity.this).equals("") ||SharePre.getUserId(getApplicationContext())!=null){
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
                                Intent intent=new Intent(GuideActivity.this,RegistActivity.class);
                                startActivity(intent);
                                GuideActivity.this.finish();
                    }else if(result.getRun().equals("1")){   //已经注册
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
   public void IsCanRunning(){
       if (ActivityCompat.checkSelfPermission(GuideActivity.this, Manifest.permission.READ_PHONE_STATE)
               != PackageManager.PERMISSION_GRANTED) {   //没有权限
           ActivityCompat.requestPermissions(GuideActivity.this,new String[] {Manifest.permission.READ_PHONE_STATE},
                   READ_PHONE_STATE);
           return;
       }else {
           //有权限
           Complite();
       }
   }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode ==READ_PHONE_STATE) {
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //TODO拥有权限 y
                Complite();
            } else {  //没有权限
                Toast("抱歉，未开启READ_PHONE_STATE权限");
            }
        }
    }

}
