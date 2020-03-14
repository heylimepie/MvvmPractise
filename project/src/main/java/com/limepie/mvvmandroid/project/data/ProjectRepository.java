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
import com.limepie.mvvmandroid.project.model.Project;

import java.util.List;

public class ProjectRepository implements LocalRepository<MutableLiveData<List<Project>>>, RemoteRepository<MutableLiveData<List<Project>>> {

    MutableLiveData<List<Project>> data = new MutableLiveData<>();

    @Override
    public MutableLiveData<List<Project>> getLocalData() {
        return null;
    }

    @Override
    public MutableLiveData<List<Project>> getRemoteData() {
        HttpManager.getInstance().getDefaultRequest(ApiService.class)
                .getProject()
                .compose(RxUtils.<Response<List<Project>>>applySchedulers())
                .subscribe(new HttpObserver<List<Project>>() {
                    @Override
                    protected void onSuccess(Response<List<Project>> response) {
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
