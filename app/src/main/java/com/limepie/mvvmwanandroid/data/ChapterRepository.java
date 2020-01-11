package com.limepie.mvvmwanandroid.data;

import androidx.lifecycle.MutableLiveData;

import com.limepie.mvvmandroid.net.http.HttpManager;
import com.limepie.mvvmwanandroid.data.local.LocalRepository;
import com.limepie.mvvmwanandroid.data.remote.RemoteRepository;
import com.limepie.mvvmwanandroid.http.ApiService;
import com.limepie.mvvmwanandroid.model.Chapter;
import com.limepie.mvvmwanandroid.model.Response;
import com.limepie.mvvmwanandroid.util.HttpObserver;
import com.limepie.mvvmwanandroid.util.RxUtils;

import java.util.List;

public class ChapterRepository implements LocalRepository<MutableLiveData<List<Chapter>>>, RemoteRepository<MutableLiveData<List<Chapter>>> {
    boolean needCache;

    MutableLiveData<List<Chapter>> data = new MutableLiveData<>();

    @Override
    public MutableLiveData<List<Chapter>> getLocalData() {
        return data;
    }

    @Override
    public MutableLiveData<List<Chapter>> getRemoteData() {
        HttpManager.getInstance().getDefaultRequest(ApiService.class)
                .getChapterInfo()
                .compose(RxUtils.<Response<List<Chapter>>>applySchedulers())
                .subscribe(new HttpObserver<List<Chapter>>() {
                    @Override
                    protected void onSuccess(Response<List<Chapter>> response) {
                        data.setValue(response.data);

                    }

                    @Override
                    protected void onFailure(String message) {

                    }
                });
        return data;
    }

    public boolean cacheValidate() {
        return false;
    }
}
