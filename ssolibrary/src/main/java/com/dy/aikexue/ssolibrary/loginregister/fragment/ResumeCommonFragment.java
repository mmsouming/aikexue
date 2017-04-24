package com.dy.aikexue.ssolibrary.loginregister.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.bean.BeanJobOrResume;
import com.dy.aikexue.ssolibrary.bean.DropBoxBean;
import com.dy.aikexue.ssolibrary.business.call.BusinessResumeCall;
import com.dy.aikexue.ssolibrary.business.impl.BusinessInvitation;
import com.dy.aikexue.ssolibrary.business.impl.BusinessResume;
import com.dy.aikexue.ssolibrary.business.impl.FindJobConfig;
import com.dy.sdk.business.itf.BusinessAction;
import com.dy.sdk.view.swiperefresh.PullToRefreshLayout;
import com.dy.sdk.view.swiperefresh.listener.SwipeListener;

/**
 * Created by zhong on 2017/2/17.
 * 邀请函，投递箱，找工作都会用到此页面
 * 用到此类的class
 * {@link com.dy.sso.activity.ResumeCommonListActivity}
 * {@link com.dy.sso.activity.FindJobActivity}
 */

public class ResumeCommonFragment extends Fragment implements SwipeListener.OnRefreshListener, SwipeListener.OnLoadListener {
    private View contentView;
    public PullToRefreshLayout refreshLayout;
    public RecyclerView recyclerView;

    public static final int TYPE_INVITATION = 1;  //邀请函
    public static final int TYPE_DROP_BOX = 2; //投递箱
    public static final int TYPE_JOB = 3;//找工作

    private static final String VALUE_TYPE = "type";
    private static final String VALUE_STATUS = "status";
    // 邀请函 状态筛选，waiting 邀请中，agree 同意，refuse 拒绝，overdue 已过期，success 达成意愿，fail 未达成意愿
    // 投递箱 状态筛选，waiting 待沟通，agree 面试中，refuse 已淘汰，overdue 已过期，success 达成意愿，fail 未达成意愿
    public static final String BRO_ACTION_WAITING = "waiting";
    public static final String BRO_ACTION_AGREE = "agree";
    public static final String BRO_ACTION_REFUSE = "refuse";
    public static final String BRO_ACTION_OVERDUE = "overdue";
    public static final String BRO_ACTION_SUCCESS = "success";
    public static final String BRO_ACTION_FAIL = "fail";
    private ResumeBro bro;
    private BusinessAction businessAction;

    public static ResumeCommonFragment newFragment(int type, String status) {
        Bundle bundle = new Bundle();
        bundle.putInt(VALUE_TYPE, type);
        bundle.putString(VALUE_STATUS, status);
        ResumeCommonFragment fragment = new ResumeCommonFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private int type;
    private String status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_resume_connom, null);
            initView();
            remoteData();
            init();
            register();
            refreshLayout.setRefreshing(true);
            Log.e("ResumeCommonFragment", "ResumeCommonFragment oncreateView");
        }
        return contentView;
    }

    private void register() {
        bro = new ResumeBro();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(bro, new IntentFilter(ResumeBro.BRO_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bro != null) {
            try {
                LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(bro);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void init() {
        switch (type) {
            case TYPE_DROP_BOX:
                //投递箱
                BusinessResume bAction = new BusinessResume(new BusinessResumeCall<DropBoxBean.DataBean.DeliverysBean>(this, 10, TYPE_DROP_BOX));
                bAction.setStatus(status);
                businessAction = bAction;
                break;
            case TYPE_INVITATION:
                //邀请函
                BusinessInvitation action = new BusinessInvitation<>(new BusinessResumeCall<DropBoxBean.Invitations>(this, 10, TYPE_INVITATION));
                action.setStatus(status);
                businessAction = action;
                break;
            case TYPE_JOB:
                //找工作
                setUpdate_JobList("", "", "", 1);
                break;
        }
    }

    FindJobConfig fAction;

    public void setUpdate_JobList(String ext, String keys, String inputKey, int sort) {
        if (fAction == null) {
            fAction = new FindJobConfig<>(new BusinessResumeCall<BeanJobOrResume>(this, 10, TYPE_JOB));
        }
        fAction.setUrlParams(ext, keys, inputKey, sort);
        businessAction = fAction;
        businessAction.refresh();
    }

    private void remoteData() {
        Bundle bundle = getArguments();
        status = bundle.getString(VALUE_STATUS);
        if (status == null) {
            status = "";
        }
        type = getArguments().getInt(VALUE_TYPE);
    }

    private void initView() {
        refreshLayout = (PullToRefreshLayout) contentView.findViewById(R.id.refreshLayout);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        refreshLayout.setLoadEnable(true);
        refreshLayout.setRefreshEnable(true);
        refreshLayout.setOnLoadListener(this);
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onRefresh() {
        businessAction.refresh();
    }

    @Override
    public void onLoad() {
        businessAction.next();
    }


    public void setRefreshEnable(boolean isEnable) {
        refreshLayout.setRefreshEnable(isEnable);
    }

    /**
     * 发送界面刷新广播
     *
     * @param context
     * @param fragmentType {@link ResumeCommonFragment#TYPE_DROP_BOX#TYPE_INVITATION#TYPE_JOB}
     * @param action       {@link ResumeCommonFragment#BRO_ACTION_AGREE#BRO_ACTION_FAIL#BRO_ACTION_OVERDUE#BRO_ACTION_REFUSE#BRO_ACTION_SUCCESS#BRO_ACTION_WAITING} 要刷新界面的状态
     * @return
     */
    public static boolean sendRefreshActionBro(Context context, int fragmentType, String action) {
        Intent intent = new Intent(ResumeBro.BRO_ACTION);
        intent.putExtra(ResumeBro.VALUE_TYPE, fragmentType);
        intent.putExtra(ResumeBro.VALUE_ACTION, action);
        return LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    class ResumeBro extends BroadcastReceiver {
        public static final String BRO_ACTION = "ResumeBro";
        public static final String VALUE_TYPE = "type";
        public static final String VALUE_ACTION = "action";

        @Override
        public void onReceive(Context context, Intent intent) {
            int fragmentType = intent.getIntExtra(VALUE_TYPE, -1);
            String action = intent.getStringExtra(VALUE_ACTION);
            if (fragmentType == -1 || action == null) return;
            if (type == fragmentType && status.equals(action)) {
                isAutoRefresh = true;
            }
        }
    }

    //是否要重新刷新，在界面重新被显示的时候被调用
    private boolean isAutoRefresh;

    @Override
    public void onResume() {
        super.onResume();
        if (isAutoRefresh) {
            onRefresh();
            isAutoRefresh = false;
        }

    }


}
