//package com.limepie.mvvmwanandroid.http;
//
//import android.util.ArrayMap;
//
//import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class HttpManager {
//
//    private Retrofit retrofit;
//    ArrayMap<Class<?>,Object> serviceArr = new ArrayMap();
//
//    public static HttpManager getInstance() {
//        return Inner.INSTANCE;
//    }
//
//    private HttpManager() {
//        retrofit = new Retrofit.Builder()
//                .baseUrl(Config.BASE_URL)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//    }
//
//    public <T> T getRequest(Class<T> clz) {
//        if(serviceArr.get(clz)!=null) {
//            return (T)serviceArr.get(clz);
//        } else {
//            T service = (T)retrofit.create(clz);
//            serviceArr.put(clz,service);
//            return service;
//        }
//    }
//
//    private static class Inner {
//        static HttpManager INSTANCE = new HttpManager();
//    }
//}
