package com.baemin.woowahan_presentation_android.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.model.CategoryModel;
import com.baemin.woowahan_presentation_android.model.TeamModel;
import com.baemin.woowahan_presentation_android.network.CategoryService;
import com.baemin.woowahan_presentation_android.network.ServiceGenerator;
import com.baemin.woowahan_presentation_android.network.TeamService;
import com.baemin.woowahan_presentation_android.user.SignInActivity;
import com.baemin.woowahan_presentation_android.util.PreferencesManager;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SplashActivity extends AppCompatActivity {

    private Callback<List<CategoryModel>> mCategoryCallback = new Callback<List<CategoryModel>>() {
        @Override
        public void onResponse(Response<List<CategoryModel>> response, Retrofit retrofit) {
            TeamService teamService = ServiceGenerator.createService(TeamService.class);
            Call<List<TeamModel>> call = teamService.loadTeams();
            call.enqueue(mTeamCallback);

            PreferencesManager.getInstance().setCategories(response.body());
        }

        @Override
        public void onFailure(Throwable t) {
            Toast.makeText(SplashActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private Callback<List<TeamModel>> mTeamCallback = new Callback<List<TeamModel>>() {
        @Override
        public void onResponse(Response<List<TeamModel>> response, Retrofit retrofit) {
            PreferencesManager.getInstance().setTeams(response.body());

            Intent intent;
            if (PreferencesManager.getInstance().getAccessToken() == null) {
                intent = new Intent(SplashActivity.this, SignInActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }

            shimmer.cancel();
            startActivity(intent);
            finish();
        }

        @Override
        public void onFailure(Throwable t) {
            shimmer.cancel();
            Toast.makeText(SplashActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
    private Shimmer shimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        shimmer = new Shimmer();
        shimmer.start((ShimmerTextView) findViewById(R.id.activity_splash_shimmer_tv));
    }

    @Override
    protected void onResume() {
        super.onResume();

        CategoryService categoryService = ServiceGenerator.createService(CategoryService.class);
        Call<List<CategoryModel>> call = categoryService.loadCategories();

        call.enqueue(mCategoryCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
