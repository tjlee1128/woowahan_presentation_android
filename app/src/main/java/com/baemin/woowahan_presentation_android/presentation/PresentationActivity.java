package com.baemin.woowahan_presentation_android.presentation;

import android.app.ProgressDialog;
import android.database.DataSetObserver;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.base.ViewPagerFixed;
import com.baemin.woowahan_presentation_android.model.CommentModel;
import com.baemin.woowahan_presentation_android.model.CommentsModel;
import com.baemin.woowahan_presentation_android.model.PresentationModel;
import com.baemin.woowahan_presentation_android.network.PresentationService;
import com.baemin.woowahan_presentation_android.network.ServiceGenerator;
import com.baemin.woowahan_presentation_android.util.Constants;
import com.baemin.woowahan_presentation_android.util.PreferencesManager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PresentationActivity extends AppCompatActivity {

    private int presentation_id;
    private String presentation_name;

    // ToolBar
    @Bind(R.id.activity_presentation_toolbar_include)
    View toolbarView;
    private ToggleButton thumbsToggleButton;

    // TabLayout & ViewPager (세로방향)
    @Bind(R.id.activity_presentation_vp)
    ViewPager presentationViewPager;
    private PresentationFragmentPagerAdapter presentationFragmentPagerAdapter;
    @Bind(R.id.activity_presentation_tl)
    TabLayout presentationTabLayout;
    @Bind(R.id.activity_presentation_sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;
    @Bind(R.id.activity_presentation_drag_view)
    View slidingPanelDragLayout;
    private ImageView slidingArrowImageView;
    private TextView slidingTitleTextView;
    private ListView slidingCommentListView;
    private RelativeLayout slidingCommentListEmptyView;
    private PresentationCommentAdapter presentationCommentAdapter;
    private EditText commentEditText;
    private Button commentPostButton;

    // Vidio & Pdf (가로방향)
    private View videoLayout;
    private VideoView videoView;
    private View placeholder;
    private MediaController mediaController;
    private View pdfLayout;
    private ViewPagerFixed pdfViewPager;
    private PresentationPdfPagerAdapter pdfPagerAdapter;
    private ImageButton modeButton;

    private PresentationModel presentationModel;

    // network
    private Callback<PresentationModel> callback = new Callback<PresentationModel>() {
        @Override
        public void onResponse(Response<PresentationModel> response, Retrofit retrofit) {
            presentationModel = response.body();

            // 세로방향
            if (PresentationActivity.this.getResources().getConfiguration().orientation == 1) {
                thumbsToggleButton.setChecked(presentationModel.isThumbs());
                thumbsToggleButton.setOnCheckedChangeListener(onCheckedChangeListener);

                slidingTitleTextView.setText("댓글(" + presentationModel.getComment_count() + " 개)");
                presentationFragmentPagerAdapter = new PresentationFragmentPagerAdapter(getSupportFragmentManager(), PresentationActivity.this, presentationModel);
                presentationViewPager.setAdapter(presentationFragmentPagerAdapter);
                presentationTabLayout.setupWithViewPager(presentationViewPager);

                if (presentationModel.getComments() != null) {
                    presentationCommentAdapter = new PresentationCommentAdapter(PresentationActivity.this, presentationModel.getComments());
                    slidingCommentListView.setAdapter(presentationCommentAdapter);
                    slidingCommentListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                }
                // empty comment
                else {

                }
                presentationCommentAdapter.registerDataSetObserver(new DataSetObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        slidingTitleTextView.setText("댓글(" + presentationCommentAdapter.getCount() + " 개)");
                        slidingCommentListView.setSelection(presentationCommentAdapter.getCount() - 1);
                    }
                });
            }
            // 가로방향
            else {
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

                        // 기본 -> 풀영상
                        if (videoLayoutParams.weight == 6f && pdfLayoutParams.weight == 4f) {
                            pdfLayout.setLayoutParams(new LinearLayout.LayoutParams(pdfLayoutParams.width, pdfLayoutParams.height, 0f));
                        }

                        // 풀영상 -> PDF
                        if (videoLayoutParams.weight == 6f && pdfLayoutParams.weight == 0f) {
                            videoLayout.setLayoutParams(new LinearLayout.LayoutParams(videoLayoutParams.width, videoLayoutParams.height, 0f));
                            pdfLayout.setLayoutParams(new LinearLayout.LayoutParams(pdfLayoutParams.width, pdfLayoutParams.height, 4f));
                        }

                        // PDF -> 기본
                        if (videoLayoutParams.weight == 0f && pdfLayoutParams.weight == 4f) {
                            videoLayout.setLayoutParams(new LinearLayout.LayoutParams(videoLayoutParams.width, videoLayoutParams.height, 6f));
                        }
                    }
                });
            }

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

        presentationCommentAdapter = new PresentationCommentAdapter(PresentationActivity.this, new ArrayList<CommentModel>());

        // 세로방향
        if (PresentationActivity.this.getResources().getConfiguration().orientation == 1) {
            // 처음 로드되는 방향이 세로일 때
            ButterKnife.bind(this);
            slidingTitleTextView = (TextView) slidingPanelDragLayout.findViewById(R.id.layout_sliding_comment_title_tv);
            slidingCommentListView = (ListView) slidingPanelDragLayout.findViewById(R.id.layout_sliding_comment_lv);
            slidingCommentListEmptyView = (RelativeLayout) slidingUpPanelLayout.findViewById(R.id.layout_sliding_comment_empty_rl);
            slidingCommentListView.setEmptyView(slidingCommentListEmptyView);
            slidingArrowImageView = (ImageView) slidingPanelDragLayout.findViewById(R.id.layout_sliding_comment_arrow);
            slidingArrowImageView.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
            commentEditText = (EditText) slidingUpPanelLayout.findViewById(R.id.layout_sliding_comment_et);
            commentPostButton = (Button) slidingUpPanelLayout.findViewById(R.id.layout_sliding_comment_input_btn);
            commentPostButton.setOnClickListener(commentInputOnClickListener);
            slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View panel, float slideOffset) {
                }

                @Override
                public void onPanelCollapsed(View panel) {
                    slidingArrowImageView.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                    slidingCommentListView.setSelection(presentationCommentAdapter.getCount() - 1);
                }

                @Override
                public void onPanelExpanded(View panel) {
                    slidingArrowImageView.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                }

                @Override
                public void onPanelAnchored(View panel) {
                }

                @Override
                public void onPanelHidden(View panel) {
                }
            });


            if (savedInstanceState == null) {
                presentation_id = getIntent().getExtras().getInt(Constants.EXTRA_PRESENTATION_ID);
                presentation_name = getIntent().getExtras().getString(Constants.EXTRA_PRESENTATION_NAME);
                initializeToolBar(presentation_name);
            }
            // 가로에서 세로로 왔을 때
            else {
                presentationModel = (PresentationModel) savedInstanceState.getSerializable(Constants.EXTRA_PRESENTATION_MODEL);
                initializeToolBar(presentationModel.getTitle());

                thumbsToggleButton.setChecked(presentationModel.isThumbs());
                thumbsToggleButton.setOnCheckedChangeListener(onCheckedChangeListener);

                slidingTitleTextView.setText("댓글(" + presentationModel.getComment_count() + " 개)");

                presentationFragmentPagerAdapter = new PresentationFragmentPagerAdapter(getSupportFragmentManager(), PresentationActivity.this, presentationModel);
                presentationViewPager.setAdapter(presentationFragmentPagerAdapter);
                presentationTabLayout.setupWithViewPager(presentationViewPager);

                presentationCommentAdapter = new PresentationCommentAdapter(PresentationActivity.this, presentationModel.getComments());
                slidingCommentListView.setAdapter(presentationCommentAdapter);

                slidingCommentListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                presentationCommentAdapter.registerDataSetObserver(new DataSetObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        slidingTitleTextView.setText("댓글(" + presentationCommentAdapter.getCount() + " 개)");
                        slidingCommentListView.setSelection(presentationCommentAdapter.getCount() - 1);
                    }
                });

                commentPostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (commentEditText.getText().toString() != null) {
                            PresentationService presentationService = ServiceGenerator.createService(PresentationService.class);
                            Call<CommentsModel> call = presentationService.postComment(presentationModel.getId(), PreferencesManager.getInstance().getUser().getId(), commentEditText.getText().toString(), presentationCommentAdapter.getLastCommentId());
                            Callback<CommentsModel> callback = new Callback<CommentsModel>() {
                                @Override
                                public void onResponse(Response<CommentsModel> response, Retrofit retrofit) {
                                    presentationCommentAdapter.addComment(response.body());
                                    commentEditText.setText("");
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    Toast.makeText(PresentationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            };
                            call.enqueue(callback);
                        }
                    }
                });
            }
        }

        // 가로방향
        else {
            videoLayout = findViewById(R.id.activity_presentation_land_vv_rl);
            videoView = (VideoView) findViewById(R.id.activity_presentation_land_vv);
            placeholder = findViewById(R.id.activity_presentation_land_holder);
            pdfLayout = findViewById(R.id.activity_presentation_land_vp_rl);
            pdfViewPager = (ViewPagerFixed) findViewById(R.id.activity_presentation_land_vp);
            modeButton = (ImageButton) findViewById(R.id.activity_presentation_land_mode_btn);

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

                        // 기본 -> 풀영상
                        if (videoLayoutParams.weight == 6f && pdfLayoutParams.weight == 4f) {
                            pdfLayout.setLayoutParams(new LinearLayout.LayoutParams(pdfLayoutParams.width, pdfLayoutParams.height, 0f));
                        }

                        // 풀영상 -> PDF
                        if (videoLayoutParams.weight == 6f && pdfLayoutParams.weight == 0f) {
                            videoLayout.setLayoutParams(new LinearLayout.LayoutParams(videoLayoutParams.width, videoLayoutParams.height, 0f));
                            pdfLayout.setLayoutParams(new LinearLayout.LayoutParams(pdfLayoutParams.width, pdfLayoutParams.height, 4f));
                        }

                        // PDF -> 기본
                        if (videoLayoutParams.weight == 0f && pdfLayoutParams.weight == 4f) {
                            videoLayout.setLayoutParams(new LinearLayout.LayoutParams(videoLayoutParams.width, videoLayoutParams.height, 6f));
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
            Call<PresentationModel> call = presentationService.loadPresentation(presentation_id, PreferencesManager.getInstance().getUser().getId());
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
    public void onBackPressed() {
        if (slidingUpPanelLayout != null &&
                (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (presentationModel != null) {
            outState.putSerializable(Constants.EXTRA_PRESENTATION_MODEL, presentationModel);
        }
        super.onSaveInstanceState(outState);
    }

    private void initializeToolBar(String title) {
        ((TextView) toolbarView.findViewById(R.id.layout_toolbar_detail_presentation_title_tv)).setText(title);
        ((RelativeLayout) toolbarView.findViewById(R.id.layout_toolbar_detail_presentation_back_rl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        thumbsToggleButton = (ToggleButton) toolbarView.findViewById(R.id.layout_toolbar_detail_presentation_thumbs_tb);
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // 좋아요를 누름
            if (isChecked) {
                PresentationService presentationService = ServiceGenerator.createService(PresentationService.class);
                Call<String> call = presentationService.thumbsUpPresentation(presentation_id, PreferencesManager.getInstance().getUser().getId());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
            // 좋아요를 품
            else {
                PresentationService presentationService = ServiceGenerator.createService(PresentationService.class);
                Call<String> call = presentationService.thumbsDownPresentation(presentation_id, PreferencesManager.getInstance().getUser().getId());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        }
    };

    private View.OnClickListener commentInputOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (commentEditText.getText().toString() != null) {
                PresentationService presentationService = ServiceGenerator.createService(PresentationService.class);
                Call<CommentsModel> call = presentationService.postComment(presentation_id, PreferencesManager.getInstance().getUser().getId(), commentEditText.getText().toString(), presentationCommentAdapter.getLastCommentId());
                Callback<CommentsModel> callback = new Callback<CommentsModel>() {
                    @Override
                    public void onResponse(Response<CommentsModel> response, Retrofit retrofit) {
                        presentationCommentAdapter.addComment(response.body());
                        commentEditText.setText("");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(PresentationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                };
                call.enqueue(callback);
            }
        }
    };
}
