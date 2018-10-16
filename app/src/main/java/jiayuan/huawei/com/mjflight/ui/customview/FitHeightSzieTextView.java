package jiayuan.huawei.com.mjflight.ui.customview;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by xuhuawei on 2017/11/30.
 */

public class FitHeightSzieTextView extends TextView {
    protected Paint mTextPaint;
    private float mMaxTextSize; // 获取当前所设置文字大小作为最大文字大小
    private float mMinTextSize = 8;    //最小的字体大小

    public FitHeightSzieTextView(Context context) {
        super(context);
    }

    public FitHeightSzieTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setGravity(getGravity() | Gravity.CENTER_VERTICAL); // 默认水平居中
        setLines(1);
        initialise();
    }

    private void initialise() {
        mTextPaint = new TextPaint();
        mTextPaint.set(this.getPaint());
        //默认的大小是设置的大小，如果撑不下了 就改变
        mMaxTextSize = 1080;
    }

    //文字改变的时候
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        refitText(text.toString(), this.getHeight());   //textview视图的高度
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

    private void refitText(String textString, int height) {
        if (height > 0) {
            int availableHeight = height - this.getPaddingTop() - this.getPaddingBottom();   //减去边距为字体的实际高度
            float trySize = mMaxTextSize;
            mTextPaint.setTextSize(trySize);
            while (mTextPaint.descent()-mTextPaint.ascent() > availableHeight) {   //测量的字体高度过大，不断地缩放
                trySize -= 1;  //字体不断地减小来适应
                if (trySize <= mMinTextSize) {
                    trySize = mMinTextSize;  //最小为这个
                    break;
                }
                mTextPaint.setTextSize(trySize);
            }
            setTextSize(px2sp(getContext(), trySize));
        }
    }

    //大小改变的时候
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (h != oldh) {
            refitText(this.getText().toString(), h);
        }
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static float px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (pxValue / fontScale);
    }

}