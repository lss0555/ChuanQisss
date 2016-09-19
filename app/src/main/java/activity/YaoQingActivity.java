package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.chuanqi.yz.R;

import Utis.SharePre;

public class YaoQingActivity extends BaseActivity {
    private ListView mList;
    private RelativeLayout mRtlNo;
    private RelativeLayout mRtlYes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yaoqing);
        initview();
        initdate();
        initevent();
    }

    private void initdate() {
        if(SharePre.getUserId(getApplicationContext())!=null){

        }else {

        }
    }

    private void initevent() {
        mRtlNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(YaoQingActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mRtlYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(YaoQingActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void initview() {
        mRtlYes = (RelativeLayout) findViewById(R.id.rtl_yes);
        mRtlNo = (RelativeLayout) findViewById(R.id.rtl_no);
    }
}
