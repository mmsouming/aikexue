package com.dy.aikexue.ssolibrary.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

import com.dy.aikexue.ssolibrary.R;
import com.dy.sdk.activity.CollectActionFragmentActivity;
import com.dy.sdk.utils.StatusBarTool;
import com.dy.sdk.view.dialog.LoadingDialog;

/**
 * sso base FragmentActivity
 */
public class BaseFragmentActivity extends CollectActionFragmentActivity {

    protected LoadingDialog loadingDialog;

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    public void onStart() {
        super.onStart();
        setTitleBackground();
    }

    public void setTitleBackground(){
        View view=findViewById(R.id.title_view);
        if(view==null){
            return;
        }
        StatusBarTool.setTitleColor(view,this);
    }

    /**
     * 创建加载窗口
     */
    private void initLoading(String msg) {
        if(loadingDialog == null) {
            loadingDialog = new LoadingDialog(this, msg);
        }else{
            loadingDialog.setMessage(msg);
        }
        loadingDialog.show();
    }

    /**
     * 隐藏加载窗口
     */
    private void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
