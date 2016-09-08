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

public class UnitTaskActivity extends BaseActivity implements View.OnClickListener,UpdatePointsListener {
    private String TAG = UnitTaskActivity.class.toString();
    private RelativeLayout mRtlBack;
    private RelativeLayout mRtlYouMi;
    private RelativeLayout mRtlDianLe;
    private RelativeLayout mRtlDianLu;
    private RelativeLayout mRtlWanPu;
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
//        /**
//         * 有米  初始化接口，应用启动的时候调用，参数：appId, appSecret
//         */
        AdManager.getInstance(this).init("89c4d15bcfe42455", "5929d7270930ad09");//有米积分墙初始化
//        // 如果使用积分广告，请务必调用积分广告的初始化接口:
        OffersManager.getInstance(this).onAppLaunch();
//        /**
//         * 万普  初始化统计器，并通过代码设置APP_ID, APP_PID
//         */
        AppConnect.getInstance("ea334c1846508fdb85929c861aa327b0", "waps", this);
//        // 设置微信平台的AppId，若不适用微信、朋友圈进行分析，则不需要设置
        AppConnect.getInstance(this).setWeixinAppId("wxc9302606e8330dbd", this);
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
        mRtlWanPu = (RelativeLayout) findViewById(R.id.rtl_wanpu);
        mRtlYouMi.setOnClickListener(this);
        mRtlDianLe.setOnClickListener(this);
        mRtlDianLu.setOnClickListener(this);
        mRtlWanPu.setOnClickListener(this);
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
            case R.id.rtl_wanpu:
                AppConnect.getInstance(this).showOffers(this, "12345waps");;//万普
                break;
        }
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
