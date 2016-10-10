package activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.chuanqi.yz.R;

import java.util.HashMap;

import Constance.constance;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Utis.SharePre;
import Utis.Utis;
import model.IsBindAccount;
import model.Result;

/**
 * 绑定支付宝
 */
public class BindWxAccountActivity extends BaseActivity {

    private EditText mEtName;
    private EditText mEtAccount;
    private RelativeLayout mRtlComplite;
    private int state=0;//0 是还没绑定   1是绑定了
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_wx);
        initview();
        getDate();
        initevent();

    }

    /**
     * 初始化绑定数据
     */
    private void getDate() {
        startProgressDialog("加载中...");
        HashMap<String,String> map=new HashMap<String, String>();
        map.put("userid", SharePre.getUserId(BindWxAccountActivity.this));
        map.put("accountstype", "1");
        OkHttpUtil.getInstance().Post(map, constance.URL.IS_BIND_WX_ALIPAY_ACCOUNT, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                stopProgressDialog();
                if(IsSuccess){
                    IsBindAccount bindAccount = GsonUtils.parseJSON(data, IsBindAccount.class);
                    if(!bindAccount.getAccount().equals("")){
                        mEtAccount.setText(bindAccount.getAccount());
                        mEtName.setText(bindAccount.getName());
                        mEtAccount.setEnabled(false);
                        mEtName.setEnabled(false);
                        mRtlComplite.setBackground(getResources().getDrawable(R.drawable.round_gray_bg));
                        state=1;
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
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
        mRtlComplite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEtAccount.getText().toString().trim().equals("")||mEtName.getText().toString().trim().equals("")){
                        Toast("请填写完整");
                }else if(mEtAccount.length()!=28){
                    Toast("您的输入有误，请到微信公众号获取正确的唯一码");
                }else if(!Utis.checkNameChese(mEtName.getText().toString())){
                    Toast("抱歉，请输入正确的名字");
                }else if(state==1){
                    Toast("您已绑定过");
                }else {
                    BindToAliPay();
                }
            }
            //绑定到支付宝
            private void BindToAliPay() {
                startProgressDialog("加载中...");
                HashMap<String,String> map=new HashMap<String, String>();
                map.put("userid", SharePre.getUserId(BindWxAccountActivity.this));
                map.put("account", mEtAccount.getText().toString());
                map.put("name", mEtName.getText().toString());
                map.put("accountstype", "1");
                OkHttpUtil.getInstance().Post(map, constance.URL.WX_ALIPAY_ACCOUNT, new OkHttpUtil.FinishListener() {
                    @Override
                    public void Successfully(boolean IsSuccess, String data, String Msg) {
                        stopProgressDialog();
                       if(IsSuccess){
                           Result bindAccount = GsonUtils.parseJSON(data, Result.class);
                           if(!bindAccount.getRun().equals("1")){
                               Toast("恭喜您，绑定成功,获得0.5元");
                               Intent intent = new Intent();
                               intent.putExtra(constance.INTENT.UPDATE_ADD_USER_MONEY,true);
                               intent.setAction(constance.INTENT.UPDATE_ADD_USER_MONEY);   //
                               sendBroadcast(intent);   //发送广播
                               setResult(1);
                               finish();
                           }
                       }else {
                           Toast(data.toString());
                       }
                    }
                });
            }
        });
    }

    private void initview() {
        mEtAccount = (EditText) findViewById(R.id.et_account);
        mEtName = (EditText) findViewById(R.id.et_name);
        mRtlComplite = (RelativeLayout) findViewById(R.id.rtl_complite);
    }
}
