package com.thedach.moves;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    private static final String BASE_URL = "https://api.poiskkino.dev/v1.4/";

    public static final String API_KEY = "Your Api key"; // TODO("Write your api key")

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                Request originalRequest = chain.request();
                Request newRequest = originalRequest.newBuilder()
                        .removeHeader("Accept-Encoding")
                        .build();
                return chain.proceed(newRequest);
            })
            .build();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build();


    /// Краткая запись single-Ton
    /// Используется только в том случае, если нам не нужно добавлять параметры в момент
    /// создания single-Ton
    public static final ApiService apiService = retrofit.create(ApiService.class);
}
