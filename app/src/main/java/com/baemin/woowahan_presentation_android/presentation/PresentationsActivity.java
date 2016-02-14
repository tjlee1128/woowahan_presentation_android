package com.baemin.woowahan_presentation_android.presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.model.PresentationModel;
import com.baemin.woowahan_presentation_android.model.PresentationsModel;
import com.baemin.woowahan_presentation_android.network.PresentationService;
import com.baemin.woowahan_presentation_android.network.ServiceGenerator;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PresentationsActivity extends AppCompatActivity {

    @Bind(R.id.activity_presentations_lv)
    ListView presentationsListView;
    private PresentationsModel mPresentationsModel;
    private Callback<PresentationsModel> mCallback = new Callback<PresentationsModel>() {
        @Override
        public void onResponse(Response<PresentationsModel> response, Retrofit retrofit) {
            mPresentationsModel = response.body();
            presentationsListView.setAdapter(new PresentationsAdapter(PresentationsActivity.this, mPresentationsModel.getRows()));
        }

        @Override
        public void onFailure(Throwable t) {
            Toast.makeText(PresentationsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentations);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        PresentationService presentationService = ServiceGenerator.createService(PresentationService.class);
        Call<PresentationsModel> call = presentationService.loadPresentations(1);

        call.enqueue(mCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
