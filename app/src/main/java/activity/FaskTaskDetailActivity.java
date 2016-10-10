package activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.chuanqi.yz.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import Constance.constance;
import Utis.UILUtils;
import Utis.Utis;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Utis.SharePre;
import model.FaskTask.faskTask;
import model.Result;
public class FaskTaskDetailActivity extends BaseActivity {
    private faskTask mTaskDetail;
    private ImageView mImgIcon;
    private TextView mTvName;
    private TextView mTvLeftNum;
    private TextView mTvLeftTime;
    private TextView mTvStep;
    private TextView mTvState;
    private String file_path=Utis.getSDPath() +"/" + "易赚ATM";
    private RelativeLayout mRtlAccept;
    private RelativeLayout mRtlComplite;
    private RelativeLayout mRtlGiveUp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_task_detail);
        initview();
        getDate();
        initTaskState();
        initevent();
    }
    private void initTaskState() {

    }

    /**
     * 下载apk
     */
    private void initevent() {
          mRtlAccept.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  boolean b = Utis.checkApkExist(getApplicationContext(), mTaskDetail.getsBandleID());
                  if(!b){
                      new downloadversion().execute(mTaskDetail.getAppUrl());
                  }else {
                      Utis.InstallSoft(getApplicationContext(),mTaskDetail.getsBandleID());
//                      Toast("抱歉，您已接受此任务");
                  }
              }
          });
        mRtlComplite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast("包名"+mTaskDetail.getsBandleID());
                boolean b = Utis.checkApkExist(getApplicationContext(), mTaskDetail.getsBandleID());
                if(b){
//                    if(Utis.isBackground(getApplicationContext(),mTaskDetail.getsBandleID())){
                        startProgressDialog("加载中...");
                        HashMap<String,String> map=new HashMap<String, String>();
                        map.put("sBandleID",""+mTaskDetail.getsBandleID());
                        map.put("ApplyName",""+mTaskDetail.getAppname());
                        map.put("userid",""+ SharePre.getUserId(FaskTaskDetailActivity.this));
                        OkHttpUtil.getInstance().Post(map, constance.URL.YINGYONG_BIAOSHI, new OkHttpUtil.FinishListener() {
                            @Override
                            public void Successfully(boolean IsSuccess, String data, String Msg) {
                                stopProgressDialog();
                                if(IsSuccess){
                                    Result result = GsonUtils.parseJSON(data, Result.class);
                                    if(result.getRun().equals("1")){
                                        Toast("恭喜您，获得"+mTaskDetail.getPrice()+"元奖励");
                                        Intent intent = new Intent();
                                        intent.putExtra(constance.INTENT.UPDATE_ADD_USER_MONEY,true);
                                        intent.setAction(constance.INTENT.UPDATE_ADD_USER_MONEY);   //
                                        sendBroadcast(intent);   //发送广播
                                        setResult(1);
                                        finish();
                                    }else {
                                        Toast("抱歉，任务不可重复做");
                                    }
                                }else {
                                    Toast(data.toString());
                                }
                            }
                        });
//                    }else {
//                        Toast("请打开此应用完成此任务");
//                    }
                }else {
                    Toast("请先完成此任务");
                }
            }
        });
        //放弃任务
        mRtlGiveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProgressDialog("加载中...");
                HashMap<String,String> map=new HashMap<String, String>();
                map.put("userid",SharePre.getUserId(getApplicationContext()));
                map.put("applyid",""+mTaskDetail.getsBandleID());
                OkHttpUtil.getInstance().Post(map, constance.URL.GIVE_UP_TASK, new OkHttpUtil.FinishListener() {
                    @Override
                    public void Successfully(boolean IsSuccess, String data, String Msg) {
                        stopProgressDialog();
                        if(IsSuccess){
                            Result result = GsonUtils.parseJSON(data, Result.class);
                            if(result.getRun().equals("1")){
                               Toast("恭喜您，任务已放弃");
                               setResult(1);
                                finish();
                            }else {
                                Toast("抱歉，任务放弃失败");
                            }
                        }else {
                        }
                    }
                });
            }
        });
    }
    private void initview() {
        mRtlGiveUp = (RelativeLayout) findViewById(R.id.rtl_give_up);
        mImgIcon = (ImageView) findViewById(R.id.img_icon);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvLeftNum = (TextView) findViewById(R.id.tv_left_num);
        mTvLeftTime = (TextView) findViewById(R.id.tv_left_time);
        mTvStep = (TextView) findViewById(R.id.tv_step);
        mTvState = (TextView) findViewById(R.id.tv_state);
        mRtlAccept = (RelativeLayout) findViewById(R.id.rtl_accept);
        mRtlComplite = (RelativeLayout) findViewById(R.id.rtl_complite);
        boolean b = Utis.checkApkExist(getApplicationContext(), mTaskDetail.getsBandleID());
        if(b){ //存在
            mTvState.setText("打开软件，完成任务");
        }else {      //不存在
            mTvState.setText("接受任务");
        }
    }
    private void getDate() {
        mTaskDetail= (faskTask) getIntent().getSerializableExtra("Task");
        UILUtils.displayImageNoAnim(mTaskDetail.getApplyIcon(),mImgIcon);
        mTvName.setText(mTaskDetail.getAppname());
        mTvLeftNum.setText("剩余"+mTaskDetail.getNowAmount()+"份");
        mTvStep.setText(mTaskDetail.getStep()+"");
    }
    /**
     * 执行下载异步加载
     * @author Administrator
     *
     */
    class downloadversion extends AsyncTask<String, Integer, String> {
        private FileOutputStream fos;
        private InputStream is;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                connection.setReadTimeout(3000);
                connection.setConnectTimeout(3000);
                int length = connection.getContentLength();
                int part = length / 100;// 把总长度分为100节，
                int num = 0;
                is = connection.getInputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                int TotalLen = 0;
                File file = new File(file_path);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                fos = new FileOutputStream(file);
                while (-1 != (len = is.read(buffer))) {
                    fos.write(buffer, 0, len);
                    TotalLen += len;
                    if (TotalLen >= part * num) {// 控制下载状态栏更新的次数
                        num++;
                        publishProgress(TotalLen * 100 / length);
                    }
                }
                fos.flush();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return "下载完成";
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            Update_Notity(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
			Install_APK();
            super.onPostExecute(result);
        }
        private void Install_APK() {
            mTvState.setText("打开软件，完成任务");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(file_path)),
                    "application/vnd.android.package-archive");
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                    intent, 0);
            startActivity(intent);
        }
    }
    /**
     * 更新下载的进度
     * @param progress
     */
    public void Update_Notity(int progress) {
        mTvState.setText("已下载" + progress + "%");
    }

}
