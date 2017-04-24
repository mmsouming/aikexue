package com.dy.aikexue.ssolibrary.loginregister.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dy.aikexue.ssolibrary.bean.PostProgressBean.DataBean.DeliveryBean.HistoryBean;
import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.loginregister.fragment.ResumeCommonFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhong on 2017/3/28.
 */

public class PostProgressAdapter extends RecyclerView.Adapter<PostProgressAdapter.VH> {

    private List<HistoryBean> mList;
    private Context mContext;
    private int mType;

    public PostProgressAdapter(Context context, int mType) {
        this.mList = new ArrayList<>();
        this.mType = mType;
        mContext = context;
    }

    public void refresh(List<HistoryBean> list) {
        if (list == null) return;
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = View.inflate(mContext, R.layout.item_post_progress, null);
        VH vh = new VH(contentView);
        return vh;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        HistoryBean bean = mList.get(position);
        long time = bean.getTime();
        String remark = bean.getRemark();
        String status = bean.getStatus();
        if (TextUtils.isEmpty(remark)) {
            holder.tvContent.setVisibility(View.GONE);
        } else {
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.tvContent.setText(remark);
        }
        if (position == 0) {
            holder.cirGray.setVisibility(View.INVISIBLE);
            holder.cirGreen.setVisibility(View.VISIBLE);
            holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_green_topBar));
            holder.tvStatus.setTextSize(18);
            holder.tvStatus.getPaint().setFakeBoldText(true);
        } else {
            holder.cirGray.setVisibility(View.VISIBLE);
            holder.cirGreen.setVisibility(View.INVISIBLE);
            holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_font_grey));
            holder.tvStatus.setTextSize(16);
            holder.tvStatus.getPaint().setFakeBoldText(false);
        }
        if (position == mList.size() - 1) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
        holder.tvStatus.setText(getStatusText(status));
        holder.tvTime.setText(formatTime(time));

    }

    private SimpleDateFormat mFormat;
    private Date mDate;

    private String formatTime(long time) {
        if (mFormat == null) {
            mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            mDate = new Date();
        }
        mDate.setTime(time);
        return mFormat.format(mDate);
    }

    /**
     * 获取状态字符串
     *
     * @param status
     * @return
     */
    private String getStatusText(String status) {
        if (status == null) return "";
//        邀请(type: invite): waiting 邀请中，agree 已同意，refuse 已拒绝，overdue 已过期，success 已达成意愿，fail 未达成意愿
//        投递(type: delivery): waiting 待沟通，agree 面试中，refuse 已淘汰，overdue 已过期，success 已达成意愿，fail 未达成意愿
        if (status.equals("waiting")) {
            return mType == ResumeCommonFragment.TYPE_DROP_BOX ? "待沟通" : "邀请中";
        } else if (status.equals("agree")) {
            return mType == ResumeCommonFragment.TYPE_DROP_BOX ? "面试中" : "已同意";
        } else if (status.equals("refuse")) {
            return mType == ResumeCommonFragment.TYPE_DROP_BOX ? "已淘汰" : "已拒绝";
        } else if (status.equals("success")) {
            return "已达成意愿";
        } else if (status.equals("fail")) {
            return "未达成意愿";
        } else if (status.equals("overdue")) {
            return "已过期";
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class VH extends RecyclerView.ViewHolder {
        private View cirGray, cirGreen, line;
        private TextView tvStatus, tvTime, tvContent;

        public VH(View itemView) {
            super(itemView);
            line = itemView.findViewById(R.id.line);
            cirGray = itemView.findViewById(R.id.cirGray);
            cirGreen = itemView.findViewById(R.id.cirGreen);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
        }
    }
}
