package com.dy.aikexue.ssolibrary.business.call;

import com.dy.aikexue.ssolibrary.bean.DropBoxBean;
import com.dy.aikexue.ssolibrary.loginregister.adapter.ResumeListAdapter;
import com.dy.aikexue.ssolibrary.loginregister.fragment.CompanyResumeFragment;
import com.dy.sdk.business.call.BusinessListCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhong on 2017/2/22.
 * 公司详情工作列表业务类
 */

public class BusinessCompanyResumeListCall extends BusinessListCallBack<DropBoxBean.DataBean.RecruitBean> {
    private CompanyResumeFragment fragment;

    public BusinessCompanyResumeListCall(CompanyResumeFragment fragment) {
        this.fragment = fragment;
    }



    @Override
    public void onError(int code) {
        DropBoxBean.DataBean.RecruitBean rctBean = null;
        if (!fragment.getAdapter().getList().isEmpty()) {
            rctBean = fragment.getAdapter().getList().get(0);
        }
        if (rctBean == null || rctBean.get_id().equals(ResumeListAdapter.EXCEPTION_NOT_DATA_ID)||rctBean.get_id().equals(ResumeListAdapter.EXCEPTION_NET_WORK_ID)||rctBean.get_id().equals(ResumeListAdapter.EXCEPTION_LOAD_ERROR_ID)) {
            List<DropBoxBean.DataBean.RecruitBean> list = new ArrayList<>();
            DropBoxBean.DataBean.RecruitBean bean;
            if (code == BusinessListCallBack.STATUS_LOAD_ERROR) {
                bean = new DropBoxBean.DataBean.RecruitBean(ResumeListAdapter.EXCEPTION_LOAD_ERROR_ID);

            } else {
                bean = new DropBoxBean.DataBean.RecruitBean(ResumeListAdapter.EXCEPTION_NET_WORK_ID);
            }
            list.add(bean);
            fragment.getAdapter().refresh(list);
        }
    }

    @Override
    public void onSuccess(List<DropBoxBean.DataBean.RecruitBean> list) {
        fragment.getAdapter().refresh(list);
    }

    @Override
    public void onNext(List<DropBoxBean.DataBean.RecruitBean> list) {
        fragment.getAdapter().addData(list);
    }

    @Override
    public void onOther(Object[] objects, boolean b) {

    }

    @Override
    public void onComplete() {
        fragment.dataComplete();
    }

    @Override
    public void onCache(List<DropBoxBean.DataBean.RecruitBean> list) {

    }
}
