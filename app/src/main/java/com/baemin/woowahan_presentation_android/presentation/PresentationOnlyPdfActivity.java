package com.baemin.woowahan_presentation_android.presentation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.base.ViewPagerFixed;
import com.baemin.woowahan_presentation_android.model.ImageModel;
import com.baemin.woowahan_presentation_android.model.PresentationModel;
import com.baemin.woowahan_presentation_android.network.PresentationService;
import com.baemin.woowahan_presentation_android.network.ServiceGenerator;
import com.baemin.woowahan_presentation_android.util.Constants;
import com.baemin.woowahan_presentation_android.util.DateConvertor;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PresentationOnlyPdfActivity extends AppCompatActivity {

    private int presentation_id;
    private PresentationModel presentationModel;

    // ToolBar
    @Bind(R.id.activity_presentation_only_pdf_toolbar_include)
    View toolbarView;

    // ViewPager
    @Bind(R.id.activity_presentation_only_pdf_vp)
    ViewPagerFixed pdfViewPager;
    private PresentationPdfPagerAdapter pdfPagerAdapter;
    @Bind(R.id.activity_presentation_only_pdf_chevronleft_rl)
    RelativeLayout leftChevronLayout;
    @Bind(R.id.activity_presentation_only_pdf_chevronright_rl)
    RelativeLayout rightChevronLayout;
    @Bind(R.id.activity_presentation_only_pdf_pagertitle_tv)
    TextView pagertitleTextView;

    @Bind(R.id.activity_presentation_only_pdf_title_tv)
    TextView titleTextView;
    @Bind(R.id.activity_presentation_only_pdf_subtitle_tv)
    TextView subtitleTextView;
    @Bind(R.id.activity_presentation_only_pdf_content_tv)
    TextView contentTextView;
    @Bind(R.id.activity_presentation_only_pdf_date_updatedat_tv)
    TextView updatedAtTextView;
    @Bind(R.id.activity_presentation_only_pdf_user_tv)
    TextView userTextView;
    @Bind(R.id.activity_presentation_only_pdf_team_name_tv)
    TextView teamnameTextView;
    @Bind(R.id.activity_presentation_only_pdf_user_image_iv)
    RoundedImageView userImageView;

    // network
    private Callback<PresentationModel> callback = new Callback<PresentationModel>() {
        @Override
        public void onResponse(Response<PresentationModel> response, Retrofit retrofit) {
            presentationModel = response.body();

            leftChevronLayout.setOnClickListener(onClickListener);
            rightChevronLayout.setOnClickListener(onClickListener);
            pdfPagerAdapter = new PresentationPdfPagerAdapter(PresentationOnlyPdfActivity.this, presentationModel.getImages());
            pdfViewPager.setAdapter(pdfPagerAdapter);
            pdfViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    pagertitleTextView.setText("" + (position + 1) + "/" + presentationModel.getImages().size());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            pagertitleTextView.setText("총 " + presentationModel.getImages().size() + "pages");

            titleTextView.setText(presentationModel.getTitle());
            subtitleTextView.setText(presentationModel.getSubtitle());

            String updatedAt = "";
            try {
                updatedAt = DateConvertor.convertToDate(presentationModel.getUpdated_at());
                updatedAt = DateConvertor.utcToLocaltime(updatedAt);
            } catch (Exception e) {
                e.printStackTrace();
            }
            updatedAtTextView.setText("modified on " + updatedAt);
            contentTextView.setText(presentationModel.getContent());

            Picasso.with(PresentationOnlyPdfActivity.this)
                    .load(Constants.API_SERVER_BASE_URL + presentationModel.getUser().getImage().getThumb_url())
                    .into(userImageView);
            userTextView.setText(presentationModel.getUser().getFullname());
            teamnameTextView.setText(presentationModel.getUser().getTeam_name());

            progressDialog.dismiss();
        }

        @Override
        public void onFailure(Throwable t) {
            Toast.makeText(PresentationOnlyPdfActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    };

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.start_right_left_enter, R.anim.start_right_left_exit);
        setContentView(R.layout.activity_presentation_only_pdf);

        ButterKnife.bind(this);

        initializeToolBar(getIntent().getExtras().getString(Constants.EXTRA_PRESENTATION_NAME));
        presentation_id = getIntent().getExtras().getInt(Constants.EXTRA_PRESENTATION_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (presentationModel == null) {
            PresentationService presentationService = ServiceGenerator.createService(PresentationService.class);
            Call<PresentationModel> call = presentationService.loadPresentation(presentation_id);
            call.enqueue(callback);

            progressDialog = new ProgressDialog(PresentationOnlyPdfActivity.this);
            progressDialog.setMessage("자료를 가져오는 중입니다.");
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(true);
            progressDialog.show();
        } else {

        }
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

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position;
            switch (v.getId()) {
                case R.id.activity_presentation_only_pdf_chevronleft_rl:
                    if (pdfViewPager.getCurrentItem() > 0) {
                        position = pdfViewPager.getCurrentItem();
                        pagertitleTextView.setText("" + (position - 1) + "/" + presentationModel.getImages().size());
                        pdfViewPager.setCurrentItem(position - 1, true);
                    }
                    break;

                case R.id.activity_presentation_only_pdf_chevronright_rl:
                    if (pdfViewPager.getCurrentItem() < presentationModel.getImages().size()) {
                        position = pdfViewPager.getCurrentItem();
                        pagertitleTextView.setText("" + (position + 1) + "/" + presentationModel.getImages().size());
                        pdfViewPager.setCurrentItem(position + 1, true);
                    }
                    break;

                default:
                    break;
            }
        }
    };
}
