package com.example.brendan.mainpackage.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by brendan on 1/27/17.
 */

//TODO- Get connected to service with given token as header
public class RetroClient {

    private static String TAG = RetroClient.class.getName();
    private static Retrofit retrofit = null;
    private static String token = "uamXrnjjrtNOgHDfeVYNBJthOJKiDqto";

    public static Retrofit getClient(String baseUrl) {
        Log.v(TAG,"Inside getClient");
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
        Gson gson = new GsonBuilder()

                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")

                .create();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addNetworkInterceptor(logger);
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("token", token);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        builder.addInterceptor(interceptor);
        OkHttpClient client = builder.build();
        if(retrofit == null){
            retrofit =new  Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}
