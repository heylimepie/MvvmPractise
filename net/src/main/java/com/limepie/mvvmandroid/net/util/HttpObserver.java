package com.limepie.mvvmandroid.net.util;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.limepie.mvvmandroid.base.data.Response;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class HttpObserver<T> implements Observer<Response<T>> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Response<T> tResponse) {
        if (tResponse.data != null) {
            onSuccess(tResponse);
        } else {
            onFailure(tResponse.errorMsg);
        }
    }

    @Override
    public void onError(Throwable e) {
        String message;
        if (e instanceof ConnectException) {
            message = "链接异常";
        } else if (e instanceof SocketTimeoutException) {
            message = "链接超时";
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
            message = "数据解析失败";
        } else if (e instanceof UnknownHostException) {
            message = "域名解析失败";
        } else {
            message = "请求失败";
        }
        onFailure(message);
    }

    @Override
    public void onComplete() {

    }

    protected abstract void onSuccess(Response<T> response);

    protected abstract void onFailure(String message);
}
