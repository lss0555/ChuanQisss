package activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.chuanqi.yz.R;

import java.util.HashMap;

import Constance.constance;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Utis.SharePre;
import model.Result;
import model.UserInfo;
public class FeedBackActivity extends BaseActivity {

    private EditText mEtText;
    private EditText mEtLxfs;
    private RelativeLayout mRtlQq;
    private RelativeLayout mRtlContaxt;
    private RelativeLayout mRtlTj;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        initview();
        initevent();
    }

    private void initevent() {
        findViewById(R.id.rtl_tj).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEtText.getText().toString().trim().equals("")||mEtLxfs.getText().toString().trim().equals("")){
                    Toast("请填写完整");
                }else {
                    ComFirm();
                }
            }
        });
        /**
         * QQ联系方式
         */
        mRtlQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch ((int) (Math.random()*2)){
                    case 0:
                        String url11 = "mqqwpa://im/chat?chat_type=wpa&uin=2881583619&version=1";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url11)));
                        break;
                    case 1:
                        String url12 = "mqqwpa://im/chat?chat_type=wpa&uin=2881583620&version=1";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url12)));
                        break;
                }
            }
        });
        /**
         * 联系方式
         */
        mRtlContaxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"059522772566"));
                startActivity(intent);
            }
        });
    }

    private void initview() {
        mEtText = (EditText) findViewById(R.id.et_text);
        mEtLxfs = (EditText) findViewById(R.id.et_lxfs);
        mRtlQq = (RelativeLayout) findViewById(R.id.rtl_qq);
        mRtlContaxt = (RelativeLayout) findViewById(R.id.rtl_contact);
        mRtlTj = (RelativeLayout) findViewById(R.id.rtl_tj);
    }

    /**
     * 确认提交
     */
    private void ComFirm() {
        startProgressDialog("加载中...");
        HashMap<String,String> map=new HashMap<>();
        map.put("userid",""+ SharePre.getUserId(getApplicationContext()));
        map.put("proposal",""+ mEtText.getText().toString().trim());
        map.put("contact",""+ mEtLxfs.getText().toString().trim());
        OkHttpUtil.getInstance().Post(map, constance.URL.FEED_BACK, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                stopProgressDialog();
                if(IsSuccess){
                    Result result = GsonUtils.parseJSON(data, Result.class);
                    if(result.getRun().equals("1")){
                          Toast("提交成功");
                        finish();
                    }else if(result.getRun().equals("0")){
                        Toast("提交失败");
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
}
