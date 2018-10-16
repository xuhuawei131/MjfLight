package jiayuan.huawei.com.mjflight.ui.activity;


import android.content.Intent;
import android.view.View;

import jiayuan.huawei.com.mjflight.R;
import jiayuan.huawei.com.mjflight.ui.customview.LedTextView;


public class LEDShowActivity extends BaseActivity {
    private LedTextView auto_scroll;
    private String value;
    private int size = 0;

    @Override
    protected void initData() {
        Intent intent = getIntent();
        value = intent.getStringExtra("value");
        size = intent.getIntExtra("textSize", 0);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_led_show);
    }

    @Override
    protected void findViewByIds() {
        auto_scroll = (LedTextView) findViewById(R.id.scroll_view);
        auto_scroll.setText(value);
        auto_scroll.setTextSize(size);
        auto_scroll.init(getWindowManager());
        auto_scroll.startScroll();

        View view_left = findViewById(R.id.view_left);
        view_left.setOnClickListener(listener);

        View view_right = findViewById(R.id.view_right);
        view_right.setOnClickListener(listener);
    }

    @Override
    protected void requestService() {

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.view_left){
                auto_scroll.setSpeedAdd(true);
            }else if (v.getId()==R.id.view_right){
                auto_scroll.setSpeedAdd(false);
            }
        }
    };
}
