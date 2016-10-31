package activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chuanqi.yz.R;

import java.math.BigDecimal;
import java.util.HashMap;

import Constance.constance;
import Fragments.HomeFragment;
import Utis.OkHttpUtil;
import Utis.GsonUtils;
import Utis.SharePre;
import Utis.Utis;
import Utis.MD5Utis;
import model.JxzAccount;
import model.Result;
import model.UserMoney;

/**
 * 存入钱庄
 */
public class IntoMoneyJqzActivity extends BaseActivity {

    private TextView mTvLeftMoney;
    private EditText mEtMoney;
    private RelativeLayout mRtlConfirm;
    private double money;
    private TextView mTvTitle;
    private TextView mTvState;
    private TextView mTvNums;
    private JxzAccount mJxzAccount;
    private RelativeLayout mRtlGoJqz;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_into_money_store);
        initview();
        initdate();
        initevent();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    private void initevent() {
        mRtlConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int InputMoney=Integer.parseInt(mEtMoney.getText().toString().trim());
                if(mEtMoney.getText().toString().trim().equals("")){
                    Toast("提示:输入金额不能为空");
                }else if(Integer.valueOf(mEtMoney.getText().toString().trim())<=0){
                    Toast("抱歉，您的输入有误");
                } else {
                    switch (getIntent().getStringExtra("jzq")) {
                        case "int":
                            IntoMoney2Jqz();
                            break;
                        case "out":
                            if(mJxzAccount!=null){
                                if(mJxzAccount.getYue()!=null ){
                                    Double userIntput = Double.valueOf(mEtMoney.getText().toString().trim());
                                    Double userYue = Double.valueOf(mJxzAccount.getYue());
                                    BigDecimal userIntputs = new BigDecimal(userIntput);
                                    BigDecimal yue = new BigDecimal(userYue);
                                    if(!Utis.compareDouble(yue,userIntputs)){
                                        Toast("抱歉，您的余额不足");
                                    } else {
                                        OutMoneyJqz();
                                    }
                                }else {
                                    Toast("抱歉，您的余额不足.");
                                }
                            }else {
                                Toast("抱歉，请求失败");
                            }
                            break;
                    }

                }
            }

            /**
             * 转出到余额
             */
            private void OutMoneyJqz() {
               //TODO
                HashMap<String,String>  map=new HashMap<String, String>();
                map.put("userid",""+SharePre.getUserId(getApplication()));
                map.put("jine",""+mEtMoney.getText().toString().trim());
                map.put("sign",""+ MD5Utis.MD5_Encode(SharePre.getUserId(getApplicationContext())+"传祺chuanqi"));
                OkHttpUtil.getInstance().Post(map, constance.URL.OUT_JQZ, new OkHttpUtil.FinishListener() {
                    @Override
                    public void Successfully(boolean IsSuccess, String data, String Msg) {
                        if(IsSuccess){
                            Result result = GsonUtils.parseJSON(data, Result.class);
                            if(result.getRun().equals("1")){
                                   Toast("转出成功");
                                    setResult(1);
                                    Intent intent = new Intent();
                                    intent.putExtra(constance.INTENT.UPDATE_ADD_USER_MONEY,true);
                                    intent.setAction(constance.INTENT.UPDATE_ADD_USER_MONEY);   //
                                    sendBroadcast(intent);   //发送广播
                                    finish();
                            }else if(result.getRun().equals("2")){
                                    Toast("抱歉，非法操作");
                            }else if(result.getRun().equals("0")){
                                Toast("抱歉,转出失败");
                            }
                        }else {

                        }
                    }
                });
            }

            /**
             * 转入到聚钱庄
             */
            private void IntoMoney2Jqz() {
                startProgressDialog("请稍后");
                HashMap<String,String> map=new HashMap<String, String>();
                map.put("userid", SharePre.getUserId(IntoMoneyJqzActivity.this));
                map.put("jine",mEtMoney.getText().toString().trim());
                map.put("sign",""+MD5Utis.MD5_Encode(SharePre.getUserId(IntoMoneyJqzActivity.this)+mEtMoney.getText().toString().trim()+"传祺chuanqi"));
                OkHttpUtil.getInstance().Post(map, constance.URL.INTO_JQZACCOUNT, new OkHttpUtil.FinishListener() {
                    @Override
                    public void Successfully(boolean IsSuccess, String data, String Msg) {
                        stopProgressDialog();
                        if(IsSuccess){
                            Result result = GsonUtils.parseJSON(data, Result.class);
                            if(result.getRun().equals("1")){
                                Toast("恭新您，转入成功");
                                setResult(1);
//                                setResult(constance.INTENT.INTO_JQZ_SUCCESS);
                                Intent intent = new Intent();
                                intent.putExtra(constance.INTENT.UPDATE_ADD_USER_MONEY,true);
                                intent.setAction(constance.INTENT.UPDATE_ADD_USER_MONEY);   //
                                sendBroadcast(intent);   //发送广播
                                finish();
                            }else if(result.getRun().equals("0")){
                                 Toast("抱歉，余额不足");
                            }else if(result.getRun().equals("2")){
                                Toast("抱歉，非法操作");
                            }
                        }else {
                            Toast(data.toString());
                        }
                    }
                });
            }
        });
    }
    private void initdate() {
        switch (getIntent().getStringExtra("jzq")) {
            case "int":
                mTvTitle.setText("转入聚钱庄");
                mTvState.setText("您当前的可用余额:");
                mTvNums.setText("转入金额");
                 AccountYue();
                break;
            case "out":
                mTvTitle.setText("转出到余额");
                mTvState.setText("您当前的聚钱庄余额:");
                mTvNums.setText("转出金额");
                JqzYue();
                break;
        }
    }
    /**
     * 聚钱装的余额
     */
    private void JqzYue() {
        startProgressDialog("加载中...");
        HashMap<String,String> maps1=new HashMap<>();
        maps1.put("udid", Utis.getIMEI(getApplicationContext()));
        OkHttpUtil.getInstance().Post(maps1, constance.URL.JQZ_MONEY, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                stopProgressDialog();
                if(IsSuccess){
                    Log.i("数据",""+data.toString());
//                showTip(data.toString());
                    mJxzAccount = GsonUtils.parseJSON(data, JxzAccount.class);
                    if(mJxzAccount.getAccrual()==null){
                        mTvLeftMoney.setText("0.0元");
                    } else {
                        mTvLeftMoney.setText(mJxzAccount.getYue()+"元");
                    }
                }
            }
        });
    }
    /**
     * 账户余额
     */
    private void AccountYue() {
        startProgressDialog("加载中...");
        HashMap<String,String> maps=new HashMap<>();
        maps.put("udid", Utis.getIMEI(getApplicationContext()));
        OkHttpUtil.getInstance().Post(maps, constance.URL.MONEY, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                stopProgressDialog();
                if(IsSuccess){
                    Log.i("数据",""+data.toString());
//                  showTip(data.toString());
                    UserMoney userMoney = GsonUtils.parseJSON(data, UserMoney.class);
                    if(userMoney.getfTodayIncome()==null){
                        mTvLeftMoney.setText("0.0元");
                    }else {
                        mTvLeftMoney.setText(userMoney.getfNotPayIncome()+"元");
                    }
                }
            }
        });
    }

    private void initview() {
        mTvLeftMoney = (TextView) findViewById(R.id.tv_left_money);
        mEtMoney = (EditText) findViewById(R.id.et_money);
        mRtlConfirm = (RelativeLayout) findViewById(R.id.rtl_confirm);

        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvState = (TextView) findViewById(R.id.tv_state);
        mTvNums = (TextView) findViewById(R.id.tv_nums);
        /**
         * 去聚钱装
         */
        mRtlGoJqz = (RelativeLayout) findViewById(R.id.rtl_jqz);
        mRtlGoJqz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),JXZActivity.class);
                startActivity(intent);
            }
        });

        if(getIntent().getBooleanExtra("IsJqz",false)){
            mRtlGoJqz.setVisibility(View.GONE);
        }
    }
}
