package jiayuan.huawei.com.mjflight.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class LedTextView extends TextView implements OnClickListener {
	private float textLength = 0f;
	private float viewWidth = 0f;
	private float step = 0f;
	private float y = 0f;
	private float temp_view_plus_text_length = 0.0f;
	private float temp_view_plus_two_text_length = 0.0f;
	public boolean isStarting = false;
	private Paint paint = null;
	private String text = "";

	public LedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public LedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public LedTextView(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		setOnClickListener(this);
	}

	public void init(WindowManager windowManager) {
		paint = getPaint();
		paint.setColor(Color.WHITE);
		text = getText().toString();
		textLength = paint.measureText(text);
		viewWidth = getWidth();
		paint.setColor(Color.GREEN);
		if (viewWidth == 0) {
			if (windowManager != null) {
				Display display = windowManager.getDefaultDisplay();
				viewWidth = display.getWidth();
			}
		}
		step = textLength;
		temp_view_plus_text_length = viewWidth + textLength;
		temp_view_plus_two_text_length = viewWidth + textLength * 2;
		y = getTextSize() + getPaddingTop();
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.step = step;
		ss.isStarting = isStarting;
		return ss;

	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		if (!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());

		step = ss.step;
		isStarting = ss.isStarting;

	}

	public static class SavedState extends BaseSavedState {
		public boolean isStarting = false;
		public float step = 0.0f;

		SavedState(Parcelable superState) {
			super(superState);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeBooleanArray(new boolean[] { isStarting });
			out.writeFloat(step);
		}

		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}

			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}
		};

		private SavedState(Parcel in) {
			super(in);
			boolean[] b = null;
			in.readBooleanArray(b);
			if (b != null && b.length > 0)
				isStarting = b[0];
			step = in.readFloat();
		}
	}

	public void startScroll() {
		isStarting = true;
		invalidate();
	}

	public void stopScroll() {
		isStarting = false;
		invalidate();
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawText(text, temp_view_plus_text_length - step, y, paint);
		if (!isStarting) {
			return;
		}
		step += 5;
		if (step > temp_view_plus_two_text_length)
			step = textLength;
		invalidate();

	}

	@Override
	public void onClick(View v) {
		if (isStarting)
			stopScroll();
		else
			startScroll();

	}

}
