package com.limepie.mvvmandroid.wechat.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.limepie.mvvmandroid.base.viewmodel.BaseViewModel;
import com.limepie.mvvmandroid.wechat.data.WeChatRepository;
import com.limepie.mvvmandroid.wechat.model.WeChat;

import java.util.List;

public class WeChatViewModel extends BaseViewModel {

    WeChatRepository repository = new WeChatRepository();

    private MutableLiveData<List<WeChat>> weChats;

    public LiveData<List<WeChat>> getWeChats() {
        if (weChats == null) {
            loadWeChats();
        }
        return weChats;
    }

    private void loadWeChats() {
        if (repository.cacheValidate()) {
            weChats= repository.getLocalData();
        } else {
            weChats = repository.getRemoteData();
        }
    }

}
