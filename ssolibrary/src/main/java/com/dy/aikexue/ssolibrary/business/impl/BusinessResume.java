package com.dy.aikexue.ssolibrary.business.impl;

import com.dy.aikexue.ssolibrary.business.call.ResumeHCall;
import com.dy.aikexue.ssolibrary.config.Config;
import com.dy.aikexue.ssolibrary.loginregister.fragment.ResumeCommonFragment;
import com.dy.sdk.business.call.BusinessListCallBack;
import com.dy.sdk.business.itf.BusinessAction;

import org.cny.awf.net.http.H;

/**
 * Created by zhong on 2017/2/17.
 * 投递箱配置
 */


public class BusinessResume<T> extends BusinessAction<T> {

    private String status = "";

    public BusinessResume(BusinessListCallBack<T> call) {
        super(call);
        setCallBack(call);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    protected String getUrl() {
        return Config.getDropBoxUrl(status, getCurrentPage(), getDefaultPageCount());
    }


    @Override
    protected void loadData(int page, int pageCount) {
        H.doGet(getUrl(), new ResumeHCall(this, ResumeCommonFragment.TYPE_DROP_BOX,mLoadCount==0));
        mLoadCount++;
    }

    //记录请求的次数
    private int mLoadCount;

}
