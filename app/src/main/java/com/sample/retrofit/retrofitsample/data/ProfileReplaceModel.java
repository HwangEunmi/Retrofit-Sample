package com.sample.retrofit.retrofitsample.data;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.sample.retrofit.retrofitsample.common.MyConstant;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/*Multipart 방식 데이터 모델*/
public class ProfileReplaceModel {

    // Request Model
    public static class RQ {

        /**
         * 프로필 사진 File
         */
        private MultipartBody.Part mpFile;

        /**
         * Request에 쓰이는 Map
         */
        private HashMap<String, RequestBody> rqMap;


        public RQ(final String path, final Bitmap bitmap, final ProfileInfoModel model) {
            super();
            rqMap = new HashMap<>();

            // Bitmap(이미지) -> File 로 변환
            // File은 MultipartBody.Part 로 한번 더 감싼다.
            if (path != null) {
                File file = new File(path);
                try {
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.close();
                    RequestBody rqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    mpFile = MultipartBody.Part.createFormData(MyConstant.PARAM.PROFILE_IMAGE, file.getName(), rqFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // RequestBody는 String 만 입력 가능하다.
            RequestBody rqGender = RequestBody.create(MediaType.parse("text/plain"), model.getGender());
            RequestBody rqNickName = RequestBody.create(MediaType.parse("text/plain"), model.getNickName());
            RequestBody rqWeight = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(model.getWeight()));
            RequestBody rqHeight = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(model.getHeight()));

            rqMap.put(MyConstant.PARAM.GENDER, rqGender);
            rqMap.put(MyConstant.PARAM.NICK_NAME, rqNickName);
            rqMap.put(MyConstant.PARAM.WEIGHT, rqWeight);
            rqMap.put(MyConstant.PARAM.HEIGHT, rqHeight);
        }


        public MultipartBody.Part getMpFile() {
            return mpFile;
        }

        public HashMap<String, RequestBody> getRqMap() {
            return rqMap;
        }

    }

    // Response Model
    public static class RS extends BaseResponseModel {

        @Expose
        public CommonProfileModel profile;

        public static class CommonProfileModel {
            @Expose
            private String name;
            @Expose
            private int point;
        }
    }

    // Type
    public Object getResponseType() {
        return ProfileReplaceModel.class;
    }
}
