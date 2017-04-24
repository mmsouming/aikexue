package com.dy.aikexue.ssolibrary.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dy.aikexue.ssolibrary.R;


/**
 * Created by:Pxy
 * Date: 2016-03-08
 * Time: 16:08
 */
public class BindPhoneDialog extends Dialog{

    private TextView  tvTitle;
    private ImageView ivClose;
    private FrameLayout flClsoe;
    private EditText edtPhone;
    private EditText  edtCode;
    private TextView  tvGetCode;
    private Button btnConfirm;


    private View.OnClickListener  listener;
    private Context  context;
    private String title;

    public BindPhoneDialog(Context context, int theme,String title) {
        super(context, theme);
        this.context=context;
        this.listener= (View.OnClickListener) context;
        this.title=title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bind_phone_dialog);


        tvTitle= (TextView) findViewById(R.id.tv_title);
//        ivClose= (ImageView) findViewById(R.id.iv_close);
        flClsoe= (FrameLayout) findViewById(R.id.fl_close_dialog);
        edtPhone= (EditText) findViewById(R.id.edt_phone);
        edtCode= (EditText) findViewById(R.id.edt_code);
        btnConfirm= (Button) findViewById(R.id.btn_confirm);
        tvGetCode= (TextView) findViewById(R.id.tv_getCode);

        if(title!=null)tvTitle.setText(title);
        //增加监听事件
//        ivClose.setOnClickListener(listener);
        btnConfirm.setOnClickListener(listener);
        flClsoe.setOnClickListener(listener);
        tvGetCode.setOnClickListener(listener);
    }

    public String  getPhone(){
        return edtPhone.getText().toString();
    }
    public String  getCode(){
        return edtCode.getText().toString();
    }

    public TextView  getCodeTextView(){
        return tvGetCode;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }
}
