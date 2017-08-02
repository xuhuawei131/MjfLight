package jiayuan.huawei.com.mjflight.constants;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import jiayuan.huawei.com.mjflight.R;
import jiayuan.huawei.com.mjflight.bean.SoundBean;

/**
 * Created by Administrator on 2016/2/19.
 */
public class Constants {
    //服务中 闪光灯状态改变的广播
    public static final String ACTION_STATUS_CHANGE="ACTION_STATUS_CHANGE";

    //服务中 闪光灯状态改变的广播
    public static final String ACTION_STATUS_CAMERA_ERROR="ACTION_STATUS_CAMERA_ERROR";

    public static boolean isLightOn=false;
    public static final List<SoundBean> soundList=new CopyOnWriteArrayList<>();
    static {
        soundList.add(new SoundBean(R.raw.bike1,"自行车1",1));
        soundList.add(new SoundBean(R.raw.bike2,"自行车2",2));
        soundList.add(new SoundBean(R.raw.bike3,"自行车3",3));
        soundList.add(new SoundBean(R.raw.bike4,"自行车4",4));
        soundList.add(new SoundBean(R.raw.car1,"汽车汽笛",5));
    }

}
