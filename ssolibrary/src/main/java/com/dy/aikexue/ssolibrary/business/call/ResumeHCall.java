package com.dy.aikexue.ssolibrary.business.call;

import android.text.TextUtils;

import com.dy.aikexue.ssolibrary.bean.BeanJobOrResume;
import com.dy.aikexue.ssolibrary.bean.DropBoxBean;
import com.dy.aikexue.ssolibrary.bean.NewUserData;
import com.dy.aikexue.ssolibrary.business.impl.FindJobConfig;
import com.dy.aikexue.ssolibrary.loginregister.fragment.ResumeCommonFragment;
import com.dy.aikexue.ssolibrary.util.Tools;
import com.dy.sdk.business.call.BusinessListCallBack;
import com.dy.sdk.business.itf.BusinessAction;
import com.dy.sdk.utils.GsonUtil;

import org.cny.awf.net.http.CBase;
import org.cny.awf.net.http.HCallback;
import org.cny.awf.net.http.HResp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhong on 2017/2/20.
 * 邀请函 找工作 投递箱 请求网络数据回调
 * {@link com.dy.sso.business.impl.BusinessInvitation,com.dy.sso.business.impl.BusinessResume,com.dy.sso.business.impl.FindJobConfig}
 */

public class ResumeHCall extends HCallback.HCacheCallback {

    private BusinessAction mBusinessAction;
    private int mType;

    public ResumeHCall(BusinessAction businessAction, int type, boolean isFirst) {
        this.mBusinessAction = businessAction;
        this.mType = type;
        this.isFirst = isFirst;
    }

    @Override
    public void onCache(CBase c, HResp res, String cache) throws Exception {
        super.onCache(c, res, cache);
        parseData(cache, true);
    }


    @Override
    public void onError(CBase cBase, String s, Throwable throwable) throws Exception {
        mBusinessAction.executeOnError(BusinessListCallBack.STATUS_NET_WORK);
        mBusinessAction.executeOnComplete();
    }

    @Override
    public void onSuccess(CBase cBase, HResp hResp, String s) throws Exception {
        parseData(s, false);
    }

    private void parseData(String s, boolean isCache) {
        if (TextUtils.isEmpty(s)) {
            if (isCache) return;
            mBusinessAction.executeOnError(BusinessListCallBack.STATUS_LOAD_ERROR);
        } else {
            try {
                List list = null;
                if (mType == ResumeCommonFragment.TYPE_JOB) {
                    int allTotal = 0;
                    int rTotal = 0;
                    BeanJobOrResume bean = GsonUtil.fromJson(s, BeanJobOrResume.class);
                    if (bean == null || bean.getCode() != 0) {
                        if (!isCache) {
                            mBusinessAction.executeOnError(BusinessListCallBack.STATUS_LOAD_ERROR);
                            return;
                        }
                    } else {
                        BeanJobOrResume.BeanJobOrResumeData data = bean.getData();
                        allTotal = data.getTotal();
                        rTotal = data.getrTotal();
                        list = data.getRecruit();
                        int pageCount = mBusinessAction.getDefaultPageCount();
                        if (Tools.isListNotNull(list)) {
                            if (list.size() < pageCount && data.getCompany() != null) {
                                list.addAll(data.getCompany());
                            }
                        } else {
                            list = data.getCompany();
                        }
                    }

                    HashMap<String, NewUserData.Data.Usr> usrs = null;
                    if (null != bean && null != bean.getData()) {
                        usrs = bean.getData().getOrg();
                    }
                    parseOtherData(isCache, usrs);
                    parseOtherData(allTotal, rTotal);
                } else {
                    DropBoxBean bean = GsonUtil.fromJson(s, DropBoxBean.class);
                    if (bean.getData() == null || bean.getCode() != 0) {
                        if (!isCache) {
                            mBusinessAction.executeOnError(BusinessListCallBack.STATUS_LOAD_ERROR);
                            return;
                        }
                    } else {
                        if (mType == ResumeCommonFragment.TYPE_DROP_BOX) {
                            list = bean.getData().getDeliverys();
                        } else if (mType == ResumeCommonFragment.TYPE_INVITATION) {
                            list = bean.getData().getInvitations();
                        }

                    }
                    //解析其他相关的数据
                    HashMap<String, DropBoxBean.DataBean.RecruitBean> value1 = bean.getData().getRecruit();
                    HashMap<String, NewUserData.Data.Usr> value3 = bean.getData().getUsr();
                    parseOtherData(isCache, value1, value3);
                }

                if (list == null) {
                    list = new ArrayList<>();
                }
                if (isCache) {
                    if (isFirst && mBusinessAction.getCurrentPage() == 1) {
                        mBusinessAction.executeOnCache(list);
                    }
                } else {
                    if (mBusinessAction.getCurrentPage() == 1) {
                        mBusinessAction.executeOnSuccess(list);
                        //第一页
                    } else {
                        mBusinessAction.executeOnNext(list);
                        //下一页
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (!isCache) {
                    mBusinessAction.executeOnError(BusinessListCallBack.STATUS_LOAD_ERROR);
                }
            }
        }
        if (isCache) return;
        mBusinessAction.executeOnComplete();

    }

    //是否是第一次请求数据
    private boolean isFirst;

    private void parseOtherData(boolean isCache, Object... objs) {
        mBusinessAction.executeOnOther(isCache, objs);
    }

    private void parseOtherData(DropBoxBean bean, boolean isCache) {
        HashMap<String, DropBoxBean.DataBean.RecruitBean> value1 = bean.getData().getRecruit();
        HashMap<String, NewUserData.Data.Usr> value3 = bean.getData().getUsr();
        mBusinessAction.executeOnOther(isCache, value1, value3);
    }

    private void parseOtherData(int allTotal, int rTotal) {
        if (mBusinessAction instanceof FindJobConfig) {
            FindJobConfig config = (FindJobConfig) mBusinessAction;
            config.setTotals(allTotal, rTotal);
        }
    }

}
