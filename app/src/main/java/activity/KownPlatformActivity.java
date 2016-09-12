package activity;

import android.os.Bundle;
import android.widget.TextView;

import com.chuanqi.yz.R;

import java.util.ArrayList;

import Views.SignCalendar;

public class KownPlatformActivity extends BaseActivity {
    private ArrayList<String> mSigned = new ArrayList<String>();
    private String date;
    private SignCalendar mSc;
    private TextView mTvAllDayNum;
    private TextView mTvDayNum;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kown_platform);
    }
}
