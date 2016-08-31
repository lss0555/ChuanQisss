package activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chuanqi.yz.R;
import com.newqm.pointwall.QEarnNotifier;
import com.newqm.pointwall.QSdkManager;

import net.youmi.android.AdManager;
import net.youmi.android.listener.Interface_ActivityListener;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsManager;

import cn.dow.android.DOW;
import cn.dow.android.listener.DLoadListener;
import cn.dow.android.listener.DataListener;
import cn.waps.AppConnect;
import cn.waps.UpdatePointsListener;

public class UnitTaskActivity extends BaseActivity implements View.OnClickListener,QEarnNotifier,UpdatePointsListener {
    private String TAG = UnitTaskActivity.class.toString();
    private RelativeLayout mRtlBack;
    private RelativeLayout mRtlYouMi;
    private RelativeLayout mRtlDianLe;
    private RelativeLayout mRtlDianLu;
    private RelativeLayout mRtlDuoMeng;
    private RelativeLayout mRtlQuMi;
    private RelativeLayout mRtlZhangShangHuDong;
    private RelativeLayout mRtlWanPu;
    private RelativeLayout mRtlSouYing;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_task);
        initview();
        initPlatForm();
    }
    /**
     * 初始化各平台的设置
     */
    private void initPlatForm() {
        /**
         * 多盟   userid 为用户唯一标识,没有用户账户系统的可以不填
         */
        DOW.getInstance(this).init("userid", new DLoadListener() {
            @Override
            public void onSuccess() {
                Log.v(TAG, "积分墙初始化完成");
            }
            @Override
            public void onStart() {
                Log.v(TAG, "积分墙初始化开始");
            }
            @Override
            public void onLoading() {
                Log.v(TAG, "积分墙初始化中...");
            }
            @Override
            public void onFail() {
                Log.v(TAG, "积分墙初始化失败");
            }
        });
//        /**
//         * 有米  初始化接口，应用启动的时候调用，参数：appId, appSecret
//         */
        AdManager.getInstance(this).init("cfdbdd2786ea88ea", "d8edde7d10dd0073");//有米积分墙初始化
//        // 如果使用积分广告，请务必调用积分广告的初始化接口:
        OffersManager.getInstance(this).onAppLaunch();
//        /**
//         * 趣米
//         */
        QSdkManager.init(this, "759ea276225e5855", "1e2bdefa3637f890"); // 初始化  		appid，秘钥 为 积分墙示例程序用key，请替换为你自己的应用的id秘钥
        QSdkManager.getsdkInstance(this).initOfferAd(this); // 缓存积分墙数据
//        /**
//         * 万普  初始化统计器，并通过代码设置APP_ID, APP_PID
//         */
        AppConnect.getInstance("09f277ca386ee99cb4c910e09f562112", "waps", this);
//        // 设置微信平台的AppId，若不适用微信、朋友圈进行分析，则不需要设置
        AppConnect.getInstance(this).setWeixinAppId("wx1e1c162350c09c66", this);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppConnect.getInstance(this).getPoints(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OffersManager.getInstance(getApplicationContext()).onAppExit();//有米广告退出回收
//        // 进行积分订单赚取监听器注册，那这里必须得注销
        AppConnect.getInstance(this).close();//万普退出
    }
    /**
     * 初始化UI
     */
    private void initview() {
        mRtlYouMi = (RelativeLayout) findViewById(R.id.rtl_youmi);
        mRtlDianLe =  (RelativeLayout) findViewById(R.id.rtl_dianle);
        mRtlDianLu = (RelativeLayout) findViewById(R.id.rtl_dianlu);
        mRtlDuoMeng = (RelativeLayout) findViewById(R.id.rtl_duomeng);
        mRtlQuMi = (RelativeLayout) findViewById(R.id.rtl_qumi);
        mRtlZhangShangHuDong = (RelativeLayout) findViewById(R.id.rtl_zhangshanghudong);
        mRtlWanPu = (RelativeLayout) findViewById(R.id.rtl_wanpu);
        mRtlSouYing = (RelativeLayout) findViewById(R.id.rtl_souying);
        mRtlYouMi.setOnClickListener(this);
        mRtlDianLe.setOnClickListener(this);
        mRtlDianLu.setOnClickListener(this);
        mRtlDuoMeng.setOnClickListener(this);
        mRtlQuMi.setOnClickListener(this);
        mRtlZhangShangHuDong.setOnClickListener(this);
        mRtlWanPu.setOnClickListener(this);
        mRtlSouYing.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rtl_youmi://有米积分墙
                OffersManager.getInstance(this).showOffersWall(new Interface_ActivityListener() {
                    @Override
                    public void onActivityDestroy(Context context) {
//                        Toast.makeText(context, "退出有米积分墙", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.rtl_duomeng:
                DOW.getInstance(this).show(this);//多盟积分墙
                break;
            case R.id.rtl_qumi:
                QSdkManager.getsdkInstance().showOffers(UnitTaskActivity.this);//趣米积分墙
                break;
            case R.id.rtl_wanpu:
                AppConnect.getInstance(this).showOffers(this, "12345waps");;//万普
                break;
        }
    }
    /* 趣米积分墙收到回调
     *   查询，增加，减少积分之后，这个接口收到通知
     *   pointTotal:当前积分数
    */
    @Override
    public void getPoints(int i) {

    }
    @Override
    public void getPointsFailed(String s) {

    }
    @Override
    public void earnedPoints(int i, int i1) {

    }

    /**
     * 万普回调接口
     * @param s
     *            虚拟货币名称.
     * @param i
     *            虚拟货币余额.
     */
    @Override
    public void getUpdatePoints(String s, int i) {

    }

    @Override
    public void getUpdatePointsFailed(String s) {

    }
}
