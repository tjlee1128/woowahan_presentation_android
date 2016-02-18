package com.baemin.woowahan_presentation_android.presentation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.base.ViewPagerFixed;
import com.baemin.woowahan_presentation_android.model.CommentModel;
import com.baemin.woowahan_presentation_android.model.CommentsModel;
import com.baemin.woowahan_presentation_android.model.ImageModel;
import com.baemin.woowahan_presentation_android.model.PresentationModel;
import com.baemin.woowahan_presentation_android.network.PresentationService;
import com.baemin.woowahan_presentation_android.network.ServiceGenerator;
import com.baemin.woowahan_presentation_android.util.Constants;
import com.baemin.woowahan_presentation_android.util.DateConvertor;
import com.baemin.woowahan_presentation_android.util.PreferencesManager;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PresentationOnlyPdfActivity extends AppCompatActivity {

    private int presentation_id;
    private String presentation_name;
    private PresentationModel presentationModel;

    // ToolBar
    @Bind(R.id.activity_presentation_only_pdf_toolbar_include)
    View toolbarView;
    private ToggleButton thumbsToggleButton;

    @Bind(R.id.activity_presentation_only_pdf_sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;
    @Bind(R.id.activity_presentation_only_pdf_drag_view)
    View slidingPanelDragLayout;
    private ImageView slidingArrowImageView;
    private TextView slidingTitleTextView;
    private ListView slidingCommentListView;
    private RelativeLayout slidingCommentListEmptyView;
    private PresentationCommentAdapter presentationCommentAdapter;
    private EditText commentEditText;
    private Button commentPostButton;

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

    // Content
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

    // 가로방향
    private ViewPagerFixed landViewPager;
    private TextView landTitleTextView;

    // network
    private Callback<PresentationModel> callback = new Callback<PresentationModel>() {
        @Override
        public void onResponse(Response<PresentationModel> response, Retrofit retrofit) {
            presentationModel = response.body();

            // 세로방향
            if (PresentationOnlyPdfActivity.this.getResources().getConfiguration().orientation == 1) {
                thumbsToggleButton.setChecked(presentationModel.isThumbs());
                thumbsToggleButton.setOnCheckedChangeListener(onCheckedChangeListener);

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

                slidingTitleTextView.setText("댓글(" + presentationModel.getComment_count() + " 개)");

                if (presentationModel.getComments() != null) {
                    presentationCommentAdapter = new PresentationCommentAdapter(PresentationOnlyPdfActivity.this, presentationModel.getComments());
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
                }
                // empty comment
                else {

                }
            }
            // 가로방향
            else {
                pdfPagerAdapter = new PresentationPdfPagerAdapter(PresentationOnlyPdfActivity.this, presentationModel.getImages());
                landViewPager.setAdapter(pdfPagerAdapter);
                landTitleTextView.setText("1/" + presentationModel.getImages().size());

                landViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        landTitleTextView.setText("" + (position + 1) + "/" + presentationModel.getImages().size());
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }

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

        presentationCommentAdapter = new PresentationCommentAdapter(PresentationOnlyPdfActivity.this, new ArrayList<CommentModel>());

        // 세로방향
        if (PresentationOnlyPdfActivity.this.getResources().getConfiguration().orientation == 1) {
            // 처음 로드되는 방향이 세로일 때
            ButterKnife.bind(this);

            slidingTitleTextView = (TextView) slidingPanelDragLayout.findViewById(R.id.layout_sliding_comment_title_tv);
            slidingCommentListView = (ListView) slidingPanelDragLayout.findViewById(R.id.layout_sliding_comment_lv);
            slidingCommentListEmptyView = (RelativeLayout) slidingUpPanelLayout.findViewById(R.id.layout_sliding_comment_empty_rl);
            slidingCommentListView.setEmptyView(slidingCommentListEmptyView);
            slidingArrowImageView = (ImageView) slidingPanelDragLayout.findViewById(R.id.layout_sliding_comment_arrow);
            slidingArrowImageView.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
            commentEditText = (EditText) slidingUpPanelLayout.findViewById(R.id.layout_sliding_comment_et);
            commentEditText.clearFocus();
            commentPostButton = (Button) slidingUpPanelLayout.findViewById(R.id.layout_sliding_comment_input_btn);
            commentPostButton.setOnClickListener(commentInputOnClickListener);
            slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View panel, float slideOffset) {
                }

                @Override
                public void onPanelCollapsed(View panel) {
                    slidingArrowImageView.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);

                    if (presentationModel.getComments() != null) {
                        slidingCommentListView.setSelection(presentationCommentAdapter.getCount() - 1);
                    }
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

                pdfPagerAdapter = new PresentationPdfPagerAdapter(PresentationOnlyPdfActivity.this, presentationModel.getImages());
                pdfViewPager.setAdapter(pdfPagerAdapter);


                leftChevronLayout.setOnClickListener(onClickListener);
                rightChevronLayout.setOnClickListener(onClickListener);
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

                slidingTitleTextView.setText("댓글(" + presentationModel.getComment_count() + " 개)");

                if (presentationModel.getComments() != null) {
                    presentationCommentAdapter = new PresentationCommentAdapter(PresentationOnlyPdfActivity.this, presentationModel.getComments());
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
                } else {

                }

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
                                    presentationModel.setComment_count(presentationCommentAdapter.getCount());
                                    commentEditText.setText("");
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    Toast.makeText(PresentationOnlyPdfActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
            landViewPager = (ViewPagerFixed) findViewById(R.id.activity_presentation_land_only_pdf_vp);
            landTitleTextView = (TextView) findViewById(R.id.activity_presentation_land_only_pdf_tv);

            // 처음 로드되는 방향이 가로일 때
            if (savedInstanceState == null) {
                presentation_id = getIntent().getExtras().getInt(Constants.EXTRA_PRESENTATION_ID);
                presentation_name = getIntent().getExtras().getString(Constants.EXTRA_PRESENTATION_NAME);
            }
            // 세로에서 가로로 왔을 때
            else {
                presentationModel = (PresentationModel) savedInstanceState.getSerializable(Constants.EXTRA_PRESENTATION_MODEL);

                pdfPagerAdapter = new PresentationPdfPagerAdapter(PresentationOnlyPdfActivity.this, presentationModel.getImages());
                landViewPager.setAdapter(pdfPagerAdapter);
                landTitleTextView.setText("1/" + presentationModel.getImages().size());

                landViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        landTitleTextView.setText("" + (position + 1) + "/" + presentationModel.getImages().size());
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

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
                        presentationModel.setComment_count(presentationCommentAdapter.getCount());
                        commentEditText.setText("");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(PresentationOnlyPdfActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                };
                call.enqueue(callback);
            }
        }
    };
}
