package jiayuan.huawei.com.mjflight.constants;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import jiayuan.huawei.com.mjflight.MyApp;
import jiayuan.huawei.com.mjflight.bean.SoundBean;

/**
 * 声音辅助类
 * http://www.2cto.com/kf/201408/325318.html
 * Created by Administrator on 2017/7/13 0013.
 */

public class SoundManagerHelper {
    private static final SoundManagerHelper ourInstance = new SoundManagerHelper();
    private SoundPool soundPool;
    private int curIndex = 0;
    private SoundBean bean;
    private int currentVolume;
    private AudioManager mAudioManager;
    private boolean isAlert = false;//是否允许 鸣笛
    private static final int STREAM_TYPE=AudioManager.STREAM_MUSIC;
    private boolean isStop=false;
    public static SoundManagerHelper getInstance() {
        return ourInstance;
    }

    private SoundManagerHelper() {
        mAudioManager = (AudioManager) MyApp.appContext.getSystemService(Context.AUDIO_SERVICE);
        int max = mAudioManager.getStreamMaxVolume(STREAM_TYPE);
        currentVolume = mAudioManager.getStreamVolume(STREAM_TYPE);
        mAudioManager.setStreamVolume(STREAM_TYPE, max / 4, 0);


//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            SoundPool.Builder sb = new SoundPool.Builder();
//            sb.setMaxStreams(10);
//            sb.setAudioAttributes(null);//默认是media类型，其他的可以看看源码。都有
//            soundPool = sb.build();
//        } else {
//            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
//        }
        soundPool = new SoundPool(10, STREAM_TYPE, 5);
        for (SoundBean bean : Constants.soundList) {
            soundPool.load(MyApp.appContext, bean.resSound, bean.index);
        }
        setMusicPath(0);
    }

    /**
     * 重置音量
     */
    public void resetVolume() {
        mAudioManager.setStreamVolume(STREAM_TYPE, currentVolume, 0);
    }

    /**
     * 重置销毁
     */
    public void releaseSoundManager() {
        resetVolume();
        soundPool.release();
        isAlert = false;
    }

    public void setSoundMode() {
        isAlert = true;
    }


    public void showConfirmDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("是否启动车铃模式")
                .setMessage("启动后可以点击屏幕任意处响铃！")
                .setNegativeButton("取消", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new Dialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        setSoundMode();
                    }
                });
        builder.create().show();
    }

    /**
     * 设置铃声
     *
     * @param curIndex
     */
    public void setMusicPath(int curIndex) {
        this.curIndex = curIndex;
        this.bean = Constants.soundList.get(curIndex);
    }

    /**
     * 短按铃声
     *
     * @return
     */
    public boolean startSound() {
        if (isAlert) {
            isStop=false;
            soundPool.play(bean.index, 1, 1, 0, 0, 1);
        }
        return isAlert;
    }

    /**
     * 长按铃声
     *
     * @return
     */
    public boolean startLongSound() {
        if (isAlert) {
            isStop=false;
            handler.sendEmptyMessage(1);
        }
        return isAlert;
    }

    /**
     * 停止长按
     */
    public void stopLongSound() {
        isStop=true;
        Log.v("xhw","handler remove");
        handler.removeMessages(1);
        soundPool.stop(bean.index);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(!isStop){
                Log.v("xhw","handler add");
                soundPool.play(bean.index, 1, 1, 0, 0, 1);
                handler.sendEmptyMessageDelayed(1, 750);
            }
        }
    };


}
