package com.dy.aikexue.ssolibrary.loginregister.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.bean.NewUserData;
import com.dy.aikexue.ssolibrary.business.impl.BusinessCompanyDetail;
import com.dy.aikexue.ssolibrary.loginregister.adapter.DefaultFragmentAdapter;
import com.dy.aikexue.ssolibrary.util.Tools;
import com.dy.sdk.utils.ObjectValueUtil;
import com.dy.sdk.utils.ScreenUtil;
import com.dy.sdk.view.dialog.LoadingDialog;

import org.cny.awf.view.ImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhong on 2017/2/21.
 * 公司介绍界面
 */

public class CompanyDetailFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {
    private CompanyBaseFragment resumeFragment, detailFragment, evaluateListFragment;
    private View contentView;
    private ViewPager viewPager;
    private BusinessCompanyDetail business;
    private DefaultFragmentAdapter adapter;
    private List<Fragment> listFragment;
    private TabLayout tabLayout;
    private String companyId;
    private CoordinatorLayout contentLayout;
    private AppBarLayout barLayout;
    private TextView tvCompanyName, tvUrl, tvDetail;
    private ImageView imgPhoto;

    private static final String VALUE_ID = "companyId";

    public static CompanyDetailFragment newFragment(String companyId) {
        Bundle bundle = new Bundle();
        bundle.putString(VALUE_ID, companyId);
        CompanyDetailFragment fragment = new CompanyDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_company_details, null);
            remoteData();
//            initView();
//            initFragment();
//            init();
            initTab();
            showLoading();
            business.execute();
        }
        return contentView;
    }

    private void remoteData() {
        companyId = getArguments().getString(VALUE_ID, null);
        companyId = companyId == null ? "" : companyId;
    }

    private void initTab() {
        tabLayout.removeAllTabs();
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText(getString(R.string.companyInfo));
        TabLayout.Tab tab1 = tabLayout.newTab();
        tab1.setText(getString(R.string.InJobs));
        TabLayout.Tab tab2 = tabLayout.newTab();
        tab2.setText(getString(R.string.InterviewEvaluation));
        tabLayout.addTab(tab);
        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
    }

//    private void init() {
//        business = new BusinessCompanyDetail(new BusinessCompanyDetailCall(this, resumeFragment, detailFragment, evaluateListFragment));
//        business.setCompanyId(companyId);
//        listFragment = new ArrayList<>();
//        listFragment.add(detailFragment);
//        listFragment.add(resumeFragment);
//        listFragment.add(evaluateListFragment);
//        adapter = new DefaultFragmentAdapter(getChildFragmentManager(), listFragment);
//        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);
//    }

//    private void initFragment() {
//        resumeFragment = CompanyResumeFragment.newFragment(companyId);
//        detailFragment = CompanyIntroduceFragment.newFragment(companyId);
//        evaluateListFragment = CompanyEvaluateListFragment.newFragment(null, companyId);
//    }

//    private void initView() {
//        viewPager = (ViewPager) contentView.findViewById(R.id.viewPager);
//        tabLayout = (TabLayout) contentView.findViewById(R.id.tabLayout);
//        contentLayout = (CoordinatorLayout) contentView.findViewById(R.id.contentView);
//        barLayout = (AppBarLayout) contentView.findViewById(R.id.barLayout);
//        tvCompanyName = (TextView) contentView.findViewById(R.id.tvCompanyName);
//        tvUrl = (TextView) contentView.findViewById(R.id.tvUrl);
//        tvDetail = (TextView) contentView.findViewById(R.id.tvDetail);
//        imgPhoto = (ImageView) contentView.findViewById(R.id.imgPhoto);
//
//        barLayout.addOnOffsetChangedListener(this);
//    }


    public void setData(NewUserData.Data.Usr usr) {
        String urlPath = "attrs/orgInfo/logo";
        String nickNamePath = "attrs/orgInfo/name";
        String objWebSitePath = "attrs/orgInfo/website";
        Object objUrl = ObjectValueUtil.getInstance().getValueObject(usr, urlPath);
        Object objName = ObjectValueUtil.getInstance().getValueObject(usr, nickNamePath);
        Object objWebSite = ObjectValueUtil.getInstance().getValueObject(usr, objWebSitePath);
        tvCompanyName.setText(objName == null ? "" : (String) objName);
        tvUrl.setText(objWebSite == null ? "" : (String) objWebSite);
        if (objUrl != null) {
            List<String> listLogo = (List<String>) objUrl;
            if (!listLogo.isEmpty()) {
                String logo = listLogo.get(0);
                imgPhoto.setUrl(logo == null ? "" : logo);
            }
        }
        String companyStr = Tools.getOrgInfoBaseStr(usr.getAttrs() != null ? usr.getAttrs().getOrgInfo() : null, getContext());
        tvDetail.setText(companyStr);


    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        CompanyResumeFragment fragment = (CompanyResumeFragment) resumeFragment;
        CompanyEvaluateListFragment eFragment = (CompanyEvaluateListFragment) evaluateListFragment;
        //没有拉到最低端不能刷新数据
        if (verticalOffset == 0) {
            fragment.setRefreshEnable(true);
            eFragment.setRefreshEnable(true);
        } else {
            fragment.setRefreshEnable(false);
            eFragment.setRefreshEnable(false);
        }
        //没有拉到顶端不加载更多数据
        if (Math.abs(verticalOffset) == ScreenUtil.dip2px(getContext(), 100)) {
            fragment.setLoadEnable(true);
            eFragment.setLoadEnable(true);
        } else {
            fragment.setLoadEnable(false);
            eFragment.setLoadEnable(false);
        }
    }

    private LoadingDialog loadingDialog;

    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getContext(), getString(R.string.loadingWait));
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
