package com.example.rsrtcs.repository.remote;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RSRTCConnection {
    public String BASE_URL="https://rsrtcrfidsystem.co.in/rsrtcapi/";
    public String SMS_BASE_URL="http://Services.m-techinnovations.com/SMSSendService/SMSSendService.svc/";


    public RSRTCInterface createService()  {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);//
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(1, TimeUnit.MINUTES);

        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit.create(RSRTCInterface.class);
    }


    public RSRTCInterface createSMSService()  {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);//
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(1, TimeUnit.MINUTES);

        httpClient.networkInterceptors().add(new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder requestBuilder = chain.request().newBuilder();
                requestBuilder.header("Content-Type","text/plain");
                return chain.proceed(requestBuilder.build());
            }
        });

        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SMS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit.create(RSRTCInterface.class);
    }
}
