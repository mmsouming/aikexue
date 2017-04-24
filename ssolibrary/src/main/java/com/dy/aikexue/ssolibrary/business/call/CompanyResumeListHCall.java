package com.dy.aikexue.ssolibrary.business.call;

import com.dy.aikexue.ssolibrary.bean.CompanyDetailBean;
import com.dy.aikexue.ssolibrary.bean.DropBoxBean;
import com.dy.aikexue.ssolibrary.business.impl.BusinessCompanyResume;
import com.dy.sdk.business.call.BusinessListCallBack;
import com.dy.sdk.utils.GsonUtil;

import org.cny.awf.net.http.CBase;
import org.cny.awf.net.http.HCallback;
import org.cny.awf.net.http.HResp;

import java.util.List;

/**
 * Created by zhong on 2017/2/22.
 * 公司详情简历列表请求网络回调
 */

public class CompanyResumeListHCall extends HCallback.HCacheCallback {
    private BusinessCompanyResume mBusiness;

    public CompanyResumeListHCall(BusinessCompanyResume business) {
        this.mBusiness = business;
    }

    @Override
    public void onCache(CBase c, HResp res, String cache) throws Exception {
        super.onCache(c, res, cache);
        parseData(cache, true);
    }

    @Override
    public void onError(CBase cBase, String s, Throwable throwable) throws Exception {
        mBusiness.executeOnError(BusinessListCallBack.STATUS_NET_WORK);
        mBusiness.executeOnComplete();
    }

    @Override
    public void onSuccess(CBase cBase, HResp hResp, String s) throws Exception {
        parseData(s, false);
    }

    private void parseData(String s, boolean isCache) {
        if (s.isEmpty()) {
            if (!isCache)
                mBusiness.executeOnError(BusinessListCallBack.STATUS_LOAD_ERROR);
        } else {
            try {
                CompanyDetailBean bean = GsonUtil.fromJson(s, CompanyDetailBean.class);
                if (bean == null || bean.getCode() != 0) {
                    throw new Exception("load data error");
                } else {
                    List<DropBoxBean.DataBean.RecruitBean> listData = bean.getData().getRecruits();
                    if (isCache) {
                        mBusiness.executeOnCache(listData);
                    } else {
                        if (mBusiness.getCurrentPage() == 1) {
                            mBusiness.executeOnSuccess(listData);
                        } else {
                            mBusiness.executeOnNext(listData);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (!isCache)
                    mBusiness.executeOnError(BusinessListCallBack.STATUS_LOAD_ERROR);
            }
        }
        if (isCache) return;
        mBusiness.executeOnComplete();
    }
}
