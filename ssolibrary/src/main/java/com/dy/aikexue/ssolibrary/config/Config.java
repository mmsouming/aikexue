package com.dy.aikexue.ssolibrary.config;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.dy.aikexue.ssolibrary.loginregister.fragment.ResumeCommonFragment;
import com.dy.aikexue.ssolibrary.util.Dysso;
import com.dy.aikexue.ssolibrary.util.Tools;
import com.dy.sdk.utils.CConfigUtil;
import com.dy.sdk.utils.CToastUtil;
import com.google.gson.Gson;

import org.cny.awf.net.http.H;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @create at 2015-01-30 配置文件
 */
public class Config {


    private static Logger L = LoggerFactory.getLogger(Config.class);

    public static String SERVER_ADDR = "";
    // 获取验证码的4种用例  register resetPwd   bind   modifyBind
    public static final String TYPE_REGISTER = "register";
    public static final String TYPE_RESET_PWD = "resetPwd";
    public static final String TYPE_BIND = "bind";
    public static final String TYPE_VERIFY = "verify";
    public static final String TYPE_MODIFY_BIND = "modifyBind";
    private static String FSSRV = "";
    private static String SSO_DMS = "";
    private static String SSO_PES2 = "";
    private static String SSO_APPRAISE = "";


    public static String getPostProgressUrl(String ddid) {
        return CConfigUtil.getAddress(CConfigUtil.NAME_DMS) + "usr/api/loadDelivery?did=" + ddid + "&token=" + Dysso.getToken();
    }

    /**
     * 公司评价
     *
     * @param companyId 公司id
     * @param page
     * @param pageCount
     * @return
     */
    public static String getCompanyEvaluateListUrl(String companyId, int page, int pageCount) {
        String s = "";
        if (companyId.startsWith("u")) {
            s = "company=" + companyId;
        } else {
            s = "targetId=" + companyId;
        }
        return CConfigUtil.getAddress(CConfigUtil.NAME_DMS) + "pub/api/loadComment?" + s + "&page=" + page + "&pageCount=" + pageCount + "&source=ANDROID&empty=0&type=recruit";
    }


    /**
     * 删除简历信息
     *
     * @param rid
     * @return
     */
    public static String getResumeDelete(String rid) {
        return updateResume(rid);
//        return CConfigUtil.getAddress(CConfigUtil.NAME_DMS) + "usr/api/removeResumeElement?rid=" + rid + "&token=" + Dysso.getToken();
    }

    /**
     * 修改简历数组类型指定元素内容
     *
     * @return
     */
    public static String getResumeUpdate(String resumeId) {
        return updateResume(resumeId);
//        return CConfigUtil.getAddress(CConfigUtil.NAME_DMS) + "usr/api/updateResumeElement?rid=" + resumeId + "&token=" + Dysso.getToken();
    }

    /**
     * 添加简历数组类型字段的内容
     *
     * @return
     */
    public static String getResumeAdd(String rid) {
        return updateResume(rid);
//        return CConfigUtil.getAddress(CConfigUtil.NAME_DMS) + "usr/api/addResumeElement?rid=" + rid + "&token=" + Dysso.getToken();
    }

    public static String updateResume(String rid) {
        return CConfigUtil.getAddress(CConfigUtil.NAME_DMS) + "usr/api/updateResumeElement?rid=" + rid + "&token=" + Dysso.getToken();
    }

    public static String getLoadResume(String filter, String paging) {
        return CConfigUtil.getAddress(CConfigUtil.NAME_DMS) + "usr/api/loadResume?paging=" + paging + "&filter=" + filter + "&token=" + Dysso.getToken();
    }

    /**
     * 获取公司详情页
     *
     * @return
     */
    public static String getCompanyDetailUrl(String companyId) {
        return CConfigUtil.getAddress(CConfigUtil.NAME_DMS) + "pub/api/loadCompany?companyId=" + companyId;
    }

    public static String getCompanyResumeListUrl(String companyId, int page, int pageCount) {
        return getCompanyDetailUrl(companyId) + "&recruitPage=" + page + "&ret_rct=1&ret_usr=0&pageCount=" + pageCount;
    }

    /**
     * 获取邀请函列表
     *
     * @return
     */
    public static String getInvitationUrl(String status, int page, int pageCount) {
        return CConfigUtil.getAddress(CConfigUtil.NAME_DMS) + "usr/api/listInvitation?page=" + page + "&pageCount=" + pageCount + "&token=" + Dysso.getToken() + "&status=" + status + "&ret_rct=1&rsmOwnerId=" + Dysso.getUid();
    }

    /**
     * 获取投递箱列表
     *
     * @param status
     * @return
     */
    public static String getDropBoxUrl(String status, int page, int pageCount) {
        return CConfigUtil.getAddress(CConfigUtil.NAME_DMS) + "usr/api/listDelivery?page=" + page + "&pageCount=" + pageCount + "&token=" + Dysso.getToken() + "&status=" + status + "&ret_rct=1&rsmOwnerId=" + Dysso.getUid();
    }

    public static String getValidationCodeUrl() {
        return Config.getSrvAddr() + "pub/api/requestMark";
    }

    public static String getResetPwd(String token, String oldPwd, String newPwd) {
        return CConfigUtil.getAddress(CConfigUtil.NAME_SSO) + "usr/api/changePassword?token=" + token + "&oldPwd=" + oldPwd + "&newPwd=" + newPwd;
    }


    public static String getSaveNumber(String no, String token) {
        return CConfigUtil.getAddress(CConfigUtil.NAME_SSO) + "usr/api/updatePrivate?no=" + no + "&token=" + token;
    }

    /**
     * 获取机构列表
     *
     * @param page
     * @param pageCount
     * @return
     */
    public static String getOrgList(int page, int pageCount, List<String> ids) {
        String str = "";
        if (ids == null) {
            str = "&query_s={\"attrs.role.ORG_ADMIN\":100}";
        } else {
            str = "&query_s={\"attrs.role.ORG_ADMIN\":100,\"_id\":{\"$in\":" + new Gson().toJson(ids) + "}}";
        }


        return CConfigUtil.getAddress(CConfigUtil.NAME_SSO) + "usr/api/search?&ret_uinfo=1&pn=" + page + "&ps=" + pageCount + "&token=" + Dysso.getToken() + str;
//        return CConfigUtil.getAddress(CConfigUtil.NAME_SSO)+"usr/api/listOrgs?ret_valid=1&token="+Dysso.getToken()+"&page="+page+"&pageCount="+pageCount+"&sort=-1";
    }

    /**
     * 申请加入认证机构
     *
     * @return
     */
    public static String getApplyIntoOrg() {

        return CConfigUtil.getAddress(CConfigUtil.NAME_SSO) + "usr/api/update?token=" + Dysso.getToken();
    }

    /**
     * 获得用户认证机构列表
     *
     * @param page
     * @param pageCount
     * @return
     */
    public static String getUsrOrg(int page, int pageCount) {
        return CConfigUtil.getAddress(CConfigUtil.NAME_SSO) + "usr/api/listUserOrg?selector=org,basic&token=" + Dysso.getToken() + "&pn=" + page + "&ps=" + pageCount;
    }

    /**
     * 上传文件
     *
     * @return
     */
    public static String getUpLoadFile(String token) {
        return getFSSrvAddr() + "usr/api/uload?pub=1&token=" + token;
    }

    //新的登录接口
    public static String NewLoginAddr(String user, String pwd) {
        return getSrvAddr() + "sso/api/login?usr=" + user + "&pwd=" + pwd + "&selector=basic,role,certification,extra,privated&source=ANDROID";
    }

    //新的注册接口  login  0-注册完不登录  1-注册完直接登录
    public static String newRegisterAddr(String code, int login) {
        return getSrvAddr() + "sso/api/create?attrs=basic,certification,extra,privated&login=" + login + "&pcode=" + code;
    }

    //新版获取用户个人信息接口
    public static String getUserInfo(String token) {
        return getSrvAddr() + "sso/api/uinfo?selector=role,bandc,privated,extra&token=" + token;
    }

    //获取教育背景，教育经历信息
    public static String getUserInfo(String select, String token) {
        return getSrvAddr() + "sso/api/uinfo?selector=" + select + "&token=" + token;
    }
    //第三方登录接口
//    public static String LoginByThirdPartyAddr() {
////		return getSrvAddr() + "sso/api/findRegister";
//        return getSrvAddr() + "/tlogin/api/login";
//    }

    public static String LoginByThirdPartyAddr() {
//		return getSrvAddr() + "sso/api/findRegister";
        return getSrvAddr() + "pub/api/thirdLogin" + "?host=" + CConfigUtil.getRcpHost();
    }

    public static String unBindingThirdPartyAccount() {
//		return getSrvAddr() + "sso/api/findRegister";
        return getSrvAddr() + "usr/api/thirdUnbind" + "?host=" + CConfigUtil.getRcpHost();
    }

    //重置密码接口
    public static String getResetPwdAddr(String pwd, String pcode, String phone) {
        return getSrvAddr() + "tlogin/api/resetPwd?pwd=" + pwd + "&pcode=" + pcode + "&phone=" + phone;
//		return getSrvAddr()+"ucenter/api/resetPassword?pwd="+pwd+"&pcode="+pcode+"&phone="+phone;
    }

    //绑定手机密码
    public static String bindPhoneAddr(String phone, String code, String token, String oldPhone) {
        return bindPhoneAddr(phone, code, token, oldPhone, false);
    }

    public static String bindPhoneAddr(String phone, String code, String token, String oldPhone, boolean isVerify) {
        if (isVerify) {
            return getSrvAddr() + "tlogin/api/bindPhone?phone=" + phone + "&pcode=" + code + "&t=Verify&do_login=1";
        } else if (oldPhone == null) {
            return getSrvAddr() + "tlogin/api/bindPhone?phone=" + phone + "&token=" + token + "&pcode=" + code + "&t=Bind" + "&token=" + Dysso.getToken();
        } else {
            return getSrvAddr() + "tlogin/api/bindPhone?phone=" + phone + "&pcode=" + code + "&token=" + token + "&phoneOld=" + oldPhone + "&t=Change" + "&token=" + Dysso.getToken();
        }
    }


    private static String SIGNATURE_CODE = "338d98dccd1a2685b5715c69f064de6c";

    /**
     * 获取验证码接口
     *
     * @param phoneNum 手机号
     * @param types    获取验证码使用场景类型
     * @return
     */
    public static String getPhoneCode(String phoneNum, String types) {
        String suffix = "key=" + SIGNATURE_CODE + "&mobile=" + phoneNum + "&types=" + types;
        suffix = addParametersMd5(suffix);
        return getSrvAddr() + "tlogin/api/sendMessage?" + suffix;

    }

    /**
     * 获取验证码接口
     *
     * @param phoneNum 手机号
     * @param types    获取验证码使用场景类型
     * @return
     */
    public static String getPhoneCode(String phoneNum, String types, String markId, String photoCode) {
        String suffix = "key=" + SIGNATURE_CODE + "&mobile=" + phoneNum + "&types=" + types;
        suffix = addParametersMd5(suffix) + "&mark=" + markId + "&vcode=" + ((photoCode == null) ? "" : photoCode);
        return getSrvAddr() + "tlogin/api/sendMessage?" + suffix;
    }

    private static String addParametersMd5(String text) {
        String md5Str = Tools.encodeStrByMd5(text);
        String newStr = text + "&sign=" + md5Str;
        return newStr;
    }

    /**
     * 获取验证码接口
     *
     * @param phoneNum 手机号
     * @param types    获取验证码使用场景类型
     * @return
     */
    public static String getPhoneCode(String phoneNum, String types, String token) {
//		return getSrvAddr()+"ucenter/api/sendMessage?mobile="+phoneNum;
        return getSrvAddr() + "tlogin/api/sendMessage?mobile=" + phoneNum + "&types=" + types + "&token=" + token;
    }

    public static String AuthAddr() {
        return getSrvAddr() + "sso/api/auth";
    }

    public static String LogoutAddr() {
        return getSrvAddr() + "sso/api/logout";
    }

    //	appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
    public static String getWechatAccessTokenApi() {
        return "https://api.weixin.qq.com/sns/oauth2/access_token";
    }

    /**
     * 更新用户信息接口
     */
    public static String updateUserInfoAddr(String token) {
        return getSrvAddr() + "usr/api/update?token=" + token;
    }

    /**
     * 获取其他用户资料
     *
     * @param uId
     * @return
     */
    public static String getOthreUserInfoAddr(String uId) {
        return getSrvAddr() + "sso/api/uinfo?tid=" + uId + "&selector=basic,extra";
    }

    /**
     * 实名认证提交审核接口地址
     *
     * @param name
     * @param subject
     * @param grade
     * @param contact
     * @param token
     * @return
     */
    public static String getSubmitAuthUrl(String name, String subject, String grade, String contact, String token) {
        String url;
        url = getSrvAddr() + "ucenter/api/t/submitApprove?"
                + "I_NAME=" + name
                + "&I_SCHOOL="
                + "&I_SUBJECT=" + subject
                + "&I_GRADE=" + grade
                + "&I_MPHONE=" + contact
                + "&I_EMAIL="
                + "&I_INTENTION="
                + "&token=" + token;
        return url;
    }

    /**
     * 获取找工作，筛选条件更多接口地址
     *
     * @param map 是否返回筛选条件文字数据和key的映射数据，1返回，其他不返回
     * @return
     */
    public static String getFindJobFilterUrl(int map, boolean isEdit) {
        String url;
        String edit = isEdit ? "&edit=1" : "";
        url = getDMSAddr() + "pub/api/loadSearchTags" + "?host=" + CConfigUtil.getRcpHost()
                + "&map=" + map + "&isNeedParse=1" + edit;
        return url;
    }

    /**
     * 获取 找工作 列表
     *
     * @param sort
     * @param page
     * @param pageCount
     * @param ext       城市、筛选-更多 Map<String,List<String>> 数据
     * @param keys      职位数据
     * @return
     */
    public static String getFindJobListUrl(int page, int pageCount, int sort, String ext, String keys, String inputKey) {
        String url;
        url = getDMSAddr() + "pub/api/search" + "?identify=student" + "&host=" + CConfigUtil.getRcpHost()
                + "&limit=" + pageCount + "&page=" + page + "&sort=" + sort + "&ext=" + ext
                + "&keys=" + keys + "&name=" + inputKey;
        return url;
    }

    /**
     * 获取找工作，筛选条件——职位 接口地址
     *
     * @return
     */
    public static String getFindJobTagUrl() {
        String url;
        url = getPES2Addr() + "pub/api/loadPage" + "?source=ANDROID"
                + "&host=" + CConfigUtil.getRcpHost() + "&keys=p_resume" + "&basic=1";
        return url;
    }

    /**
     * 得到简历预览/ 编辑界面 网页地址
     *
     * @param token  看自己的传 token
     * @param rId    看其他人的传 简历id
     * @param isEdit 是否是编辑简历
     * @return
     */
    public static String getResumeViewUrl(String token, String rId, boolean isEdit, String locate) {
        String url;
        if (!Tools.isStringNull(rId)) {
            url = getRcpAddr() + "space/mobile-resume-preview.html" + "?rid=" + rId;
        } else {
            String edit = isEdit ? "&edit=1" : "";
            String locateStr = Tools.isStringNull(locate) ? "" : ("#/" + locate);
            url = getRcpAddr() + "space/mobile-resume-preview.html" + "?token=" + token + edit + locateStr;
        }
        return url;
    }

    /**
     * 获取 职位详情界面 网页地址
     *
     * @param recruitId
     * @param token
     * @param invitationId
     * @param type
     * @return
     */
    public static String getJobDetailUrl(String recruitId, String token, String invitationId, int type) {
        //如果邀请id不为空，邀请函，加参数
        String idKey = (ResumeCommonFragment.TYPE_DROP_BOX == type) ? "&deliveryId=" : "&invitationId=";
        return Config.getRcpAddr() + "space/mobile-recruit-detail.html" + "?id=" + recruitId
                + idKey + invitationId + "&token=" + token;
    }

    /**
     * 投递简历 接口地址
     *
     * @param rctId
     * @param token
     * @return
     */
    public static String getSendResumeToCompanyUrl(String rctId, String token) {
        String url;
        url = getDMSAddr() + "usr/api/deliverResume" + "?rctId=" + rctId + "&token=" + token;
        return url;
    }

    /**
     * 获取操作 “投递” 简历 接口地址
     *
     * @param inviteId
     * @param status
     * @param reason
     * @param token
     * @return
     */
    public static String getOperateDeliverUrl(String inviteId, String status, String reason, String token) {
        String url;
        url = getDMSAddr() + "usr/api/changeDeliver" + "?token=" + token + "&reason=" + reason + "&iid=" + inviteId + "&status=" + status;
        return url;
    }

    /**
     * 获取操作 “邀请” 简历 接口地址
     *
     * @param inviteId
     * @param status
     * @param reason
     * @param token
     * @return
     */
    public static String getOperateInviteUrl(String inviteId, String status, String reason, String token) {
        String url;
        url = getDMSAddr() + "usr/api/changeInvite" + "?token=" + token + "&reason=" + reason + "&iid=" + inviteId + "&status=" + status;
        return url;
    }

    /**
     * 获取 更新简历 接口地址
     *
     * @param token
     * @param rId
     * @return
     */
    public static String getUpdateResumeUrl(String token, String rId) {
        String url;
        url = getDMSAddr() + "usr/api/updateResume" + "?token=" + token + "&rid=" + rId;
        return url;
    }

    /**
     * 获取 发表面试评价 接口地址
     *
     * @param token
     * @return
     */
    public static String getInterviewAppraiseUrl(String token) {
        String url;
        url = getDMSAddr() + "usr/api/publishComment" + "?token=" + token;
        return url;
    }

    private static String KUXIAO_APP_KEY_FOR_WEIBO;
    private static String KUXIAO_APP_KEY_FOR_QQ;
    private static String KUXIAO_APP_KEY_FOR_WECHAT;
    private static String KUXIAO_APP_SECRET_FOR_WECHAT;

    public static String getSrvAddr() {
        if (SERVER_ADDR.length() > 1) {
            return SERVER_ADDR;
        }
        String msg = CConfigUtil.getSso(H.CTX);
        if (!msg.equals("")) {
            return msg;
        }
        try {
            ApplicationInfo info;
            info = H.CTX.getPackageManager().getApplicationInfo(
                    H.CTX.getPackageName(), PackageManager.GET_META_DATA);
            msg = info.metaData.getString("sso");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == msg || "".equals(msg)) {
            Toast.makeText(H.CTX, "请配置服务器地址", Toast.LENGTH_SHORT).show();
            return "";
        }
        SERVER_ADDR = msg;
        return msg;
    }

    public static String getKuxiaoAppIdForWeiBo() {
        if (!TextUtils.isEmpty(KUXIAO_APP_KEY_FOR_WEIBO)) {
            return KUXIAO_APP_KEY_FOR_WEIBO;
        }
        try {
            KUXIAO_APP_KEY_FOR_WEIBO = getMetaDataString("appId-kx-weibo").substring(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return KUXIAO_APP_KEY_FOR_WEIBO;
    }

    public static String getKuxiaoAppIdForQQ() {
        if (!TextUtils.isEmpty(KUXIAO_APP_KEY_FOR_QQ)) {
            return KUXIAO_APP_KEY_FOR_QQ;
        }
        try {
            KUXIAO_APP_KEY_FOR_QQ = getMetaDataString("appId-kx-qq").substring(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return KUXIAO_APP_KEY_FOR_QQ;
    }

    public static String getKuxiaoAppIdForWechat() {
        if (!TextUtils.isEmpty(KUXIAO_APP_KEY_FOR_WECHAT)) {
            return KUXIAO_APP_KEY_FOR_WECHAT;
        }
        try {
            KUXIAO_APP_KEY_FOR_WECHAT = getMetaDataString("appId-kx-wechat").substring(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return KUXIAO_APP_KEY_FOR_WECHAT;
    }

    public static String getKuxiaoAppSecretForWechat() {
        if (!TextUtils.isEmpty(KUXIAO_APP_SECRET_FOR_WECHAT)) {
            return KUXIAO_APP_SECRET_FOR_WECHAT;
        }
        KUXIAO_APP_SECRET_FOR_WECHAT = getMetaDataString("appSecret-kx-wechat").substring(3);
        return KUXIAO_APP_SECRET_FOR_WECHAT;
    }

    public static int getMetaDataInt(String str) {
        int msg = 0;
        try {
            ApplicationInfo info;
            info = H.CTX.getPackageManager().getApplicationInfo(
                    H.CTX.getPackageName(), PackageManager.GET_META_DATA);
            msg = info.metaData.getInt(str, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        L.debug("config : getMetaDataInt {} is {}", str, msg);

        return msg;
    }

    public static String getMetaDataString(String str) {
        String msg = "";
        try {
            ApplicationInfo info;
            info = H.CTX.getPackageManager().getApplicationInfo(
                    H.CTX.getPackageName(), PackageManager.GET_META_DATA);
            msg = info.metaData.getString(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
//		L.debug("config : getMetaDataString {} is {}", str, msg);
        if (null == msg || "".equals(msg)) {
//			Toast.makeText(H.CTX, "请配置服务器地址", Toast.LENGTH_SHORT).show();
            return "";
        }
        return msg;
    }

    public static String getSRRELAddr() {
        String msg = CConfigUtil.getSrrel(H.CTX);
        if (!msg.equals("")) {
            return msg;
        }
        try {
            ApplicationInfo info;
            info = H.CTX.getPackageManager().getApplicationInfo(
                    H.CTX.getPackageName(), PackageManager.GET_META_DATA);
            msg = info.metaData.getString("sr-rel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == msg || "".equals(msg)) {
//			ToastUtil.toastShort("请配置文件服务器地址");
            return "";
        }
        return msg;
    }

    /**
     * 判断是否是爱科学的服务器地址
     *
     * @return
     */
    public static boolean isSrrelAikeXue() {
        boolean isSrrelAKX = false;
        if (Config.getSRRELAddr().contains("aikexue")) {
            isSrrelAKX = true;
        }
        return isSrrelAKX;
    }

    public static String getSyncAuthToRcp(String token) {
        String url = getRcpAddr() + "usr/sync?" + "token=" + token;
        return url;
    }

    public static String getRcpAddr() {
        String msg = CConfigUtil.getRcp(H.CTX);
        if (!msg.equals("")) {
            return msg;
        }
        try {
            ApplicationInfo info;
            info = H.CTX.getPackageManager().getApplicationInfo(
                    H.CTX.getPackageName(), PackageManager.GET_META_DATA);
            msg = info.metaData.getString("rcp");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == msg || "".equals(msg)) {
//			ToastUtil.toastShort("请配置文件服务器地址");
            return "";
        }
        return msg;
    }

    public static String getFSSrvAddr() {
        if (FSSRV.length() > 1) {
            return FSSRV;
        }
        String msg = CConfigUtil.getFs(H.CTX);
        if (!msg.equals("")) {
            FSSRV = msg;
            return msg;
        }
        try {
            ApplicationInfo info;
            info = H.CTX.getPackageManager().getApplicationInfo(
                    H.CTX.getPackageName(), PackageManager.GET_META_DATA);
            msg = info.metaData.getString("fs");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == msg || "".equals(msg)) {
            CToastUtil.toastShort(H.CTX, "请配置文件服务器地址");
            return "";
        }
        return msg;
    }

    public static String getDMSAddr() {
        if (SSO_DMS.length() > 1) {
            return SSO_DMS;
        }
        String msg = CConfigUtil.getDms(H.CTX);
        if (!msg.equals("")) {
            SSO_DMS = msg;
            return msg;
        }
        try {
            ApplicationInfo info;
            info = H.CTX.getPackageManager().getApplicationInfo(
                    H.CTX.getPackageName(), PackageManager.GET_META_DATA);
            msg = info.metaData.getString("dms");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == msg || "".equals(msg)) {
            CToastUtil.toastShort(H.CTX, "请配置文件服务器地址");
            return "";
        }
        return msg;
    }

    public static String getPES2Addr() {
        if (SSO_PES2.length() > 1) {
            return SSO_PES2;
        }
        String msg = CConfigUtil.getPes2(H.CTX);
        if (!msg.equals("")) {
            SSO_PES2 = msg;
            return msg;
        }
        try {
            ApplicationInfo info;
            info = H.CTX.getPackageManager().getApplicationInfo(
                    H.CTX.getPackageName(), PackageManager.GET_META_DATA);
            msg = info.metaData.getString("pes2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == msg || "".equals(msg)) {
            CToastUtil.toastShort(H.CTX, "请配置服务器地址");
            return "";
        }
        return msg;
    }

    public static String getAppraiseAddr() {
        if (SSO_APPRAISE.length() > 1) {
            return SSO_APPRAISE;
        }
        String msg = CConfigUtil.getAppraise(H.CTX);
        if (!msg.equals("")) {
            SSO_APPRAISE = msg;
            return msg;
        }
        try {
            ApplicationInfo info;
            info = H.CTX.getPackageManager().getApplicationInfo(
                    H.CTX.getPackageName(), PackageManager.GET_META_DATA);
            msg = info.metaData.getString("appraise");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == msg || "".equals(msg)) {
            CToastUtil.toastShort(H.CTX, "请配置服务器地址");
            return "";
        }
        return msg;
    }
}
