package activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chuanqi.yz.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Constance.constance;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Utis.SharePre;
import Utis.Utis;
import Views.SignCalendar;
import model.Result;
import model.sign.DaySign;
import model.sign.Signs;
import model.sign.signDate;

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
