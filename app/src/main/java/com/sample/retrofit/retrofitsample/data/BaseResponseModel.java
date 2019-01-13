package com.sample.retrofit.retrofitsample.data;

/*공통 Response 모델*/
public class BaseResponseModel {

    private int code;

    private String message;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
