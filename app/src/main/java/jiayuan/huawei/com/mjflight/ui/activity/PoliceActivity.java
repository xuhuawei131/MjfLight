package jiayuan.huawei.com.mjflight.ui.activity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import jiayuan.huawei.com.mjflight.R;
import jiayuan.huawei.com.mjflight.utils.Utils;


/**
 * 功能描述：
 * 模拟警车车灯
 *
 * @author WAH-WAY(xuwahwhy@163.com)
 *         <p>
 *         <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class PoliceActivity extends BaseActivity implements OnCompletionListener, Runnable {
    private int[] bgcolor = {Color.rgb(255, 15, 0), Color.rgb(0, 0, 0), Color.rgb(255, 15, 0), Color.rgb(0, 0, 0), Color.rgb(255, 15, 0), Color.rgb(0, 0, 0), Color.rgb(0, 0, 255), Color.rgb(0, 0, 0), Color.rgb(0, 0, 255), Color.rgb(0, 0, 0), Color.rgb(0, 0, 255), Color.rgb(0, 0, 0)};
    private int[] bgflashtime = {80, 50, 80, 50, 80, 250, 80, 50, 80, 50, 80, 250};
    private TextView warmingtv;
    private Handler show_handler;
    private MediaPlayer mMediaPlayer;
    private int warmingcounter = -1;

    @Override
    protected void initData() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        show_handler = new Handler();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_police);
    }

    @Override
    protected void findViewByIds() {
        warmingtv = (TextView) findViewById(R.id.warmingtv);
        warmingtv.setBackgroundColor(bgcolor[1]);
        Utils.setBright(this, 1.0f);
    }

    @Override
    protected void requestService() {
        show_handler.postDelayed(this, 50);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        show_handler.removeCallbacks(this);
    }

    @Override
    public void run() {
        warmingcounter++;
        warmingtv.setBackgroundColor(bgcolor[warmingcounter % 12]);
        show_handler.postDelayed(this, bgflashtime[warmingcounter % 12]);
        if (warmingcounter == 240) {
            warmingcounter = -1;
        }
    }

    private void playSounds(int sid) {

        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mMediaPlayer = MediaPlayer.create(this, sid);
        mMediaPlayer.setOnCompletionListener(this);
        /* 准备播放 */
        // mMediaPlayer.prepare();
		/* 开始播放 */
        mMediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }
}