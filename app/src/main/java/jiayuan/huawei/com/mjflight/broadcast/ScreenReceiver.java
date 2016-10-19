package jiayuan.huawei.com.mjflight.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import jiayuan.huawei.com.mjflight.ui.activity.LockScreenActivity;

/**
 * Created by Administrator on 2016/10/2 0002.
 */

public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
            Log.v("xhw","开屏");
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
            Log.v("xhw","锁屏");
            Intent activityIntent=new Intent();
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activityIntent.setClass(context, LockScreenActivity.class);
            context.startActivity(intent);
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
            Log.v("xhw","解锁");
        }
    }
}
