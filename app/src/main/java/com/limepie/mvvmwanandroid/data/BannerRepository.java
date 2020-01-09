package com.limepie.mvvmwanandroid.data;

import androidx.lifecycle.MutableLiveData;

import com.limepie.mvvmwanandroid.data.local.LocalRepository;
import com.limepie.mvvmwanandroid.data.remote.RemoteRepository;
import com.limepie.mvvmwanandroid.http.ApiService;
import com.limepie.mvvmwanandroid.http.HttpManager;
import com.limepie.mvvmwanandroid.model.BannerVo;
import com.limepie.mvvmwanandroid.model.Response;
import com.limepie.mvvmwanandroid.util.HttpObserver;
import com.limepie.mvvmwanandroid.util.RxUtils;

import java.util.List;

public class BannerRepository  implements RemoteRepository<MutableLiveData<List<BannerVo>>>,LocalRepository<MutableLiveData<List<BannerVo>>> {
    boolean needCache;

    MutableLiveData<List<BannerVo>> data = new MutableLiveData<>();

    @Override
    public MutableLiveData<List<BannerVo>> getLocalData() {
        return null;
    }

    @Override
    public MutableLiveData<List<BannerVo>> getRemoteData() {
        HttpManager.getInstance().getRequest(ApiService.class)
                .getBannerInfo()
                .compose(RxUtils.<Response<List<BannerVo>>>applySchedulers())
                .subscribe(new HttpObserver<List<BannerVo>>() {
                    @Override
                    protected void onSuccess(Response<List<BannerVo>> response) {
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
