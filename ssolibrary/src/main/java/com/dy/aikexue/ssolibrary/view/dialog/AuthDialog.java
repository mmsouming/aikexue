package com.dy.aikexue.ssolibrary.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dy.aikexue.ssolibrary.R;


/**
 * Created by caowenhua on 2015/11/5.
 */
public class AuthDialog extends Dialog{

    private ImageView imgX;
    private TextView tv_title;
    private Button btnAuth;
    private Button btnLogin;
    private String title;
    private String authBtnText;

    public AuthDialog(Context context, View.OnClickListener authListener, View.OnClickListener loginListener) {
        this(context, "", "", authListener, loginListener);
    }

    public AuthDialog(Context context,String title,String authBtnText,View.OnClickListener authListener, View.OnClickListener loginListener) {
        super(context, R.style.IOSScaleDialog);
        setContentView(R.layout.dialog_auth);
        assignViews();
        if(title != null && !"".equals(title)) {
            tv_title.setText(title);
        }
        if(authBtnText != null && !"".equals(authBtnText)) {
            btnAuth.setText(authBtnText);
        }
        btnAuth.setOnClickListener(authListener);
        btnLogin.setOnClickListener(loginListener);
        imgX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


    private void assignViews() {
        imgX = (ImageView) findViewById(R.id.img_x);
        btnAuth = (Button) findViewById(R.id.btn_auth);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tv_title = (TextView) findViewById(R.id.tv_dialog_title);
    }

    @Override
    public void show() {
        try {
            super.show();
        }
        catch (Exception e){}
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        }
        catch (Exception e){}
    }
}
