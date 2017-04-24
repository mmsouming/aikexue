package com.dy.aikexue.ssolibrary.loginregister.activit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.bean.CompanyDetailBean;
import com.dy.aikexue.ssolibrary.bean.DropBoxBean;
import com.dy.aikexue.ssolibrary.business.call.BusinessCompanyResumeListCall;
import com.dy.aikexue.ssolibrary.business.impl.BusinessCompanyResume;
import com.dy.aikexue.ssolibrary.loginregister.adapter.CompanyResumeListAdapter;
import com.dy.aikexue.ssolibrary.loginregister.fragment.CompanyBaseFragment;
import com.dy.sdk.view.swiperefresh.PullToRefreshLayout;
import com.dy.sdk.view.swiperefresh.listener.SwipeListener;

import java.util.List;

/**
 * 公司招聘列表
 * Created by zhong on 2017/2/22.
 */

public class CompanyResumeFragment extends CompanyBaseFragment implements SwipeListener.OnLoadListener, SwipeListener.OnRefreshListener {
    private View contentView;
    private PullToRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private CompanyResumeListAdapter adapter;
    private BusinessCompanyResume business;
    private BusinessCompanyResumeListCall businessCall;

    public static CompanyResumeFragment newFragment(String companyId) {
        Bundle bundle = new Bundle();
        bundle.putString(CompanyBaseFragment.VALUE_COMPANY_ID, companyId);
        CompanyResumeFragment fragment = new CompanyResumeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private String companyId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_resume_layout, null);
            remoteData();
//            businessCall = new BusinessCompanyResumeListCall(this);
//            business = new BusinessCompanyResume(businessCall);
//            initView();
//            init();
//            initListener();
//            refreshLayout.setRefreshing(true);
//            business.setCurrentPage(2);
//            business.setCompanyId(companyId);

        }

        return contentView;
    }

    private void remoteData() {
        companyId = getArguments().getString(VALUE_COMPANY_ID);
    }

    private void initListener() {
        refreshLayout.setOnLoadListener(this);
        refreshLayout.setOnRefreshListener(this);
    }

    public CompanyResumeListAdapter getAdapter() {
        return adapter;
    }

    private void init() {
        adapter = new CompanyResumeListAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initView() {
        refreshLayout = (PullToRefreshLayout) contentView.findViewById(R.id.refreshLayout);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        refreshLayout.setRefreshEnable(false);
        refreshLayout.setLoadEnable(true);
    }

    @Override
    public void dataError(String errorStr) {

    }

    @Override
    public void dataComplete() {
        refreshLayout.setRefreshing(false);
        refreshLayout.setLoading(false);
    }

    @Override
    public void setData(CompanyDetailBean bean, boolean isCache) {
        super.setData(bean, isCache);
        if (bean != null && bean.getData() != null) {
            List<DropBoxBean.DataBean.RecruitBean> listData = bean.getData().getRecruits();
            adapter.refresh(listData);
        }
    }

    @Override
    public void onLoad() {
        business.next();
        Log.e("onLoad", "onLoad");
    }

    public void setRefreshEnable(boolean isEnable) {
        if(refreshLayout==null)return;
        refreshLayout.setRefreshEnable(isEnable);
    }

    public void setLoadEnable(boolean isEnable) {
        if(refreshLayout==null)return;
        refreshLayout.setLoadEnable(isEnable);
    }

    @Override
    public void onRefresh() {
        business.refresh();
    }
}
