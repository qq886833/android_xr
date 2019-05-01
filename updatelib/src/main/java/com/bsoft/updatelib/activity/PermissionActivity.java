package com.bsoft.updatelib.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.bsoft.baselib.log.LogUtil;
import com.bsoft.updatelib.util.Constant;
import com.bsoft.updatelib.R;
import com.bsoft.updatelib.service.DownloadService;
import com.bsoft.updatelib.util.PermissionUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 项目名:    AppUpdate
 * 包名       com.azhon.appupdate.activity
 * 文件名:    PermissionActivity
 * 创建时间:  2018/1/27 on 17:16
 * 描述:     TODO 用来申请权限的活动
 *
 * @author 阿钟
 */

public class PermissionActivity extends AppCompatActivity {

    private static final String TAG =  "PermissionActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionUtil.requestPermission(this, Constant.PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 0) {
                // do something...
                LogUtil.e(TAG, "权限请求回调：grantResults.length = 0");
            } else {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //授予了权限
                    download();
                } else {
                    //拒绝了
                    Toast.makeText(PermissionActivity.this, R.string.permission_hint, Toast.LENGTH_LONG).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            //用户勾选了不在询问
                            goToAppDetailPage(PermissionActivity.this);
                        }
                    }
                }
            }
            finish();
        }
    }

    /**
     * 开始下载
     */
    private void download() {
        startService(new Intent(this, DownloadService.class));
    }

    /**
     * 跳转至详情界面
     */
    private void goToAppDetailPage(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }
}