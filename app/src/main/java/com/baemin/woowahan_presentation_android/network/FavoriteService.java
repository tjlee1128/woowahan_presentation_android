package com.baemin.woowahan_presentation_android.network;

import com.baemin.woowahan_presentation_android.model.PresentationsModel;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by leetaejun on 2016. 2. 19..
 */
public interface FavoriteService {
    @GET("api/favorites.json")
    Call<PresentationsModel> loadFavoritePresentation();
}
