package activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chuanqi.yz.R;
public class ShowRedPriceActivity extends BaseActivity {

    private TextView mTvNum;
    private double money;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_red_price);
        money = getIntent().getDoubleExtra("money",0.0);
        initview();
    }
    private void initview() {
        mTvNum = (TextView) findViewById(R.id.tv_num);
        mTvNum.setText(money+"元");
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
