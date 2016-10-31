package activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuanqi.yz.R;

import java.util.HashMap;

import Constance.constance;
import Utis.GsonUtils;
import Utis.Utis;
import Utis.OkHttpUtil;
import model.JxzAccount;
import model.UserMoney;

/**
 * 聚贤庄
 */
public class JXZActivity extends BaseActivity implements View.OnClickListener{
    private TextView mTvAccountYue;
    private TextView mTvJqzTz;
    private TextView mTvJqzYue;
    private TextView mTvJqzGet;
    private RelativeLayout mRtlTakeOut;
    private RelativeLayout mRtlTakeIn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jxz);
        initview();
        initdate();
        initevent();
    }
    private void initevent() {
        findViewById(R.id.rtl_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast("待开放中...");
            }
        });
    }
    /**
     * 初始化数据
     */
    private void initdate() {
        HashMap<String,String> maps=new HashMap<>();
        maps.put("udid", Utis.getIMEI(getApplicationContext()));
        OkHttpUtil.getInstance().Post(maps, constance.URL.MONEY, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    Log.i("数据",""+data.toString());
//                  showTip(data.toString());
                    UserMoney userMoney = GsonUtils.parseJSON(data, UserMoney.class);
                    if(userMoney.getfTodayIncome()==null){
                        mTvAccountYue.setText("0.0元");
                    }else {
                        mTvAccountYue.setText(userMoney.getfNotPayIncome()+"元");
                    }
                }
            }
        });

        HashMap<String,String> maps1=new HashMap<>();
        maps1.put("udid", Utis.getIMEI(getApplicationContext()));
        OkHttpUtil.getInstance().Post(maps1, constance.URL.JQZ_MONEY, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    Log.i("数据",""+data.toString());
//                showTip(data.toString());
                    JxzAccount mJxzAccount =GsonUtils.parseJSON(data, JxzAccount.class);
                    if(mJxzAccount.getAccrual()==null){
                        mTvJqzYue.setText("0.0元");
                        mTvJqzGet.setText("0.0元");
                    } else {
                        mTvJqzYue.setText(mJxzAccount.getYue()+"元");
                        mTvJqzGet.setText(mJxzAccount.getAccrual()+"元");
                    }
                }
            }
        });
    }

    private void initview() {
        mTvAccountYue = (TextView) findViewById(R.id.tv_account_yue);
        mTvJqzTz = (TextView) findViewById(R.id.tv_jqz_tz);
        mTvJqzYue = (TextView) findViewById(R.id.tv_jqz_yue);
        mTvJqzGet = (TextView) findViewById(R.id.tv_jxz_get);
        mRtlTakeIn = (RelativeLayout) findViewById(R.id.rtl_take_in);
        mRtlTakeOut = (RelativeLayout) findViewById(R.id.rtl_take_out);
        mRtlTakeIn.setOnClickListener(this);
        mRtlTakeOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rtl_take_in:
                TakeMoneyIn();
                break;
            case R.id.rtl_take_out:
                TakeMoneyOut();
                break;
        }
    }

    /**
     * 转出钱庄
     */
    private void TakeMoneyOut() {
        Intent intent_store=new Intent(getApplicationContext(), IntoMoneyJqzActivity.class);
        intent_store.putExtra("jzq","out");
        intent_store.putExtra("IsJqz",true);
        startActivityForResult(intent_store,12);
    }

    /**
     * 转入钱庄
     */
    private void TakeMoneyIn() {
        Intent intent_store=new Intent(getApplicationContext(), IntoMoneyJqzActivity.class);
        intent_store.putExtra("jzq","int");
        intent_store.putExtra("IsJqz",true);
        startActivityForResult(intent_store,12);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            initdate();
        }
    }
}
