package com.limepie.mvvmwanandroid.data.local;

import com.limepie.mvvmwanandroid.data.Repository;

public interface LocalRepository<T> {

    T getLocalData();
}
