package com.limepie.mvvmandroid.net.http;

import android.util.ArrayMap;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {

    private Retrofit retrofit;
    public static final String BASE_RETROFIT = "base";
    ArrayMap<String, Retrofit> retrofitMap = new ArrayMap<>();
    ArrayMap<Class<?>, Object> serviceArr = new ArrayMap();

    public static HttpManager getInstance() {
        return Inner.INSTANCE;
    }

    private HttpManager() {
        initDefaultRetrofit();
    }

    private void initDefaultRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitMap.put(BASE_RETROFIT, retrofit);
    }

    public void createRetrofit(String retrofitName, String baseUrl) {
        Retrofit targetRetrofit = retrofit.newBuilder().baseUrl(baseUrl).build();
        retrofitMap.put(retrofitName, targetRetrofit);
    }

    public Retrofit getOrCreateRetrofit(String retrofitName, String baseUrl) {
        if (retrofitMap.get(retrofitName) != null) {
            return retrofitMap.get(retrofitName);
        } else {
            Retrofit targetRetrofit = retrofit.newBuilder().baseUrl(baseUrl).build();
            retrofitMap.put(retrofitName, targetRetrofit);
            return targetRetrofit;
        }
    }

    public <T> T getDefaultRequest(Class<T> clz) {
        if (serviceArr.get(clz) != null) {
            return (T) serviceArr.get(clz);
        } else {
            T service = (T) retrofit.create(clz);
            serviceArr.put(clz, service);
            return service;
        }
    }

    public <T> T getTargetRequest(Class<T> clz,String retrofitName,String baseUrl) {
        if (serviceArr.get(clz) != null) {
            return (T) serviceArr.get(clz);
        } else {
            T service = (T) getOrCreateRetrofit(retrofitName,baseUrl).create(clz);
            serviceArr.put(clz, service);
            return service;
        }
    }

    private static class Inner {
        static HttpManager INSTANCE = new HttpManager();
    }
}
