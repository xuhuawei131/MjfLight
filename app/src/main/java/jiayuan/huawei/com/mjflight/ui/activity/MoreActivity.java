package jiayuan.huawei.com.mjflight.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import jiayuan.huawei.com.mjflight.R;

public class MoreActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);


        View layout_led = findViewById(R.id.layout_led);
        View layout_sos = findViewById(R.id.layout_sos);
        View layout_police = findViewById(R.id.layout_police);
        View layout_colorlight = findViewById(R.id.layout_colorlight);

        layout_led.setOnClickListener(this);
        layout_sos.setOnClickListener(this);
        layout_police.setOnClickListener(this);
        layout_colorlight.setOnClickListener(this);
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
        }
        startActivity(intent);
    }
}
