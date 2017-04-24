package com.dy.aikexue.ssolibrary.business.impl;

import com.dy.aikexue.ssolibrary.business.call.ResumeHCall;
import com.dy.aikexue.ssolibrary.config.Config;
import com.dy.aikexue.ssolibrary.loginregister.fragment.ResumeCommonFragment;
import com.dy.sdk.business.call.BusinessListCallBack;
import com.dy.sdk.business.itf.BusinessAction;

import org.cny.awf.net.http.H;

/**
 * Created by zhong on 2017/2/20.
 * 邀请函配置
 */

public class BusinessInvitation<T> extends BusinessAction<T> {
    private String status = "";

    public BusinessInvitation(BusinessListCallBack<T> call) {
        super(call);
        setCallBack(call);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    protected String getUrl() {
        return Config.getInvitationUrl(status, getCurrentPage(), getDefaultPageCount());
    }

    @Override
    protected void loadData(int page, int pageCount) {
        H.doGet(getUrl(), new ResumeHCall(this, ResumeCommonFragment.TYPE_INVITATION, mLoadCount == 0));
        mLoadCount++;
    }

    //记录请求的次数
    private int mLoadCount;
}
