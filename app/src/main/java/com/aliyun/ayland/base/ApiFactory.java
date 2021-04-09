package com.aliyun.ayland.base;

import com.aliyun.ayland.global.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiFactory {

    private static final int DEFAULT_TIME_OUT = 8;//超时时间 5s
    private static final int DEFAULT_READ_TIME_OUT = 10;
    private Retrofit mRetrofit;

    private ApiFactory(){
        OkHttpClient.Builder okhttp = new OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(new LoggingInterceptor());

        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
            .client(okhttp.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.Config.BASE_URL)
            .build();
    }


    private static class SingletonHolder {
        private static final ApiFactory INSTANCE = new ApiFactory();
    }

    public static ApiFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取对应的Service
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }
}