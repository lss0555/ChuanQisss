package activity.Red;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuanqi.yz.R;
import activity.BaseActivity;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_detail);
        initview();
        initRadioButton();
        initevent();
    }


    private void initview() {
        mRbWxin = (RadioButton) findViewById(R.id.rb_wxpay);
        mRbAlipay = (RadioButton) findViewById(R.id.rb_alipay);
        mRbIntoJqz = (RadioButton) findViewById(R.id.rb_into_jqz);
        mRtlComplite = (RelativeLayout) findViewById(R.id.rtl_complite);
        mTvDetail = (TextView) findViewById(R.id.tv_detail);
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
                    showTip("微信支付");
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
}
