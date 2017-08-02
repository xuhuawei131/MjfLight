package jiayuan.huawei.com.mjflight.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;

import jiayuan.huawei.com.mjflight.R;
import jiayuan.huawei.com.mjflight.constants.Constants;
import jiayuan.huawei.com.mjflight.constants.SoundManagerHelper;
import jiayuan.huawei.com.mjflight.services.FlashLightService;
import jiayuan.huawei.com.mjflight.utils.Utils;

import static jiayuan.huawei.com.mjflight.utils.Utils.getCurrentTime;

/**
 * 添加滑动返回功能
 * http://blog.csdn.net/u010696525/article/details/51445515
 */
public class LockScreenActivity extends BaseCameraActivity implements View.OnTouchListener, View.OnLongClickListener, View.OnClickListener {
    private TextView text_time;

    @Override
    protected void initData() {
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_lock_screen);
    }

    @Override
    protected void findViewByIds() {
        image_light = (ImageView) findViewById(R.id.image_light);
        image_light.setOnClickListener(this);

        text_time = (TextView) findViewById(R.id.text_time);


        View layout_background = findViewById(R.id.layout_background);
        layout_background.setOnClickListener(this);
        layout_background.setOnLongClickListener(this);
        layout_background.setOnTouchListener(this);

    }

    @Override
    protected void requestService() {
        boolean isRunning = Utils.isServiceRunning(this,
                FlashLightService.class.getName());
        if (isRunning) {
            image_light.setImageResource(R.mipmap.mainbutton_on1);
        } else {
            image_light.setImageResource(R.mipmap.mainbutton_off1);
        }

        String currentTime = getCurrentTime();
        text_time.setText(currentTime);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_light:
                switchLight();
                break;
            case R.id.layout_background:
                boolean isShowDialog = SoundManagerHelper.getInstance().startSound();
                if (!isShowDialog) {
                    SoundManagerHelper.getInstance().showConfirmDialog(this);
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Constants.isLightOn) {
            SoundManagerHelper.getInstance().releaseSoundManager();
        } else {
            SoundManagerHelper.getInstance().resetVolume();
        }
        unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String currentTime = getCurrentTime();
            text_time.setText(currentTime);
        }
    };

    @Override
    public boolean onLongClick(View v) {
        boolean isShowDialog = SoundManagerHelper.getInstance().startLongSound();
        if (!isShowDialog) {
            SoundManagerHelper.getInstance().showConfirmDialog(this);
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                SoundManagerHelper.getInstance().stopLongSound();
                break;
        }
        return false;
    }
}
