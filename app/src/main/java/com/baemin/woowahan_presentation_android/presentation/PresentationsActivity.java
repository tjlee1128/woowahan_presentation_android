package com.baemin.woowahan_presentation_android.presentation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.base.WPMessageDialog;
import com.baemin.woowahan_presentation_android.model.PresentationModel;
import com.baemin.woowahan_presentation_android.model.PresentationsModel;
import com.baemin.woowahan_presentation_android.network.PresentationService;
import com.baemin.woowahan_presentation_android.network.ServiceGenerator;
import com.baemin.woowahan_presentation_android.network.TeamService;
import com.baemin.woowahan_presentation_android.network.UserService;
import com.baemin.woowahan_presentation_android.util.Constants;
import com.baemin.woowahan_presentation_android.util.PreferencesManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PresentationsActivity extends AppCompatActivity {

    private String activityComeIn;

    private int user_id;
    private int team_id;

    private int category_id;
    private String access_token;

    // ToolBar
    @Bind(R.id.activity_presentations_toolbar_include)
    View toolbarView;

    // List
    @Bind(R.id.activity_presentations_lv)
    ListView presentationsListView;
    private PresentationsAdapter presentationsAdapter;
    @Bind(R.id.activity_presentations_empty_rl)
    RelativeLayout presentationsEmptyView;
    private PresentationsModel mPresentationsModel;
    private List<PresentationModel> presentationModelList;
    private WPMessageDialog messageDialog;
    private Callback<PresentationsModel> mCallback = new Callback<PresentationsModel>() {
        @Override
        public void onResponse(Response<PresentationsModel> response, Retrofit retrofit) {
            mPresentationsModel = response.body();
            presentationModelList = mPresentationsModel.getRows();
            presentationsAdapter = new PresentationsAdapter(PresentationsActivity.this, presentationModelList);
            presentationsListView.setAdapter(presentationsAdapter);
            if (presentationModelList.size() == 0) {
                presentationsListView.setEmptyView(presentationsEmptyView);
            }

            presentationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = null;

                    if (presentationModelList.get(position).getVideo() == null) {
                        intent = new Intent(PresentationsActivity.this, PresentationOnlyPdfActivity.class);
                    }

                    if (presentationModelList.get(position).getPdf() == null) {
                        intent = new Intent(PresentationsActivity.this, PresentationOnlyVideoActivity.class);
                    }

                    if (presentationModelList.get(position).getVideo() != null && presentationModelList.get(position).getPdf() != null) {
                        intent = new Intent(PresentationsActivity.this, PresentationActivity.class);
                    } else {
                        messageDialog = new WPMessageDialog(PresentationsActivity.this, "ERROR", "데이터를 불러오는데 실패했습니다.\n다시 시도해주세요", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                messageDialog.dismiss();
                            }
                        });
                    }

                    intent.putExtra(Constants.EXTRA_PRESENTATION_ID, presentationModelList.get(position).getId());
                    intent.putExtra(Constants.EXTRA_PRESENTATION_NAME, presentationModelList.get(position).getTitle());
                    startActivity(intent);
                }
            });

            progressDialog.dismiss();
        }

        @Override
        public void onFailure(Throwable t) {
            progressDialog.dismiss();
            Toast.makeText(PresentationsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
    private Callback<PresentationsModel> mResumeCallback = new Callback<PresentationsModel>() {
        @Override
        public void onResponse(Response<PresentationsModel> response, Retrofit retrofit) {
            mPresentationsModel = response.body();
            presentationModelList.clear();
            presentationModelList.addAll(mPresentationsModel.getRows());
            presentationsAdapter.notifyDataSetChanged();
            if (presentationModelList.size() == 0) {
                presentationsListView.setEmptyView(presentationsEmptyView);
            }
            progressDialog.dismiss();
        }

        @Override
        public void onFailure(Throwable t) {
            progressDialog.dismiss();
            Toast.makeText(PresentationsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private ProgressDialog progressDialog;

    // List Empty
    @OnClick(R.id.activity_presentations_empty_btn)
    public void onClick() {
        onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.start_right_left_enter, R.anim.start_right_left_exit);
        setContentView(R.layout.activity_presentations);
        ButterKnife.bind(this);

        activityComeIn = getIntent().getExtras().getString(Constants.EXTRA_PRESENTATION_COME_IN);

        if (activityComeIn.equals("category")) {
            initializeToolBar(getIntent().getExtras().getString(Constants.EXTRA_CATEGORY_NAME));
            category_id = getIntent().getExtras().getInt(Constants.EXTRA_CATEGORY_ID);
        } else if (activityComeIn.equals("user_presentations")) {
            initializeToolBar(PreferencesManager.getInstance().getUser().getFullname() + "님이 올린 자료");
            access_token = getIntent().getExtras().getString(Constants.EXTRA_ACCESS_TOKEN);
        } else if (activityComeIn.equals("user_id_presentations")) {
            initializeToolBar(getIntent().getExtras().getString(Constants.EXTRA_USER_NAME) + "님이 올린 자료");
            user_id = getIntent().getExtras().getInt(Constants.EXTRA_USER_ID);
        } else if (activityComeIn.equals("team_id_presentations")) {
            initializeToolBar(getIntent().getExtras().getString(Constants.EXTRA_TEAM_NAME) + "의 자료");
            team_id = getIntent().getExtras().getInt(Constants.EXTRA_TEAM_ID);
        } else if (activityComeIn.equals("user_favorite_presentations")) {
            initializeToolBar(PreferencesManager.getInstance().getUser().getFullname() + "님이 좋아한 자료");
            access_token = getIntent().getExtras().getString(Constants.EXTRA_ACCESS_TOKEN);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        progressDialog = new ProgressDialog(PresentationsActivity.this);
        progressDialog.setMessage("목록를 가져오는 중입니다.");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();

        if (mPresentationsModel == null) {
            Call<PresentationsModel> call = null;
            if (activityComeIn.equals("category")) {
                PresentationService presentationService = ServiceGenerator.createService(PresentationService.class);
                call = presentationService.loadPresentations(category_id, null);
            } else if (activityComeIn.equals("user_presentations")){
                UserService userService = ServiceGenerator.createService(UserService.class);
                call = userService.loadUserPresentations(access_token);
            } else if (activityComeIn.equals("user_id_presentations")) {
                PresentationService presentationService = ServiceGenerator.createService(PresentationService.class);
                call = presentationService.loadPresentations(null, user_id);
            } else if (activityComeIn.equals("team_id_presentations")) {
                TeamService teamService = ServiceGenerator.createService(TeamService.class);
                call = teamService.loadPresentationByTeam(team_id);
            } else if (activityComeIn.equals("user_favorite_presentations")) {
                UserService userService = ServiceGenerator.createService(UserService.class);
                call = userService.loadUserFavoritePresentations(access_token);
            }

            call.enqueue(mCallback);
        }
        else {
            Call<PresentationsModel> call = null;
            if (activityComeIn.equals("category")) {
                PresentationService presentationService = ServiceGenerator.createService(PresentationService.class);
                call = presentationService.loadPresentations(category_id, null);
            } else if (activityComeIn.equals("user_presentations")){
                UserService userService = ServiceGenerator.createService(UserService.class);
                call = userService.loadUserPresentations(access_token);
            } else if (activityComeIn.equals("user_id_presentations")) {
                PresentationService presentationService = ServiceGenerator.createService(PresentationService.class);
                call = presentationService.loadPresentations(null, user_id);
            } else if (activityComeIn.equals("team_id_presentations")) {
                TeamService teamService = ServiceGenerator.createService(TeamService.class);
                call = teamService.loadPresentationByTeam(team_id);
            } else if (activityComeIn.equals("user_favorite_presentations")) {
                UserService userService = ServiceGenerator.createService(UserService.class);
                call = userService.loadUserFavoritePresentations(access_token);
            }

            call.enqueue(mResumeCallback);
        }

        Log.i("activityComeIn", activityComeIn);
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
        ((RelativeLayout) toolbarView.findViewById(R.id.layout_toolbar_detail_back_rl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
