package com.dy.aikexue.ssolibrary.business.call;

import com.dy.aikexue.ssolibrary.bean.PostProgressBean;
import com.dy.aikexue.ssolibrary.loginregister.activit.PostProgressActivity;
import com.dy.sdk.business.call.BusinessPagerCallBack;

/**
 * Created by zhong on 2017/3/28.
 */

public class BusinessPostProgressCall extends BusinessPagerCallBack<PostProgressBean> {
    private PostProgressActivity mActivity;

    public BusinessPostProgressCall(PostProgressActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onError(int code) {
        mActivity.showError(code);
    }

    @Override
    public void onSuccess(PostProgressBean postProgressBean) {
        mActivity.refresh(postProgressBean);
    }

    @Override
    public void onComplete() {
        mActivity.hideLoading();
    }

    @Override
    public void onCache(PostProgressBean postProgressBean) {

    }

}
