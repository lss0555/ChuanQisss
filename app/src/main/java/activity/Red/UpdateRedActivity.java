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
import Utis.Utis;
import activity.BaseActivity;
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
    }
    /**
     * 余额与易赚红包的数据
     */
    private void initdate() {
        HashMap<String,String> map=new HashMap<>();
        map.put("userid", SharePre.getUserId(getApplicationContext()));
        OkHttpUtil.getInstance().Post(map, constance.URL.YIZUAN_RED, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                showTip(data.toString());
                YiZhuanRed yiZhuanRed = GsonUtils.parseJSON(data, YiZhuanRed.class);
                YiZhuanYue=Integer.parseInt(yiZhuanRed.getYue());
                mTvYiZuan.setText("余额:"+YiZhuanYue+"元");
            }
        });
        HashMap<String,String> maps=new HashMap<>();
        maps.put("udid", Utis.getIMEI(getApplicationContext()));
        OkHttpUtil.getInstance().Post(maps, constance.URL.MONEY, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                Log.i("数据",""+data.toString());
                UserMoney userMoney = GsonUtils.parseJSON(data, UserMoney.class);
                Yue=Integer.parseInt(userMoney.getfNotPayIncome());
                mTvYue.setText("余额:"+Yue+"元");
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
                    showTip("易赚红包支付");
                }else if(mRbYue.isChecked()){
                    payType=YUE_PAY;
                    showTip("余额支付");
                }
            }
        });
    }
}
