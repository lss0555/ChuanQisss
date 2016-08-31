package Utis.UpdateUtis;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.chuanqi.yz.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UpdataUtil {
	private  String file_path ;
	private Context context;
	private PackageInfo info;
	private Notification notification;
	private NotificationManager manager;
	private RemoteViews views;

	/**
     * 把context通过构造方法传进来
     * @param context
     */
	public UpdataUtil(Context context) {
		this.context = context;
	}
	/**
	 * 执行更新
	 * @param download_url
	 * @param file_path
	 */
	public void Update(String download_url,String file_path)
	{
		this.file_path=file_path;
		new downloadversion().execute(download_url);
	}
	/**
	 * 判断需要更新的条件
	 * @param newVersionCode
	 * @return
	 */
	public boolean IsUpdate(int newVersionCode){
		return newVersionCode>GetVersion();
	}
    /**
     * 获取版本的号
     * @return
     */
	private int GetVersion()
	{
		PackageManager manager=context.getPackageManager();
		String packageName=context.getPackageName();
		try {
			info = manager.getPackageInfo(packageName, 0);
			String name=info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info.versionCode;
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
			SendNofify_APK();
			super.onPreExecute();
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			Update_Notity(values[0]);
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
//			Install_APK();
			finitNotify();
			super.onPostExecute(result);
		}
	}
	/**
	 * 下载进度通知状态栏
	 */
	private void SendNofify_APK() {
		manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);
		views = new RemoteViews(context.getPackageName(),
				R.layout.apk_notify);
		Intent intent = new Intent();
		PendingIntent contentintent = PendingIntent.getActivity(context, 0,
				intent, 0);//
		notification = builder.setAutoCancel(true).setContent(views)
				.setWhen(System.currentTimeMillis())
				.setSmallIcon(R.mipmap.ic_launcher).setTicker("下载任务")
//				.setVibrate(new long[] { 0, 300, 100, 300 })// 设置震动
				.setSound(Uri.parse("")).setContentIntent(contentintent)// 设置给状态栏
				.build();
		manager.notify(12345, notification);
	}

	/**
	 * 更新下载的进度
	 * @param progress
	 */
	public void Update_Notity(int progress) {
//		notification.contentView.setProgressBar(R.id.pb, 100, progress, true);
		views.setViewVisibility(R.id.pb, View.VISIBLE);
		views.setProgressBar(R.id.pb,100,progress,true);
		notification.contentView.setTextViewText(R.id.tv_jd, "已下载" + progress + "%");
		manager.notify(12345, notification);
	}

	/**
	 * 完成下载
	 */
	private void finitNotify() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(file_path)),
				"application/vnd.android.package-archive");
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		notification.contentIntent = contentIntent;
		views.setTextViewText(R.id.tv_jd, "下载完成，请点击完成升级");
		views.setViewVisibility(R.id.pb, View.GONE);
		manager.notify(12345, notification);
		context.startActivity(intent);
	}
	/**
	 * 更新下载的apk文件
	 */
	private void Install_APK() {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(new File(file_path)),
				"application/vnd.android.package-archive");
		PendingIntent contenIntent = PendingIntent.getActivity(context, 0, intent,
				0);
		notification.contentIntent = contenIntent;
		manager.notify(12345, notification);
		context.startActivity(intent);

//		Intent intent = new Intent();
//		Uri data = Uri.fromFile(new File(file_path));
//		String type = "application/vnd.android.package-archive";
//		intent.setDataAndType(data, type);
//		context.startActivity(intent);
	}

}
