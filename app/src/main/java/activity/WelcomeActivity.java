package activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import model.IsBindAccount;
import model.PermissionModel;
import model.Result;
import model.UserInfo;

public class WelcomeActivity extends BaseActivity {
    private final static int READ_PHONE_STATE_CODE = 101;
    private final static int WRITE_EXTERNAL_STORAGE_CODE = 102;
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

    /**
     * 6.0以上权限判断
     */
    public void IsCanRunning(){
//        if (ActivityCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED) {   //没有权限
//            ActivityCompat.requestPermissions(WelcomeActivity.this,new String[] {Manifest.permission.READ_PHONE_STATE},
//                    READ_PHONE_STATE);
//            return;
//        }else {
//            //有权限
//            Complite();
//        }
        if (Build.VERSION.SDK_INT < 23) {
            Complite();
        } else {
            checkPermissions();
        }
    }

    /**
     * 权限回掉
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_PHONE_STATE_CODE:
            case WRITE_EXTERNAL_STORAGE_CODE:
                // 如果用户不允许，我们视情况发起二次请求或者引导用户到应用页面手动打开
                if (PackageManager.PERMISSION_GRANTED != grantResults[0]) {
                    // 二次请求，表现为：以前请求过这个权限，但是用户拒接了
                    // 在二次请求的时候，会有一个“不再提示的”checkbox
                    // 因此这里需要给用户解释一下我们为什么需要这个权限，否则用户可能会永久不在激活这个申请
                    // 方便用户理解我们为什么需要这个权限
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                        new AlertDialog.Builder(this).setTitle("权限申请").setMessage(findPermissionExplain(permissions[0]))
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        checkPermissions();
                                    }
                                }).show();
                    }
                    // 到这里就表示已经是第3+次请求，而且此时用户已经永久拒绝了，这个时候，我们引导用户到应用权限页面，让用户自己手动打开
                    else {
                        Toast.makeText(this, "部分权限被拒绝获取，将会会影响后续功能的使用，建议重新打开", Toast.LENGTH_LONG).show();
                        openAppPermissionSetting(123456789);
                    }
                    return;
                }
                // 到这里就表示用户允许了本次请求，我们继续检查是否还有待申请的权限没有申请
                if (isAllRequestedPermissionGranted()) {
                    Complite();
                } else {
                    checkPermissions();
                }
                break;
        }
    }
    private String findPermissionExplain(String permission) {
        if (models != null) {
            for (PermissionModel model : models) {
                if (model != null && model.permission != null && model.permission.equals(permission)) {
                    return model.explain;
                }
            }
        }
        return null;
    }
    private boolean isAllRequestedPermissionGranted() {
        for (final PermissionModel model : models) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, model.permission)) {
                return false;
            }
        }
        return true;
    }
    private boolean openAppPermissionSetting(int requestCode) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + this.getPackageName()));
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            // Android L 之后Activity的启动模式发生了一些变化
            // 如果用了下面的 Intent.FLAG_ACTIVITY_NEW_TASK ，并且是 startActivityForResult
            // 那么会在打开新的activity的时候就会立即回调 onActivityResult
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, requestCode);
            return true;
        } catch (Throwable e) {
            Log.e("chuanqi", "", e);
        }
        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 123456789:
                if (isAllRequestedPermissionGranted()) {
                    Complite();
                } else {
                    Toast.makeText(this, "部分权限被拒绝获取，退出", Toast.LENGTH_LONG).show();
                    WelcomeActivity.this.finish();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
    /**
     * 完成权限判断。进入
     */
    public  void Complite(){
        if(SharePre.getUserId(getApplicationContext())==null ||SharePre.getUserId(getApplicationContext()).equals("")){
            IsRegistUser();
        }else {
            Go();
        }
    }
    public void IsRegistUser(){
        HashMap<String,String> map=new HashMap<>();
        map.put("udid",Utis.getIMEI(getApplicationContext()));
        map.put("newudid",Utis.getIMEI(getApplicationContext()));
        map.put("nudid",Utis.getIMEI(getApplicationContext()));
        OkHttpUtil.getInstance().Post(map, constance.URL.USER_UDID, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    SharePre.saveIsPostUdid(getApplicationContext(),true);
                    HashMap<String,String> map1=new HashMap<>();
                    map1.put("udid", Utis.getIMEI(getApplicationContext()));
                    OkHttpUtil.getInstance().Post(map1, constance.URL.USER_INFO, new OkHttpUtil.FinishListener() {
                        @Override
                        public void Successfully(boolean IsSuccess, String data, String Msg) {
                            Log.i("首次进入个人资料",""+data.toString());
                            UserInfo mUserInfo= GsonUtils.parseJSON(data, UserInfo.class);
                            SharePre.saveUserId(WelcomeActivity.this,mUserInfo.getId());//保存userid
                            //判断有没绑定微信
                            HashMap<String,String> map=new HashMap<String, String>();
                            map.put("userid", SharePre.getUserId(WelcomeActivity.this));
                            map.put("accountstype", "1");
                            OkHttpUtil.getInstance().Post(map, constance.URL.IS_BIND_WX_ALIPAY_ACCOUNT, new OkHttpUtil.FinishListener() {
                                @Override
                                public void Successfully(boolean IsSuccess, String data, String Msg) {
                                    if(IsSuccess){
                                        Log.i("不是新版本，判断有没绑定微信账号",""+data.toString());
                                        IsBindAccount bindAccount = GsonUtils.parseJSON(data, IsBindAccount.class);
                                        if(bindAccount.getAccount().equals("") ||bindAccount.getAccount()==null){
                                            Intent intent=new Intent(getApplicationContext(),RegistActivity.class);
                                            startActivityForResult(intent,1);
                                            finish();
                                        }else {
                                            Go();
                                        }
                                    }else {
                                        Toast(data.toString());
                                    }
                                }
                            });
                        }
                    });
                }else {
                    Toast(data.toString());
                }
            }
        });
    }

    /**
     * 进入主页面
     */
    public void Go(){
        HashMap<String,String> map=new HashMap<>();
        map.put("userid",""+SharePre.getUserId(getApplicationContext()));
        OkHttpUtil.getInstance().Post(map, constance.URL.ACCOUNT_IS_NOVAL, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    Result result = GsonUtils.parseJSON(data, Result.class);
                    if(result.getRun().equals("0")){
                       //账户异常
                        SharePre.IsAccountInnoval(getApplicationContext(),true);
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(result.getRun().equals("1")){
                        //账户正常
                        SharePre.IsAccountInnoval(getApplicationContext(),false);
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
    /**
     * 这里我们演示如何在Android 6.0+上运行时申请权限
     *
     * @param
     */
    private void checkPermissions() {
        try {
            for (PermissionModel model : models) {
                if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, model.permission)) {
                    ActivityCompat.requestPermissions(this, new String[] { model.permission }, model.requestCode);
                    return;
                }
            }
            // 到这里就表示所有需要的权限已经通过申请，权限已经申请就打开
            Complite();
        } catch (Throwable e) {
            Log.e("openpersion", "", e);
        }
    }
    /**
     * 需要向用户申请的权限列表
     */
    private static PermissionModel[] models = new PermissionModel[] {
            new PermissionModel(Manifest.permission.READ_PHONE_STATE, "我们需要读取手机信息的权限来标识您的身份", READ_PHONE_STATE_CODE),
            new PermissionModel(Manifest.permission.WRITE_EXTERNAL_STORAGE, "我们需要您允许我们读写你的存储卡，以方便我们临时保存一些数据",
                    WRITE_EXTERNAL_STORAGE_CODE),
    };
}
