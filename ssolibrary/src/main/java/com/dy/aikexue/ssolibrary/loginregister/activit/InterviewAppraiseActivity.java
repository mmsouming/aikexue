package com.dy.aikexue.ssolibrary.loginregister.activit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.base.BaseActivity;
import com.dy.aikexue.ssolibrary.config.Config;
import com.dy.aikexue.ssolibrary.util.Dysso;
import com.dy.aikexue.ssolibrary.util.Tools;
import com.dy.sdk.bean.SendAppraiseEntity;
import com.dy.sdk.bean.SendAppraiseReplyBean;
import com.dy.sdk.utils.CToastUtil;
import com.dy.sdk.utils.GsonUtil;

import org.cny.awf.net.http.CBase;
import org.cny.awf.net.http.H;
import org.cny.awf.net.http.HCallback;
import org.cny.awf.net.http.HResp;

import java.util.ArrayList;
import java.util.List;

/**
 * 职位评价
 */
public class InterviewAppraiseActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private EditText et_appraise;
    private TextView tv_cur_word;

    private String str_top_right;

    private String str_jobId;
    private String str_top_title;

    private MyCallback callback;

    protected final String TYPE_RECRUIT = "recruit";
    public static final String BROADCAST_APPRAISE = "sendAppraiseBroadcast";
    public static final String BORADCAST_EXTRA_KEY = "appraise_key";

    public static Intent getStartIntent(Activity activity, String jobId) {
        return getStartIntent(activity, jobId, "职位评价", "发表");
    }

    protected static Intent getStartIntent(Activity activity, String jobId, String title, String titleRight) {
        Intent intent = new Intent(activity, InterviewAppraiseActivity.class);
        intent.putExtra("JobId", jobId);
        intent.putExtra("TopTitle", title);
        intent.putExtra("TopRight", titleRight);
        return intent;
    }

    private void extraIntent() {
        str_jobId = getIntent().getStringExtra("JobId");
        str_top_title = getIntent().getStringExtra("TopTitle");
        str_top_right = getIntent().getStringExtra("TopRight");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_appraise);
        extraIntent();
        initTitleBar();
        initViews();
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) this.findViewById(R.id.tv_title_text);
        tvTitle.setText(this.str_top_title == null ? "" : this.str_top_title);
        TextView tv_look = (TextView) findViewById(R.id.tv_title_right);
        tv_look.setText(str_top_right);
        tv_look.setVisibility(View.VISIBLE);
        findViewById(R.id.img_top_right).setVisibility(View.GONE);
        tv_look.setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);
    }

    private void initViews() {
        et_appraise = (EditText) findViewById(R.id.et_interview_appraise);
        tv_cur_word = (TextView) findViewById(R.id.tv_cur_word);

        et_appraise.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_title_right) {
            doClickTopRight();
        } else if (id == R.id.img_back) {
            finish();
        }
    }

    protected void doClickTopRight() {
        String str_et_value = et_appraise.getText().toString().trim();
        if (Tools.isStringNull(str_et_value)) {
            CToastUtil.toastShort(H.CTX, Tools.getResString(H.CTX, R.string.toast_null_appraise));
        } else {
            sendAppraise(str_et_value);
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String length = s.length() + "";
        tv_cur_word.setText(length);
    }

    private void sendAppraise(String value) {
        initLoading("发表中，请稍后...");
        String url = Config.getInterviewAppraiseUrl(Dysso.getToken());
        SendAppraiseEntity entity = new SendAppraiseEntity();
        com.dy.sdk.bean.SendAppraiseEntity.ContentsEntity content = new com.dy.sdk.bean.SendAppraiseEntity.ContentsEntity();
        content.setText(value);
        List entities = new ArrayList();
        entities.add(content);
        entity.setContents(entities);
        SendAppraiseEntity.ScoresEntity scoresEntity = new SendAppraiseEntity.ScoresEntity();
        entity.setScores(scoresEntity);
        entity.setType(TYPE_RECRUIT);
        entity.setTarget(str_jobId);
        String data = GsonUtil.toJson(entity);

        try {
            if (callback == null) {
                callback = new MyCallback();
            }
            H.doPostData(url, data, callback);
        } catch (Exception var8) {
            var8.printStackTrace();
        }
    }

    class MyCallback extends HCallback.HCacheCallback {

        @Override
        public void onError(CBase cBase, String s, Throwable throwable) throws Exception {
            dismissLoading();
            CToastUtil.toastShort(H.CTX, Tools.getResString(H.CTX, R.string.pubiishFailure));
        }

        @Override
        public void onSuccess(CBase cBase, HResp hResp, String s) throws Exception {
            dismissLoading();
            dealDatas(s);
        }

        private void dealDatas(String data) {
            try {
                if (data == null) {
                    CToastUtil.toastShort(H.CTX, Tools.getResString(H.CTX, R.string.pubiishFailure));
                } else {
                    SendAppraiseReplyBean e = GsonUtil.fromJson(data, SendAppraiseReplyBean.class);
                    if (e.getCode() == 0) {
                        CToastUtil.toastShort(H.CTX, Tools.getResString(H.CTX, R.string.appraiseSendOk));
                        Intent intent = new Intent(BROADCAST_APPRAISE);
                        intent.putExtra(BORADCAST_EXTRA_KEY, data);
                        LocalBroadcastManager.getInstance(H.CTX).sendBroadcast(intent);
                        finish();
                    } else if (e.getCode() == 301) {
                        CToastUtil.toastShort(H.CTX, Tools.getResString(H.CTX, R.string.loginFailure));
                    } else if (e.getCode() == 3) {
                        CToastUtil.toastShort(H.CTX, Tools.getResString(H.CTX, R.string.alerdayReply));
                    } else {
                        CToastUtil.toastShort(H.CTX, Tools.getResString(H.CTX, R.string.pubiishFailure));
                    }
                }
            } catch (Exception var6) {
                var6.printStackTrace();
                CToastUtil.toastShort(H.CTX, Tools.getResString(H.CTX, R.string.pubiishFailure));
            }
        }

    }
}
