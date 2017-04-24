package com.dy.aikexue.ssolibrary.business.impl;

import com.dy.aikexue.ssolibrary.bean.PostProgressBean;
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
 * Created by zhong on 2017/3/28.
 */

public class BusinessPostProgress extends BusinessPagerAction<PostProgressBean> {
    public BusinessPostProgress(BusinessPagerCallBack call) {
        super(call);
    }

    @Override
    protected void loadData() {
        H.doGet(getUrl(), new HCall());
    }

    //邀请or投递id
    private String DDID;

    public void setDDID(String DDID) {
        this.DDID = DDID;
    }

    @Override
    protected String getUrl() {
        String url = Config.getPostProgressUrl(DDID);
        return url;
    }

    class HCall extends HCallback.HCacheCallback {

        @Override
        public void onError(CBase cBase, String s, Throwable throwable) throws Exception {
            executeOnError(BusinessListCallBack.STATUS_NET_WORK);
            executeOnComplete();
        }

        @Override
        public void onSuccess(CBase cBase, HResp hResp, String s) throws Exception {
            if (s == null || s.isEmpty()) {
                executeOnError(BusinessListCallBack.STATUS_LOAD_ERROR);
            } else {
                try {
                    PostProgressBean bean = GsonUtil.fromJson(s, PostProgressBean.class);
                    if (bean == null || bean.getCode() != 0) {
                        throw new RuntimeException("load data error");
                    }
                    executeOnSuccess(bean);
                } catch (Exception e) {
                    e.printStackTrace();
                    executeOnError(BusinessListCallBack.STATUS_LOAD_ERROR);
                }
            }
            executeOnComplete();

        }
    }
}
