package com.dy.aikexue.ssolibrary.loginregister.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.bean.EvaluateBean;
import com.dy.aikexue.ssolibrary.bean.ResumeInfo;
import com.dy.sdk.utils.ScreenUtil;
import com.dy.sdk.view.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhong on 2017/3/20.
 * 公司评价适配器
 * {@link com.dy.sso.fragment.CompanyEvaluateListFragment}
 */

public class CompanyEvaluateListAdapter extends RecyclerView.Adapter<CompanyEvaluateListAdapter.VH> {


    private Context mContext;
    private List<EvaluateBean.DataBean.AppsBean> mList;
    private Map<String, ResumeInfo> mUsr;

    public CompanyEvaluateListAdapter(List<EvaluateBean.DataBean.AppsBean> list, Context context) {
        this.mList = list;
        this.mContext = context;
        mUsr = new HashMap<>();
    }

    public List<EvaluateBean.DataBean.AppsBean> getList() {
        return mList;
    }

    /**
     * 刷新数据
     *
     * @param list
     */
    public void refresh(List<EvaluateBean.DataBean.AppsBean> list) {
        if (list == null) return;
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加下一页数据
     *
     * @param list
     */
    public void next(List<EvaluateBean.DataBean.AppsBean> list) {
        if (list == null) return;
        mList.addAll(list);
        notifyDataSetChanged();
    }

    private HashMap<String, EvaluateBean.DataBean.Recruit> mRecruit = new HashMap<>();

    /**
     * 添加辅助数据
     *
     * @param objs
     */
    public void addOther(Object[] objs) {
        if (objs == null) return;
        for (int i = 0; i < objs.length; i++) {
            Object obj = objs[i];
            try {
                if (obj != null) {
                    if (i == 0) {
                        HashMap<String, ResumeInfo> u = (HashMap<String, ResumeInfo>) obj;
                        mUsr.putAll(u);
                    } else {
                        HashMap<String, EvaluateBean.DataBean.Recruit> recruit = (HashMap<String, EvaluateBean.DataBean.Recruit>) obj;
                        mRecruit.putAll(recruit);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(mContext, R.layout.item_company_evaluate, null);
        VH vh = new VH(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        EvaluateBean.DataBean.AppsBean bean = mList.get(position);
        String _id = bean.getId();

//        if (_id != null && _id.equals(ResumeListAdapter.EXCEPTION_LOAD_ERROR_ID)) {
//            //加载失败
//            holder.showErrorLayout(mContext.getString(R.string.loadDataError));
//        } else if (_id != null && _id.equals(ResumeListAdapter.EXCEPTION_NOT_DATA_ID)) {
//            //空数据
//            holder.showErrorLayout(mContext.getString(R.string.sso_DidNotFindTheRelevantData));
//        } else if (_id != null && _id.equals(ResumeListAdapter.EXCEPTION_NET_WORK_ID)) {
//            //网络错误
//            holder.showErrorLayout(mContext.getString(R.string.netWordError));
//        } else {
//            setData(holder, bean);
//        }
    }

    private void setData(VH holder, EvaluateBean.DataBean.AppsBean bean) {
        holder.showContentLayout();
        String uid = bean.getUid();
        String name = "";
        String photoUrl = "";
        long createTime = 0L;
        String content = "";
        ResumeInfo u = mUsr.get(uid);
        if (u != null && u.getOwnerInfo() != null) {
            name = u.getOwnerInfo().getName();
            photoUrl = u.getOwnerInfo().getAvatar();
        }
        createTime = bean.getTime();
        content = bean.getFirstContent();
        holder.tvName.setText(name);
        holder.tvContent.setText(content);
        holder.tvTime.setText(formatTime(createTime));
        holder.imgPhoto.setUrl(photoUrl);
        holder.onClickJobName.recId = bean.getTarget();
        String jobName = getJobName(bean.getTarget());
        if (jobName == null || jobName.isEmpty()) {
            holder.tvJob.setVisibility(View.GONE);
        } else {
            holder.tvJob.setVisibility(View.VISIBLE);
            holder.tvJob.setText(jobName);
        }
    }

    class OnClickJobName implements View.OnClickListener {
        private String recId;

        @Override
        public void onClick(View v) {

//            mContext.startActivity(CompanyEvaluateActivity.getJumpIntent(mContext, recId));
        }
    }

    private String getJobName(String id) {
        String jobName = "";
        EvaluateBean.DataBean.Recruit bean = mRecruit.get(id);
        if (bean != null) {
            jobName = bean.getTitle();
        }
        return jobName;
    }

    private SimpleDateFormat mFormat;
    private Date mDate;

    /**
     * 格式化时间2017-11-11
     *
     * @param targetTime
     * @return
     */
    private String formatTime(long targetTime) {
        if (mFormat == null) {
            mFormat = new SimpleDateFormat("yyyy-MM-dd");
            mDate = new Date();
        }
        mDate.setTime(targetTime);
        return mFormat.format(mDate);
    }



    @Override
    public int getItemCount() {
        return mList.size();
    }

    class VH extends RecyclerView.ViewHolder {

        private View layoutContent, layoutError;
        private TextView tvName, tvJob, tvTime, tvContent, tvError;
        private CircleImageView imgPhoto;
        private OnClickJobName onClickJobName;

        public VH(View itemView) {
            super(itemView);
            layoutContent = itemView.findViewById(R.id.layoutContent);
            layoutError = itemView.findViewById(R.id.layoutError);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvJob = (TextView) itemView.findViewById(R.id.tvJob);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            tvError = (TextView) itemView.findViewById(R.id.tvErrorStatus);
            imgPhoto = (CircleImageView) itemView.findViewById(R.id.imgPhoto);
            layoutError.getLayoutParams().height = ScreenUtil.getScreenHeight(mContext) - ScreenUtil.dip2px(mContext, 130);
            layoutError.requestLayout();
            tvJob.setOnClickListener(onClickJobName = new OnClickJobName());
            layoutError.setBackgroundColor(Color.WHITE);
        }

        /**
         * 显示内容区域
         */
        public void showContentLayout() {
            layoutContent.setVisibility(View.VISIBLE);
            layoutError.setVisibility(View.GONE);
        }

        /**
         * 显示错误信息
         */
        public void showErrorLayout(String errorInfo) {
            layoutContent.setVisibility(View.GONE);
            layoutError.setVisibility(View.VISIBLE);
            tvError.setText(errorInfo);
        }
    }


}
