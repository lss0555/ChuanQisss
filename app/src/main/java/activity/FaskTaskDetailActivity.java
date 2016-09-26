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

import Utis.UILUtils;
import Utis.Utis;
import model.FaskTask.faskTask;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_task_detail);
        initview();
        getDate();
        initevent();
    }
    /**
     * 下载apk
     */
    private void initevent() {
          mRtlAccept.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  new downloadversion().execute(mTaskDetail.getAppUrl());
              }
          });
        mRtlComplite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast("你确定完成任务了吗？");
            }
        });
    }

    private void initview() {
        mImgIcon = (ImageView) findViewById(R.id.img_icon);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvLeftNum = (TextView) findViewById(R.id.tv_left_num);
        mTvLeftTime = (TextView) findViewById(R.id.tv_left_time);
        mTvStep = (TextView) findViewById(R.id.tv_step);
        mTvState = (TextView) findViewById(R.id.tv_state);
        mRtlAccept = (RelativeLayout) findViewById(R.id.rtl_accept);
        mRtlComplite = (RelativeLayout) findViewById(R.id.rtl_complite);
    }
    private void getDate() {
        mTaskDetail= (faskTask) getIntent().getSerializableExtra("Task");
        UILUtils.displayImageNoAnim(mTaskDetail.getApplyIcon(),mImgIcon);
        mTvName.setText(mTaskDetail.getAppname());
        mTvLeftNum.setText("还剩下"+mTaskDetail.getNowAmount()+"个任务");
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
            mTvState.setText("下载完成");
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
