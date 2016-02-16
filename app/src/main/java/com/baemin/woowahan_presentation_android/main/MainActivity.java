package com.baemin.woowahan_presentation_android.main;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baemin.woowahan_presentation_android.presentation.PresentationsActivity;
import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.util.Constants;
import com.baemin.woowahan_presentation_android.util.PreferencesManager;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    // ToolBar
    @Bind(R.id.activity_main_toolbar_include)
    View toolbarView;

    // Drawer
    View drawerHeaderView;
    RoundedImageView userImageView;
    TextView userTextView;
    TextView teamTextView;
    @Bind(R.id.activity_main_dl)
    DrawerLayout drawerLayout;
    @Bind(R.id.activity_main_drawer_lv)
    ListView drawerListView;

    // Main List
    @Bind(R.id.activity_main_category_lv)
    ListView categoriesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.start_right_left_enter, R.anim.start_right_left_exit);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initializeToolBar();
        initializeDrawer();

        categoriesListView.setAdapter(new MainCategoriesAdapter(MainActivity.this, PreferencesManager.getInstance().getCategories()));
        categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, PresentationsActivity.class);
                intent.putExtra(Constants.EXTRA_CATEGORY_ID, PreferencesManager.getInstance().getCategories().get(position).getId());
                intent.putExtra(Constants.EXTRA_CATEGORY_NAME, PreferencesManager.getInstance().getCategories().get(position).getName());
                startActivity(intent);
            }
        });
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
        drawerList.add("관심 자료");

        drawerListView.addHeaderView(drawerHeaderView);
        drawerListView.setAdapter(new MainDrawerAdapter(MainActivity.this, drawerList));
    }
}
