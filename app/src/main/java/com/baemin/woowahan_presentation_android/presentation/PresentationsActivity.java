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

        PresentationService presentationService = ServiceGenerator.createService(PresentationService.class);
        Call<PresentationsModel> call = presentationService.loadPresentations();

        call.enqueue(mCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_presentations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
