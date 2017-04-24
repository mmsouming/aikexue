package com.dy.aikexue.ssolibrary.loginregister.activit;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.base.BaseActivity;
import com.dy.aikexue.ssolibrary.config.Config;
import com.dy.sdk.utils.CToastUtil;
import com.dy.sdk.utils.L;
import com.dy.sdk.view.dialog.LoadingDialog;

import org.cny.awf.net.http.CBase;
import org.cny.awf.net.http.H;
import org.cny.awf.net.http.HCallback;
import org.cny.awf.net.http.HResp;
import org.json.JSONObject;

/**
 * 有部分账号是有手机号码但未绑定的，登录时需绑定才能下一步操作；
 * Created by yuhy on 2016/10/17.
 */
public class BindingExistPhoneActivity extends BaseActivity implements View.OnClickListener {


    private EditText edtCode;
    private Button btnConfirm;
    private TextView tvGetCode;
    private TextView tvGetMaster;
    private TextView tvHint;
    private TextView mExistPhone, mBindExistPhoneTip;

    private Context context;
    private LoadingDialog loadingDialog;

    private String phone, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            phone = getIntent().getStringExtra("existPhone");
        }
        if (TextUtils.isEmpty(phone)) {
            finish();
            return;
        }


        setContentView(R.layout.activity_bind_exist_phone);
        context = this;
        initTitleBar();

        edtCode = (EditText) findViewById(R.id.edt_code);
        tvGetCode = (TextView) findViewById(R.id.tv_getCode);
        tvGetMaster = (TextView) findViewById(R.id.tv_get_master);
        tvHint = (TextView) findViewById(R.id.tv_hint1);
        btnConfirm = (Button) findViewById(R.id.btn_submit);
        mExistPhone = (TextView) findViewById(R.id.tv_exist_phone);
        mBindExistPhoneTip = (TextView) findViewById(R.id.tv_bind_exist_phone_tip);
        mExistPhone.setText(phone);
        mBindExistPhoneTip.setText(getString(R.string.bind_exist_phone_tip, phone));

        tvGetCode.setOnClickListener(this);
        tvGetMaster.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        loadingDialog = new LoadingDialog(this, "正在修改绑定手机");
    }

    //初始化头部标签
    private void initTitleBar() {
        TextView tv_title = (TextView) findViewById(R.id.tv_title_text);
        tv_title.setText("绑定手机");
        findViewById(R.id.backLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public Dialog getCodeLoadingDialog() {
        loadingDialog.setMessage("发送验证码中...");
        return loadingDialog;
    }

    public Dialog getLoadingDialog() {
        loadingDialog.setMessage("加载中...");
        return loadingDialog;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_getCode) {
            //获取验证码
            doGetPhoneCode();
        } else if (id == R.id.tv_get_master) {
            //联系客服
            doGetMaster();
        } else if (id == R.id.backLogin) {
            finish();
        } else if (id == R.id.btn_submit) {
            // 修改绑定账号请求
            doUpdateBindRequest();
        }
    }

    private void doGetPhoneCode() {
        getCodeLoadingDialog().show();
        //请求获取验证码
        H.doPost(Config.getPhoneCode(phone, Config.TYPE_VERIFY), getPhoneCodeCallBack);
    }

    //显示60s倒计时按钮
    CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            SpannableStringBuilder builder = new SpannableStringBuilder("" + millisUntilFinished / 1000 + "s后可重发");
            builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.greenBg)), 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.edittext_hint_color)), builder.length() - 4, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvGetCode.setText(builder);
        }

        @Override
        public void onFinish() {
            tvGetCode.setEnabled(true);
            tvGetCode.setText(R.string.regUsrtips_get_phone_code);
        }
    };
    protected HCallback.HCacheCallback getPhoneCodeCallBack = new HCallback.HCacheCallback() {
        //        0：success, 1 服务器错误，2 手机号码错误，3 手机已注册或被绑定， 4 参数错误
        @Override
        public void onSuccess(CBase c, HResp res, String data) throws Exception {
            L.debug("phoneCode : " + data);
            getCodeLoadingDialog().dismiss();
            try {
                JSONObject json = new JSONObject(data);
                int code = json.getInt("code");
                if (code == 0) {
                    timer.start();
                    CToastUtil.toastLong(context, getString(R.string.get_code_success_hint));
                } else if (3 == code) {
                    CToastUtil.toastLong(context, "该手机号已被其它账号绑定");
                } else {
                    CToastUtil.toastShort(context, getString(R.string.net_error_hint));
                }
            } catch (Exception e) {
                e.printStackTrace();
                CToastUtil.toastShort(context, getString(R.string.net_error_hint));
            }
        }

        @Override
        public void onError(CBase cBase, String s, Throwable throwable) throws Exception {
            getCodeLoadingDialog().dismiss();
            CToastUtil.toastLong(context, getString(R.string.net_error_hint));
        }
    };

    private void doUpdateBindRequest() {
        code = edtCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            CToastUtil.toastShort(context, "验证码不能为空");
            return;
        }

        getLoadingDialog().show();
        String url = Config.bindPhoneAddr(phone, code, null, null, true);
        L.info("url---" + url);
        H.doGet(url, bindCallBack);
    }

    protected HCallback.HCacheCallback bindCallBack = new HCallback.HCacheCallback() {
        //        0：成功,1:参数错误,2:手机号码有误,3:该手机已经绑定,4:类型错误,5:验证码有误,6:token有误或者过期,7:数据库错误
        @Override
        public void onSuccess(CBase c, HResp res, String s) throws Exception {
            L.info("bindCallBack onSuccess---" + s);
            getLoadingDialog().dismiss();
            try {
                JSONObject json = new JSONObject(s);
                int code = json.getInt("code");
                if (code == 0) {
                    Intent intent = new Intent();
                    intent.putExtra("loginInfo", s);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    L.debug(s);
                    CToastUtil.toastShort(context, json.getString("msg"));
                }
            } catch (Exception e) {
                L.debug("parse json exception---" + e.getMessage());
                CToastUtil.toastShort(context, getString(R.string.server_error_hint));
            }
        }

        @Override
        public void onError(CBase cBase, String s, Throwable throwable) throws Exception {
            getLoadingDialog().dismiss();
            CToastUtil.toastShort(context, "请检查网络后重试");
            L.debug("bind phone error" + s + throwable.getMessage());
        }
    };

    public void doGetMaster() {

    }
}
