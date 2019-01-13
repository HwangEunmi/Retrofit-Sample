package com.sample.retrofit.retrofitsample.common;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/*HTTP 통신을 하기 위한 Retrofit 서비스 인터페이스 생성*/
public interface ApiService {
    // url 형식 맞추는법 (GET)    -> https://stackoverflow.com/questions/37698501/retrofit-2-path-vs-query
    //                 (GET, POST)  -> https://kor45cw.tistory.com/5


    /**
     * GET 방식 예제
     * Url : /api/activity/exercise/search?searchWord=value
     */
    @GET(MyConstant.Url.GET_ACTIVITY_EXERCISE_SEARCH)
    Call<String> getExerciseSearchList(@Query("searchWord") String searchWord);


    /**
     * POST 방식 예제
     * Url : /api/activity/exercise/input/save
     */
    @POST(MyConstant.Url.POST_ACTIVITY_EXERCISE_RECORD_INPUT)
    Call<String> inputExerciseRecord(@Body RequestBody model);


    /**
     * Multipart 방식 예제
     * Url : /api/profile/update
     */
    @Multipart
    @POST(MyConstant.Url.POST_PROFILE_IMAGE_UPLOAD)
    Call<String> getUpdateProfileInfo(@Part MultipartBody.Part file, @PartMap HashMap<String, RequestBody> info);

}
