package Utis;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.chuanqi.yz.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import Constance.constance;

/**
 * 常用的一些数据转换、提取
 *
 * @author xpq 2015年11月23日 下午2:48:18
 */
public class Utis {

    private static SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());

    /**
     * 将时间转换成标准的yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String formatTime(Date date) {
        return defaultDateFormat.format(date);
    }

    /**
     * 用于文件存储的格式,yyyyMMdd_HHmmss
     *
     * @param date
     * @return
     */
    public static String formatTimeForFile(Date date) {
        return fileDateFormat.format(date);
    }
    /* 32位小md5加密
    *
            * @return md5(value) or ""
            */
    public static String md5(String val) {
        try {
            String result = null;
            if (val != null && val.length() > 0) {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(val.getBytes(), 0, val.length());
                result = String.format("%032x", new BigInteger(1, md5.digest()));
            }
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 获取文件后缀名,如果没有后缀名则返回""
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }

    /**
     * 获取url文件地址的文件名，例如http://www.baidu.com/a/b/c.jgp将返回c.jpg
     * 如果不包含斜杠，则返回地址本身
     *
     * @param url url地址，其中斜杠为正斜杠/
     * @return
     */
    public static String getUrlFileName(String url) {
        if (url == null || url.equalsIgnoreCase("")) return "";

        int i = url.lastIndexOf("/");
        return url.substring(i + 1);
    }

    /**
     * 估算控件相对于当前activity的位置，通常用于被套在多层容器的控件或还未显示的控件，对dialog容器内的控件可能无效
     * <p>
     * 根据控件的布局方式不同，得到的位置也不同，
     * 例如：控件是相对于容器左上角布局，得到的是控件的左上角值
     * 如果控件相对于容器居中，得到的是控件的中心点值，得到的值是控件即将用于布局的值
     * </p>
     *
     * @param v
     * @return 返回数组int[4], int[0]:控件相对屏幕x轴值,int[1]:控件相对屏幕y轴值,
     * int[2]:控件宽,int[3]:控件高,(宽高都是估算值，在控件未初始化时可以使用，如果已经初始化了，直接用getWidth或getHeight)
     */
    public static int[] measureView(View v) {
        int[] loc = new int[4];
        int[] location = new int[2];
        v.getLocationInWindow(location);
//	    v.getLocationOnScreen(location);
        loc[0] = location[0];
        loc[1] = location[1];

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        loc[2] = v.getMeasuredWidth();
        loc[3] = v.getMeasuredHeight();
        if (loc[2] == 0 && v.getWidth() > 0) loc[2] = v.getWidth();
        if (loc[3] == 0 && v.getHeight() > 0) loc[3] = v.getHeight();

        return loc;
    }

    // 凑随机字符用
    private static final String randomCharacters = "`1234567890-=qwertyuiop[]\\asdfghjkl;'zxcvbnm,./"
            + "~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:\"ZXCVBNM<>?";

    // // 随机字符
    // private char randomChar() {
    // Random r = new Random();
    // int len = randomCharacters.length();
    // return randomCharacters.charAt(r.nextInt(len));
    // }

    /**
     * 简单加密
     *
     * @param str
     */
    public static String encrypt(String str) {
        byte[] randomBytes = randomCharacters.getBytes();

        // 加上字符长度
        String len = str.length() + "";
        if (len.length() < 2)
            len = "0" + len; // 不足两位补零
        String s1 = len + str; // 前两位保存长度

        // 字符对随机字符串取反
        byte[] bytes = s1.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] ^ randomBytes[i]);
        }
        String s2 = new String(bytes);

        return s2;
    }

    /**
     * 还原加密字符串（保存在cache中的），服务器密码有另一套加密方式
     *
     * @param str
     */
    public static String decrypt(String str) {
        byte[] randomBytes = randomCharacters.getBytes();

        // 与随机字符串取反
        byte[] bytes = str.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            if (i < randomBytes.length) {
                bytes[i] = (byte) (bytes[i] ^ randomBytes[i]);
            }
        }
        // 取出对应长度的数据
        String s2 = new String(bytes);
        int length = Integer.parseInt(s2.substring(0, 2));
        String s3 = s2.substring(2, length + 2);
        return s3;
    }

    /**
     * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如:
     * <p/>
     * A.网络路径: url="http://blog.foreverlove.us/girl2.png" ;
     * <p/>
     * B.本地路径:url="file://mnt/sdcard/photo/image.png";
     * <p/>
     * C.支持的图片格式 ,png, jpg,bmp,gif等等
     *
     * @param url
     * @return
     */
    private static final int size = 1024;

    public static Bitmap getImgBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream(), size);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, size);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[size];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    /**
     * 将字符串转化为集合图片
     *
     * @param img
     * @return
     */
    public static ArrayList<String> StingToImg(String img) {
        ArrayList<String> mImg = new ArrayList<>();
        String[] strarray = img.split(",");
        for (int i = 0; i < strarray.length; i++) {
            mImg.add(strarray[i]);
        }
        return mImg;
    }

    /**
     * 保存图片
     *
     * @param bigImg
     */
    public final static String FILE_SAVE_PATH = "";

    public static String getFileName(Bitmap bigImg) {
        if (getSDPath() == null) {
            Log.i("保存图片", "没有内存卡");
            return null;
        }
        File file = new File(FILE_SAVE_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File imageFile = new File(file, fileName);
        try {
            imageFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imageFile);
            bigImg.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            bigImg = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

//    /**
//     * 获取内存卡根目录
//     *
//     * @return
//     */
//    private static String getSDPath() {
//        File sdDir = null;
//        sdDir = Environment.getExternalStorageDirectory();// 获取根目录
//        return sdDir.toString();
//    }

    /**
     * 数据大小描述
     */
    public static String dataSizeDescribe(long size) {
        if (size < 1024) { // b
            return size + "b";
        } else if (size < 1024 * 1024) { // kb
            return size / 1024 + "kb";
        } else if (size < 1024 * 1024 * 1024) {
            return size / 1024 / 1024 + "mb";
        } else {
            return size / 1024 / 1024 / 1024 + "gb";
        }
    }

    public static String getTime() {
        Date dt = new Date();
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return matter1.format(dt);
    }
    public static String getDate() {
        Date dt = new Date();
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        return matter1.format(dt);
    }
    public static String getHours() {
        Date dt = new Date();
        SimpleDateFormat matter1 = new SimpleDateFormat("HH");
        return matter1.format(dt);
    }
    public static  boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    public static String   getIMEI(Context context){
        TelephonyManager telephonyManager=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei=telephonyManager.getDeviceId();
        return imei;
    }
    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String getDistanceTime(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff ;
            if(time1<time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(day!=0){
            return day+"天";
        }else if(hour!=0){
            return hour+"小时";
        }else if(min!=0){
            return min+"分钟";
        }else if(sec!=0){
            return sec+"秒钟";
        }
        return "";


//        return day + "天" + hour + "小时" + min + "分" + sec + "秒";
    }
    /**
     * 判断有无网络
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    public static String SHA1(Context context) {
        try {
            PackageInfo info =context.getPackageManager().getPackageInfo(
                    "com.chuanqi.yz", PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result=hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     2  * 获取版本号
     3  * @return 当前应用的版本号
     4  */
     public static int getVersion(Context context) {
            try {
                PackageManager manager = context.getPackageManager();
                     PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
                     String version = info.versionName;
                     int versioncode = info.versionCode;
                     return versioncode;
                 } catch (Exception e) {
                     e.printStackTrace();
                     return 0;
                 }
         }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
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
     * 返回app文件夹，在内存卡的一级目录下，以该应用名称建立的文件夹
     */
    public static String getAppFolder() {
        String path = Environment.getExternalStorageDirectory() + "/" +"YiZuanATM" + "/";
        File f = new File(path);
        if(!f.exists()) f.mkdir();
        return path;
    }
    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */
    public static boolean HasSimCard(Context context) {
        TelephonyManager telMgr = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        return result;
    }
  public static boolean IsExistPackage(Context context,String page){
      PackageManager pm = context.getPackageManager();
      List<PackageInfo> allApps = getAllApps(context);
      HashMap<String,String> map=new HashMap<>();
      for (int i=0;i<allApps.size();i++){
          if(allApps.get(i).packageName.equals(page)){
                return  true;
          }else {
              return  false;
          }
      }
      return  false;
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
    public static boolean IsRunning(Context context,String packagess){
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(200);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packagess) || info.baseActivity.getPackageName().equals(packagess)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBackground(Context context,String pakeage) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(pakeage)) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                }
//	                else{
//	                          Log.i("前台", appProcess.processName);
//	                          return re;
//	                }
            }
        }
        return false;
    }

    public static  boolean isPkgInstalled(Context context,String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * 检测String是否全是中文
     * @param name
     * @return
     */
    public static boolean checkNameChese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (!isChinese(cTemp[i])) {
                res = false;
                break;
            }
        }
        return res;
    }
    /**
     * 判定输入汉字
     * @param c
     * @return

     */

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
    public static void InstallSoft(Context context,String packages){
        Intent intent = new Intent();
        PackageManager packageManager = context.getPackageManager();
        intent = packageManager.getLaunchIntentForPackage(packages);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        context.startActivity(intent);
    }

    /**
     * 通过Url获取Bitmap
     * @param url
     * @return
     */
    public static Bitmap getBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean compareDouble(BigDecimal val1, BigDecimal val2) {
        boolean result = false;
        if (val1.compareTo(val2) < 0) {
            result = false; //第二位比较大
        }
        if (val1.compareTo(val2) == 0) {    //xiandeng
            result = true;
        }
        if (val1.compareTo(val2) > 0) { //第一位比较大
            result =true;
        }
        return result;
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static long getTimeDistance(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff ;
            if(time1<time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(sec!=0){
            return sec;
        }
        return 0;
    }
}