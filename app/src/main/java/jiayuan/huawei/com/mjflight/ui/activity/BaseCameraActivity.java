package jiayuan.huawei.com.mjflight.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.widget.ImageView;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;

import jiayuan.huawei.com.mjflight.R;
import jiayuan.huawei.com.mjflight.services.FlashLightService;
import jiayuan.huawei.com.mjflight.utils.Utils;

/**
 * Created by Administrator on 2016/10/19 0019.
 */

public abstract class BaseCameraActivity extends BaseActivity {
    protected ImageView image_light;

    protected void switchLight() {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                new String[]{Manifest.permission.CAMERA}, new PermissionsResultAction() {
                    @Override
                    public void onGranted() {
                        // 启动相机
                        boolean isRunning = Utils.isServiceRunning(BaseCameraActivity.this,
                                FlashLightService.class.getName());
                        Intent intent = new Intent(BaseCameraActivity.this, FlashLightService.class);
                        if (isRunning) {
                            image_light.setImageResource(R.mipmap.mainbutton_off1);
                            stopService(intent);
                        } else {
                            image_light.setImageResource(R.mipmap.mainbutton_on1);
                            startService(intent);
                        }
                    }
                    @Override
                    public void onDenied(String permission) {
                        switchLight();
                    }
                });
    }


}
