package com.limepie.mvvmandroid.article.http;

import com.limepie.mvvmandroid.article.model.ArticleList;
import com.limepie.mvvmandroid.base.data.Response;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {


    @GET("/article/list/{id}/json")
    Observable<Response<ArticleList>> getArticlesByPage(@Path(("id")) int pageIdx);

}
