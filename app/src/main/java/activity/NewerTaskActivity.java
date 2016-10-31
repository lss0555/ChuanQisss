package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuanqi.yz.R;
import java.util.HashMap;
import Constance.constance;
import Mob.Share.OnekeyShare;
import Utis.GsonUtils;
import Utis.MD5Utis;
import Utis.OkHttpUtil;
import Utis.SharePre;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import model.IsBindAccount;
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
                    initShareState();
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
    private Yzm yzm;
    private RelativeLayout mRtlBindPhones;
    private RelativeLayout mRtlBindAlipay;
    private TextView mTvBindAdlipayState;
    private TextView mTvBindPhoneState;
    private RelativeLayout mRtlBindAlipay1;
    private RelativeLayout mRtlBindPhone;
    private RelativeLayout mRtlLjptState;
    private RelativeLayout mRtlKnownPlatForm;
    private TextView mTvLjpt;
    private RelativeLayout mRtlShareState;
    private TextView mTvShares;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newer_task);
        initview();
        initdate();
        initevent();
    }

    private void initevent() {
        findViewById(R.id.rtl_backs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(constance.INTENT.UPDATE_ADD_USER_MONEY,true);
                intent.setAction(constance.INTENT.UPDATE_ADD_USER_MONEY);   //
                sendBroadcast(intent);   //发送广播
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra(constance.INTENT.UPDATE_ADD_USER_MONEY,true);
        intent.setAction(constance.INTENT.UPDATE_ADD_USER_MONEY);   //
        sendBroadcast(intent);   //发送广播
        finish();
    }

    /**
     * 初始化数据
     */
    private void initdate() {
        initBindPhoneState();
        initBindAlipayState();
        initLjptState();
        initShareState();
    }

    /**
     * 判断有无分享
     */
    private void initShareState() {
        HashMap<String,String> map=new HashMap<>();
        map.put("userid",""+SharePre.getUserId(getApplicationContext()));
        OkHttpUtil.getInstance().Post(map, constance.URL.IS_SHARE, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    Result result = GsonUtils.parseJSON(data, Result.class);
                    if(result.getRun().equals("0")){
                       //未分享
                    }else if(result.getRun().equals("1")){
                        //已分享
                        mRtlShareState.setBackground(getResources().getDrawable(R.drawable.round_gray_bg));
                        mRtlShareState.setEnabled(false);
                        mTvShares.setText("已完成");
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }

    /**
     * 了解平台的状态
     */
    private void initLjptState() {
         HashMap<String,String> map=new HashMap<>();
        map.put("userid",""+SharePre.getUserId(getApplicationContext()));
        OkHttpUtil.getInstance().Post(map, constance.URL.IS_LJPT, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    Result result = GsonUtils.parseJSON(data, Result.class);
                    if(result.getRun().equals("1")){
                       //未完成

                    }else {
                        //已完成
                        mRtlLjptState.setBackground(getResources().getDrawable(R.drawable.round_gray_bg));
                        mRtlKnownPlatForm.setEnabled(false);
                        mTvLjpt.setText("已完成");
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }

    /**
     * 支付宝状态
     */
    private void initBindAlipayState() {
        HashMap<String,String> map=new HashMap<String, String>();
        map.put("userid", SharePre.getUserId(getApplicationContext()));
        map.put("accountstype", "2");
        OkHttpUtil.getInstance().Post(map, constance.URL.IS_BIND_WX_ALIPAY_ACCOUNT, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                stopProgressDialog();
                if(IsSuccess){
                    IsBindAccount bindAccount = GsonUtils.parseJSON(data, IsBindAccount.class);
//                showTip(data.toString());
                    if(!bindAccount.getAccount().equals("")){
                        //有绑定
                        mRtlBindAlipay.setBackground(getResources().getDrawable(R.drawable.round_gray_bg));
                       mRtlBindAlipay.setEnabled(false);
                        mTvBindAdlipayState.setText("已完成");
                        mRtlBindAlipay1.setEnabled(false);
                    }else {
                        //未绑定
                        mRtlBindAlipay.setBackground(getResources().getDrawable(R.drawable.round_red_bg));
                        mRtlBindAlipay.setEnabled(true);
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }

    /**
     * 绑定微信状态
     */
    private void initBindPhoneState() {
        startProgressDialog("加载中...");
        HashMap<String,String> map=new HashMap<>();
        map.put("userid", SharePre.getUserId(getApplicationContext())+"");
        OkHttpUtil.getInstance().Post(map, constance.URL.IS_BIND_PHONE,new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                stopProgressDialog();
                if(IsSuccess){
                    yzm = GsonUtils.parseJSON(data, Yzm.class);
                    if(yzm.getRun().equals("1")){
                        mRtlBindPhones.setBackground(getResources().getDrawable(R.drawable.round_gray_bg));
                        mRtlBindPhones.setEnabled(false);
                        mTvBindPhoneState.setText("已完成");
                        mRtlBindPhone.setEnabled(false);
                    }else {
                        mRtlBindPhones.setBackground(getResources().getDrawable(R.drawable.round_red_bg));
                        mRtlBindPhones.setEnabled(true);
                    }
                }
            }
        });
    }

    /**
     * 绑定微信状态
     */
    private void initBindPhoneStates() {
        HashMap<String,String> map=new HashMap<>();
        map.put("userid", SharePre.getUserId(getApplicationContext())+"");
        OkHttpUtil.getInstance().Post(map, constance.URL.IS_BIND_PHONE,new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    yzm = GsonUtils.parseJSON(data, Yzm.class);
                    if(yzm.getRun().equals("1")){
                        mRtlBindPhones.setBackground(getResources().getDrawable(R.drawable.round_gray_bg));
                        mRtlBindPhones.setEnabled(false);
                        mTvBindPhoneState.setText("已完成");
                        mRtlBindPhone.setEnabled(false);
                    }else {
                        mRtlBindPhones.setBackground(getResources().getDrawable(R.drawable.round_red_bg));
                        mRtlBindPhones.setEnabled(true);
                    }
                }
            }
        });
    }
    private void initview() {
        mRtlShareState = (RelativeLayout) findViewById(R.id.rtl_share_state);
        mTvShares = (TextView) findViewById(R.id.tv_shares);
        findViewById(R.id.rtl_share).setOnClickListener(this);
        mRtlKnownPlatForm = (RelativeLayout) findViewById(R.id.rtl_known_platform);
        mRtlKnownPlatForm.setOnClickListener(this);
        findViewById(R.id.rtl_bind_wx).setOnClickListener(this);
        mRtlBindAlipay1 = (RelativeLayout) findViewById(R.id.rtl_bind_alipay);
        mRtlBindAlipay1.setOnClickListener(this);
        mRtlBindPhone = (RelativeLayout) findViewById(R.id.rtl_bind_phone);
        mRtlBindPhone.setOnClickListener(this);
        mRtlBindPhones = (RelativeLayout) findViewById(R.id.rtl_bindphones);
        mRtlBindAlipay = (RelativeLayout) findViewById(R.id.rtl_bindalipays);
        mTvBindAdlipayState = (TextView) findViewById(R.id.tv_bind_alipay_state);
        mTvBindPhoneState = (TextView) findViewById(R.id.tv_bind_phone_state);
        mRtlLjptState = (RelativeLayout) findViewById(R.id.rtl_ljpt_state);
        mTvLjpt = (TextView) findViewById(R.id.tv_ljpt_state);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rtl_known_platform://了解平台
                Intent intent_kown_platform=new Intent(getApplicationContext(),KownPlatformActivity.class);
                startActivityForResult(intent_kown_platform,11);
                break;
            case R.id.rtl_bind_wx://绑定微信
                Intent intent_wx=new Intent(getApplicationContext(),BindWxAccountActivity.class);
                startActivity(intent_wx);
                break;
            case R.id.rtl_bind_alipay://绑定支付宝
                Intent intent_Alipay=new Intent(getApplicationContext(),BindAliPayActivity.class);
                startActivityForResult(intent_Alipay,11);
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
                                startActivityForResult(intent_bind_phone,21);
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
        oks.setTitle("易钻ATM");  // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://jk.qingyiyou.cn/wx/UniqueCode/invite.html?userid="+SharePre.getUserId(getApplicationContext()));
        oks.setText("易钻ATM,快来加入一起来赚吧！");  // text是分享文本，所有平台都需要这个字段
        oks.setImageUrl("http://i.qingyiyou.cn/yz/Interface/banner/icons.png");
        oks.setUrl("http://jk.qingyiyou.cn/wx/UniqueCode/invite.html?userid="+SharePre.getUserId(getApplicationContext())); // url仅在微信（包括好友和朋友圈）中使用
        oks.setComment("易钻ATM有你才完美");// comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setSite(getString(R.string.app_name)); // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSiteUrl("http://jk.qingyiyou.cn/wx/UniqueCode/invite.html?userid="+SharePre.getUserId(getApplicationContext()));   // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toast("分享成功");
                platformType=platform.getName();
                Message message=new Message();
                message.what=1;
                mHandler.sendMessage(message);
//                initShareState();
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
        map.put("sign",""+ MD5Utis.MD5_Encode(SharePre.getUserId(getApplicationContext())+"传祺chuanqi"));
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
                    }else if(result.getRun().equals("2")){
                        Toast("抱歉，非法操作");
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            initLjptState();
            initBindAlipayState();
        }else if(resultCode==2){    //绑定手机
            initBindPhoneStates();
        }
    }
}
