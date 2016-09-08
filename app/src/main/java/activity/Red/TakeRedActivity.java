package activity.Red;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.chuanqi.yz.R;

import java.util.ArrayList;
import java.util.HashMap;

import Constance.constance;
import Utis.OkHttpUtil;
import Utis.Utis;
import Utis.OkHttpUtil;
import Utis.SharePre;
import Utis.GsonUtils;
import Views.HorizontalListView;
import Views.RoundProgressBar;
import activity.BaseActivity;
import activity.ShowRedPriceActivity;
import adapter.RedTimeAdapter;
import model.RedMoney;
import model.RedTime;
import model.RedTimeList.RedList;
import model.RedTimeList.RedTimeList;

public class TakeRedActivity extends BaseActivity {
    private  ArrayList<RedTimeList> mDate=new ArrayList<>();
    private ArrayList<RedTime> mTimes=new ArrayList<>();
    private HorizontalListView mListTime;
    private RedTimeAdapter redTimeAdapter;
    private RoundProgressBar roundProgressBar;
    private TextView mTvPrice1;
    private TextView mTvPrice2;
    private TextView mTvPrice3;
    private TextView mTvID1;
    private TextView mTvID2;
    private TextView mTvID3;
    private  int ThisTime;
    private TextView mTvRedRules;
    private TextView mTvYaoQing;
    private final int RED_TOP_1=1;
    private final int RED_TOP_2=2;
    private final int RED_TOP_3=3;
    private SwipeRefreshLayout mReFreshLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_red);
        initview();
        initdate();
        initevent();
    }
    /**
     * 初始化数据
     */
    private void initdate() {
        initRedTime();
        initRedDateing();
    }
    private void initview() {
        roundProgressBar = (RoundProgressBar) findViewById(R.id.roundProgressBar);
        roundProgressBar.setProgress(75);

        mTvPrice1 = (TextView) findViewById(R.id.tv_price1);
        mTvPrice2 = (TextView) findViewById(R.id.tv_price2);
        mTvPrice3 = (TextView) findViewById(R.id.tv_price3);
        mTvID1 = (TextView) findViewById(R.id.tv_id1);
        mTvID2 = (TextView) findViewById(R.id.tv_id2);
        mTvID3 = (TextView) findViewById(R.id.tv_id3);
        mTvRedRules = (TextView) findViewById(R.id.tv_red_rules);
        mTvYaoQing = (TextView) findViewById(R.id.tv_yaoqing);
    }
    private void initevent() {
        findViewById(R.id.tv_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), RedTxDetailActivity.class);
                startActivity(intent);
            }
        });findViewById(R.id.rtl_take_red).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//抢红包
                HashMap<String,String> map=new HashMap<String, String>();
                map.put("userid", SharePre.getUserId(getApplicationContext()));
                OkHttpUtil.getInstance().Post(map, constance.URL.TAKE_RED, new OkHttpUtil.FinishListener() {
                    @Override
                    public void Successfully(boolean IsSuccess, String data, String Msg) {
//                        Toast(data.toString());
                        if(IsSuccess){
                            RedMoney redMoney = GsonUtils.parseJSON(data, RedMoney.class);
                            if (redMoney.getRun().equals("0")) {
                                Toast("请先发红包");
                            }else  if(redMoney.getRun().equals("1")){
                                Intent intent=new Intent(getApplicationContext(), ShowRedPriceActivity.class);
                                intent.putExtra("money",redMoney.getMoney());
                                startActivity(intent);
                            }else  if(redMoney.getRun().equals("2")){
                                Toast("在该时段，您已经抢过了");
                            }else  if(redMoney.getRun().equals("3")){
                                Toast("未开放，请在每个整点的前3分钟再来抢红包");
                            }
                        }else {
                            Toast(data.toString());
                        }
                    }
                });
            }
        });findViewById(R.id.rtl_send_red).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),SendRedActivity.class);
                startActivity(intent);
            }
        });findViewById(R.id.tv_red_rules).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),TakeRedRulesActivity.class);
                startActivity(intent);
            }
        });findViewById(R.id.tv_yaoqing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTip("邀请");
//                Intent intent=new Intent(getApplicationContext(),TakeRedRulesActivity.class);
//                startActivity(intent);
            }
        });
    }
    private void initRedTime() {
        mTimes.clear();
        mTimes.add(new RedTime("09:00","已抢光"));
        mTimes.add(new RedTime("10:00","已抢光"));
        mTimes.add(new RedTime("11:00","已抢光"));
        mTimes.add(new RedTime("12:00","已抢光"));
        mTimes.add(new RedTime("13:00","已抢光"));
        mTimes.add(new RedTime("14:00","已抢光"));
        mTimes.add(new RedTime("15:00","已抢光"));
        mTimes.add(new RedTime("16:00","已抢光"));
        mTimes.add(new RedTime("17:00","已抢光"));
        mTimes.add(new RedTime("18:00","已抢光"));
        mTimes.add(new RedTime("19:00","已抢光"));
        mTimes.add(new RedTime("20:00","已抢光"));
        mTimes.add(new RedTime("21:00","已抢光"));
        mTimes.add(new RedTime("00",""));
        mListTime = (HorizontalListView) findViewById(R.id.list_time);
        redTimeAdapter = new RedTimeAdapter(getApplicationContext(),mTimes);
        mListTime.setAdapter(redTimeAdapter);
        for (int i=0;i<mTimes.size()-1;i++){
            if(Integer.parseInt(mTimes.get(i).getTime().substring(0,2))==Integer.parseInt(Utis.getHours())){
                redTimeAdapter.setRedState(i);
                mListTime.setSelection(i);
            }
        }
        mListTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(Integer.parseInt(mTimes.get(i).getTime().substring(0,2))<=Integer.parseInt(Utis.getHours()) && !mTimes.get(i).getState().equals("")){
                    redTimeAdapter.setRedState(i);
                    redTimeAdapter.notifyDataSetChanged();
                    String time=mTimes.get(i).getTime().substring(0,2);
                    HashMap<String ,String> map=new HashMap<String, String>();
                    map.put("ktime",Utis.getDate()+" "+time+":00:00");
                    map.put("jtime",Utis.getDate()+" "+time+":59:59");
                    OkHttpUtil.getInstance().Post(map, constance.URL.TAKE_TED_LIST, new OkHttpUtil.FinishListener() {
                        @Override
                        public void Successfully(boolean IsSuccess, String data, String Msg) {
                            if(IsSuccess){
                                RedList redList = GsonUtils.parseJSON(data, RedList.class);
                                if (redList.getTimerecord() != null) {
                                    mDate.clear();
                                    mDate.addAll(redList.getTimerecord());
                                    switch (mDate.size()) {
                                        case RED_TOP_1:
                                            mTvPrice1.setText("￥" + mDate.get(0).getJine());
                                            mTvPrice2.setText("暂无");
                                            mTvPrice3.setText("暂无");
                                            mTvID1.setText("ID:" + mDate.get(0).getUserid());
                                            mTvID2.setText("暂无");
                                            mTvID3.setText("暂无");
                                            break;
                                        case RED_TOP_2:
                                            mTvPrice1.setText("￥" + mDate.get(0).getJine());
                                            mTvPrice2.setText("￥" + mDate.get(1).getJine());
                                            mTvPrice3.setText("暂无");
                                            mTvID1.setText("ID:" + mDate.get(0).getUserid());
                                            mTvID2.setText("ID:" + mDate.get(1).getUserid());
                                            mTvID3.setText("暂无");
                                            break;
                                        case RED_TOP_3:
                                            mTvPrice1.setText("￥" + mDate.get(0).getJine());
                                            mTvPrice2.setText("￥" + mDate.get(1).getJine());
                                            mTvPrice3.setText("￥" + mDate.get(2).getJine());
                                            mTvID1.setText("ID:" + mDate.get(0).getUserid());
                                            mTvID2.setText("ID:" + mDate.get(1).getUserid());
                                            mTvID3.setText("ID:" + mDate.get(2).getUserid());
                                            break;
                                    }
                                } else {
                                    mTvPrice1.setText("暂无");
                                    mTvPrice2.setText("暂无");
                                    mTvPrice3.setText("暂无");
                                    mTvID1.setText("暂无");
                                    mTvID2.setText("暂无");
                                    mTvID3.setText("暂无");
                                }
                            } else {
                                Toast(data.toString());
                            }
                       }
                    });
                }else if(mTimes.get(i).getState().equals("")){
                }else {
                    Toast("时间还没开始，请稍等");
                }
            }

        });
    }
    /**
     * 正在抢红包的数据
     */
    private void initRedDateing() {
        HashMap<String ,String> map=new HashMap<String, String>();
        map.put("ktime",Utis.getDate()+" "+Utis.getHours()+":00:00");
        map.put("jtime",Utis.getDate()+" "+Utis.getHours()+":59:59");
        OkHttpUtil.getInstance().Post(map, constance.URL.TAKE_TED_LIST, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if (IsSuccess) {
                    RedList redList = GsonUtils.parseJSON(data, RedList.class);
//                showTip(data.toString());
                    if (redList.getTimerecord() != null) {
                        mDate.clear();
                        mDate.addAll(redList.getTimerecord());
                        switch (mDate.size()) {
                            case RED_TOP_1:
                                mTvPrice1.setText("￥" + mDate.get(0).getJine());
                                mTvPrice2.setText("暂无");
                                mTvPrice3.setText("暂无");
                                mTvID1.setText("ID:" + mDate.get(0).getUserid());
                                mTvID2.setText("暂无");
                                mTvID3.setText("暂无");
                                break;
                            case RED_TOP_2:
                                mTvPrice1.setText("￥" + mDate.get(0).getJine());
                                mTvPrice2.setText("￥" + mDate.get(1).getJine());
                                mTvPrice3.setText("暂无");
                                mTvID1.setText("ID:" + mDate.get(0).getUserid());
                                mTvID2.setText("ID:" + mDate.get(1).getUserid());
                                mTvID3.setText("暂无");
                                break;
                            case RED_TOP_3:
                                mTvPrice1.setText("￥" + mDate.get(0).getJine());
                                mTvPrice2.setText("￥" + mDate.get(1).getJine());
                                mTvPrice3.setText("￥" + mDate.get(2).getJine());
                                mTvID1.setText("ID:" + mDate.get(0).getUserid());
                                mTvID2.setText("ID:" + mDate.get(1).getUserid());
                                mTvID3.setText("ID:" + mDate.get(2).getUserid());
                                break;
                        }
                    } else {
                        mTvPrice1.setText("暂无");
                        mTvPrice2.setText("暂无");
                        mTvPrice3.setText("暂无");
                        mTvID1.setText("暂无");
                        mTvID2.setText("暂无");
                        mTvID3.setText("暂无");
                    }
                }
            }
        });
    }
}
