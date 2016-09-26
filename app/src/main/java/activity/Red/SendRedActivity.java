package activity.Red;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.chuanqi.yz.R;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;
import Constance.constance;
import Utis.Utis;
import Utis.SharePre;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import activity.BaseActivity;
import model.Result;
import model.UserMoney;
import model.YiZhuanRed;
import model.wxpays;

public class SendRedActivity extends BaseActivity {
    private final  int RED_NOMRAL=1;
    private final  int RED_SUPER=2;
    private final  int YIZUANRED_PAY=3;
    private final  int YUE_PAY=4;
    private final  int WXIN_PAY=5;
    private final  int ALI_PAY=6;
    private int redType;//红包类型 100 200
    private int payType;//支付方式
    private TextView mTvYue;
    private TextView mTvYiZuan;
    private RadioButton mRb_200;
    private RadioButton mRb_100;
    private RadioButton mRbAlipay;
    private RadioButton mRbWxin;
    private RadioButton mRbYue;
    private RadioButton mRbYiZuan;
    private RelativeLayout mRtlPay;
    private int Yue;
    private double YiZhuanYue;
    private RelativeLayout mRtlWxPay;
    private RelativeLayout mRtlAliPay;
    private PayReq req;
    private wxpays mWxPayInfo;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private RelativeLayout mRtlUpdate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_red);
        initview();
        initRadioButton();
        initdate();
        initevent();
    }
    /**
     * 初始化数据
     */
    private void initdate() {
        YueDate();
        YiZuanDate();
    }
    /**
     * 易赚红余额
     */
    private void YiZuanDate() {
        startProgressDialog("请稍后...");
        HashMap<String,String> maps=new HashMap<>();
        maps.put("userid", SharePre.getUserId(getApplicationContext()));
        OkHttpUtil.getInstance().Post(maps, constance.URL.YIZUAN_RED, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                stopProgressDialog();
                if(IsSuccess){
                    YiZhuanRed yiZhuanRed = GsonUtils.parseJSON(data, YiZhuanRed.class);
                    if(yiZhuanRed.getYue()==null || yiZhuanRed.getYue().equals("null")|| yiZhuanRed.getYue().equals("")){
                        mTvYiZuan.setText("余额:"+"0.0元");
                    }else {
                        mTvYiZuan.setText("余额:"+yiZhuanRed.getYue()+"元");
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }

    /**
     * 用户余额
     */
    private void YueDate() {
        startProgressDialog("加载中...");
        HashMap<String,String> maps=new HashMap<>();
        maps.put("udid", Utis.getIMEI(getApplicationContext()));
        OkHttpUtil.getInstance().Post(maps, constance.URL.MONEY, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                stopProgressDialog();
                Log.i("数据",""+data.toString());
                if(IsSuccess){
                    UserMoney userMoney = GsonUtils.parseJSON(data, UserMoney.class);
                    if(userMoney.getfNotPayIncome()==null || userMoney.getfNotPayIncome().equals("null")|| userMoney.getfNotPayIncome().equals("")){
                        mTvYue.setText("余额:"+"0.0元");
                    }else {
                        mTvYue.setText("余额:"+userMoney.getfNotPayIncome()+"元");
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }

    private void initview() {
        mTvYue = (TextView) findViewById(R.id.tv_yue);
        mTvYiZuan = (TextView) findViewById(R.id.tv_yizuan);
        mRb_100 = (RadioButton) findViewById(R.id.rb_100);
        mRb_200 = (RadioButton) findViewById(R.id.rb_200);
        mRbYiZuan = (RadioButton) findViewById(R.id.rb_yizuyan);
        mRbYue = (RadioButton) findViewById(R.id.rb_yue);
        mRbWxin = (RadioButton) findViewById(R.id.rb_wxin);
        mRbAlipay = (RadioButton) findViewById(R.id.rb_alipay);
        mRtlPay = (RelativeLayout) findViewById(R.id.rtl_pay);
        mRtlWxPay = (RelativeLayout) findViewById(R.id.rtl_wxpay);
        mRtlAliPay = (RelativeLayout) findViewById(R.id.rtl_alipay);
        mRtlUpdate = (RelativeLayout) findViewById(R.id.rtl_update);
    }


    /**
     * 初始化RadioButton
     */
    private void initRadioButton() {
        mRb_100.setChecked(true);
        mRtlUpdate.setVisibility(View.GONE);
        mRbYiZuan.setChecked(true);
        mRbYiZuan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mRbYue.setChecked(false);
                    mRbWxin.setChecked(false);
                    mRbAlipay.setChecked(false);
                }
            }
        });
        mRbYue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRbYiZuan.setChecked(false);
                    mRbWxin.setChecked(false);
                    mRbAlipay.setChecked(false);
                }
            }
        });
        mRbWxin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRbYiZuan.setChecked(false);
                    mRbYue.setChecked(false);
                    mRbAlipay.setChecked(false);
                }
            }
        });
        mRbAlipay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRbYiZuan.setChecked(false);
                    mRbYue.setChecked(false);
                    mRbWxin.setChecked(false);
                }
            }
        });
        mRb_100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRb_200.setChecked(false);
                    mRtlAliPay.setVisibility(View.VISIBLE);
                    mRtlWxPay.setVisibility(View.VISIBLE);
                    mRtlAliPay.setVisibility(View.GONE);
                    mRtlUpdate.setVisibility(View.GONE);
                }
            }
        });
        mRb_200.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRb_100.setChecked(false);
                    mRtlAliPay.setVisibility(View.GONE);
                    mRtlWxPay.setVisibility(View.GONE);
                    mRtlUpdate.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void initevent() {
        mRtlUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),UpdateRedActivity.class);
                startActivity(intent);
            }
        });
        mRtlPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRb_100.isChecked()){
                    redType=RED_NOMRAL;
//                    mRbAlipay.setVisibility(View.GONE);
                }else {
                    redType=RED_SUPER;
//                    mRtlUpdate.setVisibility(View.VISIBLE);
                }
                if(mRbYiZuan.isChecked()){
                    payType=YIZUANRED_PAY;
                }else if(mRbYue.isChecked()){
                    payType=YUE_PAY;
                }else if(mRbWxin.isChecked()){
                    payType=WXIN_PAY;
                } else if(mRbAlipay.isChecked()){
                    payType=ALI_PAY;
                }
                switch (redType){
                    case RED_NOMRAL://100红包
                        switch (payType){
                            case YIZUANRED_PAY://易赚支付
                                PayYizhuanRed2RedPool(100);
                                break;
                            case YUE_PAY://余额支付
                                PayYue2RedPool(100);
                                break;
                            case WXIN_PAY://微信支付
                                WxPay("200");
//                                msgApi.registerApp("wx541c42bc54fac5cf");
//                                req = new PayReq();
//                                req.appId = "wx541c42bc54fac5cf";
//                                req.nonceStr = "gLhTDMV6nizf73xOUhGJnEluAqn49i8V";
//                                req.packageValue = "Sign=WXPay";
//                                req.partnerId = "1390772902";
//                                req.prepayId = "wx20160925153034ba0fa16cf40717411342";
//                                req.timeStamp = "1474788635";
//                                req.sign ="F670CBC7F6B29134A4ADEBE12DFAA7B2";
//                                msgApi.sendReq(req);
                                break;
                            case ALI_PAY://支付宝支付
                                Toast("红包100   支付宝支付");
                                break;
                        }
                        break;
                    case RED_SUPER://200红包
                        switch (payType){
                            case YIZUANRED_PAY://易赚红包支付
                                PayYizhuanRed2RedPool(500);
                                break;
                            case YUE_PAY://余额支付
                                PayYue2RedPool(500);
                                break;
                            case WXIN_PAY://微信支付
                                WxPay("0.01");
                                break;
                            case ALI_PAY://支付宝支付
                                Toast("红包200   支付宝支付");
                                break;
                        }
                        break;
                }
            }
        });
    }
    public  void  PayYue2RedPool(int price){
//        if(price<=Yue){
            startProgressDialog("正在支付中...");
            HashMap<String,String> map=new HashMap<>();
            map.put("userid", SharePre.getUserId(getApplicationContext()));
            map.put("jine",""+price);
            OkHttpUtil.getInstance().Post(map, constance.URL.PAYYUE2REDPOOL, new OkHttpUtil.FinishListener() {
                @Override
                public void Successfully(boolean IsSuccess, String data, String Msg) {
                    stopProgressDialog();
                    Result result = GsonUtils.parseJSON(data, Result.class);
                    if(result.getRun().equals("1")){
                        Toast("恭喜您，支付成功");
                        YueDate();
                    }else if(result.getRun().equals("2")){
                        Toast("抱歉，您的余额不足");
                    }else if(result.getRun().equals("0")){
                        Toast("抱歉，支付失败");
                    }else if(result.getRun().equals("3")){
                        Toast("抱歉，您今天已经充值过");
                    }else if(result.getRun().equals("4")){
                        Toast("提示:还有未抢完的红包，等抢完在发");
                    }
                }
            });
//        }else {
//            showTip("抱歉，您的余额不足");
//        }
    }
    public  void  PayYizhuanRed2RedPool(int price){
//        if(price<=YiZhuanYue){
            startProgressDialog("正在支付中...");
            HashMap<String,String> map=new HashMap<>();
            map.put("userid", SharePre.getUserId(getApplicationContext()));
            map.put("jine",""+price);
            OkHttpUtil.getInstance().Post(map, constance.URL.PAY_YIZUANRED2REDPOOL, new OkHttpUtil.FinishListener() {
                @Override
                public void Successfully(boolean IsSuccess, String data, String Msg) {
                    stopProgressDialog();
                    if(IsSuccess){
                        Result result = GsonUtils.parseJSON(data, Result.class);
                        if(result.getRun().equals("1")){
                            Toast("恭喜您，支付成功");
                            YiZuanDate();
                        }else if(result.getRun().equals("2")){
                            Toast("抱歉，您的余额不足");
                        }else if(result.getRun().equals("0")){
                            Toast("抱歉，支付失败");
                        }else if(result.getRun().equals("3")){
                            Toast("抱歉，您今天已经充值过");
                        }else if(result.getRun().equals("4")){
                            Toast("提示:还有未抢完的红包，等抢完在发");
                        }
                    }else {
                        Toast(data.toString());
                    }
                }
            });
    }
    /**
     * 微信支付
     * @param price
     */
    public  void WxPay(String price){
        startProgressDialog("支付请求中...");
        HashMap<String,String> map=new HashMap<>();
        map.put("totalfee",price);
        map.put("userid",""+SharePre.getUserId(getApplicationContext()));
        OkHttpUtil.getInstance().Post(map, constance.URL.WX_PAY, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                   stopProgressDialog();
//                showTip(data.toString());
                if(IsSuccess){
                    wxpays wxpays = GsonUtils.parseJSON(data, wxpays.class);
                    if(wxpays.getRun().equals("")){

                    }
                    Log.i("微信支付",""+data.toString());
                    msgApi.registerApp("wx541c42bc54fac5cf");
                    req = new PayReq();
                    req.appId = "wx541c42bc54fac5cf";
                    req.nonceStr = wxpays.getNoncestr();
                    req.packageValue = wxpays.getPackage_();
                    req.partnerId = wxpays.getPartnerid();
                    req.prepayId = wxpays.getPrepayid();
                    req.timeStamp = wxpays.getTimestamp();
                    req.sign =wxpays.getSign();
                    msgApi.sendReq(req);
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
}
