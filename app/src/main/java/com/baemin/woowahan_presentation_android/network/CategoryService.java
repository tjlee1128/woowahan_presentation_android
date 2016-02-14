package com.baemin.woowahan_presentation_android.network;

import com.baemin.woowahan_presentation_android.model.CategoryModel;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by leetaejun on 2016. 2. 14..
 */
public interface CategoryService {
    @GET("api/categories.json")
    Call<List<CategoryModel>> loadCategories();
}
