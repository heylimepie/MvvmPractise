package com.limepie.mvvmandroid.project.http;

import com.limepie.mvvmandroid.base.data.Response;
import com.limepie.mvvmandroid.project.model.Article;
import com.limepie.mvvmandroid.project.model.ArticleList;
import com.limepie.mvvmandroid.project.model.Project;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/project/tree/json")
    Observable<Response<List<Project>>> getProject();

    @GET("/project/list/{pageIdx}/json")
    Observable<Response<ArticleList>> getSubProject(@Path("pageIdx") int pageIdx, @Query("cid") int cid);
}
