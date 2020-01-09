package com.limepie.mvvmwanandroid;

import android.util.ArrayMap;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.limepie.mvvmwanandroid.viewmodel.ChapterViewModel;

public class BaseFragment extends Fragment {
    ArrayMap<Class<?>, Object> viewModelArr = new ArrayMap<>();


    protected <T extends ViewModel> T getViewModel(Class<T> clz) {
        if (viewModelArr.get(clz) != null) {
            return (T) viewModelArr.get(clz);
        } else {
            T viewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(clz);
            viewModelArr.put(clz, viewModel);
            return viewModel;
        }
    }

}
