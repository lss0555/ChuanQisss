package activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.chuanqi.yz.R;
import java.util.HashMap;
import Constance.constance;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Utis.SharePre;
import Utis.Utis;
import Utis.MD5Utis;
import app.AndroidSystem;
import model.JxzAccount;
import model.Result;
import model.WxWithDraw;
public class WithDrawActivity extends BaseActivity {
    private final  int WXIN_PAY=1;
    private final  int ALI_PAY=2;
    private final  int PHONE_PAY=3;
    private final  int INTO_JQZ=4;
    private int payType;//支付方式
    private RadioButton mRbIntoJqz;
    private RadioButton mRbPhone;
    private RadioButton mRbAlipay;
    private RadioButton mRbWxin;
    private RelativeLayout mRtlComplite;
    private TextView mTvPrice;
    private EditText mEtPrice;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        initview();
        initdate();
        initRadioButton();
        saveUserDevice();
        initevent();
    }
    /**
     * 保存用户的设备信息
     */
    private void saveUserDevice() {
        Log.i("===当前地点bbbbb",""+SharePre.getCity(getApplicationContext()));
        if(!SharePre.getIsPostDevice(getApplicationContext())){
            HashMap<String,String> map=new HashMap<>();
            map.put("userid",""+SharePre.getUserId(getApplicationContext()));
            map.put("devicename",""+ android.os.Build.PRODUCT);
            map.put("version",""+android.os.Build.VERSION.RELEASE);
            map.put("model",""+android.os.Build.MODEL);
            map.put("ip",""+ AndroidSystem.getIP(getApplicationContext()));
            map.put("city",""+SharePre.getCity(getApplicationContext()));
            OkHttpUtil.getInstance().Post(map, constance.URL.DEVICE, new OkHttpUtil.FinishListener() {
                @Override
                public void Successfully(boolean IsSuccess, String data, String Msg) {
                    Log.i("设备信息",""+AndroidSystem.getIP(getApplicationContext())+android.os.Build.PRODUCT);
                    Log.i("保存用户设备信息=====",""+data.toString());
                    if(IsSuccess){
                        Result result = GsonUtils.parseJSON(data, Result.class);
                        if(result.getRun().equals("1")){
                            SharePre.saveIsPostDevice(getApplicationContext(),true);
                        }
                    }else {
                        Toast(data.toString());
                    }
                }
            });
        }
    }
    private void initdate() {
        mTvPrice.setText(""+getIntent().getDoubleExtra("money",0));
    }
    private void initview() {
        mTvPrice = (TextView) findViewById(R.id.tv_price);
        mEtPrice = (EditText) findViewById(R.id.et_price);
        mRbWxin = (RadioButton) findViewById(R.id.rb_wxpay);
        mRbAlipay = (RadioButton) findViewById(R.id.rb_alipay);
        mRbPhone = (RadioButton) findViewById(R.id.rb_phone);
        mRbIntoJqz = (RadioButton) findViewById(R.id.rb_into_jqz);
        mRtlComplite = (RelativeLayout) findViewById(R.id.rtl_complite);
    }
    /**
     * 初始化RadioButton
     */
    private void initRadioButton() {
        mRbWxin.setChecked(true);
        mRbWxin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mRbAlipay.setChecked(false);
                    mRbIntoJqz.setChecked(false);
                    mRbPhone.setChecked(false);
                }
            }
        });
        mRbAlipay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRbIntoJqz.setChecked(false);
                    mRbWxin.setChecked(false);
                    mRbPhone.setChecked(false);
                }
            }
        });
        mRbPhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRbIntoJqz.setChecked(false);
                    mRbWxin.setChecked(false);
                    mRbAlipay.setChecked(false);
                }
            }
        });
        mRbIntoJqz.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRbPhone.setChecked(false);
                    mRbWxin.setChecked(false);
                    mRbAlipay.setChecked(false);
                }
            }
        });
    }

    private void initevent() {
        mRtlComplite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRbWxin.isChecked()){
                    if(mEtPrice.getText().toString().equals("")){
                        Toast("请输入提现金额");
                    }
                    else if(Double.valueOf(mEtPrice.getText().toString().trim())<1){
                        Toast("抱歉，请输入金额1元以上");
                    }
                    else{
                        WxWithDraw(mEtPrice.getText().toString().trim());
                    }
                }else if(mRbAlipay.isChecked()){
                    showTip("支付宝支付");
                }else if(mRbIntoJqz.isChecked()){
                    showTip("转入聚钱庄");
                }
            }
        });
    }
    public  void  WxWithDraw(String price){
        startProgressDialog("疯狂加载中...");
        HashMap<String,String> maps1=new HashMap<>();
        maps1.put("userid", SharePre.getUserId(WithDrawActivity.this));
        maps1.put("jine", price);
        maps1.put("sign", ""+ MD5Utis.MD5_Encode(SharePre.getUserId(getApplicationContext())+price+"传祺chuanqi"));
        OkHttpUtil.getInstance().Post(maps1, constance.URL.WX_WITHDRAW, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                stopProgressDialog();
                    Log.i("提现数据",""+data.toString());
                if(IsSuccess){
                    WxWithDraw wxWithDraw = GsonUtils.parseJSON(data, WxWithDraw.class);
                    if(wxWithDraw.getRun().equals("1")){
                         Toast("恭喜您，提现成功");
//                        Intent intent = new Intent();
//                        intent.putExtra(constance.INTENT.UPDATE_ADD_USER_MONEY,true);
//                        intent.setAction(constance.INTENT.UPDATE_ADD_USER_MONEY);   //
//                        sendBroadcast(intent);   //
                         finish();
                    }else if(wxWithDraw.getRun().equals("2")){
                        Toast("提现失败,请联系客服");
                    }else if(wxWithDraw.getRun().equals("3")){
                        Toast("抱歉，您的余额不足");
                    } else if(wxWithDraw.getRun().equals("0")){
                        Toast("抱歉，您还未绑定微信");
                    } else if(wxWithDraw.getRun().equals("5")){
                        Toast("抱歉，非法操作");
                    }  else if(wxWithDraw.getRun().equals("4")){
                        Toast("抱歉，首次提现不足两元");
                    }
                    else if(wxWithDraw.getRun().equals("6")){
                        Toast("抱歉，账户异常");
                    }
                    else if(wxWithDraw.getRun().equals("7")){
                        Toast("抱歉，账户异常");
                    }
                    else if(wxWithDraw.getRun().equals("8")){
                        Toast("抱歉，账户异常");
                    }else if(wxWithDraw.getRun().equals("9")){
                        Toast("抱歉，账户异常");
                    }
                }
            }
        });
    }
}
