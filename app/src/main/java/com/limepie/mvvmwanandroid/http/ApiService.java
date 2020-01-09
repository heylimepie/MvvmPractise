package com.limepie.mvvmwanandroid.http;

import com.limepie.mvvmwanandroid.model.ArticleList;
import com.limepie.mvvmwanandroid.model.BannerVo;
import com.limepie.mvvmwanandroid.model.Chapter;
import com.limepie.mvvmwanandroid.model.Response;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/wxarticle/chapters/json")
    Observable<Response<List<Chapter>>> getChapterInfo();

    @GET("/article/list/{id}/json")
    Observable<Response<ArticleList>> getArticlesByPage(@Path(("id")) int pageIdx);

    @GET("/banner/json")
    Observable<Response<List<BannerVo>>> getBannerInfo();
}
