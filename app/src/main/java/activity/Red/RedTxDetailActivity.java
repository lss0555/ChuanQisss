package activity.Red;

import android.content.Intent;
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
import activity.BaseActivity;
import model.WxWithDraw;
import model.YiZhuanRed;

public class RedTxDetailActivity extends BaseActivity {
    private final  int WXIN_PAY=1;
    private final  int ALI_PAY=2;
    private final  int INTO_JQZ=3;
    private int payType;//支付方式
    private RadioButton mRbIntoJqz;
    private RadioButton mRbAlipay;
    private RadioButton mRbWxin;
    private RelativeLayout mRtlComplite;
    private TextView mTvDetail;
    private EditText mEtPrice;
    private TextView mTvPrice;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_detail);
        initview();
        initdate();
        initRadioButton();
        initevent();
    }
    private void initdate() {
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
                        mTvPrice.setText("0.0");
                    }else {
                        mTvPrice.setText(""+yiZhuanRed.getYue());
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }


    private void initview() {
        mEtPrice = (EditText) findViewById(R.id.et_price);
        mRbWxin = (RadioButton) findViewById(R.id.rb_wxpay);
        mRbAlipay = (RadioButton) findViewById(R.id.rb_alipay);
        mRbIntoJqz = (RadioButton) findViewById(R.id.rb_into_jqz);
        mRtlComplite = (RelativeLayout) findViewById(R.id.rtl_complite);
        mTvDetail = (TextView) findViewById(R.id.tv_detail);
        mTvPrice = (TextView) findViewById(R.id.tv_price);
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
                }
            }
        });
        mRbAlipay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRbIntoJqz.setChecked(false);
                    mRbWxin.setChecked(false);
                }
            }
        });
        mRbIntoJqz.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
                    else if((Integer.parseInt(mEtPrice.getText().toString().trim()))%100!=0){
                        Toast("请输入的金额为100的倍数");
                    }
                    else {
                        WxWithDraw(mEtPrice.getText().toString().trim());
                    }
                }else if(mRbAlipay.isChecked()){
                    showTip("支付宝支付");
                }else if(mRbIntoJqz.isChecked()){
                    showTip("转入聚钱庄");
                }
            }
        });
        mTvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),LookRedRecordActivity.class);
                startActivity(intent);
            }
        });
    }
    public  void  WxWithDraw(String price){
        startProgressDialog("疯狂加载中...");
        HashMap<String,String> maps1=new HashMap<>();
        maps1.put("userid", SharePre.getUserId(getApplication()));
        maps1.put("jine", price);
        OkHttpUtil.getInstance().Post(maps1, constance.URL.RED_WITHDRAW, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                stopProgressDialog();
                Log.i("数据",""+data.toString());
//                    showTip(data.toString());
                if(IsSuccess){
                    WxWithDraw wxWithDraw = GsonUtils.parseJSON(data, WxWithDraw.class);
                    if(wxWithDraw.getRun().equals("1")){
                        Toast("恭喜您，提现成功");
                        Intent intent = new Intent();
                        intent.putExtra(constance.INTENT.UPDATE_ADD_USER_MONEY,true);
                        intent.setAction(constance.INTENT.UPDATE_ADD_USER_MONEY);   //
                        sendBroadcast(intent);   //发送广播
                        finish();
                    }else {
                        Toast("抱歉，提现失败");
                    }
                }
            }
        });
    }
}
