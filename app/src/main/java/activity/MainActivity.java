package activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chuanqi.yz.R;
import com.tencent.android.tpush.XGCustomPushNotificationBuilder;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import Constance.constance;
import Fragments.GetFragment;
import Fragments.HomeFragment;
import Fragments.MineFragment;
import Fragments.RegalListFragment;
import Fragments.ShareFragment;
import Manager.UpdateManagers;
import Utis.SharePre;
import Utis.Utis;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Views.UnSlideViewPager;
import XinGePush.NotificationService;
import model.IsBindAccount;
import model.Result;
import model.Version;
import model.Yzm;
public class MainActivity extends BaseActivity implements View.OnClickListener{
    private NotificationService notificationService;// 获取通知数据服务
    private  int mCurentPageIndex;//当前的页数
    private ArrayList<Fragment> mFragments;
    private RelativeLayout mRtlHome;
    private RelativeLayout mRtlGet;
    private RelativeLayout mRtlMine;
    private TextView mTvHome;
    private TextView mTvGet;
    private TextView mTvMine;
    private UnSlideViewPager mVpTabs;
    private FragmentPagerAdapter mAdapter;
    private RelativeLayout mRtlShare;
    private TextView mTvShare;
    private  static MainActivity instance;
    private HomeFragment homeFragment;
    private GetFragment getFragment;
    private ShareFragment shareFragment;
    private RegalListFragment regalListFragment;
    private MineFragment mineFragment;
    private boolean IsBindWx=true;
    Message m = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XGPushConfig.enableDebug(this, true);//信鸽调试推送
        setContentView(R.layout.activity_main);
        initview();
        initPage();
        initXinGePush();
        initUpdateVersion();
        initAccountNoval();
    }
    /**
     * 用户异常
     */
    private void initAccountNoval() {
        if(SharePre.getAccountInnoval(getApplicationContext())){
            ShowDialogMessage("您的账户异常");
            mRtlGet.setEnabled(false);
            mRtlShare.setEnabled(false);
            mRtlHome.setEnabled(false);
            mRtlMine.setEnabled(false);
        }
    }
    /**
     * 版本更新
     */
    private void initUpdateVersion() {
        int version = Utis.getVersion(MainActivity.this);
        OkHttpUtil.getInstance().Post(null, constance.URL.VERSION_UPDATE, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    Version version1 = GsonUtils.parseJSON(data, Version.class);
                    if(Integer.parseInt(version1.getBbh())>Utis.getVersion(MainActivity.this)){
                        //新版本
                        UpdateManagers mUpdateManager = new UpdateManagers(MainActivity.this,version1.getGxxx(),version1.getUrl());
                        mUpdateManager.setNotUpdateMessageShow(false);
                        mUpdateManager.checkUpdateInfo();
                    }else {
                        //不是新版本
                        HashMap<String,String> map=new HashMap<String, String>();
                        map.put("userid", SharePre.getUserId(getApplicationContext()));
                        map.put("accountstype", "1");
                        OkHttpUtil.getInstance().Post(map, constance.URL.IS_BIND_WX_ALIPAY_ACCOUNT, new OkHttpUtil.FinishListener() {
                            @Override
                            public void Successfully(boolean IsSuccess, String data, String Msg) {
                                if(IsSuccess){
                                    Log.i("不是新版本，判断有没绑定微信账号",""+data.toString());
                                    IsBindAccount bindAccount = GsonUtils.parseJSON(data, IsBindAccount.class);
                                    if(bindAccount.getAccount().equals("") ||bindAccount.getAccount()==null){
                                        Intent intent=new Intent(getApplicationContext(),WxRegistActivity.class);
                                        intent.putExtra("update",true);
                                        startActivityForResult(intent,1);
                                    }
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
    /**
     * 信鸽推送
     */
    private void initXinGePush() {
        XGPushManager.registerPush(getApplicationContext(),
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        Log.i("信鸽推送状态=================",
                                "+++ register push sucess. token:" + data);
                        m.obj = "+++ register push sucess. token:" + data;
                        m.sendToTarget();
                    }
                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Log.i("信鸽推送状态================",
                                "+++ register push fail. token:" + data
                                        + ", errCode:" + errCode + ",msg:"
                                        + msg);
                        m.obj = "+++ register push fail. token:" + data
                                + ", errCode:" + errCode + ",msg:" + msg;
                        m.sendToTarget();
                    }
                });
//        initCustomPushNotificationBuilder(getApplicationContext());
    }
    /**
     * 设置通知自定义View，这样在下发通知时可以指定build_id。编号由开发者自己维护,build_id=0为默认设置
     *
     * @param context
     */
    @SuppressWarnings("unused")
    private void initCustomPushNotificationBuilder(Context context) {
        XGCustomPushNotificationBuilder build = new XGCustomPushNotificationBuilder();
        build.setSound(
                RingtoneManager.getActualDefaultRingtoneUri(
                        getApplicationContext(), RingtoneManager.TYPE_ALARM)) // 设置声音
                .setDefaults(Notification.DEFAULT_VIBRATE) // 振动
                .setFlags(Notification.FLAG_NO_CLEAR); // 是否可清除
        build.setLayoutId(R.layout.notification);
        build.setLayoutTextId(R.id.content);
        build.setLayoutTitleId(R.id.title);
        build.setLayoutIconId(R.id.icon);
        build.setLayoutIconDrawableId(R.mipmap.ic_empty);
        build.setIcon(R.mipmap.ic_empty);
        build.setLayoutTimeId(R.id.time);
    }
    //信鸽推送
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
        Log.d("TPush", "onResumeXGPushClickedResult:" + click);
        if (click != null) { // 判断是否来自信鸽的打开方式
            Toast.makeText(this, "通知被点击:" + click.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(this);
    }
    public  static  MainActivity getInstance(){
        if(instance==null){
            instance=new MainActivity();
        }
        return  instance;
    }
    public  void exitAll(){
        finish();
    }
    /**
     *
     * 初始化UI
     */
    private void initview() {
        mVpTabs = (UnSlideViewPager) findViewById(R.id.vp_tabs);
        mRtlHome = (RelativeLayout) findViewById(R.id.rtl_home);
        mRtlGet = (RelativeLayout) findViewById(R.id.rtl_get);
        mRtlMine = (RelativeLayout) findViewById(R.id.rtl_mine);
        mRtlShare = (RelativeLayout) findViewById(R.id.rtl_share);
        mTvHome = (TextView) findViewById(R.id.tv_home);
        mTvGet = (TextView) findViewById(R.id.tv_get);
        mTvMine = (TextView) findViewById(R.id.tv_mine);
        mTvShare = (TextView) findViewById(R.id.tv_share);
        mRtlHome.setOnClickListener(this);
        mRtlShare.setOnClickListener(this);
        mRtlGet.setOnClickListener(this);
        mRtlMine.setOnClickListener(this);
    }
    private void initPage() {
        mVpTabs.setOffscreenPageLimit(4);
        mFragments=new ArrayList<Fragment>();
        if(homeFragment==null){
            mFragments.add(new HomeFragment());
        }else {
            mFragments.add(homeFragment);
        }
        if(getFragment==null){
            mFragments.add(new GetFragment());
        }else {
            mFragments.add(getFragment);
        }
        if(regalListFragment==null){
            mFragments.add(new RegalListFragment());
        }else {
            mFragments.add(regalListFragment);
        }
        if(mineFragment==null){
            mFragments.add(new MineFragment());
        }else {
            mFragments.add(mineFragment);
        }
        mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return mFragments.size();
            }
            @Override
            public Fragment getItem(int arg0) {
                // TODO Auto-generated method stub
                return mFragments.get(arg0);
            }
        };
        mVpTabs.setAdapter(mAdapter);
        mVpTabs.setCurrentItem(0);
        mVpTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                ResetTab();
                switch (position) {
                    case 0:
                        ChangeType(mTvHome, getResources().getDrawable(R.mipmap.ic_home1), R.color.red);
                        break;
                    case 1:
                        ChangeType(mTvGet, getResources().getDrawable(R.mipmap.ic_get1), R.color.red);
                        break;
                    case 2:
                        ChangeType(mTvShare, getResources().getDrawable(R.mipmap.ic_share1), R.color.red);
                        break;
                    case 3:
                        ChangeType(mTvMine, getResources().getDrawable(R.mipmap.ic_mine1), R.color.red);
                        break;
                }
                mCurentPageIndex = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    /**
     * 重置Tab
     */
    public  void  ResetTab(){
        ChangeType(mTvHome, getResources().getDrawable(R.mipmap.ic_home),R.color.black);
        ChangeType(mTvGet, getResources().getDrawable(R.mipmap.ic_get),R.color.black);
        ChangeType(mTvShare, getResources().getDrawable(R.mipmap.ic_share),R.color.black);
        ChangeType(mTvMine, getResources().getDrawable(R.mipmap.ic_mine),R.color.black);
    }
    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        ResetTab();
        switch (v.getId()){
            case R.id.rtl_home:
                ChangeType(mTvHome, getResources().getDrawable(R.mipmap.ic_home1), R.color.red);
                mVpTabs.setCurrentItem(0);
                break;
            case R.id.rtl_get:
                ChangeType(mTvGet, getResources().getDrawable(R.mipmap.ic_get1), R.color.red);
                mVpTabs.setCurrentItem(1);
                break;
            case R.id.rtl_share:
                ChangeType(mTvShare, getResources().getDrawable(R.mipmap.ic_share1), R.color.red);
                mVpTabs.setCurrentItem(2);
                break;
            case R.id.rtl_mine:
                ChangeType(mTvMine, getResources().getDrawable(R.mipmap.ic_mine1), R.color.red);
                mVpTabs.setCurrentItem(3);
                break;
        }
    }
    public  void ChangeType(TextView mTv,Drawable drawable,int color){
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTv.setCompoundDrawables(null, drawable, null, null);
        mTv.setTextColor(getResources().getColor(color));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    long waitTime = 2000;
    long touchTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            if((currentTime-touchTime)>=waitTime) {
                Toast.makeText(this, "再按一次退出",Toast.LENGTH_SHORT).show();
                touchTime = currentTime;
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
