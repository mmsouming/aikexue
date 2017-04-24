package com.dy.aikexue.ssolibrary.business.impl;

import com.dy.aikexue.ssolibrary.bean.DropBoxBean;
import com.dy.aikexue.ssolibrary.business.call.BusinessCompanyResumeListCall;
import com.dy.aikexue.ssolibrary.business.call.CompanyResumeListHCall;
import com.dy.aikexue.ssolibrary.config.Config;
import com.dy.sdk.business.itf.BusinessAction;

import org.cny.awf.net.http.H;

/**
 * Created by zhong on 2017/2/22.
 * 公司详情工作列表配置
 */

public class BusinessCompanyResume extends BusinessAction<DropBoxBean.DataBean.RecruitBean> {

    private String mCompanyId;

    public BusinessCompanyResume(BusinessCompanyResumeListCall call) {
        super(call);
    }

    public void setCompanyId(String mCompanyId) {
        this.mCompanyId = mCompanyId;
    }

    @Override
    protected void loadData(int page, int pageCount) {
        H.doGet(getUrl(), new CompanyResumeListHCall(this));
    }

    @Override
    protected String getUrl() {
        return Config.getCompanyResumeListUrl(mCompanyId, getCurrentPage(), getDefaultPageCount());
    }
}
