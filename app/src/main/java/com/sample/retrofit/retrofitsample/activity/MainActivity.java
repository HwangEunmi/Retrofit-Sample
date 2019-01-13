package com.sample.retrofit.retrofitsample.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sample.retrofit.retrofitsample.MyApplication;
import com.sample.retrofit.retrofitsample.R;
import com.sample.retrofit.retrofitsample.data.ActivityExerciseSearchModel;
import com.sample.retrofit.retrofitsample.data.ActivityRecordInputModel;
import com.sample.retrofit.retrofitsample.data.ProfileInfoModel;
import com.sample.retrofit.retrofitsample.data.ProfileReplaceModel;
import com.sample.retrofit.retrofitsample.interfaces.IHTTPListener;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        // GET Request 예제
        MyApplication.getInstance().getApiManager().getApiResponse(ActivityExerciseSearchModel.RS.class,
                MyApplication.getInstance().getApiManager().getApiService(context).getExerciseSearchList(""),
                exerciseSearchListener);

        // POST Request 예제
        // JSON -> String 으로 변환하여 RequestBody에 넣기
        final String data = MyApplication.getInstance().getGson().toJson(new ActivityRecordInputModel.RQ(1, "running")); // 현재 Retrofit 통신을 JSON이 아닌 String을 사용하므로 String으로 치환 (ScalarsConverterFactory.create())
        final RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), data); // POST 방식을 이용해서 body를 보내야 하므로 content 인코딩 하기
        MyApplication.getInstance().getApiManager().getApiResponse(ActivityRecordInputModel.RS.class,
                MyApplication.getInstance().getApiManager().getApiService(context).inputExerciseRecord(body),
                exerciseRecordInputListener);

        // Multipart Request 예제
        // (편의상 비트맵 url과 비트맵 객체는 String 값으로 대체합니다.)
        final ProfileInfoModel infoModel = new ProfileInfoModel("여", "Fwang", 70, 180);
        final ProfileReplaceModel.RQ rqProfile = new ProfileReplaceModel.RQ("비트맵 url", "비트맵 객체", infoModel);
        MyApplication.getInstance().getApiManager().getApiResponse(ProfileReplaceModel.RS.class,
                MyApplication.getInstance().getApiManager().getApiService(context).getUpdateProfileInfo(rqProfile.getMpFile(), rqProfile.getRqMap()),
                profileUpdateListener);
    }

    /**
     * GET API Listener
     */
    private final IHTTPListener exerciseSearchListener = new IHTTPListener() {
        @Override
        public void onSuccess(Object result) {
            // Response 값
            final ActivityExerciseSearchModel.RS response = (ActivityExerciseSearchModel.RS) result;
        }

        @Override
        public void onFail(int code, String message, Object result) {
            Log.d("THEEND", "FAIL code: " + code + ", message: " + message);
        }
    };


    /**
     * POST API Listener
     */
    private final IHTTPListener exerciseRecordInputListener = new IHTTPListener() {
        @Override
        public void onSuccess(Object result) {
            // Response 값
            final ActivityRecordInputModel.RS response = (ActivityRecordInputModel.RS) result;
        }

        @Override
        public void onFail(int code, String message, Object result) {
            Log.d("THEEND", "FAIL code: " + code + ", message: " + message);
        }
    };


    /**
     * Multipart API Listener
     */
    private final IHTTPListener profileUpdateListener = new IHTTPListener() {
        @Override
        public void onSuccess(final Object result) {
            // Response 값
            final ProfileReplaceModel.RS response = (ProfileReplaceModel.RS) result;
        }

        @Override
        public void onFail(int code, String message, Object result) {
            Log.d("THEEND", "FAIL code: " + code + ", message: " + message);
        }
    };
}
