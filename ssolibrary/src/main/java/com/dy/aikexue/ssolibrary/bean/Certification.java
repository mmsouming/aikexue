package com.dy.aikexue.ssolibrary.bean;

import java.io.Serializable;

/**
 * Created by:Pxy
 * Date: 2016-03-21
 * Time: 18:53
 */
public class Certification implements Serializable {

    private String email;

    private String phone;

    private String realName;

    /**
     * 0  未认证
     * 1  审核中
     * 2  审核失败
     * 3  审核成功
     */
    private int status;

    private String desc;

    private String idCardFront;

    private String idCardBack;

    //身份证号码
    private String idCardNo;

    private String reason;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront;
    }

    public String getIdCardFront() {
        return this.idCardFront;
    }

    public void setIdCardBack(String idCardBack) {
        this.idCardBack = idCardBack;
    }

    public String getIdCardBack() {
        return this.idCardBack;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getIdCardNo() {
        return this.idCardNo;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return this.reason;
    }

    @Override
    public String toString() {
        return "Certification{" +
                "email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", realName='" + realName + '\'' +
                ", status=" + status +
                ", desc='" + desc + '\'' +
                ", idCardFront='" + idCardFront + '\'' +
                ", idCardBack='" + idCardBack + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}