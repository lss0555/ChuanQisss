package Interfaces;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConnectionChangeReceiver extends BroadcastReceiver {
    private NetStateListner listner;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            if(listner!=null){
               listner.NetState(false);
            }
//            Toast.makeText(context,"网络不可用",Toast.LENGTH_SHORT).show();
            //改变背景或者 处理网络的全局变量
        }else {
            if(listner!=null){
                listner.NetState(true);
            }
//            Toast.makeText(context,"网络可以用了",Toast.LENGTH_SHORT).show();
            //改变背景或者 处理网络的全局变量
        }
    }
    public interface NetStateListner{
        public  void NetState(boolean IsConnect);
    }
    public void SetNetStateListner(NetStateListner listner){
        this.listner = listner;
    }
}