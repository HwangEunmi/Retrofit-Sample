package com.sample.retrofit.retrofitsample.data;

import com.google.gson.annotations.Expose;

/*프로필 정보 모델*/
public class ProfileInfoModel {

    @Expose
    private String gender;

    @Expose
    private String nickName;

    @Expose
    private int weight;

    @Expose
    private int height;


    public ProfileInfoModel(final String gender, final String nickName, final int weight, final int height) {
        this.gender = gender;
        this.nickName = nickName;
        this.weight = weight;
        this.height = height;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
