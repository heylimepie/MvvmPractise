package com.limepie.mvvmwanandroid.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.limepie.mvvmwanandroid.data.BannerRepository;
import com.limepie.mvvmwanandroid.model.BannerVo;

import java.util.List;

public class BannerViewModel extends BaseViewModel {

    BannerRepository repository = new BannerRepository();

    private MutableLiveData<List<BannerVo>> banners;

    public LiveData<List<BannerVo>> getBanners() {
        if (banners == null) {
            loadBanners();
        }
        return banners;
    }

    private void loadBanners() {
        if (repository.cacheValidate()) {
            banners= repository.getLocalData();
        } else {
            banners = repository.getRemoteData();
        }

    }
}
