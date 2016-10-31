package Fragments;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.chuanqi.yz.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import Constance.constance;
import Interfaces.MyReceiver;
import Manager.UpdateManagers;
import Utis.OkHttpUtil;
import Utis.UILUtils;
import Utis.Utis;
import Utis.SharePre;
import Utis.GsonUtils;
import Views.Banners.Lanner;
import Views.CircleImageView;
import Views.SwitcherView;
import Views.ViewPageIndicator;
import activity.ApprenticeListActivity;
import activity.BannerLinkActivity;
import activity.FaskTaskActivity;
import activity.HelpCenterActivity;
import activity.HowToEarn.HowToEarnActivity;
import activity.IntoMoneyJqzActivity;
import activity.JXZActivity;
import activity.NewerTaskActivity;
import activity.ToWxGetOpenidMethodActivity;
import activity.YaoQingSharesActivity;
import activity.SignActivity;
import activity.Red.TakeRedActivity;
import activity.UnitTaskActivity;
import activity.WithDrawActivity;
import app.AndroidSystem;
import model.Banner.Banner;
import model.Banner.banners;
import model.IsBindAccount;
import model.Notice;
import model.JxzAccount;
import model.Result;
import model.TxRecord.jqzcr;
import model.TxRecord.JqzCrRecord;
import model.TxRecord.TxRecord;
import model.TxRecord.tx;
import model.UserAccounts;
import model.UserAllYue;
import model.UserInfo;
import model.UserMoney;
import model.Version;
import model.Yzm;
/**
 * A simple {@link Fragment} subclass. update 2016.8.31
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener{
    private  ArrayList<TxRecord> mTxRecord=new ArrayList<>();
    private  ArrayList<String > mUserId=new ArrayList<>();
    private  ArrayList<String > mTimes=new ArrayList<>();
    private ArrayList<String> mState=new ArrayList<>();
    private  ArrayList<JqzCrRecord> mCrJqz=new ArrayList<>();
    private  ArrayList<String > mJqzCrUserId=new ArrayList<>();
    private  ArrayList<String > mJqzCrTime=new ArrayList<>();
    private  ArrayList<String > mJqzCrMoney=new ArrayList<>();
    private UserMoney mUserMoney;
    private  ArrayList<JxzAccount> mJxzAccount=new ArrayList<>();
    protected boolean isVisible;
    final ArrayList<String> initUserTxRecord = new ArrayList<String>(Arrays.asList("用户100001", "用户100002", "用户100003", "用户100004"));
    private boolean IsTouch=true;
    private ViewPager page;
    private ViewPageIndicator viewPageIndicator;
    private ImageView mImgMine;
    private TextView mTvZhuHe;
    private TextView mTvUnitTask;
    private TextView mTvFastTask;
    private TextView mTvDayShop;
    private TextView mTvGongGao;
    private Lanner mLanner;
    private List<banners> lannerBeans=new ArrayList<>();
    private TextView mTvTodyIncome;
    private TextView mTvAllIncome;
    private TextView mTvJxzYue;
    private TextView mTvJxzAccrual;
    private SwitcherView mTvId;
    private SwitcherView mTvId1;
    private SwitcherView mTvTime;
    private SwitcherView mTvTime1;
    private SwitcherView mTvState;
    private SwitcherView mTvState1;
    private double UserAccount;//聚钱庄余额
    public static  HomeFragment instance;
    private ScrollView mSvDate;
    private View mEmptyDate;
    private MyReceiver myReceiver;
    private TextView mTvUpLoad;
    private SwipeRefreshLayout mReFreshLayout;
    private final int USER_TX_RECORD=1;
    private final int JQZ_INTO_RECORD=2;
    private CircleImageView mImgHead;
    private RelativeLayout mRtlLoadBanner;
    private RelativeLayout mRtlBanner;
    private LinearLayout mLlLoadTxRecord;
    private LinearLayout mLlTxRecord;
    private TextView mTvUserid;
    private ImageView mImgSuperUserIcon;
    private boolean IsBindPhone=false;
    private boolean IsBindAliPay=false;
    private boolean IsLjpt=false;
    private boolean IsShare=false;
    private TextView mTvYq;
    private TextView mTvDaySign;
    private RelativeLayout mRtlSaveMoney;
    private TextView mTvNewerTask;
    private RelativeLayout mRtlWithDraw;
    private TextView mTvJqz;
    private TextView mTvOpenBox;
    private TextView mTvHelpCenter;
    private RelativeLayout mRtlHowToEarn;
    private TextView mTvMastCenter;
    private TextView mTvUpload;
    private TextView mTvTakeRed;
    public static HomeFragment getInstance(){
        if(instance==null){
            instance=new HomeFragment();
        }
        return  instance;
    }
    public HomeFragment() {
    }
    public Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case USER_TX_RECORD:
                    setUserTx();
                    setJqzCrReord();
                    break;
                case JQZ_INTO_RECORD:
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private Runnable mRbUserRecord = new Runnable() {
        public void run() {
            Message message=new Message();
            message.what=USER_TX_RECORD;
            mHandler.sendMessage(message);
        }
    };
    private Runnable mRbJqzIntoRecord = new Runnable() {
        public void run() {
            Message message=new Message();
            message.what=JQZ_INTO_RECORD;
            mHandler.sendMessage(message);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_home,null);
        initview(layout);
        initdate();
        initViewPage(); //轮播
        initReFreshDate();
        initNewTaskState();
        initAccountNoval();
        return layout;
    }
    /**
     * 账户异常
     */
    private void initAccountNoval() {
        if(SharePre.getAccountInnoval(getActivity())){
            mRtlWithDraw.setEnabled(false);
            mRtlSaveMoney.setEnabled(false);
            mTvNewerTask.setEnabled(false);
            mTvFastTask.setEnabled(false);
            mTvUnitTask.setEnabled(false);
            mTvDaySign.setEnabled(false);
            mTvYq.setEnabled(false);
            mTvTakeRed.setEnabled(false);
            mTvOpenBox.setEnabled(false);
            mTvJqz.setEnabled(false);
        }
    }

    /**
     * 初始化新手任务的状态
     */
    private void initNewTaskState() {
        initBindAlipayState();
        initLjptState();
        initShareState();
        initUserDate();
    }
    /**
     * 判断有无分享
     */
    private void initShareState() {
        HashMap<String,String> map=new HashMap<>();
        map.put("userid",""+SharePre.getUserId(getActivity()));
        OkHttpUtil.getInstance().Post(map, constance.URL.IS_SHARE, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    Result result = GsonUtils.parseJSON(data, Result.class);
                    if(result.getRun().equals("0")){
                        //未分享
                        IsShare=false;
                    }else if(result.getRun().equals("1")){
                        //已分享
                        IsShare=true;
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
        map.put("userid",""+SharePre.getUserId(getActivity()));
        OkHttpUtil.getInstance().Post(map, constance.URL.IS_LJPT, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    Result result = GsonUtils.parseJSON(data, Result.class);
                    if(result.getRun().equals("1")){
                        //未完成
                       IsLjpt=false;
                    }else {
                        //已完成
                        IsLjpt=true;
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
        map.put("userid", SharePre.getUserId(getActivity()));
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
                        IsBindAliPay=true;
                    }else {
                        //未绑定
                        IsBindAliPay=false;
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
    /**
     * 刷新数据
     */
    private void initReFreshDate() {
        //改变加载显示的颜色
        mReFreshLayout.setColorSchemeColors(Color.RED,Color.RED);
        mReFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    initViewPage(); //轮播
                                    initdate();
                                    mReFreshLayout.setRefreshing(false);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter=new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(constance.INTENT.UPDATE_ADD_USER_MONEY);
        myReceiver=new MyReceiver();
        myReceiver.SetNetStateListner(new MyReceiver.NetStateListner() {
            @Override
            public void NetState(boolean IsConnect) {
                if(!IsConnect){
                    mReFreshLayout.setVisibility(View.GONE);
                    Toast("网络连接已断开");
                }
            }
            @Override
            public void UpdateUserMoney(boolean IsUpdate) {
                getAccountInfo();  //更新用户金额信息
                initNewTaskState();
            }
        });
        getActivity().registerReceiver(myReceiver, filter);
    }
    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(myReceiver);
        mHandler.removeCallbacks(mRbUserRecord);
        mHandler.removeCallbacks(mRbJqzIntoRecord);
    }
    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRbUserRecord);
        mHandler.removeCallbacks(mRbJqzIntoRecord);
    }
    /**
     * inidate
     */
    private void initdate() {
        GongGaoRecord();//公告记录
        UserTxRecord();//用户提现记录
        JqzStoreRecord(); //聚钱庄存入记录
        getAccountInfo();//用户金额，聚钱庄金额 用户状态
    }
    /**
     * 判断是否是皇冠用户
     */
    private void initSuperUser() {
        HashMap<String,String> map=new HashMap<String, String>();
        map.put("userid",""+SharePre.getUserId(getActivity()));
        OkHttpUtil.getInstance().Post(map, constance.URL.IS_USER_UPDATE, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    Log.i("首页判断是否是超级会员头像",""+data.toString()) ;
                    Result result = GsonUtils.parseJSON(data, Result.class);
                    if(result.getRun().equals("1")){
                        mImgSuperUserIcon.setVisibility(View.GONE);
                        //普通会员
                    }else if(result.getRun().equals("2")){
                        //已经是超级会员
                        mImgSuperUserIcon.setVisibility(View.VISIBLE);
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
    /**
     * 初始化用户信息
     */
    public UserInfo mUserInfo;
    private void initUserDate() {
        HashMap<String,String> map=new HashMap<>();
        map.put("udid", Utis.getIMEI(getActivity()));
        OkHttpUtil.getInstance().Post(map, constance.URL.USER_INFO, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                Log.i("个人资料",""+data.toString());
                if(IsSuccess){
                    mUserInfo= GsonUtils.parseJSON(data,UserInfo.class);
                    UILUtils.displayImage(mUserInfo.getHeadportrait(),mImgHead);
                    mTvUserid.setText("ID:"+SharePre.getUserId(getActivity()));
                    if(mUserInfo.getTel()!=null){
                         IsBindPhone=true;
                    }else {
                         IsBindPhone=false;
                    }
                }
            }
        });
    }
    /**
     * 公告记录
     */
    private void GongGaoRecord() {
        OkHttpUtil.getInstance().Get( constance.URL.GONGGAO, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                Log.e("Notice",""+data.toString());
                if(IsSuccess){
                    Notice notice = GsonUtils.parseJSON(data, Notice.class);
                    mTvGongGao.setText(""+notice.getContent());
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
    /**
     * 用户提现记录
     */
    private void UserTxRecord() {
        OkHttpUtil.getInstance().Get(constance.URL.USER_TX, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                Log.i("用户提现记录",""+data.toString());
                if(IsSuccess){
                    tx mTx = GsonUtils.parseJSON(data, tx.class);
                    mTxRecord.clear();
                    mTxRecord.addAll(mTx.getTx());
                    mUserId.clear();
                    mTimes.clear();
                    mState.clear();
                    for (int i=0;i<mTxRecord.size();i++){
                        mUserId.add("用户"+mTxRecord.get(i).getUserid());
                        mTimes.add(Utis.getDistanceTime(Utis.getTime(),mTxRecord.get(i).getTxtime())+"前");
                        if(mTxRecord.get(i).getAccountstyle().equals("1")){
                            mState.add("微信提现"+mTxRecord.get(i).getPrice()+"元到账");
                        }else {
                            mState.add("支付宝提现"+mTxRecord.get(i).getPrice()+"元到账");
                        }
                    }
                    Log.i("用户mUserId",""+mUserId.toString());
                    JqzStoreRecord();
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
    /**
     * 聚钱庄存入记录
     */
    private void JqzStoreRecord() {
        OkHttpUtil.getInstance().Get(constance.URL.JQZ_CR, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                Log.i("聚钱庄存入记录",""+data.toString());
                if(IsSuccess){
                    jqzcr mJqzCr = GsonUtils.parseJSON(data, jqzcr.class);
                    mCrJqz.clear();
                    mJqzCrUserId.clear();
                    mJqzCrTime.clear();
                    mJqzCrMoney.clear();
                    mCrJqz.addAll(mJqzCr.getJqzcr());
                    for (int i=0;i<mCrJqz.size();i++){
                        mJqzCrUserId.add("用户"+mCrJqz.get(i).getUserid());
                        mJqzCrTime.add(Utis.getDistanceTime(Utis.getTime(),mCrJqz.get(i).getTdate())+"前");
                        mJqzCrMoney.add("任务收益"+mCrJqz.get(i).getMoney()+"元");
                    }
                    new Thread(mRbUserRecord).start();
                    mLlLoadTxRecord.setVisibility(View.GONE);
                    mLlTxRecord.setVisibility(View.VISIBLE);
//                    new Thread(mRbJqzIntoRecord).start();
                }
            }
        });
    }

    /**
     * 用户金额，聚钱庄金额，用户状态
     */
    public  void getAccountInfo(){
        HashMap<String,String> map=new HashMap<>();
        map.put("userid",""+SharePre.getUserId(getActivity()));
        OkHttpUtil.getInstance().Post(map, constance.URL.USER_ACCOUNT, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    UserAccounts userAccounts = GsonUtils.parseJSON(data, UserAccounts.class);
                    //总收入
                    if(userAccounts.getAllsr()==null){
                        mTvTodyIncome.setText("0.0元");
                    }else {
                        mTvTodyIncome.setText(""+userAccounts.getAllsr()+"元");
                    }
                    //总余额
                    if(userAccounts.getfNotPayIncome()==null){
                        UserAccount=0.0;
                        mTvAllIncome.setText("0.0元");
                    }else {
                        UserAccount=Double.parseDouble(userAccounts.getfNotPayIncome());
                        mTvAllIncome.setText(userAccounts.getfNotPayIncome()+"元");
                    }
                    //聚钱装余额
                    if(userAccounts.getYue()==null){
                        mTvJxzYue.setText("0.0元");
                    } else {
                        mTvJxzYue.setText(userAccounts.getYue()+"元");
                    }
                    //聚钱庄收益
                    if(userAccounts.getAccrual()==null){
                        mTvJxzAccrual.setText("0.0元");
                    } else {
                        mTvJxzAccrual.setText(userAccounts.getAccrual()+"元");
                    }
                    //用户等级
                    if(userAccounts.getUserdj().equals("0")){
                      //账户异常
                        mImgSuperUserIcon.setVisibility(View.GONE);
                        SharePre.IsAccountInnoval(getActivity(),true);
                        ShowDialogMessage("您的账户异常");
                    }else if (userAccounts.getUserdj().equals("1")){
                      //账户正常
                        SharePre.IsAccountInnoval(getActivity(),false);
                        mImgSuperUserIcon.setVisibility(View.GONE);
                    }else if (userAccounts.getUserdj().equals("2")){
                        //会员
                        SharePre.IsAccountInnoval(getActivity(),false);
                        mImgSuperUserIcon.setVisibility(View.VISIBLE);
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isVisible=true;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        isVisible=false;
    }
    /**
     * 事件
     */
    private void setUserTx() {
        //显示用户提现信息
                mTvId.setResource(mUserId);
                mTvId.startRolling();
                mTvTime.setResource(mTimes);
                mTvTime.startRolling();
                mTvState.setResource(mState);
                mTvState.startRolling();
    }
    /**
     * 设置聚钱钱存入记录
     */
    public  void setJqzCrReord(){
        mTvId1.setResource(mJqzCrUserId);
        mTvId1.startRolling();
        mTvState1.setResource(mJqzCrMoney);
        mTvState1.startRolling();
        mTvTime1.setResource(mJqzCrTime);
        mTvTime1.startRolling();
    }

    /**
     * 初始化控件
     * @param layout
     */
    private void initview(View layout) {
        mImgSuperUserIcon = (ImageView) layout.findViewById(R.id.img_super_user);
        mTvUserid = (TextView) layout.findViewById(R.id.tv_userid);
        mLlTxRecord = (LinearLayout) layout.findViewById(R.id.ll_tx_record);
        mLlLoadTxRecord = (LinearLayout) layout.findViewById(R.id.ll_load_tx_record);
        mRtlBanner = (RelativeLayout) layout.findViewById(R.id.rtl_banner);
        mRtlLoadBanner = (RelativeLayout) layout.findViewById(R.id.rtl_load_banner);
        mImgHead = (CircleImageView) layout.findViewById(R.id.img_head);
        mReFreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeRefreshLayout);
        mLanner= (Lanner) layout.findViewById(R.id.lanner);
        mSvDate = (ScrollView) layout.findViewById(R.id.sv_date);
        mEmptyDate = layout.findViewById(R.id.no_date);
        mTvId = (SwitcherView) layout.findViewById(R.id.tv_id_l);
        mTvId1 = (SwitcherView) layout.findViewById(R.id.tv_id_2);
        mTvTime = (SwitcherView) layout.findViewById(R.id.tv_time_1);
        mTvTime1 = (SwitcherView) layout.findViewById(R.id.tv_time_2);
        mTvState = (SwitcherView) layout.findViewById(R.id.tv_state_1);
        mTvState1 = (SwitcherView) layout.findViewById(R.id.tv_state_2);
        mTvGongGao=  (TextView) layout.findViewById(R.id.tv_gonggao);
        mTvTodyIncome = (TextView) layout.findViewById(R.id.tv_today_income);
        mTvAllIncome = (TextView) layout.findViewById(R.id.tv_all_income);
        mTvJxzYue = (TextView) layout.findViewById(R.id.tv_jqzyue);
        mTvJxzAccrual = (TextView) layout.findViewById(R.id.tv_jxz_accrual);
        mTvUnitTask= (TextView) layout.findViewById(R.id.tv_unit_task);
        mTvFastTask=  (TextView) layout.findViewById(R.id.tv_fast_task);
        mTvYq = (TextView) layout.findViewById(R.id.tv_yq);
        mTvUnitTask.setOnClickListener(this);
        mTvFastTask.setOnClickListener(this);
        mTvDaySign = (TextView) layout.findViewById(R.id.tv_day_sign);
        mRtlSaveMoney = (RelativeLayout) layout.findViewById(R.id.rtl_save_money);
        mTvNewerTask = (TextView) layout.findViewById(R.id.tv_newer_task);
        mTvTakeRed = (TextView) layout.findViewById(R.id.tv_take_red);
        mRtlWithDraw = (RelativeLayout) layout.findViewById(R.id.rtl_withdraw);
        mTvJqz = (TextView) layout.findViewById(R.id.tv_jxz);
        mTvOpenBox = (TextView) layout.findViewById(R.id.tv_open_box);
        mTvHelpCenter = (TextView) layout.findViewById(R.id.tv_help_center);
        mRtlHowToEarn = (RelativeLayout) layout.findViewById(R.id.rtl_howto_earn);
        mTvMastCenter = (TextView) layout.findViewById(R.id.tv_master_center);
        mTvJqz=  (TextView) layout.findViewById(R.id.tv_jxz);
        mTvUpload = (TextView) mEmptyDate.findViewById(R.id.tv_upload);
        mRtlHowToEarn  .setOnClickListener(this);
        mRtlWithDraw.setOnClickListener(this);
        mTvJqz.setOnClickListener(this);
        mTvDaySign.setOnClickListener(this);
        mTvYq.setOnClickListener(this);
        mTvTakeRed.setOnClickListener(this);
        mRtlSaveMoney.setOnClickListener(this);
        mTvNewerTask .setOnClickListener(this);
        mTvTakeRed.setOnClickListener(this);
        mTvOpenBox.setOnClickListener(this);
        mTvHelpCenter.setOnClickListener(this);
        mTvMastCenter.setOnClickListener(this);
        mTvJqz.setOnClickListener(this);
        mTvUpload.setOnClickListener(this);
        mTvId.setEnabled(false);
        mTvId1.setEnabled(false);
        mTvState.setEnabled(false);
        mTvState1.setEnabled(false);
        mTvTime.setEnabled(false);
        mTvTime1.setEnabled(false);
    }

    /**
     * 轮播
     */
    private void initViewPage() {
        OkHttpUtil.getInstance().Get(constance.URL.BANNER, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    mRtlLoadBanner.setVisibility(View.GONE);
                    mRtlBanner.setVisibility(View.VISIBLE);
                    Log.i("轮播的数据",""+data.toString());
                    Banner banner = GsonUtils.parseJSON(data, Banner.class);
                    lannerBeans.clear();
                    lannerBeans.addAll(banner.getGgt());
                    mLanner.setLannerBeanList(lannerBeans);
                }else {
                    Toast(data.toString());
                }
            }
        });
        mLanner.setOnLannerItemClickListener(new Lanner.OnLannerItemClickListener() {
            @Override
            public void click(View v, banners lb) {
                if(!lb.getLink().equals("")){
                    Intent intent=new Intent(getActivity(), BannerLinkActivity.class);
                    intent.putExtra("link",lb.getLink());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.tv_fast_task://快速任务
                Intent intent1=new Intent(getActivity(),FaskTaskActivity.class);
                getActivity().startActivity(intent1);
                    if(mUserInfo!=null){
                        if(AndroidSystem.getAllApps(getActivity()).size()<=5 || !Utis.checkApkExist(getActivity(),"com.eg.android.AlipayGphone")){
                            Toast("抱歉，您未满足做任务条件");
                        }
                        else  if(mUserInfo.getTel()==null){
                            Toast("抱歉，您还未绑定手机号码");
                        }else if(!Utis.HasSimCard(getActivity())){
                            Toast("抱歉，您的SIM卡异常，请检查");
                        } else {
                            Intent intent=new Intent(getActivity(),FaskTaskActivity.class);
                            getActivity().startActivity(intent);
                        }
                    }else {
                        Toast("加载中...");
                    }
                break;
            case  R.id.tv_unit_task://联盟任务
                    if(mUserInfo!=null){
                         if(AndroidSystem.getAllApps(getActivity()).size()<=5 || !Utis.checkApkExist(getActivity(),"com.eg.android.AlipayGphone")){
                            Toast("抱歉，您未满足做任务条件");
                        }
                        else if(mUserInfo.getTel()==null){
                            Toast("抱歉，您还未绑定手机号码");
                        }else if(!Utis.HasSimCard(getActivity())){
                            Toast("抱歉，您的SIM卡异常，请检查");
                        }else {
                            Intent intent=new Intent(getActivity(),UnitTaskActivity.class);
                            getActivity().startActivity(intent);
                        }
                    }else {
                        Toast("加载中...");
                    }
                break;
            case  R.id.tv_yq://邀请分享
                    Intent intent_shop=new Intent(getActivity(), YaoQingSharesActivity.class);
                    getActivity().startActivity(intent_shop);
                break;
            case  R.id.tv_day_sign://每日签到
                    Intent intent_sign=new Intent(getActivity(), SignActivity.class);
                    getActivity().startActivity(intent_sign);
                break;
            case  R.id.rtl_save_money://转入钱庄
                    Intent intent_store=new Intent(getActivity(), IntoMoneyJqzActivity.class);
                    intent_store.putExtra("jzq","int");
                    getActivity().startActivityForResult(intent_store,12);
                break;
            case  R.id.tv_newer_task://新手任务
                    Log.i("新手任务完成状态","绑定支付宝"+IsBindAliPay+"绑定手机"+IsBindPhone+"是否了解平台"+IsLjpt+"是否完成首次分享"+IsShare);
                    if(IsLjpt && IsShare && IsBindPhone && IsBindAliPay){
                        Toast("您已完成新手任务");
                    }else {
                        Intent intent_newerTask=new Intent(getActivity(), NewerTaskActivity.class);
                        getActivity().startActivity(intent_newerTask);
                    }
                break;
            case  R.id.tv_take_red://抢红包
                    Intent intent_take_red=new Intent(getActivity(), TakeRedActivity.class);
                    getActivity().startActivity(intent_take_red);
                break;
            case  R.id.rtl_withdraw://提现
                    Intent intent_tx=new Intent(getActivity(), WithDrawActivity.class);
                    intent_tx.putExtra("money",UserAccount);
                    getActivity().startActivity(intent_tx);
                break;
            case  R.id.tv_jxz://聚钱装
                    Intent intent_jxz=new Intent(getActivity(), JXZActivity.class);
                    getActivity().startActivity(intent_jxz);
                break;
            case  R.id.tv_open_box://开宝箱
                Toast("待开放中...");
                break;
            case  R.id.tv_help_center://帮助中心
                    Intent intent_help_center=new Intent(getActivity(), HelpCenterActivity.class);
                    getActivity().startActivity(intent_help_center);
                break;
            case  R.id.rtl_howto_earn://赚钱攻略
                    Intent intent_howtoearn=new Intent(getActivity(), HowToEarnActivity.class);
                    getActivity().startActivity(intent_howtoearn);
                break;
            case  R.id.tv_master_center://师徒中心
                    Intent intent_st=new Intent(getActivity(), ApprenticeListActivity.class);
                    getActivity().startActivity(intent_st);
                break;
            case  R.id.tv_upload://点击网络加载
                if(Utis.isNetworkConnected(getActivity())){
                    mReFreshLayout.setVisibility(View.VISIBLE);
                    initdate();
                }else {
                    Toast("当前无网络，请稍后再试");
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==constance.INTENT.INTO_JQZ_SUCCESS){ //聚钱庄钱转入成功
            Toast.makeText(getActivity(),"转入成功",Toast.LENGTH_SHORT).show();
            getAccountInfo();  //用户余额
        }
    }
}
