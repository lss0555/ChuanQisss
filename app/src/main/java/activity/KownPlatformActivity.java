package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuanqi.yz.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import Constance.constance;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Utis.SharePre;
import Views.SignCalendar;
import model.Result;

public class KownPlatformActivity extends BaseActivity {
    private ArrayList<String> mSigned = new ArrayList<String>();
    private String date;
    private SignCalendar mSc;
    private TextView mTvAllDayNum;
    private TextView mTvDayNum;
    private int countdown;
    private RelativeLayout mRtlComplite;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (countdown > 1) {
                countdown--;
                mTvTip.setText("确认了解("+countdown+")");
                mRtlComplite.setClickable(false);
                mRtlComplite.setBackground(getResources().getDrawable(R.drawable.round_gray_bg));
                handler.postDelayed(this, 1000);
            } else {
                mRtlComplite.setClickable(true);
                mTvTip.setText("确认了解");
                mRtlComplite.setBackground(getResources().getDrawable(R.drawable.round_red_bg));
                handler.removeCallbacks(runnable);
            }
        }
    };
    private TextView mTvTip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kown_platform);
        initview();
        initevent();
    }

    private void initevent() {
        mRtlComplite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProgressDialog("加载中...");
                HashMap<String,String> map=new HashMap<String, String>();
                map.put("userid", SharePre.getUserId(KownPlatformActivity.this));
                OkHttpUtil.getInstance().Post(map, constance.URL.KONWN_PLATFORM, new OkHttpUtil.FinishListener() {
                    @Override
                    public void Successfully(boolean IsSuccess, String data, String Msg) {
                        stopProgressDialog();
//                        showTip(data.toString());
                        if(IsSuccess){
                            Result result = GsonUtils.parseJSON(data, Result.class);
                            if(result.getRun().equals("1")){
                                Intent intent = new Intent();
                                intent.putExtra("update",true);
                                intent.setAction("update");   //
                                sendBroadcast(intent);   //发送广播
                                Toast("恭喜您，获得0.3元奖励");
                            }else {
                                Toast("您已完成此任务");
                            }
                        }else {
                            Toast(data.toString());
                        }
                    }
                });
            }
        });
    }

    private void initview() {
        mTvTip = (TextView) findViewById(R.id.tv_tip);
        mRtlComplite = (RelativeLayout) findViewById(R.id.rtl_complite);
        countdown = 10;
        mRtlComplite.setClickable(false);
        handler.postDelayed(runnable, 1000);
    }
}
