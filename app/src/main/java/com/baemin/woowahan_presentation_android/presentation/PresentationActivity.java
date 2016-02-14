package com.baemin.woowahan_presentation_android.presentation;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.model.PresentationModel;
import com.baemin.woowahan_presentation_android.network.PresentationService;
import com.baemin.woowahan_presentation_android.network.ServiceGenerator;
import com.baemin.woowahan_presentation_android.util.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PresentationActivity extends AppCompatActivity {

    private int presentation_id;

    // ToolBar
    @Bind(R.id.activity_presentation_toolbar_include)
    View toolbarView;

    // TabLayout & ViewPager
    @Bind(R.id.activity_presentation_vp)
    ViewPager presentationViewPager;
    @Bind(R.id.activity_presentation_tl)
    TabLayout presentationTabLayout;

    // network
    private Callback<PresentationModel> callback = new Callback<PresentationModel>() {
        @Override
        public void onResponse(Response<PresentationModel> response, Retrofit retrofit) {
            presentationViewPager.setAdapter(new PresentationFragmentPagerAdapter(getSupportFragmentManager(), PresentationActivity.this, response.body()));
            presentationTabLayout.setupWithViewPager(presentationViewPager);

            progressDialog.dismiss();
        }

        @Override
        public void onFailure(Throwable t) {
            Toast.makeText(PresentationActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    };

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.start_right_left_enter, R.anim.start_right_left_exit);
        setContentView(R.layout.activity_presentation);

        ButterKnife.bind(this);

        initializeToolBar(getIntent().getExtras().getString(Constants.EXTRA_PRESENTATION_NAME));
        presentation_id = getIntent().getExtras().getInt(Constants.EXTRA_PRESENTATION_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();

        PresentationService presentationService = ServiceGenerator.createService(PresentationService.class);
        Call<PresentationModel> call = presentationService.loadPresentation(presentation_id);
        call.enqueue(callback);

        progressDialog = new ProgressDialog(PresentationActivity.this);
        progressDialog.setMessage("자료를 가져오는 중입니다.");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.end_right_left_enter, R.anim.end_right_left_exit);
    }

    private void initializeToolBar(String title) {
        ((TextView) toolbarView.findViewById(R.id.layout_toolbar_detail_title_tv)).setText(title);
        ((TextView) toolbarView.findViewById(R.id.layout_toolbar_detail_title_tv)).requestFocus();
        ((RelativeLayout) toolbarView.findViewById(R.id.layout_toolbar_detail_back_rl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
