package activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.chuanqi.yz.R;
import Utis.GsonUtils;
import java.util.ArrayList;

import Constance.constance;
import Utis.Utis;
import Views.XListView.XListView;
import adapter.TaskAdapter;
import Utis.OkHttpUtil;
import model.FaskTask.faskTask;
import model.FaskTask.task;

/**
 * Created by lss on 2016/7/25.
 */
public class FaskTaskActivity extends  BaseActivity{
    private XListView mListTask;
    private ArrayList<faskTask> mTask=new ArrayList<>();
    private TaskAdapter mTaskAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        initview();
        initdate();
    }

    private void initdate() {
        OkHttpUtil.getInstance().Get(constance.URL.FAST_TASK, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                showTip(data.toString());
                task task = GsonUtils.parseJSON(data, task.class);
                mTask.clear();
                if(task.getApplyarr()!=null){
                    mTask.addAll(task.getApplyarr());
                    mTaskAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initview() {
        mListTask = (XListView) findViewById(R.id.list_task);
        mListTask.setIsShowFooter(false);
        mListTask.setPullLoadEnable(false);
        mTaskAdapter = new TaskAdapter(getApplicationContext(), mTask);
        mListTask.setAdapter(mTaskAdapter);
        mListTask.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                new GetDataTask().execute();
            }
            @Override
            public void onLoadMore() {
            }
        });
        mListTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),FaskTaskDetailActivity.class);
                intent.putExtra("Task",mTask.get(i-1));
                startActivityForResult(intent,11);
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
            mListTask.stopRefresh();
            mListTask.stopLoadMore();
            mListTask.setRefreshTime(Utis.getTime());
        }
    }
}
