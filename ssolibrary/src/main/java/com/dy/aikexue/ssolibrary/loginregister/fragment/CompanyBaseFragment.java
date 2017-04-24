package com.dy.aikexue.ssolibrary.loginregister.fragment;

import android.support.v4.app.Fragment;

import com.dy.aikexue.ssolibrary.bean.CompanyDetailBean;


/**
 * Created by zhong on 2017/2/22.
 */

public abstract class CompanyBaseFragment extends Fragment {
    public static final String VALUE_COMPANY_ID="companyId";
    CompanyDetailBean mCompanyDetailBean;

    public void setData(CompanyDetailBean bean,boolean isCache) {
        this.mCompanyDetailBean = bean;
    }

    public abstract void dataError(String errorStr);

    public abstract void dataComplete();

}
