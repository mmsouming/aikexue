package com.dy.aikexue.ssolibrary.loginregister.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.bean.EvaluateBean;
import com.dy.aikexue.ssolibrary.loginregister.adapter.CompanyEvaluateListAdapter;
import com.dy.sdk.view.swiperefresh.PullToRefreshLayout;
import com.dy.sdk.view.swiperefresh.listener.SwipeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhong on 2017/2/22.
 * 公司评价列表
 */

public class CompanyEvaluateListFragment extends CompanyBaseFragment implements SwipeListener.OnRefreshListener, SwipeListener.OnLoadListener {
    private View mContentView;
    private PullToRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private CompanyEvaluateListAdapter mAdapter;
//    private BusinessEvaluateList mBusiness;

    private static final String VALUE_LIST = "valueList";
    private static final String VALUE_COMPANY_ID = "companyId";


    //公司id
    private String companyId;

    public static CompanyEvaluateListFragment newFragment(String companyId) {
        return newFragment(null, companyId);
    }

    public static CompanyEvaluateListFragment newFragment(ArrayList<EvaluateBean.DataBean.AppsBean> list, String companyId) {
        CompanyEvaluateListFragment fragment = new CompanyEvaluateListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(VALUE_LIST, list);
        bundle.putString(VALUE_COMPANY_ID, companyId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mContentView == null) {
            mContentView = inflater.inflate(R.layout.fragment_evaluate_layout, null);
//            initView();
//            initListener();
//            init();
            remoteData();
        }
        return mContentView;
    }

    /**
     * 获取传递的数据
     */
    private void remoteData() {
//        Object obj = getArguments().getSerializable(VALUE_LIST);
//        companyId = getArguments().getString(VALUE_COMPANY_ID);
//        mBusiness.setCompanyId(companyId);
//        if (obj == null) {
//            getRefreshLayout().setRefreshing(true);
//        } else {
//            List<EvaluateBean.DataBean.AppsBean> list = (List<EvaluateBean.DataBean.AppsBean>) obj;
//            //如果判断集合为空，添加空数据标示的实体到集合中
//            if (list.isEmpty()) {
//                EvaluateBean.DataBean.AppsBean bean = new EvaluateBean.DataBean.AppsBean(ResumeListAdapter.EXCEPTION_NOT_DATA_ID);
//                list.add(bean);
//            }
//            getAdapter().refresh(list);
//        }
    }

    /**
     * 初始化变量
     */
//    private void init() {
//        mAdapter = new CompanyEvaluateListAdapter(new ArrayList<EvaluateBean.DataBean.AppsBean>(), getContext());
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mRecyclerView.setAdapter(mAdapter);
//        mBusiness = new BusinessEvaluateList(new BusinessCompanyEvaluateCall(this));
//    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        mRefreshLayout.setLoadEnable(true);
        mRefreshLayout.setRefreshing(true);
        mRefreshLayout.setOnLoadListener(this);
        mRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * 初始化控件
     */
//    private void initView() {
//        mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.recyclerView);
//        mRefreshLayout = (PullToRefreshLayout) mContentView.findViewById(R.id.refreshLayout);
//        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(getContext()));
//    }

    @Override
    public void dataError(String errorStr) {

    }

    @Override
    public void dataComplete() {

    }

//    @Override
//    public void setData(CompanyDetailBean bean, boolean isCache) {
//        super.setData(bean, isCache);
//        Log.e("data:", "" + bean);
//    }

    public void setRefreshEnable(boolean isEnable) {
        if (mRefreshLayout == null) return;
        mRefreshLayout.setRefreshEnable(isEnable);
    }

    public void setLoadEnable(boolean isEnable) {
        if (mRefreshLayout == null) return;
        mRefreshLayout.setLoadEnable(isEnable);
    }

    public PullToRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public CompanyEvaluateListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onRefresh() {
//        mBusiness.refresh();
    }

    @Override
    public void onLoad() {
//        mBusiness.next();
    }

}
