package com.dy.aikexue.ssolibrary.business.call;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.bean.CompanyDetailBean;
import com.dy.aikexue.ssolibrary.bean.NewUserData;
import com.dy.aikexue.ssolibrary.loginregister.fragment.CompanyBaseFragment;
import com.dy.aikexue.ssolibrary.loginregister.fragment.CompanyDetailFragment;
import com.dy.sdk.business.call.BusinessPagerCallBack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by zhong on 2017/2/22.
 * 公司详情业务类
 */

public class BusinessCompanyDetailCall extends BusinessPagerCallBack<CompanyDetailBean> {

    private CompanyDetailFragment fragment;
    private CompanyBaseFragment resumeFragment, detailFragment, evaluateListFragment;

    public BusinessCompanyDetailCall(CompanyDetailFragment fragment, CompanyBaseFragment resumeFragment, CompanyBaseFragment detailFragment, CompanyBaseFragment evaluateListFragment) {
        this.fragment = fragment;
        this.resumeFragment = resumeFragment;
        this.detailFragment = detailFragment;
        this.evaluateListFragment = evaluateListFragment;
    }

    @Override
    public void onError(int code) {
        String errorSrr = "";
        if (code == STATUS_NET_WORK) {
            errorSrr = resumeFragment.getContext().getString(R.string.netWordError);
        } else {
            errorSrr = resumeFragment.getString(R.string.loadDataError);
        }
        resumeFragment.dataError(errorSrr);
        detailFragment.dataError(errorSrr);
        evaluateListFragment.dataError(errorSrr);
    }

    @Override
    public void onSuccess(CompanyDetailBean companyDetailBean) {
        if (companyDetailBean != null && companyDetailBean.getData() != null && companyDetailBean.getData().getUsr() != null) {
            HashMap<String, NewUserData.Data.Usr> usr = companyDetailBean.getData().getUsr();
            Set<String> set = usr.keySet();
            Iterator<String> iterator = set.iterator();
//            if (iterator.hasNext()) {
//                String key = iterator.next();
//                NewUserData.Data.Usr u = usr.get(key);
//                fragment.setData(u);
//            }
        }
        resumeFragment.setData(companyDetailBean, false);
        detailFragment.setData(companyDetailBean, false);
        evaluateListFragment.setData(companyDetailBean, false);
    }

    @Override
    public void onComplete() {
        resumeFragment.dataComplete();
        detailFragment.dataComplete();
        evaluateListFragment.dataComplete();
        fragment.hideLoading();
    }

    @Override
    public void onCache(CompanyDetailBean companyDetailBean) {

    }
}
