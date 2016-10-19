package jiayuan.huawei.com.mjflight.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;

import jiayuan.huawei.com.mjflight.R;
import jiayuan.huawei.com.mjflight.services.FlashLightService;
import jiayuan.huawei.com.mjflight.utils.Utils;

/**
 * 添加滑动返回功能
 * http://blog.csdn.net/u010696525/article/details/51445515
 */
public class LockScreenActivity extends BaseCameraActivity implements View.OnClickListener {
    private ImageView image_light;
    @Override
    protected void initData() {
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_lock_screen);
    }

    @Override
    protected void findViewByIds() {
        image_light = (ImageView) findViewById(R.id.image_light);
        image_light.setOnClickListener(this);
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

    }


    @Override
    public void onClick(View v) {
        switchLight();
    }
}
