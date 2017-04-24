package com.dy.aikexue.ssolibrary.loginregister.activit;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.base.BaseActivity;
import com.dy.sdk.utils.CToastUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UserAgreementActivity extends BaseActivity implements View.OnClickListener {

    public final static int TYPE_REGISTER = 1;
    public final static int TYPE_AUTH_IDENTITY = 2;

    public static Intent getStartIntent(Context ctx, int type){
        Intent intent = new Intent(ctx, UserAgreementActivity.class);
        intent.putExtra("type", type);
        return intent;
    }

    private void handleIntent(){
        mType = getIntent().getIntExtra("type", TYPE_REGISTER);
    }

    private String getTitleByType(){
        switch (mType){
            case  TYPE_AUTH_IDENTITY:
                return "实名认证协议";
            case TYPE_REGISTER:
            default:
                return "用户注册协议";
        }
    }

    private String getFileNameByType(){
        switch (mType){
            case  TYPE_AUTH_IDENTITY:
                return "auth_identity.json";
            case TYPE_REGISTER:
            default:
                return "user_agreement.json";
        }
    }

    private int mType = TYPE_REGISTER;
    private TextView mAgreement;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent();

        setContentView(R.layout.activity_user_agreement);

        findViewById(R.id.backLogin).setOnClickListener(this);

        TextView title = (TextView) findViewById(R.id.tv_title_text);
        title.setText(getTitleByType());

        mAgreement = (TextView) findViewById(R.id.tv_user_agreement);

//        agreement.setText();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = null;
                    AssetManager am = getApplicationContext().getResources().getAssets();
                    try {
                        is = am.open(getFileNameByType());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    StringBuffer sb = new StringBuffer();
                    BufferedReader ir = null;
                    try {
                        ir = new BufferedReader(new InputStreamReader(is));
                        String line = "";
                        while ((line = ir.readLine()) != null) {
                            sb.append(line);
                        }
                        if (sb != null && sb.length() > 0) {
                            final String str = sb.toString().replace("\\n", "\n");
//                            final String str = sb.toString();
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mAgreement.setText(str);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (is != null) is.close();
                            if (ir != null) ir.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CToastUtil.toastShort(UserAgreementActivity.this, "加载协议失败");
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.backLogin) {
            finish();
        }
    }
}
