//package services;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.Binder;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//import android.util.Log;
//
//import Utis.Utis;
//
///**
// * Created by zss on 2016/9/6.
// */
//public class TimerService extends Service{
//    private static final String TAG = "绑定服务";
//    @Override
//    public IBinder onBind(Intent intent) {
//        return new MyBind();
//    }
//    public class MyBind extends Binder{
//        public String getDate(){
//              return Utis.getDate();
//        }
//    }
//    @Override
//    public void onCreate() {
//        Log.i(TAG, "BindService-->onCreate()");
//        super.onCreate();
//    }
//
//    @Override
//    public void onStart(Intent intent, int startId) {
//        Log.i(TAG, "BindService-->onStart()");
//        super.onStart(intent, startId);
//    }
//
//    @Override
//    public void onDestroy() {
//        Log.i(TAG, "BindService-->onDestroy()");
//        super.onDestroy();
//    }
//
//    @Override
//    public boolean onUnbind(Intent intent) {
//        Log.i(TAG, "BindService-->onUnbind()");
//        return super.onUnbind(intent);
//    }
//}
