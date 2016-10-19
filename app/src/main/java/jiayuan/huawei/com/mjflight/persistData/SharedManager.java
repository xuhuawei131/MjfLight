package jiayuan.huawei.com.mjflight.persistData;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * $desc$
 *
 * @author xuhuawei
 * @time $date$ $time$
 */
public class SharedManager {
    private static final String SHARE_NAME = "phone_shared";

    public static void setPhoneNumber(String phoneNumber, Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("phoneNum", phoneNumber);
        editor.commit();
        context = null;
    }

    public static String getPhoneNumber(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        String phone = sp.getString("phoneNum", "");
        context = null;
        return phone;
    }
}
