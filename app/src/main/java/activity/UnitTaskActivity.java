package activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.chuanqi.yz.R;
import com.qm.pw.Conn;
import com.yql.dr.sdk.DRScoreInterface;
import com.yql.dr.sdk.DRSdk;
import com.yzhuanatm.DevInit;
import com.yzhuanatm.GetOnlineParamsListener;
import net.youmi.android.AdManager;
import net.youmi.android.listener.Interface_ActivityListener;
import net.youmi.android.offers.EarnPointsOrderList;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsChangeNotify;
import net.youmi.android.offers.PointsEarnNotify;
import net.youmi.android.offers.PointsManager;

import Utis.SharePre;
import cn.dow.android.DOW;
import cn.dow.android.listener.DLoadListener;
import cn.waps.AppConnect;
import cn.waps.UpdatePointsListener;

public class UnitTaskActivity extends BaseActivity implements View.OnClickListener,UpdatePointsListener,DRScoreInterface ,GetOnlineParamsListener , PointsChangeNotify, PointsEarnNotify {
    private String TAG = UnitTaskActivity.class.toString();
    private RelativeLayout mRtlBack;
    private RelativeLayout mRtlYouMi;
    private RelativeLayout mRtlDianLe;
    private RelativeLayout mRtlDianLu;
    private RelativeLayout mRtlWanPu;
    private UnitTaskActivity me;
    private RelativeLayout mRtlDuoMeng;
    private RelativeLayout mRtlQumi;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_task);
        me=this;
        initview();
        initPlatForm();
    }
    /**
     * 初始化各平台的设置
     */
    private void initPlatForm() {
        Conn.getInstance(UnitTaskActivity.this).set("4bfd4db0554503c2", "53e7105b377ac6e5",SharePre.getUserId(getApplicationContext()));//趣米
        Conn.getInstance(UnitTaskActivity.this).setEnListener(new com.qm.lo.inter.QEarnNotifier() {
            @Override
            public void getPoints(float v) {
            }
            @Override
            public void getPointsFailed(String s) {
            }
            @Override
            public void earnedPoints(float v, float v1) {
            }
        });//设置积分获取回调
        //userid不能写死，可以为空值多盟
        DOW.getInstance(this).init(SharePre.getUserId(getApplicationContext()));
        DOW.getInstance(this).init(new DLoadListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Log.e("多盟","成功");
            }
            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                Log.e("多盟","加载开始");
            }
            @Override
            public void onLoading() {
                // TODO Auto-generated method stub
                Log.e("多盟","加载中");
            }
            @Override
            public void onFail() {
                // TODO Auto-generated method stub
                Log.e("多盟","失败");
            }
        });
//        /**
//         * 有米  初始化接口，应用启动的时候调用，参数：appId, appSecret
//         */
        AdManager.getInstance(this).init("89c4d15bcfe42455", "5929d7270930ad09");//有米积分墙初始化
        OffersManager.getInstance(this).setUsingServerCallBack(true);
        OffersManager.getInstance(this).setCustomUserId(SharePre.getUserId(getApplicationContext()));
        OffersManager.getInstance(this).onAppLaunch();
//        /**
//         * 万普  初始化统计器，并通过代码设置APP_ID, APP_PID
//         */
        AppConnect.getInstance("ea334c1846508fdb85929c861aa327b0", "waps", this);
//        // 设置微信平台的AppId，若不适用微信、朋友圈进行分析，则不需要设置
        AppConnect.getInstance(this).setWeixinAppId("wx541c42bc54fac5cf", this);
        //点入
        DRSdk.initialize(this, true, ""); // 建议在应用启动调用，初始化sdk
        DRSdk.setUserId(SharePre.getUserId(UnitTaskActivity.this)); // 设置用户id
        //点乐
        DevInit.initGoogleContext(UnitTaskActivity.this, "0d2d5326dcf0985530a1a413aac31f6b");
        DevInit.setCurrentUserID(UnitTaskActivity.this,SharePre.getUserId(UnitTaskActivity.this));
    }
    @Override
    public void onResume() {
        super.onResume();
        AppConnect.getInstance(this).getPoints(this);
//        DevInit.getTotalMoney(this,me);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        OffersManager.getInstance(getApplicationContext()).onAppExit();//有米广告退出回收
        PointsManager.getInstance(this).unRegisterNotify(this);
        PointsManager.getInstance(this).unRegisterPointsEarnNotify(this);
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
        mRtlDuoMeng = (RelativeLayout) findViewById(R.id.rtl_duomeng);
        mRtlQumi = (RelativeLayout) findViewById(R.id.rtl_qumi);
        mRtlQumi.setOnClickListener(this);
        mRtlYouMi.setOnClickListener(this);
        mRtlDianLe.setOnClickListener(this);
        mRtlDianLu.setOnClickListener(this);
        mRtlWanPu.setOnClickListener(this);
        mRtlDuoMeng.setOnClickListener(this);
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
            case R.id.rtl_wanpu:   //万普
                AppConnect.getInstance(this).showOffers(this,SharePre.getUserId(getApplicationContext()));;//万普
                break;
            case R.id.rtl_dianlu:  //点入
                DRSdk.showOfferWall(UnitTaskActivity.this, DRSdk.DR_OFFER);
                break;
            case R.id.rtl_dianle: //点乐
                DevInit.showOffers(this);
                break;
            case R.id.rtl_duomeng: //多盟
                DOW.getInstance(UnitTaskActivity.this).show(this);
                break;
            case R.id.rtl_qumi: //趣米
                Conn.getInstance(this).launch();
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
    /****************************************************/
	/* 点入获取积分回调 */
	/* score : 积分 */
    /****************************************************/
    @Override
    public void scoreResultCallback(int score) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this,
                AlertDialog.THEME_HOLO_LIGHT);
        dialog.setTitle("显示积分结果");
        dialog.setMessage("" + score);
        dialog.setPositiveButton("确定", null);
        dialog.setCancelable(false);
        dialog.show();
    }
    /****************************************************/
	/* 点入消费积分回调 */
	/* status : true 消费成功, false 消费失败 */
    /****************************************************/
    @Override
    public void spendScoreCallback(boolean status) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this,
                AlertDialog.THEME_HOLO_LIGHT);
        dialog.setTitle("消费积分结果");
        dialog.setMessage(status ? "消费成功" : "余额不足");
        dialog.setPositiveButton("确定", null);
        dialog.show();
    }
    /**
     * 点乐 总金额回调 获取自定义在线参数，返回自定义的参数值，无网或请求失败返回的是实时的值，无网或请求失败返回本地保存的值。
     * @param s
     */
    private long amount = 0l, startTime = 0l;
    @Override
    public void onParamsReturn(String s) {
        Log.w("result", ">>>>>>>><<<<<"
                + (System.currentTimeMillis() - startTime) + ">>>>>>>><<<<<");
    }

    /**
     * 有米的回调接口
     * @param v
     */
    @Override
    public void onPointBalanceChange(float v) {
    }
    @Override
    public void onPointEarn(Context context, EarnPointsOrderList earnPointsOrderList) {
    }
}
