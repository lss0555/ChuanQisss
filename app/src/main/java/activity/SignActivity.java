package activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.chuanqi.yz.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Views.SignCalendar;

public class SignActivity extends BaseActivity implements View.OnClickListener{
    private ArrayList<String> mSigned = new ArrayList<String>();
    private String date;
    private SignCalendar mSc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        initview();
        initsign();
        initdate();
    }

    /**
     * initdate
     */
    private void initdate() {
        mSigned.clear();
        mSigned.add("2016-07-01");
        mSigned.add("2016-07-02");
        mSigned.add("2016-07-03");
        mSigned.add("2016-07-04");
        mSc.addMarks(mSigned,0);
    }

    /**
     * sign init
     */
    private void initsign() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        date = formatter.format(curDate);
    }
    /**
     * UI
     */
    private void initview() {
        mSc = (SignCalendar) findViewById(R.id.sc);
        findViewById(R.id.rtl_sign).setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rtl_sign:
                List<String> list = new ArrayList<String>();
                list.add(date);
                mSc.addMarks(list, 0);
                break;

        }
    }
}
