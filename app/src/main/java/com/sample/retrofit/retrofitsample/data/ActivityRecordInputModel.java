package com.sample.retrofit.retrofitsample.data;

import com.google.gson.annotations.Expose;

// POST 방식 데이터 모델
public class ActivityRecordInputModel {

    // Request Model
    public static class RQ {

        @Expose
        public int exerciseId;

        @Expose
        public String exerciseName;


        public RQ(final int exerciseId, final String exerciseName) {
            this.exerciseId = exerciseId;
            this.exerciseName = exerciseName;
        }

    }

    // Response Model
    public static class RS extends BaseResponseModel {

        @Expose
        public int calory;

    }

}
