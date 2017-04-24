package com.dy.aikexue.ssolibrary.util;


import com.dy.aikexue.ssolibrary.config.Config;

/**
 * Created by Administrator on 2015/10/16.
 */
public class ThirdPartyConstants {

    public static final String TYPE_FOR_WECHAT= "WECHAT";

    public static final String TYPE_FOR_WEIBO= "SINA";

    public static final String TYPE_FOR_QQ = "QQ";

    public static final String KEY_FOR_THIRD_PARTY = "R_ID_THIRD";

    public static final int REQUEST_CODE_FOR_WEIBO = 2;
    /** 当前 DEMO 应用的 KUXIAO_APP_KEY_FOR_WEIBO，第三方应用应该使用自己的 KUXIAO_APP_KEY_FOR_WEIBO 替换该 KUXIAO_APP_KEY_FOR_WEIBO */
//    public static final String KUXIAO_APP_KEY_FOR_WEIBO = "2538920155";

//    private static final String KUXIAO_APP_KEY_FOR_QQ = "1104589894";
    private static final String KUXIAO_APP_KEY_FOR_QQ_TEST = "1104588504";//测试用

//    private static final String KUXIAO_APP_KEY_FOR_WECHAT = "wx270243445e3233ee";
    private static final String KUXIAO_APP_KEY_FOR_WECHAT_TEST = "wx0d164d99f04cfa9f";//测试用
    private static final String KUXIAO_APP_SECRET_FOR_WECHAT_TEST = "95edb1ea531b59d0157e609aec07697c";//测试用
   public static boolean isDebug = false;

    public static String getThirdPartyNameByType(String type){
        if(TYPE_FOR_WECHAT.equals(type)){
            return "微信";
        }else if(TYPE_FOR_QQ.equals(type)){
            return "QQ";
        }else if(TYPE_FOR_WEIBO.equals(type)){
            return "微博";
        }else{
            return "";
        }
    }

    public static String getKuxiaoAppKeyForWeibo(){
        return Config.getKuxiaoAppIdForWeiBo();
    }

    public static String getKuxiaoAppKeyForQq(){
        if(!isDebug){
            return Config.getKuxiaoAppIdForQQ();
        }else{
            return KUXIAO_APP_KEY_FOR_QQ_TEST;
        }
    }

    public static String getKuxiaoAppKeyForWechat(){
        if(!isDebug){
            return Config.getKuxiaoAppIdForWechat();
        }else{
            return KUXIAO_APP_KEY_FOR_WECHAT_TEST;
        }
    }

    public static String getKuxiaoAppSecretForWechat(){
        if(!isDebug){
            return Config.getKuxiaoAppSecretForWechat();
        }else{
            return KUXIAO_APP_SECRET_FOR_WECHAT_TEST;
        }
    }

    public static void setDebug(boolean isDebug){
        ThirdPartyConstants.isDebug = isDebug;
    }

    private static boolean isWechatLoginRequest = false;
    private static ThirdPartyLoginData tpld;
    public static boolean isWechatLoginRequest() {
        return isWechatLoginRequest;
    }
    public static void setIsWechatLoginRequest(boolean isWechatLoginRequest) {
        ThirdPartyConstants.isWechatLoginRequest = isWechatLoginRequest;
    }
    public static ThirdPartyLoginData getTpld() {
        return tpld;
    }
    public static void setTpld(ThirdPartyLoginData tpld) {
        ThirdPartyConstants.tpld = tpld;
    }

    public class ThirdPartyLoginData{
        private String access_token;
        private String expires_in;
        private String refresh_token;
        private String openid;
        private String scope;
        public String getAccess_token() {
            return access_token;
        }
        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }
        public String getExpires_in() {
            return expires_in;
        }
        public void setExpires_in(String expires_in) {
            this.expires_in = expires_in;
        }
        public String getRefresh_token() {
            return refresh_token;
        }
        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }
        public String getOpenid() {
            return openid;
        }
        public void setOpenid(String openid) {
            this.openid = openid;
        }
        public String getScope() {
            return scope;
        }
        public void setScope(String scope) {
            this.scope = scope;
        }
    }











    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     *
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL = "http://www.kuiao.cn";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     *
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     *
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     *
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE_FOR_WEIBO =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
}
