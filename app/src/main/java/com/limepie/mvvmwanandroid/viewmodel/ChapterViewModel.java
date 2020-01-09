package com.limepie.mvvmwanandroid.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.limepie.mvvmwanandroid.data.ChapterRepository;
import com.limepie.mvvmwanandroid.model.Chapter;

import java.util.List;

public class ChapterViewModel extends BaseViewModel {
    ChapterRepository repository = new ChapterRepository();

    private MutableLiveData<List<Chapter>> chapters;

    public LiveData<List<Chapter>> getChapters() {
        if (chapters == null) {
            loadChapters();
        }
        return chapters;
    }

    private void loadChapters() {
        if (repository.cacheValidate()) {
            chapters= repository.getLocalData();
        } else {
            chapters = repository.getRemoteData();
        }

    }

}
