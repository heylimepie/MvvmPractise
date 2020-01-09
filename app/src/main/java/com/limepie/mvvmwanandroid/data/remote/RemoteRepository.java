package com.limepie.mvvmwanandroid.data.remote;

import com.limepie.mvvmwanandroid.data.Repository;

public interface RemoteRepository<T> {
    T getRemoteData();
}
