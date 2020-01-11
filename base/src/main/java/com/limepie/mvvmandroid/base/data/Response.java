package com.limepie.mvvmandroid.base.data;

public class Response<T> {
    public int errorCode;
    public String errorMsg;
    public T data;
}
