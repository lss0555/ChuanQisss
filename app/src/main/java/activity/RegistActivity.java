package activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.chuanqi.yz.R;

public class RegistActivity extends BaseActivity {
    private ListView mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initview();
    }

    private void initview() {
        ListView mList=  (ListView) findViewById(R.id.list);
        mList.setSelection(1);
    }
}
