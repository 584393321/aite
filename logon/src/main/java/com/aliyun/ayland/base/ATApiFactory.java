package com.aliyun.ayland.base;

import com.aliyun.ayland.global.ATConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ATApiFactory {

    private static final int DEFAULT_TIME_OUT = 8;//超时时间 5s
    private static final int DEFAULT_READ_TIME_OUT = 10;
    private Retrofit mRetrofit;

    private ATApiFactory(){
        OkHttpClient.Builder okhttp = new OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(new ATTokenInterceptor());

        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
            .client(okhttp.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(ATConstants.Config.BASE_URL)
            .build();
    }


    private static class SingletonHolder {
        private static final ATApiFactory INSTANCE = new ATApiFactory();
    }

    public static ATApiFactory getInstance() {
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