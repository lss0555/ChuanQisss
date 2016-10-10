package activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chuanqi.yz.R;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;

import Constance.constance;
import Utis.UILUtils;
import Utis.Utis;
import Utis.GsonUtils;
import Utis.SharePre;
import Utis.OkHttpUtil;
import model.Result;
import model.UserInfo;

public class WxRegistActivity extends BaseActivity {
    private TextView mTvNum;
    private double money;
    private TextView mTvPast;
    private RelativeLayout mRtlToWx;
    private RelativeLayout mRtlMethod;
    private RelativeLayout mRtlBind;
    private TextView mEtWxKey;
    private RelativeLayout mRtlPast;
    private EditText mEtName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_regist);
        initview();
        initUserid();
        initevent();
    }
    private void initUserid() {
        HashMap<String,String> map=new HashMap<>();
        map.put("udid",Utis.getIMEI(getApplicationContext()));
        OkHttpUtil.getInstance().Post(map, constance.URL.USER_UDID, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    SharePre.saveIsPostUdid(getApplicationContext(),true);
                    HashMap<String,String> map1=new HashMap<>();
                    map1.put("udid", Utis.getIMEI(getApplicationContext()));
                    OkHttpUtil.getInstance().Post(map1, constance.URL.USER_INFO, new OkHttpUtil.FinishListener() {
                        @Override
                        public void Successfully(boolean IsSuccess, String data, String Msg) {
                            Log.i("首次进入个人资料",""+data.toString());
                            UserInfo mUserInfo= GsonUtils.parseJSON(data, UserInfo.class);
                            SharePre.saveUserId(WxRegistActivity.this,mUserInfo.getId());//保存userid
                        }
                    });
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
    private void initevent() {
        /**
         * 跳转到微信
         */
      mRtlToWx.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              try {
                  Intent intent = new Intent(Intent.ACTION_MAIN);
                  ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                  intent.addCategory(Intent.CATEGORY_LAUNCHER);
                  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  intent.setComponent(cmp);
                  startActivity(intent);
              } catch (ActivityNotFoundException e) {
                  Toast.makeText(getApplicationContext(), "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_LONG).show();
              }
          }
      });
        mEtWxKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager myClipboard;
                myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                ClipData.Item item = myClipboard.getPrimaryClip().getItemAt(0);
                String text = item.getText().toString();
                mEtWxKey.setText(text+"");
            }
        });
        mRtlBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Regist();
            }
        });
        mRtlMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_method=new Intent(WxRegistActivity.this,ToWxGetOpenidMethodActivity.class);
                startActivity(intent_method);
            }
        });
    }
    private void initview() {
        mRtlPast = (RelativeLayout) findViewById(R.id.rtl_past);
        mRtlToWx = (RelativeLayout) findViewById(R.id.rtl_to_wx);
        mRtlMethod = (RelativeLayout) findViewById(R.id.rtl_method);
        mRtlBind = (RelativeLayout) findViewById(R.id.rtl_bind);
        mEtWxKey = (TextView) findViewById(R.id.et_openid);
        mEtName = (EditText) findViewById(R.id.et_name);
    }
    /**
     * 注册===================================================================
     */
    private void Regist() {
        if(mEtWxKey.getText().toString().trim().equals("") ||mEtName.getText().toString().trim().equals("")){
                Toast("请输入完整");
        }else if(mEtWxKey.getText().toString().length()!=28){
                Toast("您的唯一码格式不对，请到微信公众号重新获取");
        }else if(!Utis.checkNameChese(mEtName.getText().toString())){
            Toast("抱歉，请输入正确的名字");
        }else {
            startProgressDialog("加载中...");
             HashMap<String,String> map=new HashMap<>();
            map.put("userid",""+SharePre.getUserId(getApplicationContext()));
            map.put("name",""+mEtName.getText().toString().trim());
            map.put("openid",""+mEtWxKey.getText().toString().trim());
            OkHttpUtil.getInstance().Post(map, constance.URL.REGIST, new OkHttpUtil.FinishListener() {
                @Override
                public void Successfully(boolean IsSuccess, String data, String Msg) {
                    stopProgressDialog();
//                    Toast(data.toString());
                    if(IsSuccess){
                        Result result = GsonUtils.parseJSON(data, Result.class);
                        if(result.getRun().equals("1")){
                            setResult(11);
                            finish();
                        }else if (result.getRun().equals("2")){
                            Toast("抱歉，您的微信唯一码已被绑定过");
                        }else {
                            Toast("抱歉，您的唯一码有误");
                        }
                    }
                }
            });
        }
    }
}
