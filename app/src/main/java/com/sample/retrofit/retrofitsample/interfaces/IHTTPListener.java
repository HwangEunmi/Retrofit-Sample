package com.sample.retrofit.retrofitsample.interfaces;

// 통신에 쓰이는 interface
public interface IHTTPListener {

    // 성공
    public void onSuccess(final Object result);

    // 실패
    public void onFail(final int code, final String message, final Object result);

}
