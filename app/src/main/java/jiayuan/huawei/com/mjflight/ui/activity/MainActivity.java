package jiayuan.huawei.com.mjflight.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;


import jiayuan.huawei.com.mjflight.R;
import jiayuan.huawei.com.mjflight.constants.Constants;
import jiayuan.huawei.com.mjflight.constants.SoundManagerHelper;
import jiayuan.huawei.com.mjflight.persistData.SharedManager;
import jiayuan.huawei.com.mjflight.services.FlashLightService;
import jiayuan.huawei.com.mjflight.services.SmsLocationService;
import jiayuan.huawei.com.mjflight.utils.Utils;

import static jiayuan.huawei.com.mjflight.utils.Utils.getCurrentTime;

public class MainActivity extends BaseCameraActivity implements View.OnTouchListener, View.OnLongClickListener, View.OnClickListener {
    private String number = null; //用intent启动拨打电话
    private TextView text_time;

    @Override
    protected void initData() {
        registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void findViewByIds() {

        image_light = (ImageView) findViewById(R.id.image_light);
        text_time = (TextView) findViewById(R.id.text_time);

        AssetManager mgr = getAssets();//得到AssetManager
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/UnidreamLED.ttf");//根据路径得到Typeface
        text_time.setTypeface(tf);//设置字体

        View tel_husband = findViewById(R.id.tel_husband);
        View set_more = findViewById(R.id.set_more);
        View sms_husband_location = findViewById(R.id.sms_husband_location);
        View update_num = findViewById(R.id.update_num);


        tel_husband.setOnClickListener(this);
        set_more.setOnClickListener(this);
        sms_husband_location.setOnClickListener(this);
        update_num.setOnClickListener(this);

        View layout_background = findViewById(R.id.layout_background);
        layout_background.setOnClickListener(this);
        layout_background.setOnLongClickListener(this);
        layout_background.setOnTouchListener(this);

        image_light.setOnClickListener(this);

        String currentTime = getCurrentTime();
        text_time.setText(currentTime);
    }

    @Override
    protected void requestService() {
        String phone = SharedManager.getPhoneNumber(MainActivity.this);
        if (TextUtils.isEmpty(phone)) {
            showDialog();
        } else {
            number = phone;
        }
        toggleTranslucent(true);
    }

    private void showDialog() {
        final EditText inputServer = new EditText(this);
        inputServer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        inputServer.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        InputFilter mInputFilter[] = {new InputFilter.LengthFilter(11)};
        inputServer.setFilters(mInputFilter);

        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入紧急人电话号！").setView(inputServer).setNegativeButton("取消", null);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        if (inputName.length() < 11) {
                            Toast.makeText(MainActivity.this, "请输入正确手机号码", Toast.LENGTH_SHORT).show();
                        } else {
                            number = inputName;
                            SharedManager.setPhoneNumber(inputName, MainActivity.this);
                            dialog.dismiss();
                        }
                    }
                });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isRunning = Utils.isServiceRunning(this,
                FlashLightService.class.getName());
        if (isRunning) {
            image_light.setImageResource(R.mipmap.mainbutton_on1);
        } else {
            image_light.setImageResource(R.mipmap.mainbutton_off1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_light:
                switchLight();
                break;
            case R.id.tel_husband:
                telGe();
                break;
//            case R.id.sms_husband:
//                smsGe();
//                break;
            case R.id.update_num:
                showDialog();
                break;
            case R.id.sms_husband_location:
                requestLocationPermission();
                showTost("正在请求定位，请稍等！");
                break;
            case R.id.set_more:
                Intent intent = new Intent();
                intent.setClass(this, MoreActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_background:
                boolean isShowDialog=SoundManagerHelper.getInstance().startSound();
                if (!isShowDialog) {
                    SoundManagerHelper.getInstance().showConfirmDialog(this);
                }
                break;
        }
    }

    private void telGe() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        startActivity(intent);
    }

//    private void smsGe() {
//        String message = "亲爱的哥，我想你了啊！么么哒！";
//        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("sms_body", message);
//        startActivity(intent);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        if (Constants.isLightOn) {
            SoundManagerHelper.getInstance().releaseSoundManager();
        } else {
            SoundManagerHelper.getInstance().resetVolume();
        }
    }


    protected void requestLocationPermission() {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.DISABLE_KEYGUARD,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS
                }, new PermissionsResultAction() {
                    @Override
                    public void onGranted() {
                        // 启动相机
                        Intent intent = new Intent(MainActivity.this, SmsLocationService.class);
                        intent.putExtra("num", number);
                        startService(intent);
                    }

                    @Override
                    public void onDenied(String permission) {
                        Intent intent = new Intent(MainActivity.this, SmsLocationService.class);
                        intent.putExtra("num", number);
                        startService(intent);
                    }
                });
    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String currentTime = getCurrentTime();
            text_time.setText(currentTime);
        }
    };

    @Override
    public boolean onLongClick(View v) {
        boolean isShowDialog = SoundManagerHelper.getInstance().startLongSound();
        if (!isShowDialog) {
            SoundManagerHelper.getInstance().showConfirmDialog(this);
        }
        return false;
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                SoundManagerHelper.getInstance().stopLongSound();
                break;
        }
        return false;
    }
}
