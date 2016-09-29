package activity.Red;

import android.os.AsyncTask;
import android.os.Bundle;

import com.chuanqi.yz.R;

import java.util.ArrayList;
import java.util.HashMap;

import Constance.constance;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Utis.SharePre;
import Utis.Utis;
import Views.XListView.XListView;
import activity.BaseActivity;
import adapter.RedRecordsAdapter;
import model.RedRecord.record;
import model.RedRecord.recordList;

public class LookRedRecordActivity extends BaseActivity {
    private ArrayList<recordList> mDate=new ArrayList<>();
    private XListView mList;
    private RedRecordsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_red_record);
        initview();
        initdate();
    }
    private void initdate() {
        HashMap<String,String> map=new HashMap<>();
        map.put("userid", SharePre.getUserId(LookRedRecordActivity.this));
        OkHttpUtil.getInstance().Post(map, constance.URL.TAKE_TED_RECORD, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    record record = GsonUtils.parseJSON(data, record.class);
                    if(record.getRecord()!=null){
                        mDate.clear();
                        mDate.addAll(record.getRecord());
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
        adapter = new RedRecordsAdapter(getApplicationContext(),mDate);
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
