package com.dy.aikexue.ssolibrary.loginregister.activit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.dy.aikexue.ssolibrary.util.ThirdPartyConstants;
import com.dy.aikexue.ssolibrary.util.Tools;
import com.dy.sdk.listener.OnDialogButtonClickListener;
import com.dy.sdk.utils.CToastUtil;
import com.dy.sdk.utils.ThemeUtil;
import com.dy.sdk.view.dialog.CommonDialog;
import com.dy.sdk.view.dialog.LoadingDialog;
import com.google.gson.Gson;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.cny.awf.net.http.CBase;
import org.cny.awf.net.http.H;
import org.cny.awf.net.http.HCallback;
import org.cny.awf.net.http.HResp;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {


    private EditText edtUsr;
    private EditText edtPwd;
    private Button btnLogin;
    private RelativeLayout rvRegist;
    private RelativeLayout rvForgetPwd;
    private TextView tvTitle;
    private View loginByWeiboBtn, loginByQQBtn, loginByWeixinBtn;

    public static Context context;
    private LoadingDialog loadingDialog;
    private Logger logger = LoggerFactory.getLogger(LoginActivity.class);
    private String user, pwd;

    /**
     * 保存用户信息至数据库
     */
    private DataHelper dataHelper;

    private boolean isExistWeibo = false;
    private boolean isExistQQ = false;
    private boolean isExistWechat = false;

    /**
     * 微博 Web 授权类，提供登陆等功能
     */
    private WeiboAuth mWeiboAuth;

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;

    public Tencent mTencent;

    private IWXAPI mWeChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_old);
        context = this;
        initView();
    }

    private void initView() {
        //顶部tab初始化
        LinearLayout lvBack = (LinearLayout) findViewById(R.id.backLogin);
        lvBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title_text);
        // 处理判断  是否为酷校登录
        initTitleBar();

        edtUsr = (EditText) findViewById(R.id.usr);
        edtPwd = (EditText) findViewById(R.id.pwd);
        btnLogin = (Button) findViewById(R.id.login);
        rvRegist = (RelativeLayout) findViewById(R.id.register);
        rvForgetPwd = (RelativeLayout) findViewById(R.id.rv_forget_pwd);

        //第三方登录相关view
        loginByWeiboBtn = findViewById(R.id.login_by_weibo);
        loginByQQBtn = findViewById(R.id.login_by_qq);
        loginByWeixinBtn = findViewById(R.id.login_by_weixin_layout);
        loginByWeiboBtn.setOnClickListener(this);
        loginByQQBtn.setOnClickListener(this);
        loginByWeixinBtn.setOnClickListener(this);

        initThirdPartyData();

        TextView tv_regis = (TextView) findViewById(R.id.tv_regis);
        TextView not_paw = (TextView) findViewById(R.id.not_paw);

        btnLogin.setBackgroundDrawable(ThemeUtil.getButtonSolidSelectDrawable(this));
        if (SpUtil.isLoginByTeacher(this)) {
            tv_regis.setTextColor(getResources().getColor(R.color.color_blue_press));
            not_paw.setTextColor(getResources().getColor(R.color.color_blue_press));
        }

        btnLogin.setOnClickListener(this);
        rvRegist.setOnClickListener(this);
        rvForgetPwd.setOnClickListener(this);
        edtPwd.addTextChangedListener(this);

        if (dataHelper == null) {
            dataHelper = DataHelper.getInstance(context);
        }

        loadingDialog = new LoadingDialog(context, "登录中...");

    }

    private void initTitleBar() {
        TextView tv_title = (TextView) findViewById(R.id.tv_title_text);
        if (Config.isSrrelAikeXue()) {
            tv_title.setText(getString(R.string.tv_title_login_akx));
        } else {
            tv_title.setText(getString(R.string.tv_title_login_kx));
        }
        findViewById(R.id.backLogin).setOnClickListener(this);
    }


    private void initThirdPartyData() {
        isExistWeibo = Tools.existPackage("com.sina.weibo", getApplicationContext());
        isExistQQ = Tools.existPackage("com.tencent.mobileqq", getApplicationContext());
        isExistWechat = Tools.existPackage("com.tencent.mm", getApplicationContext());
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (R.id.login == id) {
            doLogin();
        } else if (R.id.register == id) {
            startActivity(new Intent(context, RegisterActivity.class));
        } else if (R.id.rv_forget_pwd == id) {
            //忘记密码
            ToResetPwdActivity();
        } else if (R.id.backLogin == id) {
            Dysso.handleCancelListener();
            finish();
        } else if (id == R.id.login_by_weibo) {
            if (isExistWeibo) {
                initWeiboData();
                try {
                    mSsoHandler = new SsoHandler(LoginActivity.this, mWeiboAuth);
                    mSsoHandler.authorize(ThirdPartyConstants.REQUEST_CODE_FOR_WEIBO, new AuthListener(), (String) null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
//                mWeiboAuth.anthorize(new AuthListener());
                Toast.makeText(LoginActivity.this, "请先安装微博客户端", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.login_by_qq) {
//            try {
//                if (isExistQQ) {
//                    initQQData();
//                    mTencent.login(this, "all", loginListener);
//                } else {
//                    Toast.makeText(LoginActivity.this, "请先安装QQ客户端", Toast.LENGTH_SHORT).show();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        } else if (id == R.id.login_by_weixin_layout) {
            try {
                if (isExistWechat) {
                    initWeixinData();
                    final SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_kuxiao";
                    boolean success = mWeChat.sendReq(req);
                    ThirdPartyConstants.setIsWechatLoginRequest(success);
                    if (success) {
                        loadingDialog.show();
                        ThirdPartyConstants.setTpld(null);
                    } else {
                        Toast.makeText(LoginActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "请先安装微信客户端", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                loadingDialog.dismiss();
            }
        }
    }

    protected void ToResetPwdActivity() {
        Dysso.createInstance(context).startResetPwdActivity(context, true);
    }

    /**
     * 登录相关操作
     */
    private void doLogin() {
        user = edtUsr.getText().toString().trim();
        pwd = edtPwd.getText().toString().trim();
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pwd)) {
            CToastUtil.toastShort(context, "用户名和密码不能为空");
            return;
        }
        loadingDialog.show();
        String url = Config.NewLoginAddr(user, pwd);
        logger.info(url);
        H.doPost(url, hCacheCallback);

    }

    private HCallback.HCacheCallback hCacheCallback = new HCallback.HCacheCallback() {
        @Override
        public void onError(CBase cBase, String s, Throwable throwable) throws Exception {
            logger.error("login onError--" + s);
            loadingDialog.dismiss();
            CToastUtil.toastShort(context, getString(R.string.net_error_hint));
        }

        @Override
        public void onSuccess(CBase cBase, HResp hResp, String s) throws Exception {
            logger.info("login onSuccess----" + s);
            loadingDialog.dismiss();

            handleLoginJson(s);
        }
    };

    /**
     * 处理接口返回的数据
     * @param data
     */
    private void handleLoginJson(String data) {
        try {
            Gson gson = new Gson();
            NewUserData newUserData = gson.fromJson(data, NewUserData.class);
            logger.info(newUserData.toString());
            int code = newUserData.getCode();
            if (code == 0 && !TextUtils.isEmpty(newUserData.getData().getToken())) {
                try {
                    if (dataHelper == null) {
                        dataHelper = DataHelper.getInstance(context);
                    }
                    // 保存用户信息至数据库
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserId(newUserData.getData().getUsr().getId());
                    userInfo.setToken(newUserData.getData().getToken());
                    userInfo.setAttrs(data);
                    //绑定手机以及认证状态
                    Attrs attrs = newUserData.getData().getUsr().getAttrs();
                    if (attrs != null) {
                        Attrs.Basic basic = attrs.getBasic();
                        Certification certification = attrs.getCertification();
                        Extra extra = attrs.getExtra();
                        userInfo.setAttrsInfo(attrs);
                        userInfo.setBasicUserInfo(basic);
                        userInfo.setExtraInfo(extra);
                        if (basic != null) {
                            userInfo.setUserName(basic.getNickName());
                            userInfo.setGender(basic.getGender());
                            userInfo.setSign(basic.getDesc());
                            userInfo.setHeadurl(basic.getAvatar());
//                                userInfo.setPhone(basic.getPhone());
                        }
                        if (extra != null) {
                            userInfo.setLocation(extra.getProvince() + "/" + extra.getCity());
                        }
                        if (certification != null) {
                            userInfo.setPass(certification.getStatus());
                            userInfo.setCertification(certification);
                        }
                        if (attrs.getPrivated() != null) {
                            userInfo.setPhone(attrs.getPrivated().getPhone());
                        }
                    } else {
                        userInfo.setPass(0);
                        userInfo.setPhone("");
                    }
                    long id = dataHelper.SaveUserInfo(userInfo);
                    logger.info("save  data success--" + id);
                    logger.info("userinfo------" + userInfo.toString());
                    //登录回调
                    SSOHTTP http = new SSOHTTP();
                    http.setToken(newUserData.getData().getToken());
                    http.setUserInfo(userInfo);
                    Intent mIntent = new Intent("SSO");
                    mIntent.putExtra("uid", newUserData.getData().getUsr().getId());
                    mIntent.putExtra("token", newUserData.getData().getToken());
                    LocalBroadcastManager.getInstance(context)
                            .sendBroadcast(mIntent);
                    Dysso.handleListener(http);//回调
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CToastUtil.toastLong(context, "登录成功");
                //关闭当前activity
                finish();
            } else if (code == 13) {
                startActivityForResult(VerificationPhoneActivity.getIntent(this, user), 100);
//                Dysso.createInstance(context).startBindingExistPhoneActivity(this, user, 100);
            } else {
                if (newUserData != null && !TextUtils.isEmpty(newUserData.getMsg())) {
                    CToastUtil.toastShort(context, newUserData.getMsg());
                } else {
                    CToastUtil.toastShort(context, getString(R.string.server_error_hint));
                }
                judgeForgotPwd(code);
            }
        } catch (Exception e) {
            logger.error("parse  json exception----" + e.getMessage());
            CToastUtil.toastShort(context, getString(R.string.server_error_hint));
        }
    }

    private void judgeForgotPwd(int code) {
        if (code == 2) {
            notPwd++;
            if (notPwd > 3) {
                final CommonDialog.TwoButtonBuilder builder = new CommonDialog.TwoButtonBuilder(context);
                builder.setLeftButtonText(getString(R.string.resetPwd));
                builder.setRightButtonText(getString(R.string.cancel));
                builder.setContentText(getString(R.string.loginErrorIsNotPwd));
                builder.setLeftClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public void onDialogButtonClick(View view) {
                        ToResetPwdActivity();
                    }
                });

                builder.setIsNeedToShow(true).build();
            }
        }
    }

    int notPwd = 0;

    /**
     * 初始化第三方登录相关接口
     */
    private void initWeiboData() {
        // 创建微博实例
        if (mWeiboAuth == null) {
            mWeiboAuth = new WeiboAuth(this, ThirdPartyConstants.getKuxiaoAppKeyForWeibo(), ThirdPartyConstants.REDIRECT_URL, ThirdPartyConstants.SCOPE_FOR_WEIBO);
        }
    }

    private void initQQData() {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(ThirdPartyConstants.getKuxiaoAppKeyForQq(), this);
//            mTencent = Tencent.createInstance("222222", this);
        }
    }

    private void initWeixinData() {
        if (mWeChat == null) {
            mWeChat = WXAPIFactory.createWXAPI(this, ThirdPartyConstants.getKuxiaoAppKeyForWechat(), false);
            mWeChat.registerApp(ThirdPartyConstants.getKuxiaoAppKeyForWechat());
        }
    }

    @Override
    public void onBackPressed() {
        Dysso.handleCancelListener();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Dysso.isShowing = false;
    }

    @Override
    public void finish() {
        Dysso.setSsoListenerNull();
        super.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ThirdPartyConstants.isWechatLoginRequest()) {
            ThirdPartyConstants.setIsWechatLoginRequest(false);
            ThirdPartyConstants.ThirdPartyLoginData tpld = ThirdPartyConstants.getTpld();
            if (tpld != null) {
                loginByThirdParty(tpld.getOpenid(), tpld.getAccess_token(), ThirdPartyConstants.TYPE_FOR_WECHAT);
            } else {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            }
        }
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logger.debug("sso : onActivityResult, requestCode : {}, resultCode : {}", requestCode, resultCode);
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
//        if(mSsoHandler != null){
//            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
//        }
        if (requestCode == ThirdPartyConstants.REQUEST_CODE_FOR_WEIBO && mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        } else if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        } else if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.RESULT_LOGIN) {
                Tencent.handleResultData(data, loginListener);
//                Log.d(TAG, "-->onActivityResult handle logindata");
            }
        } else if (requestCode == 100) {
            boolean isSuccess = false;
            String loginInfo = "";
            if (resultCode == RESULT_OK && data != null) {
                loginInfo = data.getStringExtra("loginInfo");
                if (!TextUtils.isEmpty(loginInfo)) {
                    isSuccess = true;
                }
            }

            if (!isSuccess) {
//                CToastUtil.toastShort(this, "登录失败");
            } else {
                handleLoginJson(loginInfo);
            }
        }
    }

    IUiListener loginListener = new IUiListener() {
        @Override
        public void onComplete(Object response) {
            if (null == response) {
//                Util.showResultDialog(MainActivity.this, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
//                Util.showResultDialog(MainActivity.this, "返回为空", "登录失败");
                return;
            }
//            Util.showResultDialog(MainActivity.this, response.toString(), "登录成功");
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {
            initOpenidAndToken(values);
        }

        @Override
        public void onError(UiError e) {
        }

        @Override
        public void onCancel() {
        }
    };


    public void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            logger.debug("sso : login by qq success, openId : {}, token : {}", openId, token);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                loginByThirdParty(openId, token, ThirdPartyConstants.TYPE_FOR_QQ);
//                mTencent.setAccessToken(token, expires);
//                mTencent.setOpenId(openId);
                //请求服务器登录或注册
            }
        } catch (Exception e) {
        }
    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                logger.debug("sso : login by weibo success, data : {}", mAccessToken.toString());
                //请求服务器登录或注册
                loginByThirdParty(mAccessToken.getUid(), mAccessToken.getToken(), ThirdPartyConstants.TYPE_FOR_WEIBO);
            } else {
                logger.debug("sso : login by weibo success, but key is incorrect");
                // 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
//                String code = values.getString("code");
            }
        }

        @Override
        public void onCancel() {
            logger.debug("sso : login by weibo cancel");
        }

        @Override
        public void onWeiboException(WeiboException e) {
            logger.debug("sso : login by weibo occur exception : {}", e.toString());
        }
    }

    private void loginByThirdParty(String id, String token, String type) {
//        progressDialog.show();
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
        logger.debug("sso : login by thirdparty url : {}", Config.LoginByThirdPartyAddr()
//                + "?time=" + expireTime
                + "&openid=" + id
                + "&R_ID_THIRD_TYPE=" + type
                + "&access_token=" + token
                + "&p=1");
        // p=1时token永久有效

        H.doPost(Config.LoginByThirdPartyAddr()
//                + "?time=" + expireTime
                + "&openid=" + id
                + "&R_ID_THIRD_TYPE=" + type
                + "&access_token=" + token
                + "&p=1", hCacheCallback);
//        H.doPost(Config.LoginByThirdPartyAddr()
////                + "?time=" + expireTime
//                + "?R_ID_THIRD=" + id
//                + "&R_ID_THIRD_TYPE=" + type
//                + "&access_token=" + token
//                + "&p=1", hCacheCallback);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        user = edtUsr.getText().toString().trim();
        pwd = edtPwd.getText().toString().trim();
//        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pwd)) {
//            if(SpUtil.isLoginByTeacher(this)){
//                btnLogin.setBackgroundResource(R.drawable.shape_button_blue_false);
//            }else{
//                btnLogin.setBackgroundResource(R.drawable.sso_login_transparent);
//            }
//        } else {
//            if(SpUtil.isLoginByTeacher(this)){
//                btnLogin.setBackgroundResource(R.drawable.select_button_blue);
//            }else{
//                btnLogin.setBackgroundResource(R.drawable.btnselector);
//            }
//        }
    }
}