package com.redli.tmvpsimple.http;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by redli on 2017/3/30.
 */

public class Api {
    private static ApiService SERVICE;

    /**
     * 请求超时时间
     */
    private static final int DEFAULT_TIMEOUT = 5;

    public static ApiService getDefault(){

        /**
         * 手动创建一个OkHttpClient并设置超时时间
         */
        OkHttpClient.Builder httpClientBuilder =  new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        /**
         * 对所有请求添加请求头(全局header,可局部动态添加header)
         */
        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                okhttp3.Response originalResponse = chain.proceed(request);
                return originalResponse.newBuilder().header("key1", "value1").addHeader("key2", "value2").build();
            }
        });

        SERVICE = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Url.BASE_URL)
                .build().create(ApiService.class);

        return SERVICE;
    }

}
