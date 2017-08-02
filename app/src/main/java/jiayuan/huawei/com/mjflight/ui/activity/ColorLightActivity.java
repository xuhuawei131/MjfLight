package jiayuan.huawei.com.mjflight.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import jiayuan.huawei.com.mjflight.R;
import jiayuan.huawei.com.mjflight.ui.customview.RevealLayout;
import jiayuan.huawei.com.mjflight.utils.Utils;

/**
 * 功能描述：
 * 颜色灯
 *
 * @author WAH-WAY(xuwahwhy@163.com)
 *         <p>
 *         <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class ColorLightActivity extends Activity implements View.OnClickListener {
    private RevealLayout reveal_layout;
    private Resources myColor;
    private int lightItem;

    private int btnIdArray[]={R.id.centerView,R.id.viewGreen,R.id.viewBlue,R.id.viewRed,R.id.viewYellow,R.id.viewPink,};
    private int viewIdArray[]={R.id.view_white,R.id.view_green,R.id.view_blue,R.id.view_red,R.id.view_yellow,R.id.view_pink};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_colorlight);
        findViewById();
    }

    private void initData() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window myWindow = this.getWindow();
        myWindow.setFlags(flag, flag);
        myColor = getBaseContext().getResources();
    }

    private void findViewById() {
//        SetColor(R.color.white);
        reveal_layout=(RevealLayout)findViewById(R.id.reveal_layout);
        for(int i=0;i<btnIdArray.length;i++){
            findViewById(btnIdArray[i]).setOnClickListener(this);
        }

        Utils.setBright(this, 1.0f);// 改变屏幕亮度
    }

    @Override
    public void onClick(View v) {
        int clickId=v.getId();
        for(int i=0;i<btnIdArray.length;i++){
            if(clickId==btnIdArray[i]){
                reveal_layout.next(viewIdArray[i],(int)v.getX(),(int)v.getY(),200,null);
                break;
            }
        }
    }


//    /**
//     * 选择颜色
//     */
//    public void selectColor() {
//        final String[] items = {"白色", "红色", "黑色", "黄色", "粉色"};
//        new AlertDialog.Builder(this).setTitle("选择颜色") // 此处 this 代表当前Activity
//                .setItems(items, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int item) {
//
//                        Toast.makeText(getApplicationContext(), items[item],
//                                Toast.LENGTH_SHORT).show(); // 将选中的文本内容按照土司提示
//                        // 方式显示出来,
//                        // 此处的getApplicationContext()
//                        // 得到的也是当前的Activity对象，可用当前Activity对象的名字.this代替（Activity.this）
//                        switch (item) {
//                            case 0:
//                                SetColor(R.color.white);
//                                break;
//                            case 1:
//                                SetColor(R.color.red);
//                                break;
//                            case 2:
//                                SetColor(R.color.black);
//                                break;
//                            case 3:
//                                SetColor(R.color.yellow);
//                                break;
//                            case 4:
//                                SetColor(R.color.pink);
//                                break;
//                            default:
//                                SetColor(R.color.white);
//                                break;
//                        }
//                    }
//                }).show();// 显示对话框
//    }
//
//    /**
//     * 选择亮度
//     */
//    public void selectBright() {
//        final String[] items = {"100%", "75%", "50%", "25%", "10%"};
//        new AlertDialog.Builder(this)
//                .setTitle("选择亮度")
//                .setSingleChoiceItems(items, lightItem,
//                        new DialogInterface.OnClickListener() { // 此处数字为选项的下标，从0开始，
//                            // 表示默认哪项被选中
//                            public void onClick(DialogInterface dialog, int item) {
//                                Toast.makeText(getApplicationContext(),
//                                        items[item], Toast.LENGTH_SHORT).show();
//                                lightItem = item;
//                                switch (item) {
//                                    case 0:
//                                        Utils.setBright(ColorLightActivity.this, 1.0f);// 改变屏幕亮度
//                                        break;
//                                    case 1:
//                                        Utils.setBright(ColorLightActivity.this, 0.75F);
//                                        break;
//                                    case 2:
//                                        Utils.setBright(ColorLightActivity.this, 0.5F);
//                                        break;
//                                    case 3:
//                                        Utils.setBright(ColorLightActivity.this, 0.25F);
//                                        break;
//                                    case 4:
//                                        Utils.setBright(ColorLightActivity.this, 0.1F);
//                                        break;
//                                    default:
//                                        Utils.setBright(ColorLightActivity.this, 1.0F);
//                                        break;
//                                }
//                                dialog.cancel();
//                            }
//                        }).show();// 显示对话框
//    }
//    /**
//     * 功能描述：
//     * 设置屏幕颜色
//     *
//     * @param color_1 <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
//     * @author WAH-WAY(xuwahwhy@163.com)
//     * <p>创建日期 ：2015年5月30日 上午11:43:46</p>
//     */
//    private void SetColor(int color_1) {
////        Drawable drawable_color = myColor.getDrawable(color_1);
////        mylayout.setBackgroundDrawable(drawable_color);
//    }


}