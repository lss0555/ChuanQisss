package Manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;


import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chuanqi.yz.R;
import okhttp3.Call;
public class UpdateManagers{
    /* 下载包安装路径 */
    private static final String savePath = getSDPath() +"/" + "易赚ATM";
    private static final String saveFileName = savePath + "/yizuan.apk";
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private Context mContext;
    //提示语
//    private String updateMsg = "版本更新内容...";
    //返回的安装包url
//    private String apkUrl = "http://bmob-cdn-4915.b0.upaiyun.com/2016/09/07/e7fc983d4049bd7c808197d358aa8fdd.apk";
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;
    private TextView mProgressTv;
    private int progress;
    boolean notUpdateShow = true; // 没有更新是否显示通知
    private Thread downLoadThread;
    private boolean interceptFlag = false;
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    Log.i("======",progress + "");
                    mProgress.setProgress(progress);
                    mProgressTv.setText("已下载"+progress + "%");
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                default:
                    break;
            }
        };
    };
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                File file = new File(savePath);
                file.getParentFile().mkdir();
//				file.deleteOnExit();
                file.delete();
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                int count = 0;
                byte buf[] = new byte[1024];
                do{
                    int numread = is.read(buf);
                    count += numread;
                    progress =(int)(((float)count / length) * 100);
                    //更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if(numread <= 0){
//                    if(progress==100){
                        //下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf,0,numread);
                }while(!interceptFlag);//点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }

        }
    };
    private Context context;
    private String updateMsg;
    private String apkUrl;

    public UpdateManagers(Context context,String updateMsg,String apkUrl) {
        this.context = context;
        this.updateMsg = updateMsg;
        this.apkUrl = apkUrl;
    }
    //外部接口让主Activity调用
    public void checkUpdateInfo(){
        UpdateTip();
    }

    public void UpdateTip(){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("软件版本更新");
                    builder.setMessage(updateMsg);
                    builder.setPositiveButton("确定", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            showDownloadDialog();
                        }
                    });
                   builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                     }
                    });
                    builder.create().show();
    }

    //获取当前版本号码和名称
    public PackageInfo getCurrentVersion() {
        PackageInfo info = null;
        try {
            info = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    private void showNoticeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("软件版本更新");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("立即下载", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });
        builder.setNegativeButton("暂不更新", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    private void showDownloadDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("正在下载..." );
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_download_apk_progress, null);
        mProgress = (ProgressBar)v.findViewById(R.id.progress);
        mProgressTv = (TextView)v.findViewById(R.id.progressTv);
        builder.setView(v);
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        builder.setCancelable(false);
        builder.create().show();
        downloadApk();
//        new downloadversion().execute("http://bmob-cdn-4915.b0.upaiyun.com/2016/09/07/e7fc983d4049bd7c808197d358aa8fdd.apk");
    }
    /**
     * 没有更新是否显示信息，默认显示
     */
    public void setNotUpdateMessageShow(boolean b) {
        notUpdateShow = b;
    }
    /**
     * 下载apk
     */
    private void downloadApk(){
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }
    /**
     * 安装apk
     */
    private void installApk(){
        mProgressTv.setText("下载完成");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(savePath)),
                "application/vnd.android.package-archive");
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent, 0);
        context.startActivity(intent);
    }
    public static String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if(sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
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
                File file = new File(savePath);
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
        public  void Install_APK() {
            mProgressTv.setText("下载完成");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(savePath)),
                    "application/vnd.android.package-archive");
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    intent, 0);
            context.startActivity(intent);
        }
    }
    /**
     * 更新下载的进度
     * @param progress
     */
    public void Update_Notity(int progress) {
        mProgressTv.setText("已下载" + progress + "%");
        mProgress.setProgress(progress);
    }
}