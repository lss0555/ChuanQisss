package activity;
import android.os.Bundle;
import android.view.View;

import com.chuanqi.yz.R;

public class ShowBoxPriceActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_box_price);
        initview();
    }
    private void initview() {
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
