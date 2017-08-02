package jiayuan.huawei.com.mjflight.services;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import jiayuan.huawei.com.mjflight.R;
import jiayuan.huawei.com.mjflight.locations.LocationHelper;


/**
 * http://gundumw100.iteye.com/blog/1774899
 * Created by Administrator on 2016/10/24 0024.
 */

public class SmsLocationService extends Service implements BDLocationListener {
    public static final String SENT_SMS_ACTION = "com.mjf.mail.other.SmsPay.SENT_SMS_ACTION";
    public static final String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

    private LocationHelper helper;
    private boolean isLocationSucc = true;
    private String number;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        helper = new LocationHelper(getApplicationContext());
        helper.setLocationOption(helper.getDefaultLocationClientOption());
        helper.registerListener(this);

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(SENT_SMS_ACTION);
        intentFilter.addAction(DELIVERED_SMS_ACTION);
        registerReceiver(mBroadcastReceiver,intentFilter);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        number = intent.getStringExtra("num");
        helper.start();// 定位SDK
//        helper.requestLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.stop();
        helper.unregisterListener(this);
        unregisterReceiver(mBroadcastReceiver);
    }


    private BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(SENT_SMS_ACTION.equals(action)){
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context,
                                "短信发送成功", Toast.LENGTH_SHORT)
                                .show();
                        stopSelf();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        break;
                }
            }else{
                Toast.makeText(context,
                        "收信人已经成功接收", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    private void sendSMS( String message) {
        SmsManager sms = SmsManager.getDefault();
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(SmsLocationService.this, 0, sentIntent, 0);
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent deliverPI = PendingIntent.getBroadcast(SmsLocationService.this, 0, deliverIntent, 0);
        ArrayList<String> msgs = sms.divideMessage(message);
        ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliverIntents = new ArrayList<PendingIntent>();
        for (int i = 0; i < msgs.size(); i++) {
            sentIntents.add(null);
            deliverIntents.add(null);
        }
        sentIntents.add(msgs.size() - 1, sentPI);
        deliverIntents.add(msgs.size() - 1, deliverPI);
        sms.sendMultipartTextMessage(number, null, msgs, sentIntents, deliverIntents);
}


    @Override
    public void onReceiveLocation(BDLocation location) {
        if (null != location && location.getLocType() != BDLocation.TypeServerError) {
            if (isLocationSucc) {
                isLocationSucc = false;
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                String addr = location.getAddrStr();
                String appName = this.getString(R.string.app_name);
                String url = String.format("http://api.map.baidu.com/marker?location=%f,%f&output=html&coord_type=gcj02&title=我的地址&content=%s&src=%s", lat, lng, addr, appName);

                String message = "我在:" + addr + " 这里，快来接我！" + url;
                sendSMS(message);
                helper.stop();
            }
        }
    }
}
