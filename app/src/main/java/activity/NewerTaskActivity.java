package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import com.chuanqi.yz.R;
import java.util.HashMap;
import Constance.constance;
import Mob.Share.OnekeyShare;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Utis.SharePre;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import model.Result;
import model.Yzm;

/**
 * 新手任务
 */
public class NewerTaskActivity extends BaseActivity implements View.OnClickListener{
    private String platformType;
    public Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 1:
                    getShareMoney();
                    Log.i("＝＝＝＝＝＝＝＝＝＝＝分享状态","Success");
                 break;
                case 2:
                    Log.i("＝＝＝＝＝＝＝＝＝＝＝分享状态","");
                    break;
                case 3:
                    Log.i("＝＝＝＝＝＝＝＝＝＝＝分享状态","cancle");
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newer_task);
        initview();
    }

    private void initview() {
        findViewById(R.id.rtl_known_platform).setOnClickListener(this);
        findViewById(R.id.rtl_bind_wx).setOnClickListener(this);
        findViewById(R.id.rtl_bind_alipay).setOnClickListener(this);
        findViewById(R.id.rtl_bind_phone).setOnClickListener(this);
        findViewById(R.id.rtl_share).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rtl_known_platform://了解平台
                Intent intent_kown_platform=new Intent(getApplicationContext(),KownPlatformActivity.class);
                startActivity(intent_kown_platform);
                break;
            case R.id.rtl_bind_wx://绑定微信
                Intent intent_wx=new Intent(getApplicationContext(),BindWxAccountActivity.class);
                startActivity(intent_wx);
                break;
            case R.id.rtl_bind_alipay://绑定支付宝
                Intent intent_Alipay=new Intent(getApplicationContext(),BindAliPayActivity.class);
                startActivity(intent_Alipay);
                break;
            case R.id.rtl_bind_phone://绑定手机
                startProgressDialog("加载中...");
                HashMap<String,String> map=new HashMap<>();
                map.put("userid",SharePre.getUserId(getApplicationContext()));
                OkHttpUtil.getInstance().Post(map, constance.URL.IS_BIND_PHONE,new OkHttpUtil.FinishListener() {
                    @Override
                    public void Successfully(boolean IsSuccess, String data, String Msg) {
//                    showTip(data.toString()+"用户UserId"+SharePre.getUserId(getActivity()));
                        stopProgressDialog();
                        Log.w("绑定状态",""+data.toString()+"用户UserId"+SharePre.getUserId(getApplicationContext()));
                        if(IsSuccess){
                          Yzm  yzm = GsonUtils.parseJSON(data, Yzm.class);
                            if(yzm.getRun().equals("1")){
                                //已绑定
                                Toast("您已绑定手机号");
                            }else {
                                //未绑定
                                Intent intent_bind_phone=new Intent(getApplicationContext(),BindPhoneActivity.class);
                                startActivity(intent_bind_phone);
                            }
                        }
                    }
                });
                break;
            case R.id.rtl_share://分享
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Share();
                    }
                }).start();
                break;
        }
    }
    public  void  Share(){
        ShareSDK.initSDK(getApplicationContext());
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();//关闭sso授权
        oks.setTitle("易赚ATM");  // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://weibo.com/ttarticle/p/show?id=2309404017475523111097");
        oks.setText("易赚ATM,快来加入一起来赚吧！");  // text是分享文本，所有平台都需要这个字段
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl("http://bmob-cdn-4915.b0.upaiyun.com/2016/09/08/e52ab1af40b10546807df1d125c2bc70.jpg");
        oks.setUrl("http://weibo.com/ttarticle/p/show?id=2309404017475523111097"); // url仅在微信（包括好友和朋友圈）中使用
        oks.setComment("易赚有你才完美");// comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setSite(getString(R.string.app_name)); // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSiteUrl("http://weibo.com/ttarticle/p/show?id=2309404017475523111097");   // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toast("分享成功");
                platformType=platform.getName();
                Message message=new Message();
                message.what=1;
                mHandler.sendMessage(message);
            }
            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("分享状态","onError");
                Message message=new Message();
                message.what=2;
                mHandler.sendMessage(message);
            }
            @Override
            public void onCancel(Platform platform, int i) {
                Toast("分享取消");
                Message message=new Message();
                message.what=3;
                mHandler.sendMessage(message);
            }
        });
        oks.show(getApplicationContext()); // 启动分享GUI
    }
    private void getShareMoney() {
        HashMap<String,String> map=new HashMap<String, String>();
        map.put("userid",""+ SharePre.getUserId(getApplicationContext()));
        map.put("rwstyle",""+platformType);
        OkHttpUtil.getInstance().Post(map, constance.URL.SHARE_GET, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                Toast(data.toString()+platformType);
                if(IsSuccess){
                    Result result = GsonUtils.parseJSON(data, Result.class);
                    if(result.getRun().equals("1")){
                        Toast("恭喜您获得0.3元");
                        Intent intent = new Intent();
                        intent.putExtra("update",true);
                        intent.setAction("update");   //
                        sendBroadcast(intent);
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
}
