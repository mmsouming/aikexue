package com.dy.aikexue.ssolibrary.loginregister.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.bean.DropBoxBean;
import com.dy.aikexue.ssolibrary.loginregister.activit.JobDetailActivity;
import com.dy.aikexue.ssolibrary.loginregister.fragment.ResumeCommonFragment;
import com.dy.aikexue.ssolibrary.util.Dysso;
import com.dy.sdk.utils.ScreenUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhong on 2017/2/22.
 */

public class CompanyResumeListAdapter extends RecyclerView.Adapter<CompanyResumeListAdapter.CompanyResumeViewHolder> {

    private List<DropBoxBean.DataBean.RecruitBean> mList;
    private Context mContext;
    private Map<String, Object> mHash;

    public List<DropBoxBean.DataBean.RecruitBean> getList() {
        return mList;
    }

    public CompanyResumeListAdapter(Context context) {
        this.mContext = context;
        mList = new ArrayList<>();
        mHash = new HashMap<>();
    }

    public void refresh(List<DropBoxBean.DataBean.RecruitBean> list) {
        if (list == null || list.size() == 0) {
            if (list == null) {
                list = new ArrayList<>();
            }
            DropBoxBean.DataBean.RecruitBean bean = new DropBoxBean.DataBean.RecruitBean(ResumeListAdapter.EXCEPTION_NOT_DATA_ID);
            list.add(bean);
        }
        mList = list;
        notifyDataSetChanged();
    }

    public void addData(List<DropBoxBean.DataBean.RecruitBean> list) {
        if (list == null) return;
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addOther(Object obj) {
        if (obj == null) return;
        if (obj instanceof Map) {
            mHash.putAll((Map<? extends String, ?>) obj);
        }
    }

    @Override
    public CompanyResumeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_company_resume, null);
        CompanyResumeViewHolder vh = new CompanyResumeViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(CompanyResumeViewHolder holder, int position) {
        DropBoxBean.DataBean.RecruitBean bean = mList.get(position);
        holder.onClickItem.bean = null;
        if (bean.get_id().equals(ResumeListAdapter.EXCEPTION_LOAD_ERROR_ID)) {
            holder.showErrorView(mContext.getString(R.string.loadDataError));
            holder.tvErrorStatus.setText(mContext.getString(R.string.loadDataError));
        } else if (bean.get_id().equals(ResumeListAdapter.EXCEPTION_NET_WORK_ID)) {
            holder.showErrorView(mContext.getString(R.string.net_error_hint));
            holder.tvErrorStatus.setText(mContext.getString(R.string.net_error_hint));
        } else if (bean.get_id().equals(ResumeListAdapter.EXCEPTION_NOT_DATA_ID)) {
            holder.showErrorView(mContext.getString(R.string.sso_DidNotFindTheRelevantData));
            holder.tvErrorStatus.setText(mContext.getString(R.string.sso_DidNotFindTheRelevantData));
        } else {
            holder.onClickItem.bean = bean;
            holder.showContentView();
            String title = bean.getTitle();
            String city = bean.getCity();
            long createTime = bean.getCreateTime();
            String expe = bean.getExpe();
            holder.tvJobName.setText(title == null ? "" : title);
            holder.tvCity.setText(city == null || city.equals("") ? "" : "[" + city + "]");
            holder.tvTime.setText(formatTime(createTime));
            holder.tvExperience.setText(getInfo(bean));
            holder.tvWage.setText(getWageStr(bean.getMinPay(), bean.getMaxPay()));
        }
    }

    /**
     * 获取 经验，学历，就业类型的字符串
     *
     * @param bean
     * @return
     */
    public static String getInfo(DropBoxBean.DataBean.RecruitBean bean) {
        String expe = "";
        String requireEdu = "";
        List<String> properties;
        String property = "";

        String s = "";
        if (bean != null) {
            expe = bean.getExpe();
            requireEdu = bean.getRequireEdu();
            properties = bean.getProperty();
            if (properties != null && !properties.isEmpty()) {
                property = properties.get(0);
            }
        }

        if (expe != null && !expe.isEmpty()) {
            s += expe;
        }
        if (requireEdu != null && !requireEdu.isEmpty()) {
            if (expe != null && !expe.isEmpty()) {
                s += " / ";
            }
            s += requireEdu;
        }

        if (property != null && !property.isEmpty()) {
            if (expe != null && !expe.isEmpty() || requireEdu != null && !requireEdu.isEmpty()) {
                s += " / ";
            }
            s += property;
        }
        return s;
    }

    /**
     * 格式化工资 1K-3K
     *
     * @param minPay
     * @param maxPay
     * @return
     */
    private String getWageStr(float minPay, float maxPay) {
        return (int) minPay + "K" + "-" + (int) maxPay + "K";
    }

    private SimpleDateFormat timeFormat;
    private Date mDate;

    private String formatTime(long time) {
        if (timeFormat == null) {
            timeFormat = new SimpleDateFormat("yyyy-MM-dd");
            mDate = new Date();
        }
        mDate.setTime(time);
        return timeFormat.format(mDate);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class CompanyResumeViewHolder extends RecyclerView.ViewHolder {
        private TextView tvJobName, tvWage, tvExperience, tvTime, tvCity, tvErrorStatus;
        private View statusView, contentView, line;
        private OnClickItem onClickItem;

        public CompanyResumeViewHolder(View itemView) {
            super(itemView);
            tvJobName = (TextView) itemView.findViewById(R.id.tvJobName);
            tvWage = (TextView) itemView.findViewById(R.id.tvWage);
            tvExperience = (TextView) itemView.findViewById(R.id.tvExperience);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvCity = (TextView) itemView.findViewById(R.id.tvCity);
            statusView = itemView.findViewById(R.id.statusView);
            contentView = itemView.findViewById(R.id.contentView);
            line = itemView.findViewById(R.id.line);
            tvErrorStatus = (TextView) itemView.findViewById(R.id.tvErrorStatus);
            statusView.getLayoutParams().height = ScreenUtil.getScreenHeight(mContext) - ScreenUtil.dip2px(mContext, 130);
            statusView.requestLayout();
            statusView.setBackgroundColor(Color.WHITE);
            onClickItem = new OnClickItem();
            itemView.setOnClickListener(onClickItem);
        }

        void showContentView() {
            statusView.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
        }

        void showErrorView(String statusStr) {
            statusView.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
    }

    class OnClickItem implements View.OnClickListener {
        DropBoxBean.DataBean.RecruitBean bean;


        @Override
        public void onClick(View v) {
            if (bean == null) return;
            Intent intent = JobDetailActivity.getStartIntent((Activity) mContext, bean.get_id(), Dysso.getToken(), null, ResumeCommonFragment.TYPE_JOB);
            mContext.startActivity(intent);
        }
    }
}
