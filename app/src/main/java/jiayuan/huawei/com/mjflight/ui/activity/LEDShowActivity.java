package jiayuan.huawei.com.mjflight.ui.activity;


import android.content.Intent;

import jiayuan.huawei.com.mjflight.R;
import jiayuan.huawei.com.mjflight.ui.customview.LedTextView;


public class LEDShowActivity extends BaseActivity {
	private LedTextView auto_scroll;
	private String value;
	private int size=0;
	@Override
	protected void initData() {
		 Intent intent = getIntent();
		 value=intent.getStringExtra("value");
		 size=intent.getIntExtra("textSize", 0);
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
	}

	@Override
	protected void requestService() {

	}

}
