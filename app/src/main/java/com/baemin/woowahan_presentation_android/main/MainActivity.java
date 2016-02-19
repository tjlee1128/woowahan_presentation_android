package com.baemin.woowahan_presentation_android.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baemin.woowahan_presentation_android.base.ViewPagerFixed;
import com.baemin.woowahan_presentation_android.model.PresentationModel;
import com.baemin.woowahan_presentation_android.model.PresentationsModel;
import com.baemin.woowahan_presentation_android.network.FavoriteService;
import com.baemin.woowahan_presentation_android.network.ServiceGenerator;
import com.baemin.woowahan_presentation_android.presentation.PresentationsActivity;
import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.search.SearchActivity;
import com.baemin.woowahan_presentation_android.util.Constants;
import com.baemin.woowahan_presentation_android.util.PreferencesManager;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    // ToolBar
    @Bind(R.id.activity_main_toolbar_include)
    View toolbarView;

    //
    private ProgressDialog progressDialog;

    // Drawer
    private View drawerHeaderView;
    private RoundedImageView userImageView;
    private TextView userTextView;
    private TextView teamTextView;
    @Bind(R.id.activity_main_dl)
    DrawerLayout drawerLayout;
    @Bind(R.id.activity_main_drawer_lv)
    ListView drawerListView;

    // Main List
    @Bind(R.id.activity_main_category_lv)
    ListView categoriesListView;
    View categoriesHeaderView;
    private ViewPagerFixed headerViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.start_right_left_enter, R.anim.start_right_left_exit);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initializeToolBar();
        initializeDrawer();
        initializeHeader();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initializeToolBar() {
        ((RelativeLayout) toolbarView.findViewById(R.id.layout_toolbar_drawer_search_drawer_rl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        ((RelativeLayout) toolbarView.findViewById(R.id.layout_toolbar_drawer_search_search_rl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initializeDrawer() {
        drawerHeaderView = getLayoutInflater().inflate(R.layout.layout_drawer_header, null, false);

        userImageView = (RoundedImageView) drawerHeaderView.findViewById(R.id.layout_drawer_header_user_image_iv);
        userTextView = (TextView) drawerHeaderView.findViewById(R.id.layout_drawer_header_user_tv);
        teamTextView = (TextView) drawerHeaderView.findViewById(R.id.layout_drawer_header_teamname_tv);

        Picasso.with(MainActivity.this)
                .load(Constants.API_SERVER_BASE_URL + PreferencesManager.getInstance().getUser().getImage().getThumb_url())
                .into(userImageView);
        userTextView.setText(PreferencesManager.getInstance().getUser().getFullname());
        teamTextView.setText(PreferencesManager.getInstance().getUser().getTeam_name());

        List<String> drawerList = new ArrayList<>();
        drawerList.add("올린 자료");
        drawerList.add("좋아한 자료");

        drawerListView.addHeaderView(drawerHeaderView);
        drawerListView.setAdapter(new MainDrawerAdapter(MainActivity.this, drawerList));
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // Nothing
                } else if (position == 1) {
                    Intent intent = new Intent(MainActivity.this, PresentationsActivity.class);
                    intent.putExtra(Constants.EXTRA_PRESENTATION_COME_IN, "user_presentations");
                    intent.putExtra(Constants.EXTRA_ACCESS_TOKEN, PreferencesManager.getInstance().getAccessToken());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, PresentationsActivity.class);
                    intent.putExtra(Constants.EXTRA_PRESENTATION_COME_IN, "user_favorite_presentations");
                    intent.putExtra(Constants.EXTRA_ACCESS_TOKEN, PreferencesManager.getInstance().getAccessToken());
                    startActivity(intent);
                }
            }
        });
    }

    private void initializeHeader() {
        categoriesHeaderView = getLayoutInflater().inflate(R.layout.layout_main_lv_header, null, false);

        headerViewPager = (ViewPagerFixed) categoriesHeaderView.findViewById(R.id.layout_main_lv_header_vp);
        FavoriteService favoriteService = ServiceGenerator.createService(FavoriteService.class);
        Call<PresentationsModel> call = favoriteService.loadFavoritePresentation();

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("목록를 가져오는 중입니다.");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();

        call.enqueue(new Callback<PresentationsModel>() {
            @Override
            public void onResponse(Response<PresentationsModel> response, Retrofit retrofit) {
                List<PresentationModel> presentationModelList = response.body().getRows();

                headerViewPager.setAdapter(new MainFavoritePagerAdapter(MainActivity.this, presentationModelList));

                categoriesListView.addHeaderView(categoriesHeaderView);
                categoriesListView.setAdapter(new MainCategoriesAdapter(MainActivity.this, PreferencesManager.getInstance().getCategories().getRows()));
                categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, PresentationsActivity.class);
                        intent.putExtra(Constants.EXTRA_PRESENTATION_COME_IN, "category");
                        intent.putExtra(Constants.EXTRA_CATEGORY_ID, PreferencesManager.getInstance().getCategories().getRows().get(position - 1).getId());
                        intent.putExtra(Constants.EXTRA_CATEGORY_NAME, PreferencesManager.getInstance().getCategories().getRows().get(position - 1).getName());
                        startActivity(intent);
                    }
                });

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
