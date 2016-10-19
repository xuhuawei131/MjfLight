package jiayuan.huawei.com.mjflight.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;

import jiayuan.huawei.com.mjflight.R;
import jiayuan.huawei.com.mjflight.persistData.SharedManager;
import jiayuan.huawei.com.mjflight.services.FlashLightService;
import jiayuan.huawei.com.mjflight.utils.Utils;

public class MainActivity extends BaseCameraActivity implements View.OnClickListener {
    private String number = null; //用intent启动拨打电话

    @Override
    protected void initData() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void findViewByIds() {
        image_light = (ImageView) findViewById(R.id.image_light);
        View tel_husband = findViewById(R.id.tel_husband);
        View sms_husband = findViewById(R.id.sms_husband);
        View set_more = findViewById(R.id.set_more);

        tel_husband.setOnClickListener(this);
        sms_husband.setOnClickListener(this);
        set_more.setOnClickListener(this);
        image_light.setOnClickListener(this);
    }

    @Override
    protected void requestService() {
        String phone = SharedManager.getPhoneNumber(MainActivity.this);
        if (TextUtils.isEmpty(phone)) {
            showDialog();
        } else {
            number = phone;
        }
    }

    private void showDialog() {
        final EditText inputServer = new EditText(this);
        inputServer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        inputServer.setInputType(EditorInfo.TYPE_CLASS_PHONE);
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
            case R.id.sms_husband:
                smsGe();
                break;
            case R.id.set_more:
                Intent intent = new Intent();
                intent.setClass(this, MoreActivity.class);
                startActivity(intent);
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

    private void smsGe() {
        String message = "亲爱的哥，我想你了啊！么么哒！";
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
        intent.putExtra("sms_body", message);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
