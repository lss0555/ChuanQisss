package Maps;

import android.text.TextUtils;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;

/**
 * Created by zss on 2016/8/30.
 */
public class Location {
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();
    /**
     * 开始定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void startLocation(){
        //根据控件的选择，重新设置定位参数
        resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }
    // 根据控件的选择，重新设置定位参数
    private void resetOption() {
        // 设置是否需要显示地址信息
//        locationOption.setNeedAddress(cbAddress.isChecked());
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
//        locationOption.setGpsFirst(cbGpsFirst.isChecked());
        // 设置是否开启缓存
//        locationOption.setLocationCacheEnable(cbCacheAble.isChecked());
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
//        locationOption.setOnceLocationLatest(cbOnceLastest.isChecked());
            try{
                // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
                locationOption.setInterval(Long.valueOf("1000"));
            }catch(Throwable e){
                e.printStackTrace();
            }
            try{
                // 设置网络请求超时时间
                locationOption.setHttpTimeOut(Long.valueOf("30000"));
            }catch(Throwable e){
                e.printStackTrace();
            }
    }
}
