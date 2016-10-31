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

import java.util.HashMap;

import Constance.constance;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Utis.SharePre;
import Utis.MD5Utis;
import Utis.Utis;
import activity.BaseActivity;
import model.Result;
import model.UserMoney;
import model.YiZhuanRed;

public class UpdateRedActivity extends BaseActivity {
    private final  int YIZUANRED_PAY=1;
    private final  int YUE_PAY=2;
    private int payType;//支付方式
    private TextView mTvYue;
    private TextView mTvYiZuan;
    private RadioButton mRbYue;
    private RadioButton mRbYiZuan;
    private RelativeLayout mRtlPay;
    private int Yue;
    private int YiZhuanYue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_red);
        initview();
        initRadioButton();
        initdate();
        initevent();
    }
    private void initview() {
        mTvYue = (TextView) findViewById(R.id.tv_yue);
        mTvYiZuan = (TextView) findViewById(R.id.tv_yizuan);
        mRbYiZuan = (RadioButton) findViewById(R.id.rb_yizuyan);
        mRbYue = (RadioButton) findViewById(R.id.rb_yue);
        mRtlPay = (RelativeLayout) findViewById(R.id.rtl_pay);
        mRbYiZuan.setChecked(true);
    }
    /**
     * 余额与易赚红包的数据
     */
    private void initdate() {
        startProgressDialog("玩命加载中...");
        HashMap<String,String> map=new HashMap<>();
        map.put("userid", SharePre.getUserId(UpdateRedActivity.this));
        OkHttpUtil.getInstance().Post(map, constance.URL.YIZUAN_RED, new OkHttpUtil.FinishListener() {
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
                } else {
                    Toast(data.toString());
                }
            }
        });
        HashMap<String,String> maps=new HashMap<>();
        maps.put("udid", Utis.getIMEI(getApplicationContext()));
        OkHttpUtil.getInstance().Post(maps, constance.URL.MONEY, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    Log.i("数据",""+data.toString());
                    UserMoney userMoney = GsonUtils.parseJSON(data, UserMoney.class);
                    if(userMoney.getfNotPayIncome()==null || userMoney.getfNotPayIncome().equals("null")||userMoney.getfNotPayIncome().equals("")){
                        mTvYue.setText("余额:"+"0.0元");
                    }else {
                        mTvYue.setText("余额:"+userMoney.getfNotPayIncome()+"元");
                    }
                } else {
                    Toast(data.toString());
                }
            }
        });
    }
    /**
     * 初始化RadioButton
     */
    private void initRadioButton() {
        mRbYiZuan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mRbYue.setChecked(false);
                }
            }
        });
        mRbYue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRbYiZuan.setChecked(false);
                }
            }
        });
    }
    private void initevent() {
        mRtlPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRbYiZuan.isChecked()){
                    payType=YIZUANRED_PAY;
                    UpdateType("1");
                }else if(mRbYue.isChecked()){
                    payType=YUE_PAY;
                    UpdateType("2");
                }
            }
        });
    }
    public  void  UpdateType(String type){
        startProgressDialog("加载中...");
        HashMap<String,String> map=new HashMap<>();
        map.put("userid",SharePre.getUserId(UpdateRedActivity.this));
        map.put("style",""+type);
        map.put("sign",""+ MD5Utis.MD5_Encode(SharePre.getUserId(getApplicationContext())+type+"传祺chuanqi"));
        OkHttpUtil.getInstance().Post(map, constance.URL.SJ_RED, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                stopProgressDialog();
                 if(IsSuccess){
                     Result result = GsonUtils.parseJSON(data, Result.class);
                     if (result.getRun().equals("1")){
                         Toast("恭喜您，升级成功");
                         Intent intent = new Intent();
                         intent.putExtra(constance.INTENT.UPDATE_ADD_USER_MONEY,true);
                         intent.setAction(constance.INTENT.UPDATE_ADD_USER_MONEY);   //
                         sendBroadcast(intent);//发送广播
                         setResult(1);
                         finish();
                     }else if(result.getRun().equals("2")){
                         Toast("抱歉，非法操作");
                     } else{
                         Toast("对不起，您的账户余额不足");
                     }
                 }else {
                     showTip(data.toString());
                 }
            }
        });
    }
}
