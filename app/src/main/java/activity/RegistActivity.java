package activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.chuanqi.yz.R;

import java.util.HashMap;

import Constance.constance;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Utis.SharePre;
import Utis.Utis;
import model.Result;
import model.UserInfo;

public class RegistActivity extends BaseActivity {
    private UserInfo mUserInfo;
    private ListView mList;
    private RelativeLayout mRtlNo;
    private RelativeLayout mRtlYes;
    private EditText mEtYqm;
    private boolean IsRegist;
    private RelativeLayout mRtlRegist;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initview();
        mRtlRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent intent=new Intent(getApplicationContext(),WxRegistActivity.class);
                  startActivityForResult(intent,1);
            }
        });
    }
    private void initview() {
        mRtlRegist = (RelativeLayout) findViewById(R.id.rtl_wx_regist);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==11){
            Toast("绑定成功");
            Intent intent=new Intent(RegistActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
