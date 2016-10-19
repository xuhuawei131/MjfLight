package jiayuan.huawei.com.mjflight.ui.activity;

import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import jiayuan.huawei.com.mjflight.R;


/**
 * 功能描述：
 * 编辑led显示的内容
 *
 * @author WAH-WAY(xuwahwhy@163.com)
 *         <p>
 *         <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class LEDEditActivity extends BaseActivity implements OnClickListener {
    private EditText edit;
    private TextView edit_count;
    private SeekBar changeTextSize;
    private ImageView play, reset;
    private int textSize = 50;

    @Override
    protected void initData() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_led_edit);
    }

    @Override
    protected void findViewByIds() {
        edit = (EditText) findViewById(R.id.edit_led);
        edit_count = (TextView) findViewById(R.id.count);
        changeTextSize = (SeekBar) findViewById(R.id.led_seekBar);

        View led_min=findViewById(R.id.led_min);
        View led_max=findViewById(R.id.led_max);


        led_min.setOnClickListener(this);
        led_max.setOnClickListener(this);

        play = (ImageView) findViewById(R.id.led_play);
        reset = (ImageView) findViewById(R.id.led_reset);

        edit.addTextChangedListener(new TextWatcher() {
            CharSequence tem;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tem = s;
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                int len = tem.length();
                if (len > 30) {
                    Toast.makeText(getApplicationContext(), "你已输入超过30字", Toast.LENGTH_SHORT).show();
                    edit_count.setText("" + 0);
                    CharSequence str = tem.subSequence(0, 30);
                    edit.setText(str);
                    edit.setSelection(30);
                } else {
                    edit_count.setText("" + (30 - tem.length()));
                }
            }
        });

//        LayerDrawable progressDrawable = (LayerDrawable) changeTextSize.getProgressDrawable();
//        Drawable[] outDrawables = new Drawable[progressDrawable.getNumberOfLayers()];
//        for (int i = 0; i < progressDrawable.getNumberOfLayers(); i++) {
//            switch (progressDrawable.getId(i)) {
//                case android.R.id.background:// 设置进度条背景
//                    outDrawables[i] = getResources().getDrawable(R.drawable.led_slider_empty);
//                    break;
//                case android.R.id.secondaryProgress:// 设置二级进度条
//                    outDrawables[i] = getResources().getDrawable(R.drawable.led_slider_empty);
//                    break;
//                case android.R.id.progress:// 设置进度条
//                    ScaleDrawable oidDrawable = (ScaleDrawable) progressDrawable.getDrawable(i);
//                    Drawable drawable = getResources().getDrawable(R.drawable.led_slider_fill);
//                    ClipDrawable proDrawable = new ClipDrawable(drawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
//                    proDrawable.setLevel(oidDrawable.getLevel());
//                    outDrawables[i] = proDrawable;
//                    break;
//                default:
//                    break;
//            }
//        }
//        progressDrawable = new LayerDrawable(outDrawables);
//        changeTextSize.setProgressDrawable(progressDrawable);

        changeTextSize.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    textSize = progress;
                }
            }
        });

        play.setOnClickListener(this);
        reset.setOnClickListener(this);
    }

    @Override
    protected void requestService() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.led_play:
                String content = edit.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    showTost("不能为空！");
                } else {
                    Intent intent = new Intent(this, LEDShowActivity.class);
                    intent.putExtra("value", edit.getText().toString().trim());
                    intent.putExtra("textSize", (textSize + 100));
                    startActivity(intent);
                }
                break;
            case R.id.led_reset:
                edit.setText("");
                break;
            case R.id.led_min:
                changeTextSize.setProgress(0);
                changeTextSize.setSecondaryProgress(0);
                break;
            case R.id.led_max:
                changeTextSize.setProgress(100);
                changeTextSize.setSecondaryProgress(100);
                break;
        }
    }

}
