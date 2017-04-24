package com.dy.aikexue.ssolibrary.loginregister.activit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.base.BaseActivity;
import com.dy.aikexue.ssolibrary.bean.PostProgressBean;
import com.dy.aikexue.ssolibrary.business.call.BusinessPostProgressCall;
import com.dy.aikexue.ssolibrary.business.impl.BusinessPostProgress;
import com.dy.aikexue.ssolibrary.loginregister.adapter.PostProgressAdapter;
import com.dy.aikexue.ssolibrary.loginregister.fragment.ResumeCommonFragment;
import com.dy.aikexue.ssolibrary.util.Dysso;
import com.dy.sdk.business.call.BusinessListCallBack;
import com.dy.sdk.utils.ObjectValueUtil;
import com.dy.sdk.utils.ThemeUtil;
import com.dy.sdk.view.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @auth zhongq
 * 投递进度页面
 */

public class PostProgressActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private ImageView mImgBack;
    private TextView mTvTitle, mTvSave, mTvErrorStatus;
    private LoadingDialog mLoadingDialog;
    private View mErrorLayout;
    private BusinessPostProgress mBusiness;
    private ObjectValueUtil mValueUtil;
    private PostProgressAdapter mAdapter;
    private View mTitleView;


    private static final String VALUE_DDID = "ddid";
    private static final String VALUE_TYPE = "type";
    private static final String VALUE_LIST = "list";

    public static Intent getJumpIntent(Context context, String ddid, int type) {
        return getJumpIntent(context, ddid, type, null);
    }

    public static Intent getJumpIntent(Context context, String ddid, int type, ArrayList<PostProgressBean.DataBean.DeliveryBean.HistoryBean> list) {
        Intent intent = new Intent(context, PostProgressActivity.class);
        intent.putExtra(VALUE_DDID, ddid);
        intent.putExtra(VALUE_TYPE, type);
        if (list != null) {
            intent.putExtra(VALUE_LIST, list);
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Dysso.isSessionValid()) {
            Dysso.createInstance(this).login(this, null);
            finish();
            return;
        }
        setContentView(R.layout.activity_post_progress);
        remoteData();
        initView();
        init();
        initListener();
        loadData();
    }

    private String DDID;
    private int mType;
    private List<PostProgressBean.DataBean.DeliveryBean.HistoryBean> mList;

    private void remoteData() {
        DDID = getIntent().getStringExtra(VALUE_DDID);
        mType = getIntent().getIntExtra(VALUE_TYPE, ResumeCommonFragment.TYPE_INVITATION);
        mList = (List<PostProgressBean.DataBean.DeliveryBean.HistoryBean>) getIntent().getSerializableExtra(VALUE_LIST);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        mImgBack.setOnClickListener(this);
        mErrorLayout.setOnClickListener(this);
    }

    /**
     * 初始化view
     */
    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mImgBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvSave = (TextView) findViewById(R.id.tv_save);
        mTvErrorStatus = (TextView) findViewById(R.id.tvErrorStatus);
        mErrorLayout = findViewById(R.id.errorLayout);
        mTitleView = findViewById(R.id.title_view);
    }

    private void init() {
        mBusiness = new BusinessPostProgress(new BusinessPostProgressCall(this));
        mTvSave.setVisibility(View.GONE);
        mTvTitle.setText(getString(R.string.jobDes));
        mValueUtil = ObjectValueUtil.getInstance();
        mAdapter = new PostProgressAdapter(this, mType);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mBusiness.setDDID(DDID);
        mTitleView.setBackgroundColor(ThemeUtil.getThemeColor(this));
    }


    /**
     * 显示loading窗口
     */
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this, getString(R.string.loadingWait));
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    /**
     * 隐藏loading窗口
     */
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public void showContent() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorLayout.setVisibility(View.GONE);
    }

    public void showError(int code) {
        mRecyclerView.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.VISIBLE);
        if (code == BusinessListCallBack.STATUS_LOAD_ERROR) {
            //加载数据失败
            mTvErrorStatus.setText(getString(R.string.loadDataError));
        } else if (code == BusinessListCallBack.STATUS_NET_WORK) {
            //网络错误
            mTvErrorStatus.setText(getString(R.string.netWordError));
        }
    }

    /**
     * 加载数据
     */
    public void loadData() {
        if (mList != null) {
            mAdapter.refresh(mList);
        } else {
            showLoading();
            mBusiness.execute();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.errorLayout) {
            loadData();
        }
    }

    public void refresh(PostProgressBean bean) {
        showContent();
        Object obj = mValueUtil.getValueObject(bean, "data/delivery/history");
        if (obj == null) return;
        List<PostProgressBean.DataBean.DeliveryBean.HistoryBean> list = (List<PostProgressBean.DataBean.DeliveryBean.HistoryBean>) obj;
        mAdapter.refresh(list);
    }

}
