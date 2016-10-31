package activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chuanqi.yz.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import Constance.constance;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Utis.SharePre;
import Utis.Utis;
import Utis.OkHttpUtil;
import Views.XListView.XListView;
import adapter.RedRecordsAdapter;
import adapter.UserAllProfitAdapter;
import model.AllProfit.allprofit;
import model.AllProfit.profit;
import model.Allprifit;
import model.RedRecord.record;
import model.RedRecord.recordList;

public class AllProfitActivity extends BaseActivity {
    private ArrayList<allprofit> mDate=new ArrayList<>();
    private XListView mList;
    private UserAllProfitAdapter adapter;
    private TextView mTvLoadDate;
    private TextView mTvAccount;
    private TextView mTvTaskIncome;
    private TextView mTvTuDiGet;
    private TextView mTvGet;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_profit);
        initview();
        initdatail();
        initdate();
    }
    /**
     * 初始化详情
     */
    private void initdatail() {
        HashMap<String,String> map=new HashMap<>();
        map.put("userid",""+SharePre.getUserId(getApplicationContext()));
        OkHttpUtil.getInstance().Post(map, constance.URL.ALL_PROFIT_DETAIL, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                Log.i("收益明细",""+data.toString());
                if(IsSuccess){
                    Allprifit allprifit = GsonUtils.parseJSON(data, Allprifit.class);
                    if(allprifit.getYue()==null){//余额
                        mTvAccount.setText("0.0元");
                    }else {
                        mTvAccount.setText(allprifit.getYue()+"元");
                    }
                    if(allprifit.getRwsr()==null){//任务收入
                        mTvTaskIncome.setText("0.0元");
                    }else {
                        mTvTaskIncome.setText(allprifit.getRwsr()+"元");
                    }
                    if(allprifit.getSttc()==null){//收徒提成
                        mTvTuDiGet.setText("0.0元");
                    }else {
                        mTvTuDiGet.setText(allprifit.getSttc()+"元");
                    }
                    if(allprifit.getTixian()==null){//已提现
                        mTvGet.setText("0.0元");
                    }else if(allprifit.getTixian().length()>3){
                        mTvGet.setText(allprifit.getTixian().substring(0,2)+"元");
                    }else {
                        mTvGet.setText(allprifit.getTixian()+"元");
                    }
                }else {
                    Toast(data.toString());
                }

            }
        });
    }

    private void initdate() {
        HashMap<String,String> map=new HashMap<>();
        map.put("userid", SharePre.getUserId(AllProfitActivity.this));
        OkHttpUtil.getInstance().Post(map, constance.URL.ALL_PROFIT, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                showTip(data.toString());
                Log.i("明细收益",""+data.toString());
                if(IsSuccess){
                    mTvLoadDate.setVisibility(View.GONE);
                    profit mProfit = GsonUtils.parseJSON(data, profit.class);
                    if(mProfit.getIncomerecord()!=null){
                        ArrayList<allprofit> incomerecord = mProfit.getIncomerecord();
                        Collections.reverse(incomerecord);
                        mDate.clear();
                        mDate.addAll(incomerecord);
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
    private void initdates() {
        HashMap<String,String> map=new HashMap<>();
        map.put("userid", SharePre.getUserId(AllProfitActivity.this));
        OkHttpUtil.getInstance().Post(map, constance.URL.ALL_PROFIT, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                showTip(data.toString());
                Log.i("明细收益",""+data.toString());
                if(IsSuccess){
                    mTvLoadDate.setVisibility(View.GONE);
                    profit mProfit = GsonUtils.parseJSON(data, profit.class);
                    if(mProfit.getIncomerecord()!=null){
                        mDate.clear();
                        mDate.addAll(mProfit.getIncomerecord());
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }

    private void initview() {
        mTvAccount = (TextView) findViewById(R.id.tv_account);
        mTvTaskIncome = (TextView) findViewById(R.id.tv_task_income);
        mTvTuDiGet = (TextView) findViewById(R.id.tv_tudi_get);
        mTvGet = (TextView) findViewById(R.id.tv_get); //已提现


        mTvLoadDate = (TextView) findViewById(R.id.tv_load_date);
        mList = (XListView) findViewById(R.id.list);
        mList.setPullLoadEnable(false);
        adapter = new UserAllProfitAdapter(getApplicationContext(),mDate);
        mList.setAdapter(adapter);
        mList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                new GetDataTask().execute();
            }
            @Override
            public void onLoadMore() {

            }
        });
    }
    private class GetDataTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
                initdates();
            } catch (InterruptedException e) {
            }
            return "";
        }
        protected void onPostExecute(String result) {
            mList.stopRefresh();
            mList.stopLoadMore();
            mList.setRefreshTime(Utis.getTime());
        }
    }
}
