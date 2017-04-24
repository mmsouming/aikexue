package com.dy.aikexue.ssolibrary.loginregister.activit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.base.BaseFragmentActivity;
import com.dy.aikexue.ssolibrary.loginregister.fragment.CompanyEvaluateListFragment;

/**
 * @author zhongq
 *         公司面试评价页面
 */
public class CompanyEvaluateActivity extends BaseFragmentActivity implements View.OnClickListener {
    private ImageView imgBack;
    private TextView tvTitle, tvSave;

    //公司id
    private String companyId;

    private static final String VALUE_ID = "companyId";

    /**
     *
     * @param context
     * @param companyId 公司id、职位id
     * @return
     */
    public static Intent getJumpIntent(Context context, String companyId) {
        Intent intent = new Intent(context, CompanyEvaluateActivity.class);
        intent.putExtra(VALUE_ID, companyId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_evaluate);
        initView();
        init();
        initListener();
        companyId = getIntent().getStringExtra(VALUE_ID);
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContent, CompanyEvaluateListFragment.newFragment(companyId)).commit();

    }

    private void initListener() {
        imgBack.setOnClickListener(this);
    }

    private void init() {
        tvTitle.setText(getString(R.string.companyEva));
        tvSave.setVisibility(View.GONE);
    }

    private void initView() {
        imgBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSave = (TextView) findViewById(R.id.tv_save);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
