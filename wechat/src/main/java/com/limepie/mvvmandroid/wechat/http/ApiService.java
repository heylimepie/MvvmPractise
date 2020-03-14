package com.limepie.mvvmandroid.wechat.http;

import com.limepie.mvvmandroid.base.data.Response;
import com.limepie.mvvmandroid.wechat.model.ArticleList;
import com.limepie.mvvmandroid.wechat.model.WeChat;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

//    https://wanandroid.com/wxarticle/chapters/json
    @GET("/wxarticle/chapters/json")
    Observable<Response<List<WeChat>>> getWeChat();

    @GET("/wxarticle/list/{id}/{pageIndex}/json")
    Observable<Response<ArticleList>> getSubArticles(@Path("id") int wechatId,@Path("pageIndex") int pageIndex);
}
