package jiayuan.huawei.com.mjflight.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import jiayuan.huawei.com.mjflight.utils.StatusBarCompat2;

/**
 * $desc$
 *
 * @author xuhuawei
 * @time $date$ $time$
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected abstract void initData();

    protected abstract void setContentView();

    protected abstract void findViewByIds();

    protected abstract void requestService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initData();
        setContentView();
        findViewByIds();
        requestService();
    }
    protected void toggleTranslucent(boolean isTranslucent) {
        if (isTranslucent) {
            StatusBarCompat2.translucentStatusBar(this);
        } else {
            StatusBarCompat2.setStatusBarColor(this, StatusBarCompat2.COLOR_DEFAULT_WHITE);
        }
    }
    public void showTost(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void showTost(int text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}

