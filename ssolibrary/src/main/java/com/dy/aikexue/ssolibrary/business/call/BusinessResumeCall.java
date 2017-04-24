package com.dy.aikexue.ssolibrary.business.call;

import com.dy.aikexue.ssolibrary.bean.DropBoxBean;
import com.dy.aikexue.ssolibrary.loginregister.fragment.ResumeCommonFragment;
import com.dy.aikexue.ssolibrary.loginregister.adapter.ResumeListAdapter;
import com.dy.sdk.business.call.BusinessListCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhong on 2017/2/17.
 * 找工作，邀请函，投递箱业务类
 */

public class BusinessResumeCall<T> extends BusinessListCallBack<T> {
    private int mType;
    private ResumeCommonFragment mFragment;
    private ResumeListAdapter mAdapter;

    public BusinessResumeCall(ResumeCommonFragment fragment, int pageCount, int type) {
        this.mType = type;
        setPageCount(pageCount);
        this.mFragment = fragment;
        mAdapter = new ResumeListAdapter(mFragment.getContext(), type);
        mFragment.recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onError(int code) {
        int count = mAdapter.getItemCount();
        String firstId = judgeErrorList();
        if (count == 0 || firstId.equals(ResumeListAdapter.EXCEPTION_LOAD_ERROR_ID)
                || firstId.equals(ResumeListAdapter.EXCEPTION_NET_WORK_ID)
                || firstId.equals(ResumeListAdapter.EXCEPTION_NOT_DATA_ID)) {
            List<T> mList = new ArrayList<>();
            Object bean = null;
            if (mType == ResumeCommonFragment.TYPE_DROP_BOX) {
                //投递箱
                if (code == BusinessListCallBack.STATUS_NET_WORK) {
                    bean = new DropBoxBean.DataBean.DeliverysBean(ResumeListAdapter.EXCEPTION_NET_WORK_ID);
                } else {
                    bean = new DropBoxBean.DataBean.DeliverysBean(ResumeListAdapter.EXCEPTION_LOAD_ERROR_ID);
                }
            } else if (mType == ResumeCommonFragment.TYPE_INVITATION) {
                //邀请函
                if (code == BusinessListCallBack.STATUS_NET_WORK) {
                    bean = new DropBoxBean.Invitations(ResumeListAdapter.EXCEPTION_NET_WORK_ID);
                } else {
                    bean = new DropBoxBean.Invitations(ResumeListAdapter.EXCEPTION_LOAD_ERROR_ID);
                }
            } else if (mType == ResumeCommonFragment.TYPE_JOB) {
                //找工作
                if (code == BusinessListCallBack.STATUS_NET_WORK) {
                    bean = new DropBoxBean.DataBean.RecruitBean(ResumeListAdapter.EXCEPTION_NET_WORK_ID);
                } else {
                    bean = new DropBoxBean.DataBean.RecruitBean(ResumeListAdapter.EXCEPTION_LOAD_ERROR_ID);
                }
            }
            mList.add((T) bean);
            mAdapter.refresh(mList);
        }
    }

    private String judgeErrorList() {
        try {
            if (mAdapter.getItemCount() != 0) {
                Object obj = mAdapter.getList().get(0);
                if (mType == ResumeCommonFragment.TYPE_INVITATION) {
                    DropBoxBean.Invitations bean = (DropBoxBean.Invitations) obj;
                    return bean.get_id() == null ? "" : bean.get_id();
                } else if (mType == ResumeCommonFragment.TYPE_DROP_BOX) {
                    DropBoxBean.DataBean.DeliverysBean bean = (DropBoxBean.DataBean.DeliverysBean) obj;
                    return bean.get_id() == null ? "" : bean.get_id();
                } else if (mType == ResumeCommonFragment.TYPE_JOB) {
                    DropBoxBean.DataBean.RecruitBean bean = (DropBoxBean.DataBean.RecruitBean) obj;
                    return bean.get_id() == null ? "" : bean.get_id();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    public void onSuccess(List<T> data) {
        data = onSuccessData(data);
        mAdapter.refresh(data);
        judgeNextPage(data);
    }

    public List<T> onSuccessData(List<T> list) {
        if (list == null) {
            list = new ArrayList<T>();
        }
        if (mType == ResumeCommonFragment.TYPE_DROP_BOX) {
            if (list.isEmpty()) {
                DropBoxBean.DataBean.DeliverysBean bean = new DropBoxBean.DataBean.DeliverysBean();
                bean.set_id(ResumeListAdapter.EXCEPTION_NOT_DATA_ID);
                list.add((T) bean);
            }
        } else if (mType == ResumeCommonFragment.TYPE_INVITATION) {
            if (list.isEmpty()) {
                DropBoxBean.Invitations invitations = new DropBoxBean.Invitations();
                invitations.set_id(ResumeListAdapter.EXCEPTION_NOT_DATA_ID);
                list.add((T) invitations);
            }
        } else if (mType == ResumeCommonFragment.TYPE_JOB) {
            if (list.isEmpty()) {
                DropBoxBean.DataBean.RecruitBean bean = new DropBoxBean.DataBean.RecruitBean();
                bean.set_id(ResumeListAdapter.EXCEPTION_NOT_DATA_ID);
                list.add((T) bean);
            }
        }
        return list;
    }

    @Override
    public void onNext(List<T> data) {
        mAdapter.add(data);
        judgeNextPage(data);
    }

    public void setEveryTotals(int allTotal,int rTotal,String inputName){
        mAdapter.setListTotal(allTotal,rTotal,inputName);
    }


    @Override
    public void onOther(Object[] args, boolean isCache) {
        mAdapter.addOtherData(args);
    }

    @Override
    public void onComplete() {
        mFragment.refreshLayout.setRefreshing(false);
        mFragment.refreshLayout.setLoading(false);
    }

    @Override
    public void onCache(List<T> data) {
        mAdapter.refresh(data);
    }

    /**
     * 判断还有无下一页
     *
     * @param data
     */
    private void judgeNextPage(List<T> data) {
        if (data.size() < getPageCount()) {
            mFragment.refreshLayout.setLoadEnable(false);
        } else {
            mFragment.refreshLayout.setLoadEnable(true);
        }
    }

}
