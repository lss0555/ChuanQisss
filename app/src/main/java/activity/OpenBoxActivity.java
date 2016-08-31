package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chuanqi.yz.R;

public class OpenBoxActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_box);
        initview();
    }
    private void initview() {
        findViewById(R.id.rtl_open1).setOnClickListener(this);
        findViewById(R.id.rtl_open2).setOnClickListener(this);
        findViewById(R.id.rtl_open3).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rtl_open1:
                openBox();
                break;
            case R.id.rtl_open2:
                openBox();
                break;
            case R.id.rtl_open3:
                openBox();
                break;
        }
    }
    public  void openBox(){
        Intent intent=new Intent(getApplicationContext(),ShowBoxPriceActivity.class);
        startActivity(intent);
    }
}
