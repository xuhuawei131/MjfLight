package jiayuan.huawei.com.mjflight.ui.activity;

import android.content.Intent;
import android.view.View;

import jiayuan.huawei.com.mjflight.R;

public class MoreActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void initData() {

    }
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_more);
    }

    @Override
    protected void findViewByIds() {
        View layout_led = findViewById(R.id.layout_led);
        View layout_sos = findViewById(R.id.layout_sos);
        View layout_police = findViewById(R.id.layout_police);
        View layout_colorlight = findViewById(R.id.layout_colorlight);
        View layout_sound = findViewById(R.id.layout_sound);

        layout_led.setOnClickListener(this);
        layout_sos.setOnClickListener(this);
        layout_police.setOnClickListener(this);
        layout_colorlight.setOnClickListener(this);
        layout_sound.setOnClickListener(this);
    }

    @Override
    protected void requestService() {
        toggleTranslucent(true);
    }
    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()) {
            case R.id.layout_led:
                intent.setClass(this,LEDEditActivity.class);
                break;
            case R.id.layout_sos:
                intent.setClass(this,SOSActivity.class);
                break;
            case R.id.layout_police:
                intent.setClass(this,PoliceActivity.class);
                break;
            case R.id.layout_colorlight:
                intent.setClass(this,ColorLightActivity.class);
                break;
            case R.id.layout_sound:
                intent.setClass(this,RingListActivity.class);
                break;
        }
        startActivity(intent);
    }
}
