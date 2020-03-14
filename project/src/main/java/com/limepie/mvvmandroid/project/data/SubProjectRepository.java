package com.limepie.mvvmandroid.project.data;

import androidx.lifecycle.MutableLiveData;

import com.limepie.mvvmandroid.base.data.Response;
import com.limepie.mvvmandroid.base.data.local.LocalRepository;
import com.limepie.mvvmandroid.base.data.remote.RemoteRepository;
import com.limepie.mvvmandroid.base.util.HttpObserver;
import com.limepie.mvvmandroid.base.util.RxUtils;
import com.limepie.mvvmandroid.net.http.HttpManager;
import com.limepie.mvvmandroid.project.http.ApiService;
import com.limepie.mvvmandroid.project.model.Article;
import com.limepie.mvvmandroid.project.model.ArticleList;

import java.util.List;

public class SubProjectRepository implements LocalRepository<MutableLiveData<List<Article>>>, RemoteRepository<MutableLiveData<List<Article>>> {

    MutableLiveData<List<Article>> data = new MutableLiveData<>();
    int page;
    int id = -1;

    @Override
    public MutableLiveData<List<Article>> getLocalData() {
        return null;
    }

    @Override
    public MutableLiveData<List<Article>> getRemoteData() {
        if(id<0) {
            throw new RuntimeException("id should be available");
        }
        HttpManager.getInstance().getDefaultRequest(ApiService.class)
                .getSubProject(page,id)
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

    public SubProjectRepository setId(int id) {
        this.id = id;
        return this;
    }

    public boolean cacheValidate() {
        return false;
    }

    public void loadMore() {
        page ++ ;
        HttpManager.getInstance().getDefaultRequest(ApiService.class)
                .getSubProject(page,id)
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
}
