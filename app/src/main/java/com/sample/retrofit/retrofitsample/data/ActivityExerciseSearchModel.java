package com.sample.retrofit.retrofitsample.data;

import com.google.gson.annotations.Expose;

// GET 방식 데이터 모델
public class ActivityExerciseSearchModel {

    // Request Model
    public static class RQ {

    }

    // Response Model
    public static class RS extends BaseResponseModel {

        @Expose
        public String exerciseType;

        @Expose
        public String exerciseName;

    }
}
