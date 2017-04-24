package com.dy.aikexue.ssolibrary.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.dy.aikexue.ssolibrary.R;
import com.dy.sdk.activity.CollectActionActivity;
import com.dy.sdk.utils.StatusBarTool;
import com.dy.sdk.view.dialog.LoadingDialog;


/**
 * sso base Activity
 */
public class BaseActivity extends CollectActionActivity {

    protected boolean firstClick;
    protected LoadingDialog loadingDialog;

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * 创建加载窗口
     */
    protected void initLoading(String msg) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this, msg);
        } else {
            loadingDialog.setMessage(msg);
        }
        loadingDialog.show();
    }

    /**
     * 隐藏加载窗口
     */
    protected void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        if (firstClick) {
            firstClick = false;
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firstClick = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        setTitleBackground();
    }

    public void setTitleBackground() {
        View view = findViewById(R.id.title_view);
        if (view == null) {
            return;
        }
        StatusBarTool.setTitleColor(view, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoading();
        loadingDialog = null;
    }
}
