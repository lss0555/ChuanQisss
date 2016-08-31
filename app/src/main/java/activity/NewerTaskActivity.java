package activity;

import android.os.Bundle;
import android.view.View;

import com.chuanqi.yz.R;

/**
 * 新手任务
 */
public class NewerTaskActivity extends BaseActivity implements View.OnClickListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newer_task);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case
        }
    }
}
