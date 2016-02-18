package com.baemin.woowahan_presentation_android.network;

import com.baemin.woowahan_presentation_android.model.AuthenticationModel;
import com.baemin.woowahan_presentation_android.model.PresentationsModel;
import com.baemin.woowahan_presentation_android.model.User;
import com.baemin.woowahan_presentation_android.model.UserModel;
import com.baemin.woowahan_presentation_android.model.UsersModel;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by leetaejun on 2016. 2. 14..
 */

/*
    post :modify
    post :authentication
    post :signup
    post :signin
*/

public interface UserService {

    @GET("users.json")
    Call<UsersModel> loadUsers();

    @GET("api/users.json")
    Call<UserModel> loadUserInfo(@Query("access_token") String access_token);

    @POST("api/users/modify.json")
    Call<AuthenticationModel> modifyUserInfo(@Body UserModel user);

    @FormUrlEncoded
    @POST("api/users/authentication.json")
    Call<AuthenticationModel> loadAuthentication(@Field("access_token") String access_token);

    @POST("api/users/signup.json")
    Call<AuthenticationModel> signupUser(@Body UserModel user);

    @FormUrlEncoded
    @POST("api/users/signin.json")
    Call<AuthenticationModel> signinUser(@Field("email") String email, @Field("password") String password);

    @GET("api/users/presentations.json")
    Call<PresentationsModel> loadUserPresentations(@Query("access_token") String access_token);

    @GET("api/users/favorite_presentations.json")
    Call<PresentationsModel> loadUserFavoritePresentations(@Query("access_token") String access_token);
}
