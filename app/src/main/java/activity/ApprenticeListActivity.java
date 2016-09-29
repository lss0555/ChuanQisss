package activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.chuanqi.yz.R;

import java.util.ArrayList;
import java.util.HashMap;
import Constance.constance;
import Utis.Utis;
import Utis.GsonUtils;
import Utis.SharePre;
import Utis.OkHttpUtil;
import Views.XListView.XListView;
import adapter.ApprenticeAdapter;
import model.Master.tudi;
import model.Master.tudiList;

public class ApprenticeListActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<tudiList> mTudi=new ArrayList<>();
    private XListView mList;
    private ApprenticeAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoutu_detail);
        initview();
        getDate();
    }
    private void getDate() {
        HashMap<String,String> map=new HashMap<>();
        map.put("userid", SharePre.getUserId(ApprenticeListActivity.this));
        OkHttpUtil.getInstance().Post(map, constance.URL.TUDI_LIST, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    tudi tudi = GsonUtils.parseJSON(data, tudi.class);
                    mTudi.clear();
                    if(tudi.getSt()!=null){
                        mTudi.addAll(tudi.getSt());
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
    private void initview() {
        findViewById(R.id.tv_receive).setOnClickListener(this);
        mList = (XListView) findViewById(R.id.list);
        mList.setPullLoadEnable(false);
        adapter = new ApprenticeAdapter(getApplicationContext(),mTudi);
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
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_receive:
                Intent intent_getApprentice=new Intent(getApplicationContext(),GetApprenticeActivity.class);
                startActivity(intent_getApprentice);
                break;
        }
    }
    private class GetDataTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
                getDate();
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
