package com.dy.aikexue.ssolibrary.business.impl;

import com.dy.aikexue.ssolibrary.bean.CompanyDetailBean;
import com.dy.aikexue.ssolibrary.config.Config;
import com.dy.sdk.business.call.BusinessListCallBack;
import com.dy.sdk.business.call.BusinessPagerCallBack;
import com.dy.sdk.business.itf.BusinessPagerAction;
import com.dy.sdk.utils.GsonUtil;

import org.cny.awf.net.http.CBase;
import org.cny.awf.net.http.H;
import org.cny.awf.net.http.HCallback;
import org.cny.awf.net.http.HResp;

/**
 * Created by zhong on 2017/2/22.
 * 公司详情配置
 */

public class BusinessCompanyDetail extends BusinessPagerAction<CompanyDetailBean> {
    public BusinessCompanyDetail(BusinessPagerCallBack<CompanyDetailBean> callBack) {
        super(callBack);
    }

    @Override
    protected String getUrl() {
        return Config.getCompanyDetailUrl(mCompanyId);
    }

    private String mCompanyId = "";

    public void setCompanyId(String mCompanyId) {
        this.mCompanyId = mCompanyId;
    }

    @Override
    protected void loadData() {
        H.doGet(getUrl(), new HCall());
    }


    class HCall extends HCallback.HCacheCallback {

        @Override
        public void onCache(CBase c, HResp res, String cache) throws Exception {
            super.onCache(c, res, cache);
            parseData(cache, true);
        }

        @Override
        public void onError(CBase cBase, String s, Throwable throwable) throws Exception {
            executeOnError(BusinessListCallBack.STATUS_NET_WORK);
            executeOnComplete();
        }

        @Override
        public void onSuccess(CBase cBase, HResp hResp, String s) throws Exception {
            parseData(s, false);
        }

        private void parseData(String s, boolean isCache) {
            if (s == null || s.equals("")) {
                if (!isCache) {
                    executeOnError(BusinessListCallBack.STATUS_LOAD_ERROR);
                }
            } else {
                try {
                    CompanyDetailBean bean = GsonUtil.fromJson(s, CompanyDetailBean.class);
                    if (bean != null && bean.getCode() == 0) {
                        if (isCache) {
                            executeOnCache(bean);
                        } else {
                            executeOnSuccess(bean);
                        }
                    } else {
                        throw new Exception("load data error");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (!isCache) {
                        executeOnError(BusinessListCallBack.STATUS_LOAD_ERROR);
                    }
                }
            }
            if (isCache) return;
            executeOnComplete();
        }
    }

}
