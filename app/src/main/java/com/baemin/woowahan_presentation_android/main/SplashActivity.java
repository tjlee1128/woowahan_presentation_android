package com.baemin.woowahan_presentation_android.main;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.model.CategoriesModel;
import com.baemin.woowahan_presentation_android.model.CategoryModel;
import com.baemin.woowahan_presentation_android.model.TeamModel;
import com.baemin.woowahan_presentation_android.model.User;
import com.baemin.woowahan_presentation_android.model.UserModel;
import com.baemin.woowahan_presentation_android.model.UsersModel;
import com.baemin.woowahan_presentation_android.network.CategoryService;
import com.baemin.woowahan_presentation_android.network.ServiceGenerator;
import com.baemin.woowahan_presentation_android.network.TeamService;
import com.baemin.woowahan_presentation_android.network.UserService;
import com.baemin.woowahan_presentation_android.user.SignInActivity;
import com.baemin.woowahan_presentation_android.util.PreferencesManager;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    private Callback<CategoriesModel> mCategoryCallback = new Callback<CategoriesModel>() {
        @Override
        public void onResponse(Response<CategoriesModel> response, Retrofit retrofit) {
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

            UserService userService = ServiceGenerator.createService(UserService.class);
            Call<UsersModel> call = userService.loadUsers();
            call.enqueue(mUsersCallback);
        }

        @Override
        public void onFailure(Throwable t) {
            Toast.makeText(SplashActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private Callback<UsersModel> mUsersCallback = new Callback<UsersModel>() {
        @Override
        public void onResponse(Response<UsersModel> response, Retrofit retrofit) {

            Realm realm = Realm.getDefaultInstance();

            RealmResults<User> results = realm.where(User.class).findAll();

            Log.i("results", "" + results.size());

            List<UserModel> list = response.body().getRows();
            for (int i = results.size(); i < list.size(); i++) {
                realm.beginTransaction();
                User user = realm.createObject(User.class); // Create a new object
                user.setId(list.get(i).getId());
                user.setEmail(list.get(i).getEmail());
                user.setFullname(list.get(i).getFullname());
                user.setTeam_id(list.get(i).getTeam_id());
                user.setTeam_name(list.get(i).getTeam_name());
                user.setImage(list.get(i).getImage().getThumb_url());
                user.setPresentations_count(list.get(i).getPresentations_count());
                realm.commitTransaction();
            }

            Log.i("results", "" + results.size());

            Intent intent;
            if (PreferencesManager.getInstance().getAccessToken() == null) {
                intent = new Intent(SplashActivity.this, SignInActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }

            realm.close();

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CategoryService categoryService = ServiceGenerator.createService(CategoryService.class);
//                Call<List<CategoryModel>> call = categoryService.loadCategories();
                Call<CategoriesModel> call = categoryService.loadCategoriesWithPresentation();

                call.enqueue(mCategoryCallback);
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
