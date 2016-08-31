package Fragments;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chuanqi.yz.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import Constance.constance;
import Utis.OkHttpUtil;
import Utis.Utis;
import Utis.StatusBarUtils;
import Utis.GsonUtils;
import Views.AlwaysMarqueeTextView;
import Views.Banners.Lanner;
import Views.VerticalSwitchTextView;
import Views.ViewPageIndicator;
import activity.ApprenticeListActivity;
import activity.BannerLinkActivity;
import activity.FaskTaskActivity;
import activity.HelpCenterActivity;
import activity.HowToEarn.HowToEarnActivity;
import activity.IntoMoneyJqzActivity;
import activity.JXZActivity;
import activity.NewerTaskActivity;
import activity.OneShopActivity;
import activity.OpenBoxActivity;
import activity.SignActivity;
import activity.Red.TakeRedActivity;
import activity.UnitTaskActivity;
import activity.WithDrawActivity;
import model.Banner.Banner;
import model.Banner.banners;
import model.Notice;
import model.JxzAccount;
import model.TxRecord.jqzcr;
import model.TxRecord.JqzCrRecord;
import model.TxRecord.TxRecord;
import model.TxRecord.tx;
import model.UserMoney;
/**
 * A simple {@link Fragment} subclass. update 2016.8.31
 */
public class HomeFragment extends Fragment implements View.OnClickListener{
    private  List<TxRecord> mTxRecord=new ArrayList<>();
    private  ArrayList<String > mUserId=new ArrayList<>();
    private  List<String > mTimes=new ArrayList<>();
    private List<String > mState=new ArrayList<>();
    private  ArrayList<JqzCrRecord> mCrJqz=new ArrayList<>();
    private  ArrayList<String > mJqzCrUserId=new ArrayList<>();
    private  ArrayList<String > mJqzCrTime=new ArrayList<>();
    private  ArrayList<String > mJqzCrMoney=new ArrayList<>();
    private UserMoney mUserMoney;
    private  ArrayList<JxzAccount> mJxzAccount=new ArrayList<>();
    protected boolean isVisible;
    final ArrayList<String> list = new ArrayList<String>(Arrays.asList("30分钟", "20分钟", "12秒钟", "2小时"));
    int num=100000;
    private boolean IsTouch=true;
    private ViewPager page;
    private ViewPageIndicator viewPageIndicator;
    private AlwaysMarqueeTextView mTtTip;
    private ImageView mImgMine;
    private TextView mTvZhuHe;
    private TextView mTvUnitTask;
    private TextView mTvFastTask;
    private TextView mTvDayShop;
    private VerticalSwitchTextView mTvShow;
    private TextView mTvGongGao;
    private Lanner mLanner;
//    private ArrayList<LannerBean> lannerBeans;
    private List<banners> lannerBeans=new ArrayList<>();
    private TextView mTvTodyIncome;
    private TextView mTvAllIncome;
    private TextView mTvJxzYue;
    private TextView mTvJxzAccrual;
    private VerticalSwitchTextView mTvId;
    private VerticalSwitchTextView mTvId1;
    private VerticalSwitchTextView mTvTime;
    private VerticalSwitchTextView mTvTime1;
    private VerticalSwitchTextView mTvState;
    private VerticalSwitchTextView mTvState1;
    private int UserAccount;//聚钱庄余额
    public static  HomeFragment instance;
    private ReceiveBroadCast receiveBroadCast;

    public static HomeFragment getInstance(){
        if(instance==null){
            instance=new HomeFragment();
        }
        return  instance;
    }
    public HomeFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_home,null);
        initViewPage(layout);
        initview(layout);
        initdate();
        registBrocasts();
        return layout;
    }

    private void registBrocasts() {
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("update");    //只有持有相同的action的接受者才能接收此广播
        getActivity().registerReceiver(receiveBroadCast, filter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(receiveBroadCast);
    }

    public class ReceiveBroadCast extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getBooleanExtra("update",false)){
                getAccountInfo();
            }
        }
    }
    /**
     * inidate
     */
    private void initdate() {
        //公告
        OkHttpUtil.getInstance().Get( constance.URL.GONGGAO, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                showTip(data.toString());
                Notice notice = GsonUtils.parseJSON(data, Notice.class);
                mTvGongGao.setText(""+notice.getContent());
            }
        });
        //用户提现记录
        OkHttpUtil.getInstance().Get(constance.URL.USER_TX, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
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
                initUserTx();
            }
        });
//        //聚钱庄存入记录
        OkHttpUtil.getInstance().Get(constance.URL.JQZ_CR, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                jqzcr mJqzCr = GsonUtils.parseJSON(data, jqzcr.class);
                mCrJqz.clear();
                mJqzCrUserId.clear();
                mJqzCrTime.clear();
                mJqzCrMoney.clear();
                mCrJqz.addAll(mJqzCr.getJqzcr());
                for (int i=0;i<mCrJqz.size();i++){
                    mJqzCrUserId.add("用户"+mCrJqz.get(i).getUserid());
                    mJqzCrTime.add(Utis.getDistanceTime(Utis.getTime(),mCrJqz.get(i).getTdate())+"前");
                    mJqzCrMoney.add("成功转入聚钱庄"+mCrJqz.get(i).getMoney()+"元");
                }
                setJqzCrReord();
            }
        });
        UserAccount();  //用户余额
        JqzAccount();  //聚钱庄余额
    }
    public  void getAccountInfo(){
        UserAccount();  //用户余额
        JqzAccount();  //聚钱庄余额
    }
    private void UserAccount() {   //用户余额
            HashMap<String,String> maps=new HashMap<>();
            maps.put("udid", Utis.getIMEI(getActivity()));
            OkHttpUtil.getInstance().Post(maps, constance.URL.MONEY, new OkHttpUtil.FinishListener() {
                @Override
                public void Successfully(boolean IsSuccess, String data, String Msg) {
                    Log.i("数据",""+data.toString());
//                  showTip(data.toString());
                    UserMoney userMoney = GsonUtils.parseJSON(data, UserMoney.class);
                    UserAccount=Integer.parseInt(userMoney.getfNotPayIncome());
                    mTvAllIncome.setText(userMoney.getfNotPayIncome()+"元");
                    mTvTodyIncome.setText(""+userMoney.getfTodayIncome()+"元");
                }
            });
    }

    private void JqzAccount() { //聚钱庄余额
        HashMap<String,String> maps1=new HashMap<>();
        maps1.put("udid", Utis.getIMEI(getActivity()));
        OkHttpUtil.getInstance().Post(maps1, constance.URL.JQZ_MONEY, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                  Log.i("数据",""+data.toString());
//                showTip(data.toString());
                JxzAccount mJxzAccount =GsonUtils.parseJSON(data, JxzAccount.class);
                mTvJxzYue.setText(mJxzAccount.getYue()+"元");
                mTvJxzAccrual.setText(mJxzAccount.getAccrual()+"元");
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
    private void initUserTx() {
        //显示用户提现信息
        mTvId.setCbInterface(new VerticalSwitchTextView.VerticalSwitchTextViewCbInterface() {
            @Override
            public void showNext(int index) {}
            @Override
            public void onItemClick(int index) {}
        });
        mTvId.setTextContent(mUserId);
        mTvTime.setCbInterface(new VerticalSwitchTextView.VerticalSwitchTextViewCbInterface() {
            @Override
            public void showNext(int index) {}
            @Override
            public void onItemClick(int index) {}
        });
        mTvTime.setTextContent(mTimes);

        mTvState.setCbInterface(new VerticalSwitchTextView.VerticalSwitchTextViewCbInterface() {
            @Override
            public void showNext(int index) {}
            @Override
            public void onItemClick(int index) {}
        });
        mTvState.setTextContent(mState);
    }
    /**
     * 设置聚钱钱存入记录
     */
    public  void setJqzCrReord(){
        mTvId1.setCbInterface(new VerticalSwitchTextView.VerticalSwitchTextViewCbInterface() {
            @Override
            public void showNext(int index) {}
            @Override
            public void onItemClick(int index) {}
        });
        mTvId1.setTextContent(mJqzCrUserId);
        mTvState1.setCbInterface(new VerticalSwitchTextView.VerticalSwitchTextViewCbInterface() {
            @Override
            public void showNext(int index) {}
            @Override
            public void onItemClick(int index) {}
        });
        mTvState1.setTextContent(mJqzCrMoney);
        mTvTime1.setCbInterface(new VerticalSwitchTextView.VerticalSwitchTextViewCbInterface() {
            @Override
            public void showNext(int index) {}
            @Override
            public void onItemClick(int index) {}
        });
        mTvTime1.setTextContent(mJqzCrTime);
    }
    private void initview(View layout) {
        mTvId = (VerticalSwitchTextView) layout.findViewById(R.id.tv_id);
        mTvId1 = (VerticalSwitchTextView) layout.findViewById(R.id.tv_id1);
        mTvTime = (VerticalSwitchTextView) layout.findViewById(R.id.tv_time);
        mTvTime1 = (VerticalSwitchTextView) layout.findViewById(R.id.tv_time1);
        mTvState = (VerticalSwitchTextView) layout.findViewById(R.id.tv_state);
        mTvState1 = (VerticalSwitchTextView) layout.findViewById(R.id.tv_state1);
        mTvGongGao=  (TextView) layout.findViewById(R.id.tv_gonggao);
        mTvTodyIncome = (TextView) layout.findViewById(R.id.tv_today_income);
        mTvAllIncome = (TextView) layout.findViewById(R.id.tv_all_income);
        mTvJxzYue = (TextView) layout.findViewById(R.id.tv_jqzyue);
        mTvJxzAccrual = (TextView) layout.findViewById(R.id.tv_jxz_accrual);
        layout.findViewById(R.id.tv_unit_task).setOnClickListener(this);
        layout.findViewById(R.id.tv_fast_task).setOnClickListener(this);
        layout.findViewById(R.id.tv_day_shop).setOnClickListener(this);
        layout.findViewById(R.id.tv_day_sign).setOnClickListener(this);
        layout.findViewById(R.id.rtl_save_money).setOnClickListener(this);
        layout.findViewById(R.id.tv_newer_task).setOnClickListener(this);
        layout.findViewById(R.id.tv_take_red).setOnClickListener(this);
        layout.findViewById(R.id.rtl_withdraw).setOnClickListener(this);
        layout.findViewById(R.id.tv_jxz).setOnClickListener(this);
        layout.findViewById(R.id.tv_open_box).setOnClickListener(this);
        layout.findViewById(R.id.tv_help_center).setOnClickListener(this);
        layout.findViewById(R.id.rtl_howto_earn).setOnClickListener(this);
        layout.findViewById(R.id.tv_master_center).setOnClickListener(this);
    }
    private void initViewPage(View layout) {
        mLanner= (Lanner) layout.findViewById(R.id.lanner);
        OkHttpUtil.getInstance().Get(constance.URL.BANNER, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                Banner banner = GsonUtils.parseJSON(data, Banner.class);
                lannerBeans.clear();
                lannerBeans.addAll(banner.getGgt());
                mLanner.setLannerBeanList(lannerBeans);
            }
        });
        mLanner.setOnLannerItemClickListener(new Lanner.OnLannerItemClickListener() {
            @Override
            public void click(View v, banners lb) {
                Intent intent=new Intent(getActivity(), BannerLinkActivity.class);
                intent.putExtra("link",lb.getLink());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.tv_fast_task://快速任务
                Intent intent_Task=new Intent(getActivity(), FaskTaskActivity.class);
                getActivity().startActivity(intent_Task);
                break;
            case  R.id.tv_unit_task://联盟任务
                Intent intent=new Intent(getActivity(), UnitTaskActivity.class);
                getActivity().startActivity(intent);
                break;
            case  R.id.tv_day_shop://每日夺宝
                Intent intent_shop=new Intent(getActivity(), OneShopActivity.class);
                getActivity().startActivity(intent_shop);
                break;
            case  R.id.tv_day_sign://每日签到
                Intent intent_sign=new Intent(getActivity(), SignActivity.class);
                getActivity().startActivity(intent_sign);
                break;
            case  R.id.rtl_save_money://转入钱庄
                Intent intent_store=new Intent(getActivity(), IntoMoneyJqzActivity.class);
                intent_store.putExtra("money",UserAccount);
                getActivity().startActivityForResult(intent_store,12);
                break;
            case  R.id.tv_newer_task://新手任务
                Intent intent_newerTask=new Intent(getActivity(), NewerTaskActivity.class);
                getActivity().startActivity(intent_newerTask);
                break;
            case  R.id.tv_take_red://抢红包
                Intent intent_take_red=new Intent(getActivity(), TakeRedActivity.class);
                getActivity().startActivity(intent_take_red);
                break;
            case  R.id.rtl_withdraw://提现
                Intent intent_tx=new Intent(getActivity(), WithDrawActivity.class);
                getActivity().startActivity(intent_tx);
                break;
            case  R.id.tv_jxz://抢红包
                Intent intent_jxz=new Intent(getActivity(), JXZActivity.class);
                getActivity().startActivity(intent_jxz);
                break;
            case  R.id.tv_open_box://开宝箱
                Intent intent_openbox=new Intent(getActivity(), OpenBoxActivity.class);
                getActivity().startActivity(intent_openbox);
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
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==constance.INTENT.INTO_JQZ_SUCCESS){ //聚钱庄钱转入成功
            Toast.makeText(getActivity(),"转入成功",Toast.LENGTH_SHORT).show();
            UserAccount();  //用户余额
            JqzAccount();
        }
    }
}
