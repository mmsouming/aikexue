package com.dy.aikexue.ssolibrary.business.impl;

import com.dy.aikexue.ssolibrary.business.call.ResumeHCall;
import com.dy.aikexue.ssolibrary.config.Config;
import com.dy.aikexue.ssolibrary.loginregister.fragment.ResumeCommonFragment;
import com.dy.aikexue.ssolibrary.util.Tools;
import com.dy.sdk.business.call.BusinessListCallBack;
import com.dy.sdk.business.itf.BusinessAction;

import org.cny.awf.net.http.H;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by fuq on 2017/2/22.
 * 找工作 地址配置
 */

public class FindJobConfig<T> extends BusinessAction<T> {

    private static Logger L = LoggerFactory.getLogger(FindJobConfig.class);
    private String ext;
    private String keys;
    private String inputKey;
    private int sort;
    //记录请求的次数
    private int mLoadCount;

    public void setUrlParams(String ext, String keys, String inputKey, int sort) {
        this.ext = ext == null ? "" : ext;
        this.keys = keys == null ? "" : keys;
        this.inputKey = Tools.isStringNull(inputKey) ? "" : inputKey;
        this.sort = sort;
    }

    public FindJobConfig(BusinessListCallBack<T> call) {
        super(call);
        setCallBack(call);
    }

    @Override
    protected String getUrl() {
        String url = Config.getFindJobListUrl(getCurrentPage(), getDefaultPageCount(), sort, ext, keys, inputKey);
        L.info("find job url：" + url);
        return url;
    }

    @Override
    protected void loadData(int page, int pageCount) {
        H.doGet(getUrl(), new ResumeHCall(this, ResumeCommonFragment.TYPE_JOB, mLoadCount == 0));
        mLoadCount++;
    }

    /**
     * 设置搜索到的职位数
     *
     * @param allTotal 职位和公司总数
     * @param rTotal   职位数
     */
    public void setTotals(int allTotal, int rTotal) {
//        if (!Tools.isStringNull(inputKey)) {
//            BusinessResumeCall callback = (BusinessResumeCall) getCallBack();
//            callback.setEveryTotals(allTotal, rTotal,inputKey);
//        }
    }
}
