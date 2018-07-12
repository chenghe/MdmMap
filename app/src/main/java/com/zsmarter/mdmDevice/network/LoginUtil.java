package com.zsmarter.mdmDevice.network;

import com.zsmarter.mdmdevice.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hecheng on 2018/6/22
 */
public class LoginUtil {
    private static NetUrl netUrl;
//    private static final String BASE_URL = "http://192.168.1.126/";
    private static final String BASE_URL = BuildConfig.WEB_BASE_MSP_URL;
    private static final int DEFAULT_TIMEOUT = 9000;
    private OkHttpClient okHttpClient;

    public static NetUrl getNetWork() {
        if (netUrl == null) {
            netUrl = new LoginUtil().initRetrofit();
        }

        return netUrl;
    }

    private LoginUtil() {
        initRetrofit();
    }

    private void initOkHttp() {

        //设置Log,将会以title 为 OkHttp 打印URL Log
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .build();
    }

    private NetUrl initRetrofit() {

        initOkHttp();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        netUrl = retrofit.create(NetUrl.class);
        return netUrl;
    }
}
