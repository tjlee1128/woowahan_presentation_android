package com.baemin.woowahan_presentation_android.network;

import com.baemin.woowahan_presentation_android.util.Constants;
import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by leetaejun on 2016. 2. 12..
 */
public class ServiceGenerator {
    private static OkHttpClient httpClient  = new OkHttpClient();
    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Constants.API_SERVER_BASE_URL).addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }
}