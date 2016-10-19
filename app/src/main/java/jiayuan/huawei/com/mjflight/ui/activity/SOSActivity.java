package jiayuan.huawei.com.mjflight.ui.activity;

import android.graphics.Color;
import android.hardware.Camera;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import jiayuan.huawei.com.mjflight.R;
import jiayuan.huawei.com.mjflight.utils.Utils;


public class SOSActivity extends BaseActivity implements Runnable, OnClickListener {
    private Camera m_Camera = null;
    private Camera.Parameters mParameters;
    private static final int red = Color.rgb(255, 0, 0);
    private static final int black = Color.rgb(0, 0, 0);
    private int[] bgcolor = {red, black, red, black, red, black, red, black,
            red, black, red, black, red, black, red, black, red, black};
    private int[] bgflashtime = new int[]{
            300, 300, 300, 300, 300,// ...	S
            900,
            900, 300, 900, 300, 900,// ---	O
            900,
            300, 300, 300, 300, 300,// ...	S
            2100
    };
    private Handler show_handler;
    private TextView warmingtv;
    private int warmingcounter = -1;
    private boolean isOpen = false;

    @Override
    protected void initData() {
        show_handler = new Handler();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏

        try {
            m_Camera = Camera.open();
            mParameters = m_Camera.getParameters();
            isOpen = true;
        } catch (Exception e) {
            isOpen = false;
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_sos);
    }

    @Override
    protected void findViewByIds() {
        warmingtv = (TextView) findViewById(R.id.warmingtv);
        if (!isOpen) {
            StringBuilder sb = new StringBuilder();
            sb.append("正在拍照，请关闭相机后重试\n");
            sb.append("相继被占用，请关闭占用相机应用后重试\n");
            sb.append("无法获取相机权限，请打开家电手电筒的相机权限\n");
            warmingtv.setText(sb.toString());
        }
        warmingtv.setBackgroundColor(bgcolor[1]);
    }

    @Override
    protected void requestService() {
        Utils.setBright(this, 1.0f);
        show_handler.postDelayed(this, 50);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        show_handler.removeCallbacks(this);
        if (isOpen) {
            m_Camera.release();
            m_Camera = null;
        }
    }

    @Override
    public void run() {
        warmingcounter++;
        warmingtv.setBackgroundColor(bgcolor[warmingcounter % 18]);
        if (warmingcounter % 2 == 0) {
            if (isOpen && m_Camera != null) {
                mParameters = m_Camera.getParameters();
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                m_Camera.setParameters(mParameters);
            }

        } else {
            if (isOpen && m_Camera != null) {
                mParameters = m_Camera.getParameters();
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                m_Camera.setParameters(mParameters);
            }
        }
        show_handler.postDelayed(this, bgflashtime[warmingcounter % 18]);
    }
}
