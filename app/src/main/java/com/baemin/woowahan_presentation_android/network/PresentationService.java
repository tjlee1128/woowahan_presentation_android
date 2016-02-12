package com.baemin.woowahan_presentation_android.network;

import com.baemin.woowahan_presentation_android.model.PresentationModel;
import com.baemin.woowahan_presentation_android.model.PresentationsModel;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by leetaejun on 2016. 2. 12..
 */
public interface PresentationService {
    @GET("api/presentations.json")
    Call<PresentationsModel> loadPresentations();

    @GET("api/presentations/{id}.json")
    Call<PresentationModel> loadPresentation(@Path("id") int id);
}
