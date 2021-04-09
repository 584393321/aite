package com.aliyun.ayland.api;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by fr on 2018/7/26.
 */

public interface ATLoginService {
    @POST("/villagecenter/api/codeToRegister")
    Observable<String> codeToRegister(@FieldMap Map<String, String> params);
}
