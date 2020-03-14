package com.limepie.mvvmandroid.wechat.data;

import androidx.lifecycle.MutableLiveData;

import com.limepie.mvvmandroid.base.data.Response;
import com.limepie.mvvmandroid.base.data.local.LocalRepository;
import com.limepie.mvvmandroid.base.data.remote.RemoteRepository;
import com.limepie.mvvmandroid.base.util.HttpObserver;
import com.limepie.mvvmandroid.base.util.RxUtils;
import com.limepie.mvvmandroid.net.http.HttpManager;
import com.limepie.mvvmandroid.wechat.http.ApiService;
import com.limepie.mvvmandroid.wechat.model.WeChat;

import java.util.List;

public class WeChatRepository implements LocalRepository<MutableLiveData<List<WeChat>>>, RemoteRepository<MutableLiveData<List<WeChat>>> {

    MutableLiveData<List<WeChat>> data = new MutableLiveData<>();

    @Override
    public MutableLiveData<List<WeChat>> getLocalData() {
        return null;
    }

    @Override
    public MutableLiveData<List<WeChat>> getRemoteData() {

        HttpManager.getInstance().getDefaultRequest(ApiService.class)
                .getWeChat()
                .compose(RxUtils.<Response<List<WeChat>>>applySchedulers())
                .subscribe(new HttpObserver<List<WeChat>>() {
                    @Override
                    protected void onSuccess(Response<List<WeChat>> response) {
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
