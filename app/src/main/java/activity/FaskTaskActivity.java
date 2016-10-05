package activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.chuanqi.yz.R;
import Utis.GsonUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Constance.constance;
import Utis.Utis;
import Utis.SharePre;
import Views.XListView.XListView;
import adapter.TaskAdapter;
import Utis.OkHttpUtil;
import model.FaskTask.faskTask;
import model.FaskTask.task;
import model.Result;
import model.TaskState;

/**
 * Created by lss on 2016/7/25.
 */
public class FaskTaskActivity extends  BaseActivity{
    private XListView mListTask;
    private ArrayList<faskTask> mTask=new ArrayList<>();
    private TaskAdapter mTaskAdapter;
    private String RunningTask="";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        initview();
        initTaskState();
        initdate();
        initevent();
    }

    /**
     * 判断任务的状态
     */
    private void initTaskState() {
        startProgressDialog("加载中...");
        HashMap<String,String> map=new HashMap<>();
        map.put("userid",""+SharePre.getUserId(getApplicationContext()));
        OkHttpUtil.getInstance().Post(map, constance.URL.IS_APPLYTASK, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                stopProgressDialog();
                if(IsSuccess){
                    TaskState taskState = GsonUtils.parseJSON(data, TaskState.class);
                    Log.i("任务状态==============",""+data.toString());
                    if(!taskState.getApplyid().equals("")){
                        RunningTask=taskState.getApplyid();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),""+data.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initevent() {
        mListTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                startProgressDialog("加载中...");
                boolean b = Utis.checkApkExist(getApplicationContext(), mTask.get(i-2).getsBandleID());
                if(!b){
                    HashMap<String,String> map=new HashMap<String, String>();
                    map.put("sBandleID",""+mTask.get(i-2).getsBandleID());
                    map.put("userid",""+ SharePre.getUserId(FaskTaskActivity.this));
                    Log.i("检查是否可做",""+mTask.get(i-2).getsBandleID()+SharePre.getUserId(getApplicationContext()));
                    OkHttpUtil.getInstance().Post(map, constance.URL.IS_DONE, new OkHttpUtil.FinishListener() {
                        @Override
                        public void Successfully(boolean IsSuccess, String data, String Msg) {
                            Log.i("检查是否可做返回结果",""+data.toString());
                            stopProgressDialog();
                            if(IsSuccess){
                                Result result = GsonUtils.parseJSON(data, Result.class);
                                if(result.getRun().equals("0")){
                                    Intent intent=new Intent(getApplicationContext(),FaskTaskDetailActivity.class);
                                    intent.putExtra("Task",mTask.get(i-2));
                                    startActivityForResult(intent,11);
                                }else if(result.getRun().equals("1")){
                                    Toast("您已做过此任务");
                                }
                            }else {
                                Toast(data.toString());
                            }
                        }
                    });
                }else {
                    stopProgressDialog();
                   Toast("抱歉，您已完成此任务");
                }
            }
        });
    }
    private void initdate() {
        startProgressDialog("努力加载中...");
        OkHttpUtil.getInstance().Get(constance.URL.FAST_TASK, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                stopProgressDialog();
//                showTip(data.toString());
                Log.i("快速任务列表",""+data.toString());
                task task = GsonUtils.parseJSON(data, task.class);
                List<faskTask> applyarr = task.getApplyarr();
                mTask.clear();
                if(task.getApplyarr()!=null){
                    if(!RunningTask.equals("")){
                        for (int i=0;i<task.getApplyarr().size();i++){
                            if(applyarr.get(i).getsBandleID().equals(RunningTask)){
                                mTask.add(applyarr.get(i));
                                applyarr.remove(i);
                            }
                        }
                    }
                    mTask.addAll(applyarr);
                    mTaskAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void initdates() {
        OkHttpUtil.getInstance().Get(constance.URL.FAST_TASK, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                showTip(data.toString());
                Log.i("快速任务列表",""+data.toString());
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
        View headLayout=View.inflate(getApplicationContext(),R.layout.item_top_task,null);
        View Top_items = headLayout.findViewById(R.id.ll_top_items);
        mListTask.addHeaderView(headLayout);
        mListTask.setAdapter(mTaskAdapter);
        Top_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),YaoQingSharesActivity.class);
                startActivity(intent);
            }
        });
        mListTask.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                new GetDataTask().execute();
            }
            @Override
            public void onLoadMore() {
            }
        });
        findViewById(R.id.tv_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FaskTaskActivity.this,TaskHelpCenterActivity.class);
                startActivity(intent);
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
            mListTask.stopRefresh();
            mListTask.stopLoadMore();
            mListTask.setRefreshTime(Utis.getTime());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            initdate();
        }
    }
}
