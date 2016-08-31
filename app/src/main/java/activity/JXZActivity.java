package activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chuanqi.yz.R;
/**
 * 聚贤庄
 */
public class JXZActivity extends BaseActivity implements View.OnClickListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jxz);
        initview();
    }
    private void initview() {
       findViewById(R.id.tv_get_detail).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_get_detail://收益明细
                Intent intent=new Intent(getApplicationContext(),JxzGetDetailActivity.class);
                startActivity(intent);
                break;
        }
    }
}
