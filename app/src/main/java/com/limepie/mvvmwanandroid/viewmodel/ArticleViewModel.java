package com.limepie.mvvmwanandroid.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.limepie.mvvmwanandroid.data.ArticleRepository;
import com.limepie.mvvmwanandroid.model.Article;

import java.util.List;

public class ArticleViewModel extends BaseViewModel {

    ArticleRepository repository = new ArticleRepository();

    private MutableLiveData<List<Article>> articles;

    public LiveData<List<Article>> getArticles() {
        if (articles == null) {
            loadArticles();
        }
        return articles;
    }

    private void loadArticles() {
        if (repository.cacheValidate()) {
            articles= repository.getLocalData();
        } else {
            articles = repository.getRemoteData();
        }
    }

    public void loadMore() {
        repository.loadMore();
    }
}
