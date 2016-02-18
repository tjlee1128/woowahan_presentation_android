package com.baemin.woowahan_presentation_android.search;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.baemin.woowahan_presentation_android.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    // ToolBar
    @Bind(R.id.activity_search_toolbar_include)
    View toolbarView;

    // TabLayout & ViewPager (세로방향)
    @Bind(R.id.activity_search_vp)
    ViewPager searchViewPager;
    private SearchFragmentPagerAdapter searchFragmentPagerAdapter;
    @Bind(R.id.activity_search_tl)
    TabLayout searchTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.start_right_left_enter, R.anim.start_right_left_exit);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        initializeToolBar();

        searchFragmentPagerAdapter = new SearchFragmentPagerAdapter(getSupportFragmentManager(), SearchActivity.this);
        searchViewPager.setAdapter(searchFragmentPagerAdapter);
        searchTabLayout.setupWithViewPager(searchViewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.end_right_left_enter, R.anim.end_right_left_exit);
    }

    private void initializeToolBar() {
        ((TextView) toolbarView.findViewById(R.id.layout_toolbar_detail_title_tv)).setText("자료 검색");
        toolbarView.findViewById(R.id.layout_toolbar_detail_back_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
