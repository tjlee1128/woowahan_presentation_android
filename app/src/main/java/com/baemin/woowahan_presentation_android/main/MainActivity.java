package com.baemin.woowahan_presentation_android.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.baemin.woowahan_presentation_android.presentation.PresentationsActivity;
import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.util.Constants;
import com.baemin.woowahan_presentation_android.util.PreferencesManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.activity_main_category_lv)
    ListView categoriesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.start_right_left_enter, R.anim.start_right_left_exit);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

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
}
