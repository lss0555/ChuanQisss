package activity.HowToEarn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chuanqi.yz.R;

import activity.BaseActivity;

public class HowToEarnActivity extends BaseActivity implements View.OnClickListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_earn);
        initview();
    }

    private void initview() {
        findViewById(R.id.rtl_earn_first).setOnClickListener(this);
        findViewById(R.id.rtl_shitu).setOnClickListener(this);
        findViewById(R.id.rtl_jxztz).setOnClickListener(this);
        findViewById(R.id.rtl_shop).setOnClickListener(this);
        findViewById(R.id.rtl_red).setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rtl_earn_first:
                Intent intent=new Intent(getApplicationContext(),EarnFirstMoneyDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.rtl_shitu:
                Intent intent_shitu=new Intent(getApplicationContext(),ShiTuDetailActivity.class);
                startActivity(intent_shitu);
                break;
            case R.id.rtl_jxztz:
                Intent intent_jxztz=new Intent(getApplicationContext(),JxzTzDetailActivity.class);
                startActivity(intent_jxztz);
                break;
            case R.id.rtl_shop:
                Intent intent_shop=new Intent(getApplicationContext(),ShopDetailActivity.class);
                startActivity(intent_shop);
                break;
            case R.id.rtl_red:
                Intent intent_red=new Intent(getApplicationContext(),RedDetailActivity.class);
                startActivity(intent_red);
                break;

        }
    }
}
