//package Fragments.JingDianTask;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.Toast;
//import com.chuanqi.yz.R;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import Constance.constance;
//import Fragments.BaseFragment;
//import Utis.Utis;
//import Utis.OkHttpUtil;
//import Utis.GsonUtils;
//import Utis.SharePre;
//import Views.XListView.XListView;
//import activity.FaskTaskDetailActivity;
//import activity.YaoQingSharesActivity;
//import adapter.TaskAdapter;
//import model.FaskTask.faskTask;
//import model.FaskTask.task;
//import model.Result;
//import model.TaskState;
///**
// */
//public class JingDianFragment extends BaseFragment {
//    private ArrayList<faskTask> mTask=new ArrayList<>();
//    private XListView mListTask;
//    private TaskAdapter mTaskAdapter;
//    private String RunningTask="";
//    public JingDianFragment() {
//        // Required empty public constructor
//    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View layout=inflater.inflate(R.layout.fragment_jinadian,null);
//        initTaskState();
//        initview(layout);
//        initdate();
//        initevent();
//        return layout;
//    }
//    private void initdate() {
//        startProgressDialog("努力加载中...");
//        OkHttpUtil.getInstance().Get(constance.URL.FAST_TASK, new OkHttpUtil.FinishListener() {
//            @Override
//            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                stopProgressDialog();
////                showTip(data.toString());
//                Log.i("快速任务列表",""+data.toString());
//                task task = GsonUtils.parseJSON(data, task.class);
//                List<faskTask> applyarr = task.getApplyarr();
//                mTask.clear();
//                if(task.getApplyarr()!=null){
//                    if(!RunningTask.equals("")){
//                        for (int i=0;i<task.getApplyarr().size();i++){
//                            if(applyarr.get(i).getsBandleID().equals(RunningTask)){
//                                mTask.add(applyarr.get(i));
//                                applyarr.remove(i);
//                            }
//                        }
//                    }
//                    mTask.addAll(applyarr);
//                    mTaskAdapter.notifyDataSetChanged();
//                }
//            }
//        });
//        for(int i=0;i<mTask.size();i++){
//            if(mTask.get(i).getsBandleID().equals(RunningTask)){
//                mTaskAdapter.setTaskRunning(i);
//                mListTask.setSelection(i);
//                mTaskAdapter.notifyDataSetChanged();
//            }
//        }
//    }
//    private void initview(View layout) {
//        mListTask = (XListView) layout.findViewById(R.id.list_task);
//        mListTask.setIsShowFooter(false);
//        mListTask.setPullLoadEnable(false);
//        mTaskAdapter = new TaskAdapter(getActivity(), mTask);
//        View headLayout=View.inflate(getActivity(),R.layout.item_top_task,null);
//        View Top_items = headLayout.findViewById(R.id.ll_top_items);
//        mListTask.addHeaderView(headLayout);
//        mListTask.setAdapter(mTaskAdapter);
//        Top_items.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(getActivity(), YaoQingSharesActivity.class);
//                startActivity(intent);
//            }
//        });
//        mListTask.setXListViewListener(new XListView.IXListViewListener() {
//            @Override
//            public void onRefresh() {
//                new GetDataTask().execute();
//            }
//            @Override
//            public void onLoadMore() {
//            }
//        });
//    }
//    private void initevent() {
//        mListTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
//                startProgressDialog("加载中...");
//                boolean b = Utis.checkApkExist(getActivity(), mTask.get(i-2).getsBandleID());
//                if(!b){
//                    HashMap<String,String> map=new HashMap<String, String>();
//                    map.put("sBandleID",""+mTask.get(i-2).getsBandleID());
//                    map.put("userid",""+ SharePre.getUserId(getActivity()));
//                    Log.i("检查是否可做",""+mTask.get(i-2).getsBandleID()+SharePre.getUserId(getActivity()));
//                    OkHttpUtil.getInstance().Post(map, constance.URL.IS_DONE, new OkHttpUtil.FinishListener() {
//                        @Override
//                        public void Successfully(boolean IsSuccess, String data, String Msg) {
//                            Log.i("检查是否可做返回结果",""+data.toString());
//                            Log.i("行点击任务",""+mTask.get(i-2).toString());
//                            stopProgressDialog();
//                            if(IsSuccess){
//                                Result result = GsonUtils.parseJSON(data, Result.class);
//                                if(result.getRun().equals("0")){
//                                    Intent intent=new Intent(getActivity(), FaskTaskDetailActivity.class);
//                                    intent.putExtra("Task",mTask.get(i-2));
//                                    startActivityForResult(intent,11);
//                                }else if(result.getRun().equals("1")){
//                                    Toast("您已做过此任务");
//                                }
//                            }else {
//                                Toast(data.toString());
//                            }
//                        }
//                    });
//                }else {
//                    stopProgressDialog();
//                    Toast("抱歉，您已完成此任务");
//                }
//            }
//        });
//    }
//    private void initdates() {
//        OkHttpUtil.getInstance().Get(constance.URL.FAST_TASK, new OkHttpUtil.FinishListener() {
//            @Override
//            public void Successfully(boolean IsSuccess, String data, String Msg) {
////                showTip(data.toString());
//                Log.i("快速任务列表",""+data.toString());
//                task task = GsonUtils.parseJSON(data, task.class);
//                List<faskTask> applyarr = task.getApplyarr();
//                mTask.clear();
//                if(task.getApplyarr()!=null){
//                    if(!RunningTask.equals("")){
//                        for (int i=0;i<task.getApplyarr().size();i++){
//                            if(applyarr.get(i).getsBandleID().equals(RunningTask)){
//                                mTask.add(applyarr.get(i));
//                                applyarr.remove(i);
//                            }
//                        }
//                    }
//                    mTask.addAll(applyarr);
//                    mTaskAdapter.notifyDataSetChanged();
//                }
//            }
//        });
////        for(int i=0;i<mTask.size();i++){
////            if(mTask.get(i).getsBandleID().equals(RunningTask)){
////                mTaskAdapter.setTaskRunning(i);
////                mListTask.setSelection(i);
////                mTaskAdapter.notifyDataSetChanged();
////            }
////        }
//    }
//    private class GetDataTask extends AsyncTask<Void, Void, String> {
//        protected String doInBackground(Void... params) {
//            try {
//                Thread.sleep(2000);
//                initdates();
//            } catch (InterruptedException e) {
//            }
//            return "";
//        }
//        protected void onPostExecute(String result) {
//            mListTask.stopRefresh();
//            mListTask.stopLoadMore();
//            mListTask.setRefreshTime(Utis.getTime());
//        }
//    }
//
//    /**
//     * 判断任务的状态
//     */
//    private void initTaskState() {
//        startProgressDialog("加载中...");
//        HashMap<String,String> map=new HashMap<>();
//        map.put("userid",""+ SharePre.getUserId(getActivity()));
//        OkHttpUtil.getInstance().Post(map, constance.URL.IS_APPLYTASK, new OkHttpUtil.FinishListener() {
//            @Override
//            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                stopProgressDialog();
//                if(IsSuccess){
//                    TaskState taskState = GsonUtils.parseJSON(data, TaskState.class);
//                    Log.i("正在进行任务状态==============",""+data.toString());
//                    if(!taskState.getApplyid().equals("")){
//                        RunningTask=taskState.getApplyid();
//                    }
//                    Log.i("正在进行任务状态RunningTask",""+RunningTask);
//                }else {
//                    Toast.makeText(getActivity(),""+data.toString(),Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode==1){
//            initTaskState();
//            initdate();
//        }
//    }
//}
