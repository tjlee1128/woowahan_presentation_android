package com.baemin.woowahan_presentation_android.network;

import com.baemin.woowahan_presentation_android.model.CommentModel;
import com.baemin.woowahan_presentation_android.model.CommentsModel;
import com.baemin.woowahan_presentation_android.model.PresentationModel;
import com.baemin.woowahan_presentation_android.model.PresentationsModel;


import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by leetaejun on 2016. 2. 12..
 */
public interface PresentationService {
    @GET("api/presentations.json")
    Call<PresentationsModel> loadPresentations(@Query("category_id") Integer category_id, @Query("user_id") Integer user_id);

    @GET("api/presentations/{id}.json")
    Call<PresentationModel> loadPresentation(@Path("id") int id, @Query("user_id") int user_id);

    @GET("api/presentations/{presentation_id}/comments.json")
    Call<CommentsModel> loadCommentWithPresentationId(@Path("presentation_id") int presentation_id);

    @FormUrlEncoded
    @POST("api/presentations/{presentation_id}/comments.json")
    Call<CommentsModel> postComment(@Path("presentation_id") int presentation_id, @Field("user_id") int user_id, @Field("content") String content, @Field("comment_id") int comment_id);

    @FormUrlEncoded
    @POST("api/presentations/{presentation_id}/thumbsup.json")
    Call<String> thumbsUpPresentation(@Path("presentation_id") int presentation_id, @Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("api/presentations/{presentation_id}/thumbsdown.json")
    Call<String> thumbsDownPresentation(@Path("presentation_id") int presentation_id, @Field("user_id") int user_id);
}
