package com.dy.aikexue.ssolibrary.loginregister.activit;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.bean.PostProgressBean;
import com.dy.aikexue.ssolibrary.config.Config;
import com.dy.aikexue.ssolibrary.loginregister.fragment.ResumeCommonFragment;
import com.dy.aikexue.ssolibrary.util.Dysso;
import com.dy.aikexue.ssolibrary.util.Tools;
import com.dy.aikexue.ssolibrary.view.dialog.ReasonDialog;
import com.dy.sdk.activity.WebViewActivity;
import com.dy.sdk.listener.OnDialogButtonClickListener;
import com.dy.sdk.utils.CToastUtil;
import com.dy.sdk.utils.GsonUtil;
import com.dy.sdk.view.dialog.CommonDialog;
import com.dy.sdk.view.dialog.LoadingDialog;
import com.google.gson.reflect.TypeToken;

import org.cny.awf.net.http.CBase;
import org.cny.awf.net.http.H;
import org.cny.awf.net.http.HCallback;
import org.cny.awf.net.http.HResp;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 职位详情界面，native——html 界面
 */
public class JobDetailActivity extends WebViewActivity implements ReasonDialog.OnReasonListener {

    private String recruitId;
    private String token;
    private String inviteId;
    private int type;
    private LoadingDialog loadingDialog;
    private ReasonDialog reasonDialog;
    private CommonDialog dialog;
    private MyReceiver receiver;

    protected final String OPERATE_AGREE = "agree";
    protected final String OPERATE_REFUSE = "refuse";
    protected final String OPERATE_WAITING = "waiting";

    public static Intent getStartIntent(Activity activity, String jobId, String token, String inviteId, int type) {
        Intent intent = new Intent(activity, JobDetailActivity.class);
        intent.putExtra("title", "职位详情");
        intent.putExtra("url", Config.getJobDetailUrl(jobId, token, inviteId == null ? "" : inviteId, type));
        intent.putExtra("recruitId", jobId);
        intent.putExtra("token", token);
        intent.putExtra("inviteId", inviteId);
        intent.putExtra("type", type);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extraIntent();
        mWebView.setWebViewClient(new MClient());
        bindJavaScript(new JobJSInterface());
        initLoading();
        initListener();
        loadingDialog.show();
    }

    private void initListener() {
        reasonDialog.setOnReasonListener(this);

        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(InterviewAppraiseActivity.BROADCAST_APPRAISE);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    private void extraIntent() {
        recruitId = getIntent().getStringExtra("recruitId");
        token = getIntent().getStringExtra("token");
        inviteId = getIntent().getStringExtra("inviteId");
        type = getIntent().getIntExtra("type", 1);
    }

    @Override
    public void onCommit(String contentStr) {
        //拒绝提交
        refuseReason = contentStr == null ? "" : contentStr;
        String url = Config.getOperateInviteUrl(inviteId, OPERATE_REFUSE, refuseReason, Dysso.getToken());
        operateResume(OPERATE_REFUSE, url);
        reasonDialog.dismiss();
    }

    @Override
    public void onNot() {
        refuseReason = "";
        //拒绝暂不添加理由 提交
        String url = Config.getOperateInviteUrl(inviteId, OPERATE_REFUSE, refuseReason, Dysso.getToken());
        operateResume(OPERATE_REFUSE, url);
        reasonDialog.dismiss();
    }

    class JobJSInterface {
        final Handler mHandler = new Handler();

        @JavascriptInterface
        @org.xwalk.core.JavascriptInterface
        public void addYourResume(final String object) {
            mHandler.post(new Runnable() {
                public void run() {
                    //需要完善简历
                    editResume();
                }
            });
        }

        @JavascriptInterface
        @org.xwalk.core.JavascriptInterface
        public void submitResume(final String object) {
            mHandler.post(new Runnable() {
                public void run() {
                    //投递简历
                    String url = Config.getSendResumeToCompanyUrl(recruitId, token);
                    submitResumeToComp(url);
                }
            });
        }

        @JavascriptInterface
        @org.xwalk.core.JavascriptInterface
        public void reSubmitResume(final String obj) {
            mHandler.post(new Runnable() {
                public void run() {
                    //重新投递简历
                    String url = Config.getOperateDeliverUrl(inviteId, OPERATE_WAITING, "", token);
                    submitResumeToComp(url);
                }
            });
        }

        @JavascriptInterface
        @org.xwalk.core.JavascriptInterface
        public void jumpToCompanyPage(final String companyId) {
            mHandler.post(new Runnable() {
                public void run() {
                    //跳转公司首页
                    Intent intent = CompanyDetailsActivity.getStartIntent(JobDetailActivity.this, companyId);
                    startActivity(intent);
                }
            });
        }

        @JavascriptInterface
        @org.xwalk.core.JavascriptInterface
        public void refuseInvitation(final String object) {
            mHandler.post(new Runnable() {
                public void run() {
                    //拒绝邀请
                    reasonDialog.show();

                }
            });
        }

        @JavascriptInterface
        @org.xwalk.core.JavascriptInterface
        public void agreeInvitation(final String object) {
            mHandler.post(new Runnable() {
                public void run() {
                    //同意邀请
                    String url = Config.getOperateInviteUrl(inviteId, OPERATE_AGREE, refuseReason, Dysso.getToken());
                    operateResume(OPERATE_AGREE, url);
                }
            });
        }

        @JavascriptInterface
        @org.xwalk.core.JavascriptInterface
        public void publishComment(final String object) {
            mHandler.post(new Runnable() {
                public void run() {
                    //发表评价
                    Intent intent = InterviewAppraiseActivity.getStartIntent(JobDetailActivity.this, recruitId);
                    startActivity(intent);
                }
            });
        }

        @JavascriptInterface
        @org.xwalk.core.JavascriptInterface
        public void showHistory(final String beanStr) {
            mHandler.post(new Runnable() {
                public void run() {
                    //查看投递历史
                    try {
                        JSONObject jsonObject = new JSONObject(beanStr);
                        String listStr = jsonObject.optString("list");
                        String strType = jsonObject.optString("type");
                        int type;
                        if ("delivery".equals(strType)) {
                            type = ResumeCommonFragment.TYPE_DROP_BOX;
                        } else if ("invitation".equals(strType)) {
                            type = ResumeCommonFragment.TYPE_INVITATION;
                        } else {
                            type = ResumeCommonFragment.TYPE_JOB;
                        }
                        ArrayList<PostProgressBean.DataBean.DeliveryBean.HistoryBean> list =
                                GsonUtil.fromJson(listStr, new TypeToken<ArrayList<PostProgressBean.DataBean.DeliveryBean.HistoryBean>>() {
                                }.getType());
                        if (Tools.isListNotNull(list)) {
                            Intent intent = PostProgressActivity.getJumpIntent(JobDetailActivity.this, inviteId, type, list);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @JavascriptInterface
        @org.xwalk.core.JavascriptInterface
        public void showMoreComment(final String object) {
            mHandler.post(new Runnable() {
                public void run() {
                    //查看更多评价
                    Intent intent = CompanyEvaluateActivity.getJumpIntent(JobDetailActivity.this, recruitId);
                    startActivity(intent);
                }
            });
        }

        @JavascriptInterface
        @org.xwalk.core.JavascriptInterface
        public void showDeliveryExplanation(final String ruleBean) {
            mHandler.post(new Runnable() {
                public void run() {
                    //查看投递规则
                    try {
                        JSONObject jsonObject = new JSONObject(ruleBean);
                        String rule = jsonObject.optString("tip");
                        String regExp = "(?<=\\{)[^{}]+(?=\\})";

                        List<String> list = new ArrayList<>();
                        Pattern p = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);
                        Matcher matcher = p.matcher(rule);

                        while (matcher.find()) {
                            list.add(matcher.group());
                        }
                        SpannableString ss = matchRule(list, rule.replace("{{", "").replace("}}", ""));
                        showSubmitRule(ss);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private SpannableString matchRule(List<String> list, String src) {
        SpannableString span = new SpannableString(src);
        for (int i = 0; i < list.size(); i++) {
            int matchLength = list.get(i).length();
            int head = src.indexOf(list.get(i));
            span.setSpan(new ForegroundColorSpan(Tools.getResColor(H.CTX, R.color.color_green_topBar)), head, head + matchLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return span;
    }

    private void showSubmitRule(CharSequence rule) {
        CommonDialog.OneButtonBuilder builder = new CommonDialog.OneButtonBuilder(this);
        CommonDialog ruleDialog = builder.setTitle("投递规则")
                .setContentText(rule)
                .setCenterButtonText("知道了")
                .build();
        ruleDialog.show();
    }

    private void editResume() {
        CommonDialog.TwoButtonBuilder builder = new CommonDialog.TwoButtonBuilder(this);
        dialog = builder.setTitle("提示").setContentText("你的简历信息还未完善哦！").setLeftButtonText("暂不处理").setRightButtonText("前往完善")
                .setLeftClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public void onDialogButtonClick(View view) {
                        dialog.dismiss();
                    }
                })
                .setRightClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public void onDialogButtonClick(View view) {
//                        Intent intent = ResumeActivity.getStartMineIntent(JobDetailActivity.this, Dysso.getToken(), "我的简历", true);
//                        startActivity(intent);
                    }
                }).build();
        dialog.show();
    }

    /**
     * 投递简历
     */
    private void submitResumeToComp(String url) {
        initLoading();
        loadingDialog.show();
        H.doGet(url, new HCallback.HCacheCallback() {
            @Override
            public void onError(CBase cBase, String s, Throwable throwable) throws Exception {
                dismisslLoading();
                CToastUtil.toastShort(H.CTX, "投递简历失败");
            }

            @Override
            public void onSuccess(CBase cBase, HResp hResp, String s) throws Exception {
                dismisslLoading();
                if (!Tools.isStringNull(s)) {
                    JSONObject jsonObject = new JSONObject(s);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        doJSMethod("update_delivery_waiting");
                    } else {
                        String msg = jsonObject.optString("msg");
                        CToastUtil.toastShort(H.CTX, Tools.isStringNull(msg) ? "投递简历失败" : msg);
                    }
                } else {
                    CToastUtil.toastShort(H.CTX, "投递简历失败");
                }
            }
        });
    }

    //拒绝理由
    private String refuseReason = "";

    /**
     * 操作简历
     *
     * @param status
     */
    private void operateResume(final String status, String url) {
        initLoading();
        loadingDialog.show();
        H.doGet(url, new HCallback.HCacheCallback() {
            @Override
            public void onError(CBase cBase, String s, Throwable throwable) throws Exception {
                dismisslLoading();
                CToastUtil.toastShort(H.CTX, "操作失败");
            }

            @Override
            public void onSuccess(CBase cBase, HResp hResp, String s) throws Exception {
                dismisslLoading();
                if (!Tools.isStringNull(s)) {
                    JSONObject jsonObject = new JSONObject(s);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        if (status.equals(OPERATE_AGREE)) {
                            doJSMethod("update_invitation_agree");
                            ResumeCommonFragment.sendRefreshActionBro(JobDetailActivity.this, ResumeCommonFragment.TYPE_INVITATION, ResumeCommonFragment.BRO_ACTION_WAITING);
                            ResumeCommonFragment.sendRefreshActionBro(JobDetailActivity.this, ResumeCommonFragment.TYPE_INVITATION, ResumeCommonFragment.BRO_ACTION_AGREE);
                        } else if (status.equals(OPERATE_REFUSE)) {
                            doJSMethod("update_invitation_refuse");
                            ResumeCommonFragment.sendRefreshActionBro(JobDetailActivity.this, ResumeCommonFragment.TYPE_INVITATION, ResumeCommonFragment.BRO_ACTION_WAITING);
                            ResumeCommonFragment.sendRefreshActionBro(JobDetailActivity.this, ResumeCommonFragment.TYPE_INVITATION, ResumeCommonFragment.BRO_ACTION_REFUSE);
                        }
                    } else {
                        String msg = jsonObject.optString("msg");
                        CToastUtil.toastShort(H.CTX, Tools.isStringNull(msg) ? "操作失败" : msg);
                    }
                } else {
                    CToastUtil.toastShort(H.CTX, "操作失败");
                }
            }
        });
    }

    private void doJSMethod(String param) {
        mWebView.load("javascript:jsBridge(\"" + param + "\")");
    }

    private void initLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this, "数据加载中，请稍后...");
            reasonDialog = new ReasonDialog(this);
        }
    }

    private void dismisslLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public class MClient extends WebViewClient {
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            int height = Tools.dip2px(H.CTX, 50);
            mWebView.load("javascript:getPhoneHeight(" + height + ")");
            dismisslLoading();
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String linkUrl) {
            return true;
        }
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (InterviewAppraiseActivity.BROADCAST_APPRAISE.equals(intent.getAction())) {
                //发布面试评价成功
                doJSMethod("update_appraise_completed");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismisslLoading();
        loadingDialog = null;
        if (receiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        }
    }

}
