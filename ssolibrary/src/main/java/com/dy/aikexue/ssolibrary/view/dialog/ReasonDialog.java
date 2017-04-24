package com.dy.aikexue.ssolibrary.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.dy.aikexue.ssolibrary.R;
import com.dy.sdk.utils.ScreenUtil;

/**
 * Created by zhong on 2017/3/1.
 */

public class ReasonDialog extends Dialog implements View.OnClickListener {

    private View contentView;

    private Button btNot, btCommit;
    private EditText edContent;

    public ReasonDialog(Context context) {
        this(context, 0);
    }

    public ReasonDialog(Context context, int themeResId) {
        super(context, R.style.resumeDialog);
        setDialogFillWindow();
        initView();
        initListener();
        setContentView(contentView);
    }

    private void initListener() {
        btCommit.setOnClickListener(this);
        btNot.setOnClickListener(this);
    }

    private void initView() {
        contentView = View.inflate(getContext(), R.layout.item_resume_reason, null);
        btNot = (Button) contentView.findViewById(R.id.btNot);
        btCommit = (Button) contentView.findViewById(R.id.btCommit);
        edContent = (EditText) contentView.findViewById(R.id.edContent);
    }

    /**
     * 设置对话框填充整个窗口
     */
    private void setDialogFillWindow() {
        Window window = this.getWindow();
        // 设置宽高
        window.setLayout(ScreenUtil.getScreenWidth(getContext()) - ScreenUtil.dip2px(getContext(), 50), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btCommit) {
            //提交
            executeOnCommit();
        } else if (id == R.id.btNot) {
            //暂不填写
            executeOnNot();
        }
    }

    OnReasonListener onReasonListener;

    public void setOnReasonListener(OnReasonListener onReasonListener) {
        this.onReasonListener = onReasonListener;
    }

    private void executeOnCommit() {
        if (onReasonListener != null) {
            onReasonListener.onCommit(edContent.getText().toString());
        }
    }

    private void executeOnNot() {
        if (onReasonListener != null) {
            onReasonListener.onNot();
        }
    }

    public interface OnReasonListener {
        void onCommit(String contentStr);

        void onNot();
    }

    @Override
    public void show() {
        super.show();
        setDialogFillWindow();
    }
}
