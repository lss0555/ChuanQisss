package activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chuanqi.yz.R;

import java.util.HashMap;

import Constance.constance;
import Fragments.HomeFragment;
import Utis.OkHttpUtil;
import Utis.GsonUtils;
import Utis.SharePre;
import Utis.Utis;
import model.Result;

/**
 * 存入钱庄
 */
public class IntoMoneyJqzActivity extends BaseActivity {

    private TextView mTvLeftMoney;
    private EditText mEtMoney;
    private RelativeLayout mRtlConfirm;
    private double money;

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
                }else {
                    IntoMoney2Jqz();
                }
            }
            /**
             * 转入到聚钱庄
             */
            private void IntoMoney2Jqz() {
                startProgressDialog("请稍后");
                HashMap<String,String> map=new HashMap<String, String>();
                map.put("userid", SharePre.getUserId(getApplicationContext()));
                map.put("jine",mEtMoney.getText().toString().trim());
                OkHttpUtil.getInstance().Post(map, constance.URL.INTO_JQZACCOUNT, new OkHttpUtil.FinishListener() {
                    @Override
                    public void Successfully(boolean IsSuccess, String data, String Msg) {
                        stopProgressDialog();
                        if(IsSuccess){
                            Result result = GsonUtils.parseJSON(data, Result.class);
                            if(result.getRun().equals("1")){
                                Toast("恭新您，转入成功");
                                setResult(constance.INTENT.INTO_JQZ_SUCCESS);
                                Intent intent = new Intent();
                                intent.putExtra("update",true);
                                intent.setAction("update");   //
                                sendBroadcast(intent);   //发送广播
                                finish();
                            }else if(result.getRun().equals("0")){
                                 Toast("抱歉，余额不足");
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
        money = getIntent().getDoubleExtra("money",0);
        mTvLeftMoney.setText(""+ money);
    }
    private void initview() {
        mTvLeftMoney = (TextView) findViewById(R.id.tv_left_money);
        mEtMoney = (EditText) findViewById(R.id.et_money);
        mRtlConfirm = (RelativeLayout) findViewById(R.id.rtl_confirm);
    }
}
