package Utis;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.util.Iterator;
import java.util.Map;

/**数据缓存
 * Created by lss on 2016/2/29.
 */
public class SharePre {
    private static final String sharedPreferencesName = "DATA_CACHE";
    /**
     * 登录状态
     * @param context
     * @param
     */
//    public  static void saveLoginState(Context context,boolean logined) {
//        SharedPreferences sp = context.getSharedPreferences(Constant.SP.LOGIN_STATE, Context.MODE_PRIVATE);
//        sp.edit().putBoolean(Constant.SP.LOGIN_STATE, logined).commit();
//    }
//    public static boolean islogined(Context context) {
//        SharedPreferences sp = context.getSharedPreferences(Constant.SP.LOGIN_STATE, Context.MODE_PRIVATE);
//        boolean logined = sp.getBoolean(Constant.SP.LOGIN_STATE, false);
//        return logined;
//    }

    /**
     * 缓存引导图
     * @param context
     * @param GuideImgUrl
     */
    public static void saveGuideImgUrl(Context context, String GuideImgUrl) {
        SharedPreferences sp = context.getSharedPreferences("GuideImgUrl", Context.MODE_PRIVATE);
        sp.edit().putString("GuideImgUrl", GuideImgUrl).commit();
    }
    /**
     * 读取引导图
     */
    public static String getGuideImgUrl(Context context) {
        SharedPreferences sp = context.getSharedPreferences("GuideImgUrl", Context.MODE_PRIVATE);
        return  sp.getString("GuideImgUrl", "");
    }

//    /**
//     * 缓存引导图
//     * @param context
//     * @param GuideBp
//     */
//    public static void saveGuideBp(Context context, Bitmap GuideBp) {
//        SharedPreferences sp = context.getSharedPreferences("GuideBp", Context.MODE_PRIVATE);
//        sp.edit().put("GuideImgUrl", GuideBp).commit();
//    }
//    /**
//     * 读取引导图
//     */
//    public static String getGuideBp(Context context) {
//        SharedPreferences sp = context.getSharedPreferences("GuideBp", Context.MODE_PRIVATE);
//        return  sp.getString("GuideBp", "");
//    }

    /**
     * 缓存用户Id
     * @param context
     * @param UserId
     */
    public static void saveUserId(Context context, String UserId) {
        SharedPreferences sp = context.getSharedPreferences("UserId", Context.MODE_PRIVATE);
        sp.edit().putString("UserId", UserId).commit();
    }
    /**
     * 读取用户Id
     */
    public static String getUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("UserId", Context.MODE_PRIVATE);
        return  sp.getString("UserId", "");
    }
    /**
     * 保存用户是否上传udid和位置
     * @param context
     * @param IsPostUdid
     */
    public  static void saveIsPostUdid(Context context,boolean IsPostUdid) {
        SharedPreferences sp = context.getSharedPreferences("IsPostUdid", Context.MODE_PRIVATE);
        sp.edit().putBoolean("IsPostUdid", IsPostUdid).commit();
    }
    public static boolean IsPostUdid(Context context) {
        SharedPreferences sp = context.getSharedPreferences("IsPostUdid", Context.MODE_PRIVATE);
        boolean IsPostUdid = sp.getBoolean("IsPostUdid", false);
        return IsPostUdid;
    }


    public  static void saveCity(Context context,String City) {
        SharedPreferences sp = context.getSharedPreferences("City", Context.MODE_PRIVATE);
        sp.edit().putString("City", City).commit();
    }
    public static String  getCity(Context context) {
        SharedPreferences sp = context.getSharedPreferences("City", Context.MODE_PRIVATE);
        return sp.getString("City",null);
    }
    /**
     * 移除缓存数据
     * @param key
     */
//    public static void removeUserInfoCache(Context context,String key) {
//        SharedPreferences sp = context.getSharedPreferences(Constant.SP.USER_INFO, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.remove(key);
//        editor.commit();
//    }

//    private static SharedPreferences getSharedPreferences(String sharedPreferencesName, int mode) {
//        return PlayGoodsApplication.getInstance().getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
//    }

    /**
     * 简单数据缓存，使用SharedPreferences保存数据，不会因为应用关闭而消失
     * <p>只保存简单数据类型(String自行转换)，复杂数据可使用sqlite:{ DatabaseHelper}保存</p>
     * @param key
     * @param value
     */
    public static void writeCache(Context context,String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }
    /**
     * 简单数据缓存，使用SharedPreferences保存数据，不会因为应用关闭而消失
     * <p>只保存简单数据类型(String自行转换)，复杂数据可使用sqlite:{ DatabaseHelper}保存</p>
     * @param valueMap
     */
    public static void writeCache(Context context,Map<String, String> valueMap) {
        SharedPreferences sp = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Iterator<String> iterator = valueMap.keySet().iterator();
        while( iterator.hasNext() ) {
            String key = iterator.next();
            editor.putString(key, valueMap.get(key));
        }
        editor.commit();
    }

    /**
     * 移除缓存数据
     * @param key
     */
    public static void removeCache(Context context,String key) {
        SharedPreferences sp = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }
    /**
     * 读取缓存数据，如果不存在则返回null
     * @param key
     */
    public static String readCache(Context context,String key) {
        return context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE).getString(key, null);
    }
}