package com.baemin.woowahan_presentation_android.presentation;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.base.ViewPagerFixed;
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
    private String presentation_name;
    private int currentLayoutDirection;

    // ToolBar
    @Bind(R.id.activity_presentation_toolbar_include)
    View toolbarView;

    // TabLayout & ViewPager (세로방향)
    @Bind(R.id.activity_presentation_vp)
    ViewPager presentationViewPager;
    private PresentationFragmentPagerAdapter presentationFragmentPagerAdapter;
    @Bind(R.id.activity_presentation_tl)
    TabLayout presentationTabLayout;

    // Vidio & Pdf (가로방향)
    private View videoLayout;
    private VideoView videoView;
    private View placeholder;
    private MediaController mediaController;
    private View pdfLayout;
    private ViewPagerFixed pdfViewPager;
    private PresentationPdfPagerAdapter pdfPagerAdapter;
    private Button modeButton;

    private PresentationModel presentationModel;

    // network
    private Callback<PresentationModel> callback = new Callback<PresentationModel>() {
        @Override
        public void onResponse(Response<PresentationModel> response, Retrofit retrofit) {
            presentationModel = response.body();

            presentationFragmentPagerAdapter = new PresentationFragmentPagerAdapter(getSupportFragmentManager(), PresentationActivity.this, presentationModel);
            presentationViewPager.setAdapter(presentationFragmentPagerAdapter);
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

        if (savedInstanceState == null) {
            ButterKnife.bind(this);
            initializeToolBar(getIntent().getExtras().getString(Constants.EXTRA_PRESENTATION_NAME));
            presentation_id = getIntent().getExtras().getInt(Constants.EXTRA_PRESENTATION_ID);
        } else {
            presentationModel = (PresentationModel) savedInstanceState.getSerializable(Constants.EXTRA_PRESENTATION_MODEL);
            if (savedInstanceState.getInt(Constants.EXTRA_PRESENTATION_ACTIVITY_DIRECTION) == 1) {
                ButterKnife.bind(this);
                initializeToolBar(presentationModel.getTitle());

                presentationFragmentPagerAdapter = new PresentationFragmentPagerAdapter(getSupportFragmentManager(), PresentationActivity.this, presentationModel);
                presentationViewPager.setAdapter(presentationFragmentPagerAdapter);
                presentationTabLayout.setupWithViewPager(presentationViewPager);
            } else {
                videoLayout = findViewById(R.id.activity_presentation_land_vv_rl);
                videoView = (VideoView) findViewById(R.id.activity_presentation_land_vv);
                placeholder = findViewById(R.id.activity_presentation_land_holder);
                pdfLayout = findViewById(R.id.activity_presentation_land_vp_rl);
                pdfViewPager = (ViewPagerFixed) findViewById(R.id.activity_presentation_land_vp);
                modeButton = (Button) findViewById(R.id.activity_presentation_land_mode_btn);

                Uri uri= Uri.parse(Constants.API_SERVER_BASE_URL + presentationModel.getVideo().getUrl());
                videoView.setVideoURI(uri);
                mediaController = new MediaController(PresentationActivity.this);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        placeholder.setVisibility(View.GONE);
                    }
                });

                pdfPagerAdapter = new PresentationPdfPagerAdapter(PresentationActivity.this, presentationModel.getImages());
                pdfViewPager.setAdapter(pdfPagerAdapter);

                modeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout.LayoutParams videoLayoutParams = (LinearLayout.LayoutParams) videoLayout.getLayoutParams();
                        LinearLayout.LayoutParams pdfLayoutParams = (LinearLayout.LayoutParams) pdfLayout.getLayoutParams();

                        Log.i("weight", "video : " + videoLayoutParams.weight);
                        Log.i("weight", "pdf : " + pdfLayoutParams.weight);

                        if (videoLayoutParams.weight == 7f && pdfLayoutParams.weight == 3f) {
                            pdfLayout.setLayoutParams(new LinearLayout.LayoutParams(pdfLayoutParams.width, pdfLayoutParams.height, 0f));
                        }

                        if (videoLayoutParams.weight == 7f && pdfLayoutParams.weight == 0f) {
//                            videoLayoutParams.weight = 0;
//                            pdfLayoutParams.weight = 3;
                            videoLayout.setLayoutParams(new LinearLayout.LayoutParams(videoLayoutParams.width, videoLayoutParams.height, 0f));
                            pdfLayout.setLayoutParams(new LinearLayout.LayoutParams(pdfLayoutParams.width, pdfLayoutParams.height, 3f));
                        }

                        if (videoLayoutParams.weight == 0f && pdfLayoutParams.weight == 3f) {
//                            videoLayoutParams.weight = 7;
                            videoLayout.setLayoutParams(new LinearLayout.LayoutParams(videoLayoutParams.width, videoLayoutParams.height, 7f));
                        }
                    }
                });
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (presentationModel == null) {
            PresentationService presentationService = ServiceGenerator.createService(PresentationService.class);
            Call<PresentationModel> call = presentationService.loadPresentation(presentation_id);
            call.enqueue(callback);

            progressDialog = new ProgressDialog(PresentationActivity.this);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (presentationModel != null) {
            outState.putInt(Constants.EXTRA_PRESENTATION_ACTIVITY_DIRECTION, PresentationActivity.this.getResources().getConfiguration().orientation);
            outState.putSerializable(Constants.EXTRA_PRESENTATION_MODEL, presentationModel);
        }
        super.onSaveInstanceState(outState);
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
