package com.wts.chillarpay.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient mInstance;
    private String BASE_URL = "http://api.chillarpay.com/api/";
    public static String KING = "Basic JSQjJCUjU1RXXiYlJl42NTc2Y2hpbGxycGVeJio3Njg3NjhKS0gkXiVISkdKSDpeJiVeJiYmJiYlJSVjaGlsbHJwZTdeKiZXVFMmKl4mKjg4NzY3OCVeJiUmKg==";
    private retrofit2.Retrofit retrofit;

    private RetrofitClient() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(7000, TimeUnit.SECONDS)
                .readTimeout(7000, TimeUnit.SECONDS).build();
        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }
    public interfaceAPI getApi() {
        return retrofit.create(interfaceAPI.class);
    }
}
