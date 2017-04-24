package com.dy.aikexue.ssolibrary.loginregister.activit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.base.BaseActivity;
import com.dy.aikexue.ssolibrary.bean.Attrs;
import com.dy.aikexue.ssolibrary.bean.Certification;
import com.dy.aikexue.ssolibrary.bean.Extra;
import com.dy.aikexue.ssolibrary.bean.NewUserData;
import com.dy.aikexue.ssolibrary.bean.SSOHTTP;
import com.dy.aikexue.ssolibrary.config.Config;
import com.dy.aikexue.ssolibrary.db.DataHelper;
import com.dy.aikexue.ssolibrary.db.UserInfo;
import com.dy.aikexue.ssolibrary.util.Dysso;
import com.dy.aikexue.ssolibrary.util.SpUtil;
import com.dy.aikexue.ssolibrary.util.Tools;
import com.dy.aikexue.ssolibrary.util.VerificationModule;
import com.dy.sdk.utils.CToastUtil;
import com.dy.sdk.utils.ThemeUtil;
import com.dy.sdk.view.dialog.LoadingDialog;
import com.google.gson.Gson;

import org.cny.awf.net.http.CBase;
import org.cny.awf.net.http.H;
import org.cny.awf.net.http.HCallback;
import org.cny.awf.net.http.HResp;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by:Pxy
 * Date: 2016-03-14
 * Time: 15:16
 * 新的注册
 */
public class RegisterActivity extends BaseActivity implements OnClickListener, TextWatcher {
    private EditText edtUser;
    private EditText edtCode;
    private EditText edtPwd;
    private TextView tvGetCode;
    private Button btnRegister;
    private CheckBox cb4UserArragment;
    private TextView tvUserArragment;
    private ImageView hidePwdImg;

    private Logger logger = LoggerFactory.getLogger(RegisterActivity.class);
    private String usr, code, pwd;
    private Context context;
    private LoadingDialog loadingDialog, getPhonecodeDialog;
    private boolean isShowPwd = true;

    private DataHelper dataHelper;

    //可接受密码字符
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;
        initView();
    }

    private void initView() {
        //顶部tab初始化
        LinearLayout lvBack = (LinearLayout) findViewById(R.id.backLogin);
        lvBack.setOnClickListener(this);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title_text);
        tvTitle.setText("注册大洋通行证");

        edtUser = (EditText) findViewById(R.id.regUsr);
        edtCode = (EditText) findViewById(R.id.regUsr_phone_code);
        edtPwd = (EditText) findViewById(R.id.regPwd);
        tvGetCode = (TextView) findViewById(R.id.tv_get_phone_code);
        btnRegister = (Button) findViewById(R.id.registerUsr);
        cb4UserArragment = (CheckBox) findViewById(R.id.read_label);
        tvUserArragment = (TextView) findViewById(R.id.tv_user_agreement);
        hidePwdImg = (ImageView) findViewById(R.id.hidePwdImg);

        btnRegister.setBackgroundDrawable(ThemeUtil.getButtonSolidSelectDrawable(this));
        if(SpUtil.isLoginByTeacher(this)){
            tvGetCode.setTextColor(getResources().getColor(R.color.color_blue_topBar));
            tvGetCode.setBackgroundDrawable(ThemeUtil.getButtonStrokeSelectDrawable(this));
            tvUserArragment.setTextColor(getResources().getColor(R.color.color_blue_topBar));
            cb4UserArragment.setButtonDrawable(getResources().getDrawable(R.drawable.reg_readimg_selector_blue));
        }
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
        btnRegister.setOnClickListener(this);
        tvUserArragment.setOnClickListener(this);
        hidePwdImg.setOnClickListener(this);
        edtPwd.addTextChangedListener(this);

        getPhonecodeDialog = new LoadingDialog(this, "正在发送验证码,请稍等...");
        loadingDialog = new LoadingDialog(context, "正在注册，请稍等...");
        verificationModule = new VerificationModule(this,false, new VerificationModule.DefaultVerifyCallback(){
            @Override
            public void verifySuccess(String mark, String verificationCode) {
                //显示图片验证码
                doGetCode(mark, verificationCode);
            }

            @Override
            public void verifyImage(String mark, String imageUrl) {
                //不显示图片验证码
                doGetCode(mark, null);
            }
        });
    }

    @Override
    protected void onDestroy() {
        verificationModule.cancelVerify();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (R.id.registerUsr == id) {
            doRegister();
        } else if (R.id.tv_get_phone_code == id) {
            // 获取验证码
//            verificationModule.requestVerification();
            doGetCodeIfCan();
        } else if (R.id.tv_user_agreement == id) {
            //跳转至用户协议activity
            startActivity(new Intent(this, UserAgreementActivity.class));
        } else if (R.id.hidePwdImg == id) {
            doSeePwd();
        } else if (R.id.backLogin == id) {
            finish();
        }
    }

    /**
     * 获取短信验证码
     */
    private void doGetCodeIfCan() {
        usr = edtUser.getText().toString().trim();
        if (TextUtils.isEmpty(usr)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Tools.isRightMobilePhone(usr)) {
            Toast.makeText(this, "请输入正确的11位手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        verificationModule.requestVerification();
    }

    private void doGetCode(String mark, String verificationCode) {
        //获取短信验证码
        getPhonecodeDialog.show();
//        H.doPost(Config.getPhoneCode(usr, Config.TYPE_REGISTER), getPhoneCodeCallBack);
        H.doPost(Config.getPhoneCode(usr, Config.TYPE_REGISTER, mark, verificationCode), getPhoneCodeCallBack);
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
        public void onSuccess(CBase c, HResp res, String data) throws Exception {
            logger.info("phoneCode : " + data);
            getPhonecodeDialog.dismiss();
            try {
                JSONObject json = new JSONObject(data);
                int code = json.getInt("code");
                if (code == 0) {
                    tvGetCode.setEnabled(false);
                    timer.start();
                    CToastUtil.toastLong(context, getString(R.string.get_code_success_hint));
                } else if (3 == code) {
                    CToastUtil.toastLong(context, "该手机号已被注册");
                } else if (10 == code) {
                    CToastUtil.toastLong(context, "图片验证码错误");
                } else {
                    CToastUtil.toastShort(context, getString(R.string.server_error_hint));
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                CToastUtil.toastShort(context, getString(R.string.server_error_hint));
            }
        }

        @Override
        public void onError(CBase cBase, String s, Throwable throwable) throws Exception {
            getPhonecodeDialog.dismiss();
            CToastUtil.toastShort(context, getString(R.string.server_error_hint));
        }
    };

    /**
     * 用户点击查看密码
     */
    private void doSeePwd() {
        pwd = edtPwd.getText().toString().trim();
        if (isShowPwd) {
            hidePwdImg.setBackgroundResource(R.drawable.sso_showpwd);
            edtPwd.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            hidePwdImg.setBackgroundResource(R.drawable.sso_hidepwd);
            edtPwd.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        isShowPwd = !isShowPwd;
        edtPwd.setSelection(pwd.length());
        edtPwd.setKeyListener(keyListener);
    }

    /**
     * 注册请求
     */
    private void doRegister() {
        usr = edtUser.getText().toString();
        code = edtCode.getText().toString();
        pwd = edtPwd.getText().toString();

        if (TextUtils.isEmpty(usr)) {
            CToastUtil.toastShort(context, "请输入手机号");
            return;
        }
        if (!Tools.isRightMobilePhone(usr)) {
            CToastUtil.toastShort(context, "请输入正确的11位手机号");
            return;
        }
        if (TextUtils.isEmpty(code)||code.length()<6) {
            CToastUtil.toastShort(context, "请输入6位数字验证码");
            return;
        }
        if (TextUtils.isEmpty(pwd)||pwd.length() < 6) {
            CToastUtil.toastShort(context, "密码长度至少为6位");
            return;
        }
        if (!cb4UserArragment.isChecked()) {
            CToastUtil.toastShort(context, "请先阅读并同意用户协议");
            return;
        }
        try {
            JSONObject json = new JSONObject();
            json.put("pwd", pwd);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(usr);
            json.put("usr", jsonArray);
            loadingDialog.show();
            H.doPostData(Config.newRegisterAddr(code, 1), json, hCacheCallback);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("exception" + e.getMessage());
        }
    }

    HCallback.HCacheCallback hCacheCallback = new HCallback.HCacheCallback() {
        //        0：登录成功，1：参数错误，2：json body错误，3：添加用户失败，4：登录失败
        @Override
        public void onError(CBase cBase, String s, Throwable throwable) throws Exception {
            throwable.printStackTrace();
            loadingDialog.dismiss();
            CToastUtil.toastShort(context, getString(R.string.net_error_hint));
        }

        @Override
        public void onSuccess(CBase cBase, HResp hResp, String s) throws Exception {
            logger.info("register onSuccess--" + s);
            loadingDialog.dismiss();
            try {
                Gson gson = new Gson();
                NewUserData newUserData = gson.fromJson(s, NewUserData.class);
                logger.info(newUserData.toString());
                int code = newUserData.getCode();
                if (code == 0) {
                    try {
                        if (dataHelper == null) {
                            dataHelper = DataHelper.getInstance(context);
                        }
                        // 保存用户信息至数据库
                        UserInfo userInfo = new UserInfo();
                        userInfo.setUserId(newUserData.getData().getUsr().getId());
                        userInfo.setToken(newUserData.getData().getToken());
                        userInfo.setAttrs(s);
                        //绑定手机以及认证状态
                        userInfo.setPhone(usr);
                        userInfo.setPass(0);

                        Attrs attrs = newUserData.getData().getUsr().getAttrs();
                        if (attrs != null) {
                            Attrs.Basic basic = attrs.getBasic();
                            Certification certification = attrs.getCertification();
                            Extra extra = attrs.getExtra();
                            userInfo.setBasicUserInfo(basic);
                            userInfo.setCertification(certification);
                            userInfo.setExtraInfo(extra);
                            if (basic != null) {
                                userInfo.setUserName(basic.getNickName());
                                userInfo.setGender(basic.getGender());
                                userInfo.setSign("这家伙很懒，什么也没留下");
                                userInfo.setHeadurl(basic.getAvatar());
                            }
                            if (extra != null) {
                                userInfo.setLocation(extra.getProvince() + "/" + extra.getCity());
                            }
                            userInfo.setCertification(certification);
                        }
                        long id = dataHelper.SaveUserInfo(userInfo);
                        logger.info("save  data success--" + id);
                        logger.info("userInfo------" + userInfo.toString());
                        // 注册回调
                        SSOHTTP http = new SSOHTTP();
                        http.setToken(newUserData.getData().getToken());
                        http.setUserInfo(userInfo);
                        Intent mIntent = new Intent("SSO");
                        mIntent.putExtra("token", newUserData.getData().getToken());
                        LocalBroadcastManager.getInstance(getApplicationContext())
                                .sendBroadcast(mIntent);
                        Dysso.handleListener(http);//回调
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("code 0 exception--" + e.getMessage());
                    }
                    //2016-03-25 结束该LoginActivity和RegisterActivity
                    if (LoginActivity.context != null) {
                        ((LoginActivity) LoginActivity.context).finish();
                    }
                    finish();
                    CToastUtil.toastLong(context, "注册成功");

                } else if (code == 3) {
                    CToastUtil.toastLong(context, "该手机号已被注册");
                } else if (10 == code) {
                    CToastUtil.toastLong(context, "验证码错误");
                } else {
                    JSONObject object = new JSONObject(s);
                    Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                CToastUtil.toastShort(context, getString(R.string.server_error_hint));
            }
        }
    };


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        usr = edtUser.getText().toString();
        code = edtCode.getText().toString();
        pwd = edtPwd.getText().toString();
//        if (Tools.isRightMobilePhone(usr) && !TextUtils.isEmpty(code) && !TextUtils.isEmpty(pwd)) {
//            if(SpUtil.isLoginByTeacher(this)){
//                btnRegister.setBackgroundResource(R.drawable.select_button_blue);
//            }else{
//                btnRegister.setBackgroundResource(R.drawable.btnselector);
//            }
//        } else {
//            if(SpUtil.isLoginByTeacher(this)){
//                btnRegister.setBackgroundResource(R.drawable.shape_button_blue_false);
//            }else{
//                btnRegister.setBackgroundResource(R.drawable.sso_login_transparent);
//            }
//        }
    }
}
