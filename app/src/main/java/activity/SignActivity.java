package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
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
import model.sign.DaySign;
import model.sign.Signs;
import model.sign.signDate;

public class SignActivity extends BaseActivity implements View.OnClickListener{
    private ArrayList<String> mSigned = new ArrayList<String>();
    private String date;
    private SignCalendar mSc;
    private TextView mTvAllDayNum;
    private TextView mTvDayNum;
    private RelativeLayout mRtlSign;
    private TextView mTvSignState;

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
        /**
         * 一个月中签到得天
         */
        startProgressDialog("加载中...");
        HashMap<String,String> map=new HashMap<>();
        map.put("userid", SharePre.getUserId(getApplicationContext()));
        OkHttpUtil.getInstance().Post(map, constance.URL.MONTH_SIGN, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                showTip(data.toString());
                stopProgressDialog();
                if(IsSuccess){
                    Log.i("签到天数",""+data.toString());
                    Signs signs = GsonUtils.parseJSON(data, Signs.class);
                    if(signs.getQdjl()!=null){
                        ArrayList<signDate> qdjl = signs.getQdjl();
                        mTvAllDayNum.setText(""+qdjl.size());
                        mSigned.clear();
                        for (int i=0;i<qdjl.size();i++){
                            mSigned.add(qdjl.get(i).getDtime());
                            if(qdjl.get(i).getDtime().equals(Utis.getDate())){
                               mRtlSign.setBackground(getResources().getDrawable(R.drawable.round_light_red_bg));
                                mTvSignState.setText("今日已签到");
                            }else {
                                mRtlSign.setBackground(getResources().getDrawable(R.drawable.round_red_bg));
                                mTvSignState.setText("今日未签到");
                            }
                        }
                        mSc.addMarks(mSigned,0);
                    }
                }
            }
        });
        /**
         * 获取连续签到天数
         */
        startProgressDialog("加载中...");
        HashMap<String,String> map1=new HashMap<>();
        map1.put("userid",SharePre.getUserId(getApplicationContext()));
        OkHttpUtil.getInstance().Post(map1, constance.URL.DAY_SIGN, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                showTip(data.toString());
                stopProgressDialog();
                if(IsSuccess){
                    DaySign daySign = GsonUtils.parseJSON(data, DaySign.class);
                    if(daySign.getCount()+""== null){
                        mTvDayNum.setText("0");
                    }else {
                        mTvDayNum.setText(""+daySign.getCount());
                    }
                }
            }
        });
    }
    /**
     * UI
     */
    private void initview() {
        mTvDayNum = (TextView) findViewById(R.id.tv_day_num);
        mTvAllDayNum = (TextView) findViewById(R.id.tv_all_day_num);
        mSc = (SignCalendar) findViewById(R.id.sc);
        mRtlSign = (RelativeLayout) findViewById(R.id.rtl_sign);
        mRtlSign.setOnClickListener(this);
        mTvSignState = (TextView) findViewById(R.id.tv_sign_state);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rtl_sign:
                if(mTvSignState.getText().toString().equals("今日已签到")){
                    Toast("您今日已签到");
                }else {
                    SignToday();
                }
                break;
        }
    }
    /**
     * 今天签到
     */
    private void SignToday() {
        startProgressDialog("加载中...");
        HashMap<String,String> map=new HashMap<>();
        map.put("userid",SharePre.getUserId(getApplicationContext()));
        map.put("dtime",""+Utis.getDate());
       OkHttpUtil.getInstance().Post(map, constance.URL.SIGN, new OkHttpUtil.FinishListener() {
           @Override
           public void Successfully(boolean IsSuccess, String data, String Msg) {
//               Result result = GsonUtils.parseJSON(data, Result.class);
               stopProgressDialog();
               Log.i("签到信息i",data.toString());
               if(IsSuccess){
//                   if(result.getRun().equals("1")){
                       List<String> list = new ArrayList<String>();
                       list.add(Utis.getDate());
                       mSc.addMarks(list,0);
                       Intent intent = new Intent();
                       intent.putExtra("update",true);
                       intent.setAction("update");   //
                       sendBroadcast(intent);
                       mRtlSign.setBackground(getResources().getDrawable(R.drawable.round_light_red_bg));
                       mTvSignState.setText("今日已签到");
                       Toast.makeText(getApplicationContext(),"签到成功",Toast.LENGTH_SHORT).show();
//                   }else if(result.getRun().equals("0")){
//                       Toast.makeText(getApplicationContext(),"您今天已经签到过",Toast.LENGTH_SHORT).show();
//                   }
               } else {
                   Toast(data.toString());
               }
           }
       });
    }
}
