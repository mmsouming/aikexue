package com.dy.aikexue.ssolibrary.loginregister.activit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.base.BaseActivity;
import com.dy.aikexue.ssolibrary.config.Config;
import com.dy.aikexue.ssolibrary.util.Dysso;
import com.dy.aikexue.ssolibrary.util.SToastUtil;
import com.dy.aikexue.ssolibrary.util.Tools;
import com.dy.sdk.utils.CToastUtil;
import com.dy.sdk.utils.L;
import com.dy.sdk.view.dialog.LoadingDialog;

import org.cny.awf.net.http.CBase;
import org.cny.awf.net.http.H;
import org.cny.awf.net.http.HCallback;
import org.cny.awf.net.http.HResp;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class VerificationPhoneActivity extends BaseActivity implements View.OnClickListener {

    EditText ed_number;
    TextView tv_detail, tv_title;
    Button bt_get, bt_submit;

    ImageView img_back;
    public static final String PHONE = "phone";
    private static final String PHONE_CODE = "phone_code";

    public static Intent getIntent(Context context, String phone) {
        Intent intent = new Intent(context, VerificationPhoneActivity.class);
        intent.putExtra(PHONE, phone);
        return intent;
    }

    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_phone);
        phone = getIntent().getStringExtra(PHONE);
        if (phone == null || phone.equals("")) {
            SToastUtil.toast(getString(R.string.loadDataError), Toast.LENGTH_SHORT);
            finish();
            return;
        }else if(!Tools.isMobileNO(phone)){
            SToastUtil.toast(getString(R.string.phoneFormatError), Toast.LENGTH_SHORT);
            finish();
            return;
        }

        initView();
        initListener();
        judgeTime();
    }


    private void judgeTime() {
        long saveTime = getTime();
        long t = System.currentTimeMillis() - saveTime;
        int second = (int) (t / 1000);
        if (second < 60 && second > 0) {
            this.seconds = 60 - second;
            getTimer().schedule(new CountDownTask(), 0, 1000);
        }
    }

    /**
     * 获取计时器
     *
     * @return
     */
    private Timer getTimer() {
        timer = new Timer();
        return timer;
    }

    private void initView() {
        ed_number = (EditText) findViewById(R.id.ed_number);
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        bt_get = (Button) findViewById(R.id.bt_get);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        tv_title = (TextView) findViewById(R.id.tv_title_text);
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_title.setText(getString(R.string.bindPhone));
        String head = phone.substring(0, 3);
        String end = phone.substring(7, phone.length());
        String s = head + "****" + end;
        tv_detail.setText(getString(R.string.currentPhone) + s + getString(R.string.bindPhoneLogin));


    }

    private void initListener() {
        bt_get.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.bt_get) {
            showLoading(getString(R.string.identifyingLoading));
            //获取短信验证码
            H.doPost(Config.getPhoneCode(phone, Config.TYPE_VERIFY, Dysso.getToken()), getPhoneCodeCallBack);
        } else if (id == R.id.bt_submit) {
            clickSubmit();
        } else if (id == R.id.img_back) {
            finish();
        }
    }

    private void clickSubmit() {
        String code = ed_number.getText().toString();
        if (code == null||code.equals("")) {
            SToastUtil.toast(getString(R.string.inputCode), Toast.LENGTH_SHORT);
            return;
        }else if(code.length()<6){
            SToastUtil.toast(getString(R.string.input6LengthCode), Toast.LENGTH_SHORT);
            return;
        }
        showLoading(getString(R.string.loadingBindPhone));
        submit(phone, code);
    }

    private void submit(String phone, String code) {
        String url = Config.bindPhoneAddr(phone, code, "", "", true);
        H.doGet(url, new HSubmitHCall());
    }

    class HSubmitHCall extends HCallback.HCacheCallback {

        @Override
        public void onError(CBase cBase, String s, Throwable throwable) throws Exception {
            SToastUtil.toast(getString(R.string.netWordError), Toast.LENGTH_SHORT);
            hideLoading();
        }

        @Override
        public void onSuccess(CBase cBase, HResp hResp, String s) throws Exception {
            handleLoginJson(s);
        }
    }


    private void handleLoginJson(String data) {
        try {
            JSONObject json = new JSONObject(data);
            int code = json.getInt("code");
            if (code == 0) {
                saveTime(0);
                Intent intent = new Intent();
                intent.putExtra("loginInfo", data);
                setResult(RESULT_OK, intent);
                hideLoading();
                finish();
            } else {
                CToastUtil.toastShort(this, json.getString("msg"));
            }
        } catch (Exception e) {
            L.debug("parse json exception---" + e.getMessage());
            CToastUtil.toastShort(VerificationPhoneActivity.this,getString(R.string.bindError));
        } finally {
            hideLoading();
        }
    }

    /**
     * 保存发送验证的时间
     *
     * @param time
     */
    private void saveTime(long time) {
        SharedPreferences shared = getSharedPreferences(PHONE_CODE, Context.MODE_PRIVATE);
        shared.edit().putLong(PHONE_CODE, time).commit();
    }

    /**
     * 获取发送验证码的时间
     *
     * @return
     */
    private long getTime() {
        SharedPreferences shared = getSharedPreferences(PHONE_CODE, Context.MODE_PRIVATE);
        return shared.getLong(PHONE_CODE, 0);
    }

    /**
     * 记录还有多少秒可以发送验证码
     */
    private int seconds;

    private Timer timer;
    protected HCallback.HCacheCallback getPhoneCodeCallBack = new HCallback.HCacheCallback() {
        //        0：success, 1 服务器错误，2 手机号码错误，3 手机已注册或被绑定， 4 参数错误
        public void onSuccess(CBase c, HResp res, String data) throws Exception {
            try {
                JSONObject json = new JSONObject(data);
                int code = json.getInt("code");
                if (code == 0) {
                    CToastUtil.toastLong(VerificationPhoneActivity.this, getString(R.string.get_code_success_hint));
                    saveTime(System.currentTimeMillis());
                    seconds = 60;
                    getTimer().schedule(new CountDownTask(), 0, 1000);
                } else if (3 == code) {
                    CToastUtil.toastLong(VerificationPhoneActivity.this, getString(R.string.phoneIsRegister));
                } else {
                    CToastUtil.toastShort(VerificationPhoneActivity.this, getString(R.string.server_error_hint));
                }
            } catch (Exception e) {
                e.printStackTrace();
                CToastUtil.toastShort(VerificationPhoneActivity.this, getString(R.string.server_error_hint));
            } finally {
                hideLoading();
            }
        }

        @Override
        public void onError(CBase cBase, String s, Throwable throwable) throws Exception {
            hideLoading();
            CToastUtil.toastShort(VerificationPhoneActivity.this, getString(R.string.server_error_hint));
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    class CountDownTask extends TimerTask {

        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (seconds == 0) {
                bt_get.setEnabled(true);
                bt_get.setText(getString(R.string.getIdentifying));
                timer.cancel();
            } else {
                bt_get.setEnabled(false);
                bt_get.setText(getString(R.string.getIdentifying) + "(" + seconds + ")");
                seconds--;
            }
        }
    };

    LoadingDialog loadingDialog;

    private void showLoading(String text) {

        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this, getString(R.string.identifyingLoading));
        }
        loadingDialog.setMessage(text);
        loadingDialog.show();
    }

    private void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
