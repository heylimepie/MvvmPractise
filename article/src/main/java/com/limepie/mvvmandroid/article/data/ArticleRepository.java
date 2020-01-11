package com.limepie.mvvmandroid.article.data;

import androidx.lifecycle.MutableLiveData;

import com.limepie.mvvmandroid.article.http.ApiService;
import com.limepie.mvvmandroid.article.model.Article;
import com.limepie.mvvmandroid.article.model.ArticleList;
import com.limepie.mvvmandroid.base.data.Response;
import com.limepie.mvvmandroid.base.data.local.LocalRepository;
import com.limepie.mvvmandroid.base.data.remote.RemoteRepository;
import com.limepie.mvvmandroid.base.util.HttpObserver;
import com.limepie.mvvmandroid.base.util.RxUtils;
import com.limepie.mvvmandroid.net.http.HttpManager;

import java.util.List;

public class ArticleRepository  implements LocalRepository<MutableLiveData<List<Article>>>, RemoteRepository<MutableLiveData<List<Article>>> {

    MutableLiveData<List<Article>> data = new MutableLiveData<>();
    int page;

    @Override
    public MutableLiveData<List<Article>> getLocalData() {
        return null;
    }

    @Override
    public MutableLiveData<List<Article>> getRemoteData() {

        HttpManager.getInstance().getDefaultRequest(ApiService.class)
                .getArticlesByPage(page)
                .compose(RxUtils.<Response<ArticleList>>applySchedulers())
                .subscribe(new HttpObserver<ArticleList>() {
                    @Override
                    protected void onSuccess(Response<ArticleList> response) {
                        data.setValue(response.data.getDatas());
                    }

                    @Override
                    protected void onFailure(String message) {

                    }
                });

        return data;
    }

    public void loadMore() {
        page ++ ;
        HttpManager.getInstance().getDefaultRequest(ApiService.class)
                .getArticlesByPage(page)
                .compose(RxUtils.<Response<ArticleList>>applySchedulers())
                .subscribe(new HttpObserver<ArticleList>() {
                    @Override
                    protected void onSuccess(Response<ArticleList> response) {
                        data.getValue().addAll(response.data.getDatas());
                        data.setValue(data.getValue());
                    }

                    @Override
                    protected void onFailure(String message) {

                    }
                });
    }

    public boolean cacheValidate() {
        return false;
    }
}
