package jiayuan.huawei.com.mjflight;

import android.app.Application;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public class MyApp extends Application {
    public static Application appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext=this;
    }
}
