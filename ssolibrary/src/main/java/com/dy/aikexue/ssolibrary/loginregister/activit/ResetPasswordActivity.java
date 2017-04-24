package com.dy.aikexue.ssolibrary.loginregister.activit;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.base.BaseActivity;
import com.dy.aikexue.ssolibrary.config.Config;
import com.dy.aikexue.ssolibrary.util.SpUtil;
import com.dy.aikexue.ssolibrary.util.Tools;
import com.dy.aikexue.ssolibrary.util.VerificationModule;
import com.dy.sdk.utils.CToastUtil;
import com.dy.sdk.view.dialog.LoadingDialog;

import org.cny.awf.net.http.CBase;
import org.cny.awf.net.http.H;
import org.cny.awf.net.http.HCallback;
import org.cny.awf.net.http.HResp;
import org.json.JSONObject;

/**
 * 重置密码
 */
public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText edtPhone;
    private EditText edtCode;
    private EditText edtPwd;
    private TextView tvGetCode;
    private ImageView ivHidePwd;
    private Button btnSubmit;
    private TextView tvGetMaster;
    private LoadingDialog loadingDialog, getPhonecodeDialog;

    private String phone, code, pwd;
    private Context context;
    private String TAG = "ResetPasswordActivity";
    private boolean isShowPwd = true;

    private boolean isFromUpdate;

    //可接受密码字符串
    private String mAcceptedPwdChars =
            ".,1!@#$%^&*:/?'=()"
                    + "abc2ABC"
                    + "def3DEF"
                    + "ghi4GHI"
                    + "jkl5JKL"
                    + "mno6MNO"
                    + "pqrs7PQRS"
                    + "tuv8TUV"
                    + "wxyz9WXYZ"
                    + "0+"
                    + " ";
    private DigitsKeyListener keyListener;
    private VerificationModule verificationModule;

    private String verificationMark;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        context = this;
        isFromUpdate = getIntent().getBooleanExtra("update", false);
        phone = getIntent().getStringExtra("phone");
        initView();
        createVerification();
    }

    private void createVerification() {
        verificationModule = new VerificationModule(this, false, new VerificationModule.DefaultVerifyCallback() {
            @Override
            public void verifySuccess(String mark, String verificationCode) {
                //显示图片验证码
                verificationMark = mark;
                ResetPasswordActivity.this.verificationCode = verificationCode;
                sendPhoneCode();
            }

            @Override
            public void verifyImage(String mark, String imageUrl) {
                //不显示图片验证码
                verificationMark = mark;
                ResetPasswordActivity.this.verificationCode = null;
                sendPhoneCode();
            }
        });
    }


    private void initView() {
        //顶部tab初始化
        LinearLayout lvBack = (LinearLayout) findViewById(R.id.backLogin);
        lvBack.setOnClickListener(this);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title_text);


        edtPhone = (EditText) findViewById(R.id.edt_phone);
        edtCode = (EditText) findViewById(R.id.edt_code);
        edtPwd = (EditText) findViewById(R.id.edt_pwd);
        tvGetCode = (TextView) findViewById(R.id.tv_get_phone_code);
        ivHidePwd = (ImageView) findViewById(R.id.hidePwdImg);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        tvGetMaster = (TextView) findViewById(R.id.tv_get_master);

        if (SpUtil.isLoginByTeacher(this)) {
            btnSubmit.setBackgroundResource(R.drawable.select_button_blue);
            tvGetCode.setBackgroundResource(R.drawable.selector_sso_btn_blue_stroke);
            tvGetCode.setTextColor(getResources().getColor(R.color.color_blue_topBar));
        }
        if (isFromUpdate) {
            tvTitle.setText("修改密码");
            edtPhone.setInputType(InputType.TYPE_CLASS_TEXT);
            edtPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(26)}); //最大输入长度
            edtPhone.setEnabled(false);
            if (phone != null) {
                StringBuilder sb = new StringBuilder(phone);
                edtPhone.setText("已绑定手机  " + sb.replace(3, 7, "****"));
                if (SpUtil.isLoginByTeacher(this)) {
                    edtPhone.setTextColor(getResources().getColor(R.color.color_blue_topBar));
                } else {
                    edtPhone.setTextColor(getResources().getColor(R.color.yunColor));
                }

            }
        } else {
            tvTitle.setText("忘记密码");
        }
//        edtPwd.setKeyListener(DigitsKeyListener.getInstance(mAcceptedPwdChars));
        keyListener = new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT;
            }

            @Override
            protected char[] getAcceptedChars() {
                return mAcceptedPwdChars.toCharArray();
            }
        };
        //设置可接收字符
        edtPwd.setKeyListener(keyListener);

        tvGetCode.setOnClickListener(this);
        ivHidePwd.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        tvGetMaster.setOnClickListener(this);


        loadingDialog = new LoadingDialog(this, "正在提交新密码,请稍等...");
        getPhonecodeDialog = new LoadingDialog(this, "正在发送验证码,请稍等...");
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_submit) {
            doSubmitUpdate();
        } else if (id == R.id.tv_get_phone_code) {
            doGetCode();
        } else if (id == R.id.hidePwdImg) {
            doSeePwd();
        } else if (id == R.id.backLogin) {
            finish();
        } else if (id == R.id.tv_get_master) {
            doGetHelp();
        }
    }

    /**
     * 获取客服帮助
     */
    protected void doGetHelp() {
        Toast.makeText(this, "该功能需要在rcp看跳转效果...", Toast.LENGTH_SHORT).show();
    }


    /**
     * 获取短信验证码
     */
    private void doGetCode() {
        if (!isFromUpdate) phone = edtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Tools.isRightMobilePhone(phone)) {
            Toast.makeText(this, "请输入正确的11位手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        verificationModule.requestVerification();
    }

    private void sendPhoneCode() {
        getPhonecodeDialog.show();
        H.doPost(Config.getPhoneCode(phone, Config.TYPE_RESET_PWD, verificationMark, verificationCode), getPhoneCodeCallBack);
    }

    /**
     * 提交修改密码请求
     */
    private void doSubmitUpdate() {
        if (!isFromUpdate) phone = edtPhone.getText().toString().trim();

        code = edtCode.getText().toString().trim();
        pwd = edtPwd.getText().toString().trim();

        if (TextUtils.isEmpty(phone) || !Tools.isRightMobilePhone(phone)) {
            Toast.makeText(this, "请输入正确的11位手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(code) || code.length() < 6) {
            Toast.makeText(this, "请输入6位数字验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd) || pwd.length() < 6) {
            Toast.makeText(context, R.string.input_pwd_error_hint, Toast.LENGTH_LONG).show();
            return;
        }
        // 2016-03-02  submit  updates
        loadingDialog.show();
        String url = Config.getResetPwdAddr(pwd, code, phone);
        H.doPost(url, updatePwdCallback);
    }

    /**
     * 用户点击查看密码
     */
    private void doSeePwd() {
        pwd = edtPwd.getText().toString().trim();
        if (isShowPwd) {
            ivHidePwd.setBackgroundResource(R.drawable.sso_showpwd);
            edtPwd.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            ivHidePwd.setBackgroundResource(R.drawable.sso_hidepwd);
            edtPwd.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        isShowPwd = !isShowPwd;
        edtPwd.setSelection(pwd.length());
        edtPwd.setKeyListener(keyListener);
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
//                {"code":0,"data":{"code":0,"msg":"OK","result":{"count":1,"fee":0.055,"sid":6.466419376e+09}}}
//                {"code":3,"msg":"mobile(15602269889) had not been register or binded"}
        @Override
        public void onSuccess(CBase c, HResp res, String data) throws Exception {
            Log.i(TAG, "phoneCode : " + data);
            getPhonecodeDialog.dismiss();
            try {
                JSONObject json = new JSONObject(data);
                int code = json.getInt("code");
                if (code == 0) {
                    tvGetCode.setEnabled(false);
                    timer.start();
                    CToastUtil.toastLong(context, getString(R.string.get_code_success_hint));
                } else if (code == 3) {
                    CToastUtil.toastLong(context, "该手机号尚未被注册/绑定账号");
                } else if (code == 10) {
                    CToastUtil.toastLong(context, "验证码错误");
                } else {
                    CToastUtil.toastShort(context, getString(R.string.server_error_hint));
                }
            } catch (Exception e) {
                e.printStackTrace();
                CToastUtil.toastShort(context, getString(R.string.server_error_hint));
            }
        }

        @Override
        public void onError(CBase cBase, String s, Throwable throwable) throws Exception {
            getPhonecodeDialog.dismiss();
            CToastUtil.toastShort(context, getString(R.string.net_error_hint));
        }
    };


    private HCallback.HCacheCallback updatePwdCallback = new HCallback.HCacheCallback() {
        @Override
        public void onError(CBase cBase, String s, Throwable throwable) throws Exception {
            loadingDialog.dismiss();
            CToastUtil.toastShort(context, getString(R.string.net_error_hint));
        }

        @Override
        public void onSuccess(CBase cBase, HResp hResp, String s) throws Exception {
            Log.i(TAG, "updatePwdCallback onSuccess" + s);
//            {"code":5,"msg":"验证码错误"}
            loadingDialog.dismiss();
            //解析返回字符串
            try {
                JSONObject object = new JSONObject(s);
                int code = object.getInt("code");
                if (code == 0) {//重置密码成功
                    Toast.makeText(context, "修改密码成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {//返回code不为0
                    Log.i(TAG, "code  not  0/1" + s);
                    Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "parse json exception---" + e.getMessage());
                Toast.makeText(context, R.string.server_error_hint, Toast.LENGTH_SHORT).show();
            }
        }
    };


}
