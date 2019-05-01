package com.bsoft.componentXR;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bsoft.baselib.util.StringUtil;
import com.bsoft.commonlib.arouter.CommonArouterGroup;
import com.bsoft.commonlib.widget.ExpandableTextView;
import com.bsoft.updatelib.config.UpdateConfiguration;
import com.bsoft.updatelib.listener.OnButtonClickListener;
import com.bsoft.updatelib.listener.OnDownloadListener;
import com.bsoft.updatelib.manager.DownloadManager;

import java.io.File;

public class MainActivity extends AppCompatActivity  implements OnDownloadListener, OnButtonClickListener  {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        ExpandableTextView  tvIntro = (ExpandableTextView) findViewById(R.id.tvIntro);
        tv.setText(stringFromJNI());

        tvIntro.setText("查看全部的方式广东东莞广东韶关的观点十多个上过电视公司的三个哥哥是个高手高高手是个水水水水水水是个灌水灌灌灌灌灌灌灌灌灌灌灌灌灌灌灌灌!");
       // CommonArouterGroup.gotoActivity(CommonArouterGroup.CHANGE_NET_ACTIVITY);
      //  finish();

        startUpdate3();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();









    private void startUpdate3() {
        /*
         * 整个库允许配置的内容
         * 非必选
         */
        UpdateConfiguration configuration = new UpdateConfiguration()
                //输出错误日志
                .setEnableLog(true)
                //设置自定义的下载
                //.setHttpManager()
                //下载完成自动跳动安装页面
                .setJumpInstallPage(true)
                //设置对话框背景图片 (图片规范参照demo中的示例图)
                //.setDialogImage(R.drawable.ic_dialog)
                //设置按钮的颜色
                //.setDialogButtonColor(Color.parseColor("#E743DA"))
                //设置按钮的文字颜色
                .setDialogButtonTextColor(Color.WHITE)
                //支持断点下载
                .setBreakpointDownload(true)
                //设置是否显示通知栏进度
                .setShowNotification(true)
                //设置是否提示后台下载toast
                .setShowBgdToast(false)
                //设置强制更新
                .setForcedUpgrade(false)
                //设置对话框按钮的点击监听
                .setButtonClickListener(this)
                //设置下载过程的监听
                .setOnDownloadListener(this);

        DownloadManager manager = DownloadManager.getInstance(this);
        manager.setApkName("appupdate.apk")
                .setApkUrl("https://raw.githubusercontent.com/azhon/AppUpdate/master/apk/appupdate.apk")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowNewerToast(true)
                .setConfiguration(configuration)
//                .setDownloadPath(Environment.getExternalStorageDirectory() + "/AppUpdate")
                .setApkVersionCode(2)
                .setApkVersionName("2.1.8")
                .setApkSize("20.4")
                //provider中设置的authorities值必须与DownloadManager中设置的authorities一致（不设置则为应用包名）
                .setAuthorities(getPackageName()+ ".myFileProvider")
                .setApkDescription("1.支持断点下载\n2.支持Android N\n3.支持Android O\n4.支持自定义下载过程\n5.支持 设备>=Android M 动态权限的申请\n6.支持通知栏进度条展示(或者自定义显示进度)")
                .download();
    }


    @Override
    public void onButtonClick(int id) {

    }

    @Override
    public void start() {

    }

    @Override
    public void downloading(int max, int progress) {

    }

    @Override
    public void done(File apk) {

    }

    @Override
    public void cancel() {

    }

    @Override
    public void error(Exception e) {

    }
}
