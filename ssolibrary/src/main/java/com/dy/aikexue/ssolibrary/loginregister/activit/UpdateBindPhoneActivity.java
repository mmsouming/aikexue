package com.dy.aikexue.ssolibrary.loginregister.activit;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.base.BaseActivity;
import com.dy.aikexue.ssolibrary.config.Config;
import com.dy.aikexue.ssolibrary.db.DataHelper;
import com.dy.aikexue.ssolibrary.db.UserInfo;
import com.dy.aikexue.ssolibrary.util.Dysso;
import com.dy.aikexue.ssolibrary.util.SpUtil;
import com.dy.aikexue.ssolibrary.util.Tools;
import com.dy.aikexue.ssolibrary.util.VerificationModule;
import com.dy.sdk.utils.CToastUtil;

import org.cny.awf.net.http.CBase;
import org.cny.awf.net.http.H;
import org.cny.awf.net.http.HCallback;
import org.cny.awf.net.http.HResp;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by:Pxy
 * Date: 2016-03-08
 * Time: 15:47
 * 修改绑定手机号
 */
public class UpdateBindPhoneActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rel_bind_phone_mark;
    private EditText edtNewPhone;
    private EditText edtCode;
    private Button btnConfirm;
    private TextView tvGetCode;
    private TextView tvGetMaster;
    private TextView tvHint;

    private Context context;

    private String newPhone, code;

    private String type;
    public static final String TYPE_BIND = Config.TYPE_BIND;
    public static final String TYPE_MODIFY_BIND = Config.TYPE_MODIFY_BIND;

    private Logger L = LoggerFactory.getLogger(UpdateBindPhoneActivity.class);

    public static Intent getStartIntent(Activity activity, String type) {
        Intent intent = new Intent(activity, UpdateBindPhoneActivity.class);
        intent.putExtra("Type", type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone);
        context = this;
        type = getIntent().getStringExtra("Type");
        if (type == null) {
            type = TYPE_BIND;
        }
        initView();
    }

    private void initView() {
        //顶部tab初始化
        LinearLayout lvBack = (LinearLayout) findViewById(R.id.backLogin);
        lvBack.setOnClickListener(this);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title_text);

        rel_bind_phone_mark = (RelativeLayout) findViewById(R.id.rel_bind_phone_mark);
        edtNewPhone = (EditText) findViewById(R.id.edt_new_phone);
        edtCode = (EditText) findViewById(R.id.edt_code);
        tvGetCode = (TextView) findViewById(R.id.tv_getCode);
        tvGetMaster = (TextView) findViewById(R.id.tv_get_master);
        tvHint = (TextView) findViewById(R.id.tv_hint1);
        btnConfirm = (Button) findViewById(R.id.btn_submit);

        tvGetCode.setOnClickListener(this);
        tvGetMaster.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        if (SpUtil.isLoginByTeacher(this)) {
            tvGetCode.setBackgroundResource(R.drawable.selector_sso_btn_blue_stroke);
            tvGetCode.setTextColor(getResources().getColor(R.color.color_blue_topBar));
            btnConfirm.setBackgroundResource(R.drawable.select_button_blue);
        }

        String title = "";
        if (TYPE_BIND.equals(type)) {
            title = "绑定手机";
            rel_bind_phone_mark.setVisibility(View.GONE);
        } else {
            title = "更换绑定手机";
            rel_bind_phone_mark.setVisibility(View.VISIBLE);
        }
        tvTitle.setText(title);

        if (Dysso.getUserInfo() != null && Dysso.getUserInfo().getPhone() != null
                && Dysso.getUserInfo().getPhone().length() > 1) {
            tvHint.setText("您当前的手机号为：" + Dysso.getUserInfo().getPhone());
        } else {
            tvHint.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_getCode) {
            //获取验证码
            //先判断是否需要图片验证码
            getPhotoCode();
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

    private void doUpdateBindRequest() {
        newPhone = edtNewPhone.getText().toString().trim();
        code = edtCode.getText().toString().trim();
        if (TextUtils.isEmpty(newPhone) || TextUtils.isEmpty(code)) {
            CToastUtil.toastShort(context, "输入信息不能为空");
            return;
        }
        if (!Tools.isRightMobilePhone(newPhone)) {
            CToastUtil.toastShort(context, "请输入正确的11位手机号码");
            return;
        }
        if (newPhone.equals(Dysso.getUserInfo().getPhone())) {
            CToastUtil.toastShort(context, "新号码不能与原绑定号码相同");
            return;
        }

        initLoading("正在修改绑定手机");
        String url = Config.bindPhoneAddr(newPhone, code, Dysso.getToken(), Dysso.getUserInfo().getPhone());
        L.info("url---" + url);
        H.doGet(url, bindCallBack);
    }

    protected HCallback.HCacheCallback bindCallBack = new HCallback.HCacheCallback() {
        //        0：成功,1:参数错误,2:手机号码有误,3:该手机已经绑定,4:类型错误,5:验证码有误,6:token有误或者过期,7:数据库错误
        @Override
        public void onSuccess(CBase c, HResp res, String s) throws Exception {
            L.info("bindCallBack onSuccess---" + s);
            dismissLoading();
            try {
                JSONObject json = new JSONObject(s);
                int code = json.getInt("code");
                if (code == 0) {
                    CToastUtil.toastShort(context, "修改绑定手机成功");
                    // 更新绑定信息  修改数据库
                    DataHelper.getInstance(context).updateColumn(UserInfo.PHONE, newPhone, Dysso.getUid());
                    Dysso.getUserInfo().setPhone(newPhone);
                    finish();
                } else {
                    L.info(s);
                    CToastUtil.toastShort(context, json.getString("msg"));
                }
            } catch (Exception e) {
                L.error("parse json exception---" + e.getMessage());
                CToastUtil.toastShort(context, getString(R.string.server_error_hint));
            }
        }

        @Override
        public void onError(CBase cBase, String s, Throwable throwable) throws Exception {
            dismissLoading();
            L.error("bind phone error" + s + throwable.getMessage());
        }
    };

    private void doGetPhoneCode(String markId, String photoCode) {
        newPhone = edtNewPhone.getText().toString().trim();
        if (TextUtils.isEmpty(newPhone)) {
            CToastUtil.toastShort(context, "手机号不能为空");
            return;
        }
        if (!Tools.isRightMobilePhone(newPhone)) {
            CToastUtil.toastShort(context, "请输入正确的11位手机号码");
            return;
        }
        if (newPhone.equals(Dysso.getUserInfo().getPhone())) {
            CToastUtil.toastShort(context, "新号码不能与原绑定号码相同");
            return;
        }
        initLoading("发送验证码中...");
        //请求获取验证码
        H.doPost(Config.getPhoneCode(newPhone, type, markId, photoCode), getPhoneCodeCallBack);
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
            L.info("phoneCode : " + data);
            dismissLoading();
            try {
                JSONObject json = new JSONObject(data);
                int code = json.getInt("code");
                if (code == 0) {
                    timer.start();
                    CToastUtil.toastLong(context, getString(R.string.get_code_success_hint));
                } else if (3 == code) {
                    CToastUtil.toastLong(context, "该手机号已被其它账号绑定");
                } else if (code == 10) {
                    CToastUtil.toastLong(context, "图片验证码错误");
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
            dismissLoading();
            CToastUtil.toastLong(context, getString(R.string.net_error_hint));
        }
    };

    //联系客服
    public void doGetMaster() {
        try {
            Class<?> cl = Class.forName("com.dy.imsa.im.IM");
            Method method = cl.getMethod("waiter", Context.class);
            method.invoke(null, this);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    VerificationModule verificationModule;
    PhotoCodeCallBack photoCodeCallBack;

    /**
     * 获取图片验证码
     */
    private void getPhotoCode() {
        if (photoCodeCallBack == null) {
            photoCodeCallBack = new PhotoCodeCallBack();
        }
        if (verificationModule == null) {
            verificationModule = new VerificationModule(this, false, photoCodeCallBack);
        }
        verificationModule.requestVerification();
    }

    class PhotoCodeCallBack extends VerificationModule.DefaultVerifyCallback {
        @Override
        public void verifySuccess(String mark, String verificationCode) {
            //显示图片验证码
            if (verificationCode != null && !"".equals(verificationCode)) {
                //显示对话框
                doGetPhoneCode(mark, verificationCode);
            } else {
                //直接请求获取手机验证码
                doGetPhoneCode(mark, "");
            }
        }

        @Override
        public void verifyImage(String mark, String imageUrl) {
            //不显示图片验证码
            doGetPhoneCode(mark, "");
        }
    }

}
