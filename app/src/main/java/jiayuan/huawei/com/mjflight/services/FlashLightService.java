package jiayuan.huawei.com.mjflight.services;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;

import jiayuan.huawei.com.mjflight.constants.Constants;
import jiayuan.huawei.com.mjflight.ui.activity.LockScreenActivity;

public class FlashLightService extends Service {
    private Camera camera;
    private boolean isOpen = false;

    public boolean isLightOpen() {
        return isOpen;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        openFlashLight();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        this.registerReceiver(mScreenBroadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeFlashLight();
        unregisterReceiver(mScreenBroadcastReceiver);
    }

    /**
     * 功能描述：
     * 打开闪光灯
     *
     * @author WAH-WAY(xuwahwhy@163.com)
     * <p>创建日期 ：2015年5月29日 下午1:31:39</p>
     * <p>
     * <p>
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    private void openFlashLight() {
        if (!isOpen) {//闪光灯处于关闭状态
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                // your code using Camera API here - is between 1-20
            } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // your code using Camera2 API here - is api 21 or higher
            }
            if (camera == null) {
                try {
                    camera = Camera.open();
                    camera.startPreview();
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                    isOpen = true;
                    sendStatusBroadcast(isOpen);
                } catch (Exception e) {
                    Intent intent = new Intent(Constants.ACTION_STATUS_CAMERA_ERROR);
                    sendBroadcast(intent);
                    stopSelf();
                    return;
                }

            }

        }
    }

    /**
     * 功能描述：
     * 关闭闪光灯
     *
     * @author WAH-WAY(xuwahwhy@163.com)
     * <p>创建日期 ：2015年5月29日 下午1:31:31</p>
     * <p>
     * <p>
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    private void closeFlashLight() {
        if (isOpen) {//闪光灯已经打开
            if (camera != null) {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                camera.release();
                camera = null;
            }
            isOpen = false;
            sendStatusBroadcast(isOpen);
        }
    }

    private void sendStatusBroadcast(boolean isRunning) {
        Intent intent = new Intent(Constants.ACTION_STATUS_CHANGE);
        intent.putExtra("isRunning", isRunning);
        sendBroadcast(intent);
    }

    private BroadcastReceiver mScreenBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
                Intent activityIntent=new Intent(context, LockScreenActivity.class);
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(activityIntent);
            }
        }
    };
}
