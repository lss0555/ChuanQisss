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
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuanqi.yz.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import Constance.constance;
import Utis.SharePre;
import Utis.UILUtils;
import Utis.Utis;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Views.GifView;
import model.Guide.guidess;
import model.Result;
import model.UserInfo;

public class WelcomeActivity extends BaseActivity {
    private ListView mList;
    private boolean IsRegist=false;
    private WebView runWebView;
    private ImageView mImgGuide;
    private GifView gf;
    private static final int READ_PHONE_STATE = 0;
    private int countdown;
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
    private RelativeLayout mRtlComplite;
    private TextView mTvNum;
    private FrameLayout mFlGuide;
    private FrameLayout mFlGuideImg;
    private TextView mTvLoadGuideImg;
    private boolean IsNewGuideImg=false;
    private ImageView mImgLogo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initview();
        initguide();
        initevent();
    }

    private void initguide() {
        if( !SharePre.getGuideImgUrl(getApplicationContext()).equals("")){
            mTvLoadGuideImg.setVisibility(View.GONE);
            UILUtils.displayImageNoAnim(SharePre.getGuideImgUrl(getApplicationContext()),mImgGuide);
            Log.i("引导页缓存图片============",""+SharePre.getGuideImgUrl(getApplicationContext()));
        }
        OkHttpUtil.getInstance().Get(constance.URL.GUIDE, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    mTvLoadGuideImg.setVisibility(View.GONE);
                    guidess guidess = GsonUtils.parseJSON(data, guidess.class);
                    String GuiImgUrl=guidess.getGgt().get(0).getImgurl();
                    String GuideCacheImgUrl = SharePre.getGuideImgUrl(getApplicationContext()) ;
                    UILUtils.displayImageNoAnim(GuiImgUrl,mImgGuide);
                    if(GuideCacheImgUrl == null || !GuiImgUrl.equalsIgnoreCase(GuiImgUrl) ) {
                        IsNewGuideImg=true;
                        SharePre.saveGuideImgUrl(getApplicationContext(),GuiImgUrl);
                        UILUtils.displayImageNoAnim(GuiImgUrl,mImgGuide);
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });

    }
    private void initview() {
        mImgLogo = (ImageView) findViewById(R.id.img_logo);
        mFlGuideImg = (FrameLayout)  findViewById(R.id.fl_guide_img);
        mTvLoadGuideImg = (TextView) findViewById(R.id.tv_load_guide);
        mFlGuide = (FrameLayout) findViewById(R.id.fl_guide);
        mRtlComplite = (RelativeLayout) findViewById(R.id.rtl_complite);
        mTvNum = (TextView) findViewById(R.id.tv_num);
        mImgGuide = (ImageView) findViewById(R.id.img_guide);
    }
    private void initevent() {
        new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    GuideInit();
                }
                //这里的数字为延时时长
            }, 2000);
        mRtlComplite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                IsCanRunning();
            }
        });
    }

    /**
     * 引导初始化
     */
    private void GuideInit() {
        mImgLogo.setVisibility(View.GONE);
        mFlGuide.setVisibility(View.VISIBLE);
        countdown=3;
        handler.postDelayed(runnable, 1000);
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return false;
//        }
//        return false;
//    }
    public void IsCanRunning(){
        if (ActivityCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {   //没有权限
            ActivityCompat.requestPermissions(WelcomeActivity.this,new String[] {Manifest.permission.READ_PHONE_STATE},
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
    public  void Complite(){
        if(SharePre.getUserId(getApplicationContext())==null ||SharePre.getUserId(getApplicationContext()).equals("")){
            IsRegistUser();
        }else {
            Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
            startActivity(intent);
            WelcomeActivity.this.finish();
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
                        Intent intent=new Intent(WelcomeActivity.this,RegistActivity.class);
//                        Intent intent=new Intent(WelcomeActivity.this,YaoQingActivity.class);
                        startActivity(intent);
                        WelcomeActivity.this.finish();
                    }else if(result.getRun().equals("1")){   //已经注册
                        HashMap<String,String> map=new HashMap<>();
                        map.put("udid",Utis.getIMEI(getApplicationContext()));
                        OkHttpUtil.getInstance().Post(map, constance.URL.USER_UDID, new OkHttpUtil.FinishListener() {
                            @Override
                            public void Successfully(boolean IsSuccess, String data, String Msg) {
                                if(IsSuccess){
                                    SharePre.saveIsPostUdid(getApplicationContext(),true);
                                    //获取userid
                                    HashMap<String,String> map1=new HashMap<>();
                                    map1.put("udid", Utis.getIMEI(getApplicationContext()));
                                    OkHttpUtil.getInstance().Post(map1, constance.URL.USER_INFO, new OkHttpUtil.FinishListener() {
                                        @Override
                                        public void Successfully(boolean IsSuccess, String data, String Msg) {
                                            Log.i("首次进入个人资料",""+data.toString());
                                            UserInfo mUserInfo= GsonUtils.parseJSON(data, UserInfo.class);
                                            SharePre.saveUserId(WelcomeActivity.this,mUserInfo.getId());//保存userid
                                            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }else {
                                    Toast(data.toString());
                                }
                            }
                        });
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
}
