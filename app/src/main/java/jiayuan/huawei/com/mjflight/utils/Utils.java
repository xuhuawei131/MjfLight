package jiayuan.huawei.com.mjflight.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.view.WindowManager;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/2/19.
 */
public class Utils {

    public static String getCurrentTime(){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        String hourStr=hour+"";
        String minuteStr=minute+"";
        if(hour<10){
            hourStr="0"+hour;
        }
        if(minute<10){
            minuteStr="0"+minute;
        }
        return hourStr+":"+minuteStr;
    }


    /**
     * 是否有后置摄像头
     * @return true表示拥有，false表示没有
     */
    public static boolean isHoldFlashLight(){
        Camera camera = Camera.open();
        if (camera==null) {
            return false;
        }else {
            camera.release();
            camera = null;
            return true;
        }
    }
    /**
     *
     * 功能描述：
     * 打开摄像头
     * @author WAH-WAY(xuwahwhy@163.com)
     * <p>创建日期 ：2015年6月6日 上午12:47:21</p>
     *
     * @return
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    public static Camera openCemera(){
        Camera camera = null;
        try{
            camera= Camera.open();
            return camera;
        }catch(Exception e){
            return camera;
        }
    }
    /**
     *
     * 功能描述：
     * 检测服务是否在运行
     * @author WAH-WAY(xuwahwhy@163.com)
     * <p>创建日期 ：2015年5月29日 下午12:51:33</p>
     *
     * @param context
     * @param serviceName
     * @return
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    public static boolean isServiceRunning(Context context,String serviceName) {
        if(serviceName==null){
            return false;
        }
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



    /**
     *
     * 功能描述：
     * 是否支持闪光灯
     * @author WAH-WAY(xuwahwhy@163.com)
     * <p>创建日期 ：2015年5月29日 上午11:48:44</p>
     *
     * @param context
     * @return
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    public static boolean isSupportFlashLight(Context context) {
        PackageManager pm = context.getPackageManager();
        FeatureInfo[] features = pm.getSystemAvailableFeatures();
        for (FeatureInfo f : features) {
            if (PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) {// 判断设备是否支持闪光灯
                return true;
            }
        }
        return false;
    }



    /**
     *
     * 功能描述：
     * 设置屏幕亮度
     * @author WAH-WAY(xuwahwhy@163.com)
     * <p>创建日期 ：2015年5月30日 上午11:43:55</p>
     *
     * @param light
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    public static  void setBright(Activity activity, float light) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = light;
        activity.getWindow().setAttributes(lp);
    }
}
