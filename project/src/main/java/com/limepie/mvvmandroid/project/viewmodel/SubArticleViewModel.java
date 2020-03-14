package com.limepie.mvvmandroid.project.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.limepie.mvvmandroid.base.viewmodel.BaseViewModel;
import com.limepie.mvvmandroid.project.data.SubProjectRepository;
import com.limepie.mvvmandroid.project.model.Article;

import java.util.List;

public class SubArticleViewModel extends BaseViewModel {

    SubProjectRepository repository = new SubProjectRepository();

    private MutableLiveData<List<Article>> articles;

    public void setId(int id) {
        repository.setId(id);
    }

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
