package com.limepie.mvvmwanandroid.data;

import com.limepie.mvvmwanandroid.data.local.LocalRepository;
import com.limepie.mvvmwanandroid.data.remote.RemoteRepository;

public interface Repository<T> {
     T getData();
}
