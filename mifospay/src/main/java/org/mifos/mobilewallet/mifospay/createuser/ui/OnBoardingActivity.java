package org.mifos.mobilewallet.mifospay.createuser.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.base.BaseActivity;
import org.mifos.mobilewallet.mifospay.createuser.adapter.SlideAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnBoardingActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.view_page)
    ViewPager mSlideViewPager;

    @BindView(R.id.btn_start)
    ImageView btnStart;

    private SlideAdapter slideAdapter;
    private int currentPage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_on_boarding);
        ButterKnife.bind(this);

        slideAdapter = new SlideAdapter(this);
        mSlideViewPager.setAdapter(slideAdapter);

        mSlideViewPager.addOnPageChangeListener(this);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPage = position;
        if (currentPage == 2) {
            btnStart.setVisibility(View.VISIBLE);
        }
    }

    private void openActivity() {
        Intent intent = new Intent(this, DemoLoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
