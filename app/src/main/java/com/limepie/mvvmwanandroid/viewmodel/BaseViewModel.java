package com.limepie.mvvmwanandroid.viewmodel;

import androidx.lifecycle.ViewModel;

import com.limepie.mvvmwanandroid.data.Repository;
import com.limepie.mvvmwanandroid.data.local.LocalRepository;
import com.limepie.mvvmwanandroid.data.remote.RemoteRepository;

public class BaseViewModel extends ViewModel {
    Repository repository;
}
