package com.baemin.woowahan_presentation_android.presentation;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.model.PresentationModel;
import com.baemin.woowahan_presentation_android.network.PresentationService;
import com.baemin.woowahan_presentation_android.network.ServiceGenerator;
import com.baemin.woowahan_presentation_android.util.Constants;
import com.baemin.woowahan_presentation_android.util.DateConvertor;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PresentationOnlyVideoActivity extends AppCompatActivity {

    private int presentation_id;
    private String presentation_name;
    private PresentationModel presentationModel;

    // ToolBar
    @Bind(R.id.activity_presentation_only_video_toolbar_include)
    View toolbarView;

    // video view
    @Bind(R.id.activity_presentation_only_video_vv)
    VideoView videoView;
    @Bind(R.id.activity_presentation_only_video_holder)
    View placeholder;
    private MediaController mediaController;

    @Bind(R.id.activity_presentation_only_video_title_tv)
    TextView titleTextView;
    @Bind(R.id.activity_presentation_only_video_subtitle_tv)
    TextView subtitleTextView;
    @Bind(R.id.activity_presentation_only_video_content_tv)
    TextView contentTextView;
    @Bind(R.id.activity_presentation_only_video_date_updatedat_tv)
    TextView updatedAtTextView;
    @Bind(R.id.activity_presentation_only_video_user_tv)
    TextView userTextView;
    @Bind(R.id.activity_presentation_only_video_team_name_tv)
    TextView teamnameTextView;
    @Bind(R.id.activity_presentation_only_video_user_image_iv)
    RoundedImageView userImageView;

    // 가로방향
    private VideoView landVideoView;
    private View landPlaceHolder;


    // network
    private Callback<PresentationModel> callback = new Callback<PresentationModel>() {
        @Override
        public void onResponse(Response<PresentationModel> response, Retrofit retrofit) {
            presentationModel = response.body();

            // 세로방향
            if (PresentationOnlyVideoActivity.this.getResources().getConfiguration().orientation == 1) {
                Uri uri= Uri.parse(Constants.API_SERVER_BASE_URL + presentationModel.getVideo().getUrl());
                videoView.setVideoURI(uri);
                mediaController = new MediaController(PresentationOnlyVideoActivity.this);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        placeholder.setVisibility(View.GONE);
                    }
                });

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

                Picasso.with(PresentationOnlyVideoActivity.this)
                        .load(Constants.API_SERVER_BASE_URL + presentationModel.getUser().getImage().getThumb_url())
                        .into(userImageView);
                userTextView.setText(presentationModel.getUser().getFullname());
                teamnameTextView.setText(presentationModel.getUser().getTeam_name());
            }
            // 가로방향
            else {
                Uri uri= Uri.parse(Constants.API_SERVER_BASE_URL + presentationModel.getVideo().getUrl());
                videoView.setVideoURI(uri);
                mediaController = new MediaController(PresentationOnlyVideoActivity.this);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        landPlaceHolder.setVisibility(View.GONE);
                    }
                });
            }

            progressDialog.dismiss();
        }

        @Override
        public void onFailure(Throwable t) {
            Toast.makeText(PresentationOnlyVideoActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    };

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.start_right_left_enter, R.anim.start_right_left_exit);
        setContentView(R.layout.activity_presentation_only_video);


        // 세로방향
        if (PresentationOnlyVideoActivity.this.getResources().getConfiguration().orientation == 1) {
            // 처음 로드되는 방향이 세로일 때
            ButterKnife.bind(this);
            if (savedInstanceState == null) {
                presentation_id = getIntent().getExtras().getInt(Constants.EXTRA_PRESENTATION_ID);
                presentation_name = getIntent().getExtras().getString(Constants.EXTRA_PRESENTATION_NAME);
                initializeToolBar(presentation_name);
            }
            // 가로에서 세로로 왔을 때
            else {
                presentationModel = (PresentationModel) savedInstanceState.getSerializable(Constants.EXTRA_PRESENTATION_MODEL);
                initializeToolBar(presentationModel.getTitle());
            }
        }

        // 가로방향
        else {
            videoView = (VideoView) findViewById(R.id.activity_presentation_land_only_video_vv);
            landPlaceHolder = findViewById(R.id.activity_presentation_land_only_video_hodler);

            // 처음 로드되는 방향이 가로일 때
            if (savedInstanceState == null) {
                presentation_id = getIntent().getExtras().getInt(Constants.EXTRA_PRESENTATION_ID);
                presentation_name = getIntent().getExtras().getString(Constants.EXTRA_PRESENTATION_NAME);
            }
            // 세로에서 가로로 왔을 때
            else {
                presentationModel = (PresentationModel) savedInstanceState.getSerializable(Constants.EXTRA_PRESENTATION_MODEL);

                Uri uri= Uri.parse(Constants.API_SERVER_BASE_URL + presentationModel.getVideo().getUrl());
                videoView.setVideoURI(uri);
                mediaController = new MediaController(PresentationOnlyVideoActivity.this);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        landPlaceHolder.setVisibility(View.GONE);
                    }
                });
            }
        }










//        ButterKnife.bind(this);
//
//        initializeToolBar(getIntent().getExtras().getString(Constants.EXTRA_PRESENTATION_NAME));
//        presentation_id = getIntent().getExtras().getInt(Constants.EXTRA_PRESENTATION_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (presentationModel == null) {
            PresentationService presentationService = ServiceGenerator.createService(PresentationService.class);
            Call<PresentationModel> call = presentationService.loadPresentation(presentation_id);
            call.enqueue(callback);

            progressDialog = new ProgressDialog(PresentationOnlyVideoActivity.this);
            progressDialog.setMessage("자료를 가져오는 중입니다.");
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(true);
            progressDialog.show();
        } else {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        placeholder.setVisibility(View.VISIBLE);
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
