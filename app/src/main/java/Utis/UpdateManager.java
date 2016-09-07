package Utis;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chuanqi.yz.R;

import app.BaseApp;
import okhttp.OkHttpUtils;
import okhttp.callback.StringCallback;
import okhttp3.Call;

public class UpdateManager {

    /* 下载包安装路径 */
    private static final String savePath = BaseApp.getInstance().getAppFolder();
    private static final String saveFileName = savePath + "/UpdateRelease.apk";
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private Context mContext;
    //提示语
    private String updateMsg = "有最新的软件包哦，亲快下载吧~";
    //返回的安装包url
    private String apkUrl ="";
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
                    Log.e("======",progress + "");
                    mProgress.setProgress(progress);
                    mProgressTv.setText(progress + "%");
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

                File file = new File(saveFileName);
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

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    //外部接口让主Activity调用
    public void checkUpdateInfo(){
//		showNoticeDialog();
        getVersionFromServer();
    }

    public void getVersionFromServer(){
        Map<String,String> params = new HashMap<String,String>();
        Log.e("version",getCurrentVersion().versionName);
        params.put("editionNo", getCurrentVersion().versionName);
        //TODO
        OkHttpUtils.post().url("").params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
//                Result result = GsonUtils.parseJSON(response, Result.class);
//                updateMsg = result.getMsg();
//                if (result.getSuccess()) {
//                    showNoticeDialog();
//                } else if(notUpdateShow){
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                    builder.setTitle("软件版本更新");
//                    builder.setMessage(updateMsg);
//                    builder.setPositiveButton("确定", new OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    builder.create().show();
//                }
            }
        });
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
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
//                MainActivity.getInstance().exitAll();
//                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
//		noticeDialog.show();
    }
    //TODO
    private void showDownloadDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("正在下载..." );

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.progress, null);
        mProgress = (ProgressBar)v.findViewById(R.id.progress);
        mProgressTv = (TextView)v.findViewById(R.id.progressTv);

        builder.setView(v);
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO
                dialog.dismiss();
                interceptFlag = true;
//                MainActivity.getInstance().exitAll();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
//		downloadDialog.show();

        downloadApk();
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
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }
}