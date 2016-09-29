package app;
import com.chuanqi.yz.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Constance.constance;
import Utis.OkHttpUtil;
import Utis.SharePre;
import Utis.Utis;
import okhttp.OkHttpUtils;
import okio.Buffer;

public class BaseApp extends Application {
	public static Context applicationContext;
	private String CER_12306 = "-----BEGIN CERTIFICATE-----\n" +
            "MIICmjCCAgOgAwIBAgIIbyZr5/jKH6QwDQYJKoZIhvcNAQEFBQAwRzELMAkGA1UEBhMCQ04xKTAn\n" +
            "BgNVBAoTIFNpbm9yYWlsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MQ0wCwYDVQQDEwRTUkNBMB4X\n" +
            "DTA5MDUyNTA2NTYwMFoXDTI5MDUyMDA2NTYwMFowRzELMAkGA1UEBhMCQ04xKTAnBgNVBAoTIFNp\n" +
            "bm9yYWlsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MQ0wCwYDVQQDEwRTUkNBMIGfMA0GCSqGSIb3\n" +
            "DQEBAQUAA4GNADCBiQKBgQDMpbNeb34p0GvLkZ6t72/OOba4mX2K/eZRWFfnuk8e5jKDH+9BgCb2\n" +
            "9bSotqPqTbxXWPxIOz8EjyUO3bfR5pQ8ovNTOlks2rS5BdMhoi4sUjCKi5ELiqtyww/XgY5iFqv6\n" +
            "D4Pw9QvOUcdRVSbPWo1DwMmH75It6pk/rARIFHEjWwIDAQABo4GOMIGLMB8GA1UdIwQYMBaAFHle\n" +
            "tne34lKDQ+3HUYhMY4UsAENYMAwGA1UdEwQFMAMBAf8wLgYDVR0fBCcwJTAjoCGgH4YdaHR0cDov\n" +
            "LzE5Mi4xNjguOS4xNDkvY3JsMS5jcmwwCwYDVR0PBAQDAgH+MB0GA1UdDgQWBBR5XrZ3t+JSg0Pt\n" +
            "x1GITGOFLABDWDANBgkqhkiG9w0BAQUFAAOBgQDGrAm2U/of1LbOnG2bnnQtgcVaBXiVJF8LKPaV\n" +
            "23XQ96HU8xfgSZMJS6U00WHAI7zp0q208RSUft9wDq9ee///VOhzR6Tebg9QfyPSohkBrhXQenvQ\n" +
            "og555S+C3eJAAVeNCTeMS3N/M5hzBRJAoffn3qoYdAO1Q8bTguOi+2849A==\n" +
            "-----END CERTIFICATE-----";
	public BaseApp(){

	}
	private static BaseApp instance;
	public synchronized static BaseApp getInstance() {
		if( instance == null ) {
			instance = new BaseApp();
		}
		return instance;
	}
	/**
	 * 返回app文件夹，在内存卡的一级目录下，以该应用名称建立的文件夹
	 */
	public String getAppFolder() {
		String path = Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/";
		File f = new File(path);
		if(!f.exists()) f.mkdir();
		return path;
	}
	public void onCreate() {
		super.onCreate();
		applicationContext = this;
		initImageLoader(getApplicationContext());
		OkHttpUtils.getInstance().setCertificates(new InputStream[]{
                new Buffer()
                        .writeUtf8(CER_12306)
                        .inputStream()});
        OkHttpUtils.getInstance().debug("testDebug").setConnectTimeout(100000, TimeUnit.MILLISECONDS);
		getAppPackages();
	}
	/**
	 * 获取应用的信息
	 */
	private void getAppPackages() {
		PackageManager pm = getPackageManager();
		List<PackageInfo> allApps = getAllApps(getApplicationContext());
		HashMap<String,String> map=new HashMap<>();
		for (int i=0;i<allApps.size();i++){
			map.put("udid",Utis.getIMEI(getApplicationContext())) ;
			map.put("userid",SharePre.getUserId(getApplicationContext())) ;
			map.put("applyid",allApps.get(i).packageName) ;
			map.put("applyname",allApps.get(i).applicationInfo.loadLabel(pm).toString()) ;
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(allApps.get(i).lastUpdateTime);
//			c.setTimeInMillis(allApps.get(i).firstInstallTime);
			SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			map.put("dInstallTime",matter1.format(c.getTime())+"") ;
			OkHttpUtil.getInstance().Post(map, constance.URL.APP_INFO, new OkHttpUtil.FinishListener() {
				@Override
				public void Successfully(boolean IsSuccess, String data, String Msg) {
					Log.w("app信息：",""+data.toString());
//					Toast.makeText(getApplicationContext(),data.toString(),Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	/**
	 * 查询手机内非系统应用
	 * @param context
	 * @return
	 */
	public static List<PackageInfo> getAllApps(Context context) {
		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		PackageManager pManager = context.getPackageManager();
		//获取手机内所有应用
		List<PackageInfo> paklist = pManager.getInstalledPackages(0);
		for (int i = 0; i < paklist.size(); i++) {
			PackageInfo pak = (PackageInfo) paklist.get(i);
			//判断是否为非系统预装的应用程序
			if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
				// customs applications
				apps.add(pak);
			}
		}
		return apps;
	}
	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				 context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.build();
		ImageLoader.getInstance().init(config);
	}
}