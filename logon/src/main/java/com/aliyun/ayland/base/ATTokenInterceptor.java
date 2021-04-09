package com.aliyun.ayland.base;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by fr on 2018/3/27.
 */

public class ATTokenInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response originalResponse = chain.proceed(request);

        String url = request.url().toString();//请求Url

        //获取返回的json，response.body().string();只有效一次，对返回数据进行转换
        ResponseBody responseBody = originalResponse.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(Charset.forName("UTF-8"));
        }
        String bodyString = buffer.clone().readString(charset);//首次请求返回的结果

        if (isTokenExpired(bodyString)) {//根据和服务端的约定判断token过期
            //同步请求方式，获取最新的Token
//            TokenEntity tokenEntity = getNewToken();
//            //使用新的Token，创建新的请求
//            if (request.body() instanceof FormBody) {
//                FormBody.Builder newFormBody = new FormBody.Builder();
//                FormBody oidFormBody = (FormBody) request.body();
//                for (int i = 0; i < oidFormBody.size(); i++) {
//                    //根据需求修改参数
//                    if ("需要更新的token参数字段".equals(oidFormBody.encodedName(i))) {
//                        newFormBody.addEncoded(oidFormBody.encodedName(i), tokenEntity.getToken());
//                    } else {
//                        newFormBody.addEncoded(oidFormBody.encodedName(i), oidFormBody.encodedValue(i));
//                    }
//                }
//                Request.Builder builder = request.newBuilder();
//                builder.url(url);
//                builder.method(request.method(), newFormBody.build());
//                request = builder.build();
//            }
//            originalResponse.body().close();
//            //重新请求
//            return chain.proceed(request);
        }
        return originalResponse;
    }

    private boolean isTokenExpired(String response) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
//
//    /**
//     * 同步请求方式，获取最新的Token
//     */
//    private LoginEntity getNewToken() throws IOException {
//        // 通过一个特定的接口获取新的token，此处要用到同步的retrofit请求
//        Call<LoginEntity> loginCall = RetrofitHelper.getInstance().getRetrofit(Api.class)
//                .getToken("参数");
//        return loginCall.execute().body();
//    }
}
