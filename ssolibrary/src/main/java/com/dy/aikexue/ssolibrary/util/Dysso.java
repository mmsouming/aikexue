package com.dy.aikexue.ssolibrary.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

import com.dy.aikexue.ssolibrary.bean.Attrs;
import com.dy.aikexue.ssolibrary.bean.NewUserData;
import com.dy.aikexue.ssolibrary.bean.SSOHTTP;
import com.dy.aikexue.ssolibrary.config.Config;
import com.dy.aikexue.ssolibrary.db.DataHelper;
import com.dy.aikexue.ssolibrary.loginregister.activit.BindingExistPhoneActivity;
import com.dy.aikexue.ssolibrary.loginregister.activit.ResetPasswordActivity;
import com.dy.aikexue.ssolibrary.loginregister.activit.UpdateBindPhoneActivity;
import com.dy.aikexue.ssolibrary.view.LogoutListener;
import com.dy.aikexue.ssolibrary.view.SSOListener;
import com.dy.aikexue.ssolibrary.view.TokenValidListener;
import com.dy.sdk.utils.JsonUtil;
import com.dy.aikexue.ssolibrary.db.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.cny.awf.net.http.CBase;
import org.cny.awf.net.http.H;
import org.cny.awf.net.http.HCallback.HCacheCallback;
import org.cny.awf.net.http.HResp;
import org.json.JSONObject;

import java.util.List;

/**
 * @author zengdl
 * @create 2015-01-29
 */
public class Dysso {

    private static Context context;

    private static SSOListener ssoListener;
    private TokenValidListener tokenListener;
    private LogoutListener logoutListener;
    private static Dysso sInstance;

    private static String token = "";

    private static String userId = "";
    private static UserInfo userInfo;

    public static Boolean isShowing = false;
    public static boolean isHasUserData = true;

    private static DataHelper dataHelper;

    private static final String TAG = "Dysso";

    public boolean isFromRcp = false;
    public String resetPwdActPKName;//启动忘记密码的activity在rcp中对应的packagename
    public String updateBindActPkName;//绑定手机对应activity
    public String bindingExistPhoneName;//验证手机对应activity

    private Dysso(Context paramContext) {
        context = paramContext.getApplicationContext();
        //初始化DataHelper
        dataHelper = DataHelper.getInstance(context);
    }

    public static synchronized Dysso createInstance(Context paramContext) {
        if (sInstance == null) {
            sInstance = new Dysso(paramContext);
        }

        if (!checkManifestConfig(paramContext)) {
            return null;
        }
        return sInstance;
    }

    public static void setSsoListenerNull() {
        ssoListener = null;
    }

    public static Context getContext() {
        return context;
    }

    public static void closeDb() {
        if (null != dataHelper) {
            dataHelper.Close();
            dataHelper = null;
        }
    }

    public static void handleListener(SSOHTTP data) {
        token = data.getToken();
        userId = data.getUserInfo().getUserId();
        //不能通过该方法更新用户信息,因为可能有部分信息需重新获取-> dataHelper.GetUsrInfo()
//        userInfo = data.getUserInfo();
        isShowing = false;
        isHasUserData=true;
        if (ssoListener != null)
            ssoListener.onComplete(data);
    }

    public static void handleCancelListener(){
        if(ssoListener != null){
            ssoListener.onCancel();
        }
    }

    /**
     * 获取用户信息
     *
     * @return UserInfo  可能为null
     */
    public static UserInfo getUserInfo() {
        try {
            if (userInfo == null && isHasUserData) {
                if (dataHelper == null) {
                    dataHelper = DataHelper.getInstance(context);
                }
                userInfo = dataHelper.GetUsrInfo();
                if (userInfo == null) {
                    isHasUserData = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public static boolean getIsAKEOrgTeacher(){
        UserInfo info = getUserInfo();
        if(info==null)return false;
        String attrs = info.getAttrs();
        List<Attrs.Org> orgs= JsonUtil.getInstance().fromJson(attrs,"data/usr/attrs/role/org",new TypeToken<List<Attrs.Org>>(){}.getType());

        if(orgs==null)return false;

        for (int i = 0; i < orgs.size(); i++) {
            Attrs.Org o = orgs.get(i);
            if(o.AKX_TEACHER==100){
                return true;
            }
        }
        return false;

    }

    public static void resetUserInfo(){
        userInfo = null;
        isHasUserData = true;
    }

    public static String getToken() {
        try {
            if (token.length() > 0) {
                return token;
            } else {
                if (getUserInfo() == null) {
                    return "";
                }
                token = getUserInfo().getToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    /**
     * 新接口的获取用户id
     *
     * @return 返回String类型的userID  返回为null代表不存在
     */
    public static String getUid() {
        try {
            if (userId != null & userId.length() > 0) {
                return userId;
            }
            if (getUserInfo() == null) {
                userId = "";
            } else {
                userId = getUserInfo().getUserId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }

    /**
     * 获取用户名
     *
     * @return 当前用户名
     */
    public static String getUserName() {
        userInfo = getUserInfo();
        return userInfo == null ? "" : userInfo.getUserName();
    }


    //  rcp之中使用这个方法的需要迁移  获取登录返回的user数据实体对象
    public static NewUserData getUsrDataObj() {
        userInfo = getUserInfo();
        if (userInfo != null) {
            Gson gson = new Gson();
            NewUserData newUserData = gson.fromJson(userInfo.getAttrs(), NewUserData.class);
            return newUserData;
        }
        return null;
    }

    public static Attrs.Basic getBasicUserInfo() {
        Attrs.Basic basic = null;
        userInfo = getUserInfo();
        if (userInfo != null) {
            basic = userInfo.getBasicUserInfo();
            if (basic == null) {
                basic = getUsrDataObj().getData().getUsr().getAttrs().getBasic();
                userInfo.setBasicUserInfo(basic);
            }
        }
        return basic;
    }

    /**
     * 更新认证老师状态
     *
     * @param pass
     */
    public static void setStatusOfPass(int pass) {
        try {
            if (dataHelper == null) {
                dataHelper = DataHelper.getInstance(context);
            }
            dataHelper.updateIntColumn(UserInfo.PASS, pass, Dysso.getUid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean isSessionValid() {
        try {
            if (token.length() > 0) {
                return true;
            }
            userInfo = getUserInfo();
            if (userInfo == null) {
                return false;
            }
            if (userInfo.getToken().length() > 0) {
                return true;
            } else {
                dataHelper.deleteToken();
                token = "";
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void checkToken(TokenValidListener lister) {
        tokenListener = lister;
        H.CTX = context;
        H.doGet(Config.AuthAddr() + "?&token=" + getToken(), authCb);
    }

    private HCacheCallback authCb = new HCacheCallback() {

        @Override
        public void onSuccess(CBase c, HResp res, String data) throws Exception {
            JSONObject json = new JSONObject(data);
            if (json.getInt("code") == 0) {
                if (tokenListener != null)
                    tokenListener.isOk();
            } else {
                if (dataHelper == null) {
                    dataHelper = DataHelper.getInstance(context);
                }
                dataHelper.deleteToken();
                if (tokenListener != null)
                    tokenListener.notOk();
            }
        }

        @Override
        public void onError(CBase c, String cache, Throwable err)
                throws Exception {
            if (tokenListener != null)
                tokenListener.notOk();
        }
    };

    public void login(Context context, SSOListener lister) {
        if (context == null) {
            Log.d("activity", "null activity");
            throw new RuntimeException("activity should not be null");
        }
        Log.i(TAG, "Activity Name：" + context.getClass().getName());
        if (isShowing) {
            return;
        }
        isShowing = true;
        ssoListener = lister;
        Intent intent = new Intent();
        intent.setClassName(context, "com.dy.aikexue.ssolibrary.loginregister.activit.LoginActivity");
        context.startActivity(intent);
    }

    public void deleteToken() {
        try {
            if (dataHelper == null) {
                dataHelper = DataHelper.getInstance(context);
            }
            dataHelper.deleteToken();
            token = "";
            userId = null;
            userInfo = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout(LogoutListener listener) {
        logoutListener = listener;
        Log.i(TAG, Config.LogoutAddr() + "?&token=" + getToken());
        H.doGet(Config.LogoutAddr() + "?&token=" + getToken(), logoutCb);
        deleteToken();
        // TODO: 2016-03-22 退出登录数据处理
        userInfo = null;
        isHasUserData=true;//重置数据库标识是否有数据的值
        //清楚圈子缓存
        SharedPreferences sharedPreferences = context.getSharedPreferences("temp1", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
        SharedPreferences shard = context.getSharedPreferences("tempmessage", Context.MODE_PRIVATE);
        shard.edit().clear().commit();
        SharedPreferences spf = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        spf.edit().clear().commit();
    }

    private HCacheCallback logoutCb = new HCacheCallback() {

        @Override
        public void onSuccess(CBase c, HResp res, String data) throws Exception {
            if (logoutListener != null)
                logoutListener.onLogout(data);
        }

        @Override
        public void onError(CBase c, String cache, Throwable err)
                throws Exception {
            if (logoutListener != null)
                logoutListener.onLogout(cache);
        }
    };


    private static boolean checkManifestConfig(Context paramContext) {
        Object localObject;
        try {
            ComponentName localComponentName1 = new ComponentName(
                    paramContext.getPackageName(),
                    "com.dy.aikexue.ssolibrary.loginregister.activit.LoginActivity");

            localObject = paramContext.getPackageManager();

            ((PackageManager) localObject).getActivityInfo(localComponentName1,
                    0);
        } catch (PackageManager.NameNotFoundException localNameNotFoundException1) {
            localObject = "没有在AndroidManifest.xml中检测到com.dy.aikexue.ssolibrary.loginregister.activit.LoginActivity,请加上com.dy.aikexue.ssolibrary.loginregister.activit.LoginActivity";
            Log.i(TAG,
                    "AndroidManifest.xml 没有检测到com.dy.aikexue.ssolibrary.loginregister.activit.LoginActivity"
                            + (String) localObject);
            return false;
        }
        return true;
    }

    public void startResetPwdActivity(Context context, boolean isFromLogin) {
        if (context == null) {
            throw new RuntimeException("activity should not be null");
        }
        try {
            if (!isFromRcp) {
                Intent intent = new Intent(context, ResetPasswordActivity.class);
                context.startActivity(intent);
            } else {
                Intent intent = new Intent();
                if (!isFromLogin) {
                    intent.putExtra("update", true);
                    userInfo = getUserInfo();
                    if (userInfo != null) intent.putExtra("phone", userInfo.getPhone());
                }
                intent.setClassName(context, resetPwdActPKName);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(context, ResetPasswordActivity.class);
            context.startActivity(intent);
        }
    }

    public void startUpdateBindActivity(Activity context,String type) {
        if (context == null) {
            throw new RuntimeException("activity should not be null");
        }

        Intent intent = UpdateBindPhoneActivity.getStartIntent(context,type);
        context.startActivity(intent);
    }


    public void startBindingExistPhoneActivity(Activity activity, String phone, int requestCode) {
        try {
            if (!isFromRcp) {
                Intent intent = new Intent(activity, BindingExistPhoneActivity.class);
                intent.putExtra("existPhone", phone);
                activity.startActivityForResult(intent, requestCode);
            } else {

                Intent intent = new Intent();
                intent.putExtra("existPhone", phone);
                intent.setClassName(activity, bindingExistPhoneName);
                activity.startActivityForResult(intent, requestCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
