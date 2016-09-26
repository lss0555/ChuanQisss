package activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chuanqi.yz.R;

import java.util.ArrayList;
import java.util.HashMap;

import Constance.constance;
import Fragments.GetFragment;
import Fragments.HomeFragment;
import Fragments.MineFragment;
import Fragments.ShareFragment;
import Utis.SharePre;
import Utis.Utis;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Views.UnSlideViewPager;
import model.Result;

public class MainActivity extends BaseActivity implements View.OnClickListener{
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
    private MineFragment mineFragment;
//    private ServiceConnection conn=new ServiceConnection() {
//        private TimerService.MyBind binder;
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder service) {
//            binder = (TimerService.MyBind)service;
//            Log.i("服务连接状态","Success"+ binder.getDate());
//        }
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            Log.i("服务连接状态","Fails");
//        }
//    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPrePageYaoQing();
        initview();
        initPage();
        initState();
    }
    private void initPrePageYaoQing() {
        if(!SharePre.getUserId(getApplicationContext()).equals("")){
//            Intent intent=new Intent(MainActivity.this,YaoQingActivity.class);
//            startActivity(intent);
//            finish();
            return;
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
//                showTip(data.toString());
                if(IsSuccess){
                    Result result = GsonUtils.parseJSON(data, Result.class);
                    if(result.getRun().equals("0")){
                        //还没注册
                        Intent intent=new Intent(MainActivity.this,YaoQingActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(result.getRun().equals("1")){
                        //已经注册
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
    private void initState() {

    }
    public  static  MainActivity getInstance(){
        if(instance==null){
            instance=new MainActivity();
        }
        return  instance;
    }
    /**
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
        if(shareFragment==null){
            mFragments.add(new ShareFragment());
        }else {
            mFragments.add(shareFragment);
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
}
