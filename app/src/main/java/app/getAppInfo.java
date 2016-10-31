package app;

import android.content.ComponentName;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/10/14.
 */

public class getAppInfo {
//    public final int compare(ApplicationInfo a, ApplicationInfo b) {
//        ComponentName aName = a.intent.getComponent();
//        ComponentName bName = b.intent.getComponent();
//        int aLaunchCount,bLaunchCount;
//        long aUseTime,bUseTime;
//        int result = 0;
//        try {
//            //获得ServiceManager类
//            Class<?> ServiceManager = Class
//                    .forName("android.os.ServiceManager");
//            //获得ServiceManager的getService方法
//            Method getService = ServiceManager.getMethod("getService", java.lang.String.class);
//            //调用getService获取RemoteService
//            Object oRemoteService = getService.invoke(null, "usagestats");
//            //获得IUsageStats.Stub类
//            Class<?> cStub = Class
//                    .forName("com.android.internal.app.IUsageStats$Stub");
//            //获得asInterface方法
//            Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
//            //调用asInterface方法获取IUsageStats对象
//            Object oIUsageStats = asInterface.invoke(null, oRemoteService);
//            //获得getPkgUsageStats(ComponentName)方法
//            Method getPkgUsageStats = oIUsageStats.getClass().getMethod("getPkgUsageStats", ComponentName.class);
//            //调用getPkgUsageStats 获取PkgUsageStats对象
//            Object aStats = getPkgUsageStats.invoke(oIUsageStats, aName);
//            Object bStats = getPkgUsageStats.invoke(oIUsageStats, bName);
//
//            //获得PkgUsageStats类
//            Class<?> PkgUsageStats = Class.forName("com.android.internal.os.PkgUsageStats");
//
//            aLaunchCount = PkgUsageStats.getDeclaredField("launchCount").getInt(aStats);
//            bLaunchCount = PkgUsageStats.getDeclaredField("launchCount").getInt(bStats);
//            aUseTime = PkgUsageStats.getDeclaredField("usageTime").getLong(aStats);
//            bUseTime = PkgUsageStats.getDeclaredField("usageTime").getLong(bStats);
//            if((aLaunchCount>bLaunchCount)||
//                    ((aLaunchCount == bLaunchCount)&&(aUseTime>bUseTime)))
//                result = 1;
//            else if((aLaunchCount<bLaunchCount)||((aLaunchCount ==
//                    bLaunchCount)&&(aUseTime<bUseTime)))
//                result = -1;
//            else {
//                result = 0;
//            }
//        } catch (Exception e) {
//            Log.e("###", e.toString(), e);
//        }
//        return result;
//    }
        public long getTime(ApplicationInfo applicationInfo){
//            ComponentName name=applicationInfo.intent.getComponent();
            long useTime = 0;
            try{
                Class<?> ServiceManager=Class.forName("android.os.ServiceManager");
                Method getService=ServiceManager.getMethod("getService",java.lang.String.class);
                Object onRemoteService=getService.invoke(null, "usageStats");
                Class<?> cStub=Class.forName("com.android.internal.app.IUsageStats$Stub");
                Method asInteface=cStub.getMethod("anInteface",android.os.IBinder.class);
                Object oIUsageStats=asInteface.invoke(null, onRemoteService);
                Method getPkgUsageStats=oIUsageStats.getClass().getMethod("getPkgUsageStats",ComponentName.class);
                Object state=getPkgUsageStats.invoke(oIUsageStats, "");
                Class<?> PkgUsageStats=Class.forName("com.a ndroid.internal.os.PkgUsageStats");
                //lounchCount=PkgUsageStats.getDeclaredField("lacnchCount").getInt(state);
                useTime=PkgUsageStats.getDeclaredField("usageTime").getLong(state);
//                appInfo.setUseTime(useTime);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return useTime;
//            return applicationInfo.getUseTime();
        }
}
