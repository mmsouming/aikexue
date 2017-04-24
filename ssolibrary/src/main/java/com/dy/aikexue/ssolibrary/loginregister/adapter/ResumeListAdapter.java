package com.dy.aikexue.ssolibrary.loginregister.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.bean.DropBoxBean;
import com.dy.aikexue.ssolibrary.bean.NewUserData;
import com.dy.aikexue.ssolibrary.loginregister.activit.JobDetailActivity;
import com.dy.aikexue.ssolibrary.loginregister.fragment.ResumeCommonFragment;
import com.dy.aikexue.ssolibrary.util.Dysso;
import com.dy.sdk.utils.ObjectValueUtil;
import com.dy.sdk.utils.ScreenUtil;
import com.dy.sdk.utils.ThemeUtil;

import org.cny.awf.view.ImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhong on 2017/2/17.
 * 投递箱，找工作，邀请函列表适配器
 * {@link ResumeCommonFragment,com.dy.sso.activity.FindJobActivity}
 */

public class ResumeListAdapter extends RecyclerView.Adapter<ResumeListAdapter.ResumeListViewHolder> {
    private List<Object> mList;
    private int mType;
    private Map<String, Object> mHash;
    private Context mContext;
    private int rTotal = -1;
    private String keyName;

    public ResumeListAdapter(Context context, int type) {
        this.mType = type;
        this.mContext = context;
        this.mList = new ArrayList<>();
        mHash = new HashMap<>();

    }

    public List<Object> getList() {
        return mList;
    }

    /**
     * 添加其他辅助数据
     *
     * @param object
     */
    public void addOtherData(Object... object) {
        if (object == null) return;
        for (Object obj : object) {
            if (obj instanceof Map) {
                Map m = (Map) obj;
                mHash.putAll(m);
            }
        }
    }

    /**
     * 设置列表总数和职位数
     *
     * @param allTotal 总数（职位和公司和）
     * @param rTotal   职位数
     */
    public void setListTotal(int allTotal, int rTotal, String keyName) {
        this.rTotal = rTotal;
        this.keyName = keyName;
    }


    /**
     * 加载更多
     *
     * @param listData
     */
    public void add(List listData) {
        if (listData == null) return;
        mList.addAll(listData);
        notifyDataSetChanged();
    }

    /**
     * 刷新列表
     *
     * @param listData
     */
    public void refresh(List listData) {
        if (listData == null) return;
        mList = listData;
        notifyDataSetChanged();
    }

    //无网络
    public static final String EXCEPTION_NET_WORK_ID = "[%netWork%]";
    //无数据
    public static final String EXCEPTION_NOT_DATA_ID = "[%notData%]";
    //加载数据失败
    public static final String EXCEPTION_LOAD_ERROR_ID = "[%loadError%]";

    @Override
    public ResumeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_resume_list_layout, null);
        ResumeListViewHolder vh = new ResumeListViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ResumeListViewHolder holder, int position) {
        Object obj = mList.get(position);
        initData(holder, obj, position);
    }

    /**
     * 设置数据显示
     *
     * @param holder
     */
    private void initData(ResumeListViewHolder holder, Object obj, int position) {
        holder.onClickItem.obj = obj;
        String title;
        String companyName;
        String photoUrl;
        String expe;
        String city;
        float minPay;
        float maxPay;
        String createTime;
        String statusStr;
        String createTimeYear;

        String rctId = null;
        String companyId = null;
        long time = 0;
        String statusCode = null;
        String _id = "";
        String reasons = "";
        switch (mType) {
            case ResumeCommonFragment.TYPE_DROP_BOX:
                DropBoxBean.DataBean.DeliverysBean bean = (DropBoxBean.DataBean.DeliverysBean) obj;
                _id = bean.get_id();
                rctId = bean.getRctId();
                companyId = bean.getRctOwnerId();
                time = bean.getCreateTime();
                statusCode = bean.getStatus();
                reasons = bean.getAttrs() != null && bean.getAttrs().getChange() != null ? bean.getAttrs().getChange().getReason() : "";
                break;
            case ResumeCommonFragment.TYPE_INVITATION:
                DropBoxBean.Invitations invitations = (DropBoxBean.Invitations) obj;

                _id = invitations.get_id();
                rctId = invitations.getRctId();
                companyId = invitations.getOid();
                time = invitations.getTime();
                statusCode = invitations.getStatus();
                break;
        }
        Object rctObj;
        if (mType != ResumeCommonFragment.TYPE_JOB) {
            rctObj = mHash.get(rctId);

        } else {
            rctObj = obj;
            _id = ((DropBoxBean.DataBean.RecruitBean) rctObj).get_id();
            companyId = ((DropBoxBean.DataBean.RecruitBean) rctObj).getOwnerId();
            time = ((DropBoxBean.DataBean.RecruitBean) rctObj).getCreateTime();
        }
        DropBoxBean.DataBean.RecruitBean recruitBean;
        if (rctObj != null) {
            recruitBean = (DropBoxBean.DataBean.RecruitBean) rctObj;
        } else {
            recruitBean = new DropBoxBean.DataBean.RecruitBean();
        }
        if (_id != null && _id.equals(EXCEPTION_NET_WORK_ID)) {
            holder.showErrorLayout(mContext.getString(R.string.net_error_hint));
        } else if (_id != null && _id.equals(EXCEPTION_NOT_DATA_ID)) {
            holder.showErrorLayout(mContext.getString(R.string.sso_DidNotFindTheRelevantData));
        } else if (_id != null && _id.equals(EXCEPTION_LOAD_ERROR_ID)) {
            holder.showErrorLayout(mContext.getString(R.string.loadDataError));
        } else {
            if (position == 0) {
                holder.topLine.setVisibility(View.VISIBLE);
            } else {
                holder.topLine.setVisibility(View.GONE);
            }
            if (mType != ResumeCommonFragment.TYPE_JOB) {
                holder.showBottomContentLayout();
            } else {
                holder.showContentLayout();
            }

            title = recruitBean.getTitle();
            companyName = getCompanyName(companyId);
            photoUrl = getPhotoUrl(companyId);
            expe = CompanyResumeListAdapter.getInfo(recruitBean);
            city = recruitBean.getCity();
            minPay = recruitBean.getMinPay();
            maxPay = recruitBean.getMaxPay();
            createTime = formatCreateTime(time);
            createTimeYear = formatCreateTimeYear(time);

            statusStr = getDropBoxStatusStr(statusCode);
            int statusColor = getDropBoxStatusColor(statusCode);
            holder.tvStatus.setTextColor(statusColor);
            setData(holder, title, companyName, photoUrl, expe, city, minPay, maxPay, createTime, createTimeYear, statusStr, position);
            if (statusCode != null && statusCode.equals(ResumeCommonFragment.BRO_ACTION_REFUSE) && mType == ResumeCommonFragment.TYPE_DROP_BOX && reasons != null && !reasons.isEmpty()) {
                holder.tvReason.setVisibility(View.VISIBLE);
                holder.tvReason.setText(mContext.getString(R.string.Reasons) + reasons);
            } else {
                holder.tvReason.setVisibility(View.GONE);
            }
        }

    }


    public void setData(ResumeListViewHolder holder, String title, String companyName, String photoUrl
            , String expe, String city, float minPay, float maxPay, String createTime
            , String createTimeYear, String statusStr, int position) {
        holder.tvJobName.setText(title == null ? "" : title);
        holder.tvCompanyName.setText(companyName == null ? "" : companyName);
        holder.imgPhoto.setUrl(photoUrl == null ? "" : photoUrl);
        holder.tvExperience.setText(expe == null ? "" : expe);
        holder.tvWage.setText(getWageStr(minPay, maxPay));
        holder.tvTimeTwo.setText(createTimeYear);
        holder.tvTimeOne.setText(createTime);
        holder.tvStatus.setText(statusStr);
        if (city != null && !city.isEmpty()) {
            if (companyName != null && !companyName.isEmpty()) {
                city = city + " | ";
            }
        }

        holder.tvCity.setText(city == null ? "" : city);

        String text = "";

        if (rTotal != 0 && rTotal == position) {
            if (keyName != null) {
                if (keyName.length() > 7) {
                    text = keyName.substring(0, 6) + "...";
                } else {
                    text = keyName;
                }
            }
            holder.lin_no_job_explain.setVisibility(View.VISIBLE);
            holder.tv_find_company.setText(text);
        } else {
            holder.lin_no_job_explain.setVisibility(View.GONE);
        }

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

    /**
     * 获取头像路径
     *
     * @param id 用户or企业id
     * @return
     */
    private String getPhotoUrl(String id) {
        Object obj = mHash.get(id);
        String photoUrl = "";
        try {
            NewUserData.Data.Usr usr = (NewUserData.Data.Usr) obj;
            if (usr != null && usr.getAttrs() != null && usr.getAttrs().getOrgInfo() != null && usr.getAttrs().getOrgInfo().getLogo() != null) {
                List<String> lists = usr.getAttrs().getOrgInfo().getLogo();
                if (!lists.isEmpty()) {
                    photoUrl = lists.get(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photoUrl == null ? "" : photoUrl;
    }

    /**
     * 获取公司名字
     *
     * @param id 公司id
     * @return
     */
    private String getCompanyName(String id) {
        Object obj = mHash.get(id);
        Object name = "";
        try {
            NewUserData.Data.Usr usr = (NewUserData.Data.Usr) obj;
            if (usr != null) {
                name = ObjectValueUtil.getInstance().getValueObject(usr, "attrs/orgInfo/name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name == null ? "" : (String) name;
    }

    /**
     * 获取对应状态的字体颜色
     *
     * @param status
     * @return
     */
    private int getDropBoxStatusColor(String status) {
        if (status == null) return ThemeUtil.getGreenThemeColor(mContext);
        if (status.equals(ResumeCommonFragment.BRO_ACTION_WAITING)) {
            return mContext.getResources().getColor(R.color.color_font_black_grey);
        } else if (status.equals(ResumeCommonFragment.BRO_ACTION_AGREE)) {
            return ThemeUtil.getGreenThemeColor(mContext);
        } else if (status.equals(ResumeCommonFragment.BRO_ACTION_REFUSE)) {
            return mContext.getResources().getColor(R.color.color_font_grey);
        } else if (status.equals(ResumeCommonFragment.BRO_ACTION_OVERDUE)) {
            return mContext.getResources().getColor(R.color.color_font_grey);
        } else if (status.equals(ResumeCommonFragment.BRO_ACTION_SUCCESS)) {
            return ThemeUtil.getGreenThemeColor(mContext);
        } else if (status.equals(ResumeCommonFragment.BRO_ACTION_FAIL)) {
            return mContext.getResources().getColor(R.color.color_font_grey);
        }
        return ThemeUtil.getGreenThemeColor(mContext);
    }

    /**
     * 获取状态字符串
     *
     * @param status
     * @return
     */
    private String getDropBoxStatusStr(String status) {
        //邀请函：状态筛选，waiting 邀请中，agree 同意，refuse 拒绝，overdue 已过期，success 达成意愿，fail 未达成意愿
        // 投递箱 ：状态筛选，waiting 待沟通，agree 面试中，refuse 已淘汰，overdue 已过期，success 达成意愿，fail 未达成意愿
        if (status == null) return "";

        if (status.equals(ResumeCommonFragment.BRO_ACTION_WAITING)) {
            return mType == ResumeCommonFragment.TYPE_DROP_BOX ? mContext.getString(R.string.Communication) : mContext.getString(R.string.InTheInvitation);
        } else if (status.equals(ResumeCommonFragment.BRO_ACTION_AGREE)) {
            return mType == ResumeCommonFragment.TYPE_DROP_BOX ? mContext.getString(R.string.Interview) : mContext.getString(R.string.favor);
        } else if (status.equals(ResumeCommonFragment.BRO_ACTION_REFUSE)) {
            return mType == ResumeCommonFragment.TYPE_DROP_BOX ? mContext.getString(R.string.Obsolete) : mContext.getString(R.string.Refuse);
        } else if (status.equals(ResumeCommonFragment.BRO_ACTION_OVERDUE)) {
            return mContext.getString(R.string.HasExpired);
        } else if (status.equals(ResumeCommonFragment.BRO_ACTION_SUCCESS)) {
            return mContext.getString(R.string.WillingnessToReach);
        } else if (status.equals(ResumeCommonFragment.BRO_ACTION_FAIL)) {
            return mContext.getString(R.string.WishesDidNotReach);
        }
        return "";
    }

    private SimpleDateFormat mDateFormat;
    private Date mDate;

    private String formatCreateTime(long time) {
        if (mDateFormat == null) {
            mDateFormat = new SimpleDateFormat("MM-dd");
            mDate = new Date();
        }
        mDate.setTime(time);
        return mDateFormat.format(time);
    }

    private SimpleDateFormat mDateFormatYear;
    private Date mDateYear;

    private String formatCreateTimeYear(long time) {
        if (mDateFormatYear == null) {
            mDateFormatYear = new SimpleDateFormat("yyyy-MM-dd");
            mDateYear = new Date();
        }
        mDateYear.setTime(time);
        return mDateFormatYear.format(time);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ResumeListViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgPhoto;
        public TextView tvJobName, tvExperience, tvCompanyName, tvCity, tvWage, tvTimeOne, tvTimeTwo, tvStatus, tvErrorStr, tvReason;
        public View errorLayout, contentLayout, bottomLayout, bottomLine, topLine;
        public CardView cardView;
        public LinearLayout lin_no_job_explain;
        public TextView tv_find_company;
        public OnClickItem onClickItem;

        public ResumeListViewHolder(View itemView) {
            super(itemView);
            imgPhoto = (ImageView) itemView.findViewById(R.id.imgPhoto);
            tvJobName = (TextView) itemView.findViewById(R.id.tvJobName);
            tvCompanyName = (TextView) itemView.findViewById(R.id.tvCompanyName);
            tvCity = (TextView) itemView.findViewById(R.id.tvCity);
            tvWage = (TextView) itemView.findViewById(R.id.tvWage);
            tvTimeOne = (TextView) itemView.findViewById(R.id.tvTimeOne);
            tvTimeTwo = (TextView) itemView.findViewById(R.id.tvTimeTwo);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            errorLayout = itemView.findViewById(R.id.layoutError);
            tvReason = (TextView) itemView.findViewById(R.id.tv_Reason);
            tvErrorStr = (TextView) errorLayout.findViewById(R.id.tvErrorStatus);
            contentLayout = itemView.findViewById(R.id.contentLayout);
            tvExperience = (TextView) itemView.findViewById(R.id.tvExperience);
            bottomLayout = itemView.findViewById(R.id.bottomLayout);
            bottomLine = itemView.findViewById(R.id.bottomLine);
            topLine = itemView.findViewById(R.id.topLine);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            lin_no_job_explain = (LinearLayout) itemView.findViewById(R.id.lin_no_job_explain);
            tv_find_company = (TextView) itemView.findViewById(R.id.tv_find_company_explain);
            errorLayout.getLayoutParams().height = ScreenUtil.getScreenHeight(mContext) - ScreenUtil.getStatusHeight(mContext) - ScreenUtil.dip2px(mContext, 100);
            onClickItem = new OnClickItem();
            cardView.setOnClickListener(onClickItem);
            cardView.setCardElevation(ScreenUtil.dip2px(mContext, 1));
        }

        void showErrorLayout(String statusStr) {
            errorLayout.setVisibility(View.VISIBLE);
            contentLayout.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.GONE);
            bottomLine.setVisibility(View.GONE);
            tvErrorStr.setText(statusStr);
            topLine.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
        }

        void showContentLayout() {
            errorLayout.setVisibility(View.GONE);
            contentLayout.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.GONE);
            tvTimeOne.setVisibility(View.VISIBLE);
            bottomLine.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
        }

        void showBottomContentLayout() {
            cardView.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
            contentLayout.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.VISIBLE);
            tvTimeOne.setVisibility(View.GONE);
            tvTimeTwo.setVisibility(View.VISIBLE);
            bottomLine.setVisibility(View.VISIBLE);
        }
    }

    class OnClickItem implements View.OnClickListener {

        Object obj;

        @Override
        public void onClick(View v) {
            if (obj == null) return;
            if (!Dysso.isSessionValid()) {
                Dysso.createInstance(mContext).login(mContext, null);
                return;
            }
            try {
                String _id = null;
                DropBoxBean.DataBean.RecruitBean rctBean = null;
                String invitationId = "";
                if (mType == ResumeCommonFragment.TYPE_DROP_BOX) {
                    //投递箱
                    DropBoxBean.DataBean.DeliverysBean bean = (DropBoxBean.DataBean.DeliverysBean) obj;
                    invitationId = bean.get_id();
                    _id = bean.getRctId();
                } else if (mType == ResumeCommonFragment.TYPE_INVITATION) {
                    //邀请函
                    DropBoxBean.Invitations invitations = (DropBoxBean.Invitations) obj;
                    invitationId = invitations.get_id();
                    _id = invitations.getRctId();
                } else if (mType == ResumeCommonFragment.TYPE_JOB) {
                    //找工作
                    rctBean = (DropBoxBean.DataBean.RecruitBean) obj;
                    _id = rctBean.get_id();
                }
                if (rctBean == null) {
                    Object rctObj = mHash.get(_id);
                    rctBean = (DropBoxBean.DataBean.RecruitBean) rctObj;
                }
                if (rctBean != null) {
                    Intent intent = JobDetailActivity.getStartIntent((Activity) mContext, _id, Dysso.getToken(), invitationId, mType);
                    mContext.startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

}
