package activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.chuanqi.yz.R;

import java.util.ArrayList;
import java.util.HashMap;

import Constance.constance;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Utis.SharePre;
import Utis.Utis;
import Views.XListView.XListView;
import adapter.RedRecordsAdapter;
import adapter.UserAllProfitAdapter;
import model.AllProfit.allprofit;
import model.AllProfit.profit;
import model.RedRecord.record;
import model.RedRecord.recordList;

public class AllProfitActivity extends BaseActivity {
    private ArrayList<allprofit> mDate=new ArrayList<>();
    private XListView mList;
    private UserAllProfitAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_profit);
        initview();
        initdate();
    }
    private void initdate() {
        HashMap<String,String> map=new HashMap<>();
        map.put("userid", SharePre.getUserId(getApplicationContext()));
        OkHttpUtil.getInstance().Post(map, constance.URL.ALL_PROFIT, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                showTip(data.toString());
                Log.i("明细收益",""+data.toString());
                if(IsSuccess){
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
                initdate();
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
