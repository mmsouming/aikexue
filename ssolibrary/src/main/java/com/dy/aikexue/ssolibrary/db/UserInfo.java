package com.dy.aikexue.ssolibrary.db;


import com.dy.aikexue.ssolibrary.bean.Attrs;
import com.dy.aikexue.ssolibrary.bean.Extra;
import com.dy.aikexue.ssolibrary.bean.Certification;

import java.io.Serializable;

/**
 * @author zengdl
 * @create 2015-01-29
 */
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String ID = "_id";
        public static final String USERID = "userId";
    public static final String USERNAME = "userName";
    public static final String TOKEN = "token";
    public static final String ATTRS = "allback";
    public static final String PASS = "pass";
    public static final String PHONE = "phone";
    public static final String HEADURL = "head";
    public static final String SIGN = "sign";
    public static final String GENDER = "gender";
    public static final String LOCATION = "location";
    public static final String ATTRSINFO = "attrs";
    public static final String Certification = "certification";
    public static final String CHANNAL = "channel";
    public static final String ISTEACH="isTeach";
    public static final String NUMBER="number";
    private int id;
    private String userId; // 用户id
    private String userName;
    private String token;
    private String attrs;
    private int pass;//用户是否通过老师认证  通过为1
    private String phone;//用户账户是否绑定手机号  绑定了不为空
    private String headurl;
    private String sign;
    private int gender;
    private String location;
    private Attrs.Basic basicUserInfo;
    private Certification certification;
    private Extra extraInfo;
    private Attrs.Role role;
    private Attrs attrsInfo;

    private boolean isTeach;
    public void setIsTeach(boolean isTeach){
        this.isTeach=isTeach;
    }
    public boolean getIsTeach(){
        return isTeach;
    }
    public void setRole(Attrs.Role role) {
        this.role = role;
    }

    public Attrs.Role getRole() {
        return role;
    }

    public Attrs getAttrsInfo() {
        return attrsInfo;
    }

    public void setAttrsInfo(Attrs attrsInfo) {
        this.attrsInfo = attrsInfo;
    }

    private Attrs.Private privated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAttrs() {
        return attrs;
    }

    public void setAttrs(String attrs) {
        this.attrs = attrs;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Attrs.Basic getBasicUserInfo() {
        return basicUserInfo;
    }

    public void setBasicUserInfo(Attrs.Basic basicUserInfo) {
        this.basicUserInfo = basicUserInfo;
    }

    public Certification getCertification() {
        return certification;
    }

    public void setCertification(Certification certification) {
        this.certification = certification;
    }

    public Extra getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Extra extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Attrs.Private getPrivated() {
        return privated;
    }

    public void setPrivated(Attrs.Private privated) {
        this.privated = privated;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", token='" + token + '\'' +
                ", pass=" + pass +
                ", phone='" + phone + '\'' +
                ", headurl='" + headurl + '\'' +
                ", sign='" + sign + '\'' +
                ", gender=" + gender +
                ", location='" + location + '\'' +
                ", basicUserInfo=" + basicUserInfo +
                ", certification=" + certification +
                ", attrs='" + attrs + '\'' +
                ", extraInfo=" + extraInfo +
                '}';
    }
}
