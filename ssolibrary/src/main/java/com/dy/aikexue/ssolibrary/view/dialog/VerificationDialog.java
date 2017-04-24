package com.dy.aikexue.ssolibrary.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.view.VerificationImageView;
import com.dy.sdk.utils.CToastUtil;

/**
 * Created by yuhy on 2017/1/20.
 */

public class VerificationDialog extends Dialog implements View.OnClickListener{

    View close, refresh, confirm;
    EditText editText;
    VerificationImageView verificationImg;
    String imageUrl;
    String mark;
    onConfirmClickListener onConfirmClickListener;

    public VerificationDialog(Context context, String imageUrl, String mark) {
        super(context, R.style.MyDialog);
        this.imageUrl = imageUrl;
        this.mark = mark;
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_verification);

        close = findViewById(R.id.btn_close);
        verificationImg = (VerificationImageView) findViewById(R.id.verification_img);
        verificationImg.setUrl(imageUrl);
        refresh = findViewById(R.id.btn_refresh);
        editText = (EditText) findViewById(R.id.et_verification);
        confirm = findViewById(R.id.btn_confirm);

        close.setOnClickListener(this);
        refresh.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_close){
            dismiss();
        }else if(id == R.id.btn_refresh){
            verificationImg.refresh();
            //refresh verification
        }else if(id == R.id.btn_confirm){
            //确认

            String verificationCode = editText.getText().toString().trim();
            if(TextUtils.isEmpty(verificationCode)){
                CToastUtil.toastShort(getContext(), "验证码不能为空");
            }else{
                if(onConfirmClickListener != null){
                    onConfirmClickListener.onConfirmClick(mark, verificationCode);
                }
                dismiss();
            }
        }
    }


    public void refreshVerification(String mark, String imageUrl){
        this.mark = mark;
        if(verificationImg != null && imageUrl != null && !imageUrl.equals(verificationImg.getOriginalUrl())){
            //图片地址不一致才刷新url;
            this.imageUrl = imageUrl;
            verificationImg.setUrl(verificationImg.refreshUrl(imageUrl));
        }
        if(editText != null){
            editText.setText(null);
        }
    }

    public void setOnConfirmClickListener(VerificationDialog.onConfirmClickListener onConfirmClickListener) {
        this.onConfirmClickListener = onConfirmClickListener;
    }

    public interface onConfirmClickListener{
        void onConfirmClick(String mark, String verificationCode);
    }
}











