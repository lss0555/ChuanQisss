package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chuanqi.yz.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Constance.constance;
import Utis.SharePre;
import Utis.OkHttpUtil;
import Utis.GsonUtils;
import Utis.Utis;
import Views.SignCalendar;
import model.Result;
import model.sign.Signs;
import model.sign.signDate;

public class SignActivity extends BaseActivity implements View.OnClickListener{
    private ArrayList<String> mSigned = new ArrayList<String>();
    private String date;
    private SignCalendar mSc;
    private TextView mTvAllDayNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        initview();
        initdate();
    }
    /**
     * initdate
     */
    private void initdate() {
//        mSigned.clear();
//        mSigned.add("2016-08-01");
//        mSigned.add("2016-08-02");
//        mSigned.add("2016-08-03");
//        mSigned.add("2016-08-04");
//        mSigned.add("2016-08-05");
//        mSigned.add("2016-08-14");
//        mSigned.add("2016-08-22");
//        mSigned.add("2016-08-12");
//        mSigned.add("2016-08-21");
//        mSigned.add("2016-08-30");
//        mSigned.add("2016-08-28");
//        mSigned.add("2016-08-11");
//        mSc.addMarks(mSigned,0);
        startProgressDialog("加载中...");
        HashMap<String,String> map=new HashMap<>();
        map.put("userid", SharePre.getUserId(getApplicationContext()));
        OkHttpUtil.getInstance().Post(map, constance.URL.MONTH_SIGN, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                stopProgressDialog();
                Signs signs = GsonUtils.parseJSON(data, Signs.class);
                ArrayList<signDate> qdjl = signs.getQdjl();
                mTvAllDayNum.setText(""+qdjl.size());
                mSigned.clear();
                for (int i=0;i<qdjl.size();i++){
                    mSigned.add(qdjl.get(i).getDtime());
                }
                mSc.addMarks(mSigned,0);
            }
        });
    }

    /**
     * UI
     */
    private void initview() {
        mTvAllDayNum = (TextView) findViewById(R.id.tv_all_day_num);
        mSc = (SignCalendar) findViewById(R.id.sc);
        findViewById(R.id.rtl_sign).setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rtl_sign:
                SignToday();



                break;

        }
    }
    /**
     * 今天签到
     */
    private void SignToday() {
        startProgressDialog("加载中...");
        HashMap<String,String> map=new HashMap<>();
        map.put("userid", SharePre.getUserId(getApplicationContext()));
        map.put("dtime",Utis.getDate());
       OkHttpUtil.getInstance().Post(map, constance.URL.SIGN, new OkHttpUtil.FinishListener() {
           @Override
           public void Successfully(boolean IsSuccess, String data, String Msg) {
               stopProgressDialog();
//               showTip(data.toString());
               Result result = GsonUtils.parseJSON(data, Result.class);
               if(result.getRun().equals("1")){
                   Toast("签到成功");
                   List<String> list = new ArrayList<String>();
                   list.add(Utis.getDate());
                   mSc.addMarks(list,0);
                   Intent intent = new Intent();
                   intent.putExtra("update",true);
                   intent.setAction("update");   //
                   sendBroadcast(intent);
               }else {
                   Toast.makeText(getApplicationContext(),"您今天已经签到过",Toast.LENGTH_SHORT).show();
               }
           }
       });
    }
}
