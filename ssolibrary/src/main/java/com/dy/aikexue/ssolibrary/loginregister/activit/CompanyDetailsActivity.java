package com.dy.aikexue.ssolibrary.loginregister.activit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.base.BaseFragmentActivity;
import com.dy.aikexue.ssolibrary.loginregister.fragment.CompanyDetailFragment;
import com.dy.aikexue.ssolibrary.util.Dysso;
import com.dy.sdk.utils.ThemeUtil;

/**
 * 公司详情
 * @see {@link CompanyDetailFragment}
 */
public class CompanyDetailsActivity extends BaseFragmentActivity {

    private static final String VALUE_ID = "companyId";

    public static Intent getStartIntent(Context activity, String companyId) {
        Intent intent = new Intent(activity, CompanyDetailsActivity.class);
        intent.putExtra(VALUE_ID, companyId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);
        if (!Dysso.isSessionValid()) {
            Dysso.createInstance(this).login(this, null);
            finish();
            return;
        }
        String id = getIntent().getStringExtra(VALUE_ID);
        CompanyDetailFragment companyDetailFragment = CompanyDetailFragment.newFragment(id);
        getSupportFragmentManager().beginTransaction().add(R.id.contentView, companyDetailFragment).commit();
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(getString(R.string.companyDetail));
        findViewById(R.id.tv_save).setVisibility(View.GONE);
        findViewById(R.id.title_view).setBackgroundColor(ThemeUtil.getThemeColor(this));
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
