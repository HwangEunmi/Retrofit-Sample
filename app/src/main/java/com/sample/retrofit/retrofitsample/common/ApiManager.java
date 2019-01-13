package com.sample.retrofit.retrofitsample.common;

import android.content.Context;
import android.util.Log;

import com.sample.retrofit.retrofitsample.MyApplication;
import com.sample.retrofit.retrofitsample.data.BaseResponseModel;
import com.sample.retrofit.retrofitsample.interfaces.IHTTPListener;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/*Retrofit을 이용하여 REST API 통신을 하는 클래스*/
public class ApiManager {

    /**
     * Retrofit 서비스
     */
    private ApiService apiService;

    private static class Singleton {
        public static final ApiManager MANAGER = new ApiManager();
    }

    public static ApiManager getInstance() {
        return Singleton.MANAGER;
    }


    public ApiService getApiService(final Context context) {
        initApiSetting(context);
        return apiService;
    }

    /**
     * 서버 API 통신
     *
     * @param type     클래스 타입(ex. LoginMode.class)
     * @param request  요청 request
     * @param listener 통신 listener
     */
    public void getApiResponse(final Type type, final Call<String> request, final IHTTPListener listener) {
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if ((200 == response.code() || 400 == response.code() || 403 == response.code()) && response.body() != null) {
                    final Object result = MyApplication.getInstance().getGson().fromJson(response.body().toString(), type);
                    final BaseResponseModel model = (BaseResponseModel) result;
                    // 일단 일반적인 성공인 0000코드만 (성공이 0000이라고 가정)
                    if (0000 == model.getCode()) {
                        listener.onSuccess(model);
                    } else {
                        listener.onFail(model.getCode(), model.getMessage(), model);
                    }
                } else {
                    listener.onFail(-1, "", null);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onFail(-1, "", null);
            }
        });
    }

    /**
     * Retrofit 초기화 셋팅
     */
    private void initApiSetting(Context context) {
        final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(MyConstant.Url.BASE_URL + "/") // 뒤에 / 를 꼭 붙여야 한다.
                .addConverterFactory(ScalarsConverterFactory.create());
        //.addConverterFactory(GsonConverterFactory.create())

        // Request, Response 과정에서 인터셉터하여 셋팅할 수 있는 Interceptor
        // 모든 Http 요청에 헤더 추가
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                final Request request = chain.request();
                // 1) 첫번째 방법
                final Headers headers = new Headers.Builder()
                        .add(MyConstant.PARAM.HEADER_MOBILE_PLATFORM, "android")
                        .add(MyConstant.PARAM.HEADER_APP_VERSION, "111")
                        // .add("Content-Type", "application/json;charset=utf-8") // GET, POST 일 경우
                        .add("Content-Type", "multipart/form-data; boundary=" + MyConstant.FORM_DATA_BOUNDARY) // Multipart 인 경우
                        .build();
                Request newRequest = request.newBuilder().headers(headers).build();

                // 2) 두번째 방법
                // Request newRequest = request.newBuilder()
                //         .add(MyConstant.PARAM.HEADER_MOBILE_PLATFORM, "android")
                //         .add(MyConstant.PARAM.HEADER_APP_VERSION, "111")
                //         // .add("Content-Type", "application/json;charset=utf-8") // GET, POST 일 경우
                //         .add("Content-Type", "multipart/form-data; boundary=" + MyConstant.FORM_DATA_BOUNDARY) // Multipart 인 경우
                //         .build();

                okhttp3.Response response = chain.proceed(newRequest); // 새로운 Request로 Response 구하기
                return response;
            }
        };

        // 통신 중 일어나는 로그를 인터셉트하는 Interceptor
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("THEEND", "retrofit: " + message);
            }
        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        builder.interceptors().add(logInterceptor);
        OkHttpClient client = builder.build();
        retrofitBuilder.client(client);

        Retrofit retrofit = retrofitBuilder.build();
        apiService = retrofit.create(ApiService.class);
    }


    public ApiManager getApiManager() {
        return ApiManager.getInstance();
    }

}
