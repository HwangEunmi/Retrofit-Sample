*** Retrofit 이란?**
Retrofit은 REST API 통신을 위해 구현된 라이브러리이다. 
AsyncTask 없이 Background Thread에서 실행되며 callback을 통해 Main Thread에서의 UI 업데이트를 간단하게 할 수 있도록 제공하고 있다.
Retrofit 문서 : [http://devflow.github.io/retrofit-kr/](http://devflow.github.io/retrofit-kr/)

*** Retrofit을 사용하는 이유?**
Retrofit 이외에 다른 라이브러리도 있지만 Retrofit을 사용하기로 한 이유는 성능과 간단한 구현방법 때문이다.
Retrofit은 AsyncTask로 구현된 통신이나 Volley에 비해 응답속도가 매우 빠른것으로 나와있다.
참고 : [http://instructure.github.io/blog/2013/12/09/volley-vs-retrofit/](http://instructure.github.io/blog/2013/12/09/volley-vs-retrofit/)

*** Retrofit2 는 기본적으로 OkHttp를 네트워킹 게층으로 활용하며 그 위에 구축된다.**

*** Retrofit 에는 Converter가 두가지가 있다.**
**1.** **GsonConverterFactory.create() :** 결과값을 Gson으로 자동으로 파싱해서 JSON형태로 받을 수 있다. 
**2.** **ScalarsConverterFactory.create() :** 결과값을 String으로 받는다.
                                             그래서 Gson으로 내가 직접 파싱해야 한다.

(만약 Response의 규격이 정해져 있다면
ex. int code
    String message
    Object data
GsonConverterFactory.create() 를 사용하는 것이 좋고, API에 따라 가지각색이라면 ScalarsConverterFactory.create()를 사용하는 것이 좋다.)

(확실하진 않지만 두개 동시에는 사용이 불가능한듯 하다.)


*** Retrofit에는 크게 7개의 어노테이션이 있다.**

**1. @Query** 
       ex. 

    @GET("/posts") 
    Call<List<ResponseGet>> getSecond(@Query("userId") String id);
           
일 때, url은 http://jsonplaceholder.typicode.com/posts?userId=1 가 된다.

**2. @Path** 
       ex. 

    @GET("/posts/{userId}")
    Call<ResponseGet> getFirst(@Path("userId") String id);

   일 때, id로 들어간 String 값을 {userId} 로 넘겨준다. 
   즉, url은 http://jsonplaceholder.typicode.com/posts/1 가 된다.

**3. @Field**        
     서버에 데이터를 보낼때 Request 데이터를 하나씩 지정해서 보내려면 사용한다.
     form-encoded 데이터로 전송해야 하므로 (contentType 지정) @FormUrlEncoded 어노테이션 지정해줘야 한다. (안하면 오류)            
      ex.  

     @FormUrlEncoded    
     @POST("/login_url/")
     Call<User> login(@field("email")String email,@field("password")String password); 

**4. @FieldMap**
    Field 형식을 통해 넘겨주는 값들이 여러개일 경우 FieldMap 사용한다. (Retrofit에서는 Map보다 HashMap 형식을 쓰기 권장)
    form-encoded 데이터로 전송해야 하므로 (contentType 지정) @FormUrlEncoded 어노테이션 지정해줘야 한다. (안하면 오류)
   ex. 

      @FormUrlEncoded 
      @POST("/posts") 
      Call<ResponseGet> postFirst(@FieldMap HashMap<String, Object> parameters);
           
여기서 String은 키값, Object는 데이터이다.

**5. @Body**
   Request로 넘겨주는 값이 Json형식일 경우 사용한다. 
   (ex. ActivitySearchModel.class)
    ex. 

    @POST("/users/")
    Call<User> signUp(@Body User user);

 (=> 어차피 보통 Data클래스 만드니까 @Field 보단 @Body 사용해도 될 듯 하다.) 

**6. @Part**
   Multipart 요청시 사용한다. (POST/PUT)
   서버에 데이터를 보낼때 Request 데이터를 하나씩 지정해서 보내려면 사용한다.
   이 때 @Multipart 어노테이션 사용함으로써 multipart 라는것을 지정해줘야 한다.
   ex.

     @Multipart
     @POST(MyConstant.Url.POST_PROFILE_IMAGE_UPLOAD)
     Call<String> getUpdateProfileInfo(@Part(“imageFile”) RequestBody file);



**7. @PartMap**
   Multipart 요청시 사용한다. (POST/PUT)
   Part 형식을 통해 넘겨주는 값들이 여러개일 경우 PartMap을 사용한다. (Retrofit에서는 Map보다 HashMap 형식을 쓰기 권장)
이 때 @Multipart 어노테이션 사용함으로써 multipart 라는것을 지정해줘야 한다.
   ex. 

    @Multipart
    @POST(MyConstant.Url.POST_PROFILE_IMAGE_UPLOAD)
    Call<String> getUpdateProfileInfo(@Part MultipartBody.Part file, @PartMap Map<String, RequestBody> info);

이 때 image같은 File 객체는 RequestBody를 그냥 사용하는것 보다는 MultipartBody.Part로 한번 더 감싸는 것이 좋다.

ex. 
	

    // Bitmap(이미지) -> File
    if (path != null) {
    File file = new File(path);
      try {
        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                                      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        os.close();
        RequestBody rqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        mpFile = MultipartBody.Part.createFormData(MyConstant.PARAM.PROFILE_IMAGE, file.getName(), rqFile); // 키값, 파일이름, 데이터
         } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
          e.printStackTrace();
      }
    }

 
*** Retrofit에서 Header를 설정할 때**
ex. 

    @Headers("Cache-Control: max-age=640000")
    @GET("/widget/list")
    Call<List<Widget>> widgetList();

나 
ex. 

    @Headers({
        "Accept: application/vnd.github.v3.full+json",
        "User-Agent: Retrofit-Sample-App"})
    @GET("/users/{username}")
    Call<User> getUser(@Path("username") String username);

이런 식으로 annotation을 달아주면 되지만, 사실 모든 API에 적용하려고 할때는 번거로우므로 
okHttp의 Interceptor로 header를 지정해주는 것이 좋다.

ex. 

    Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
       .baseUrl(MyConstant.Url.BASE_URL + "/")
                    .addConverterFactory(ScalarsConverterFactory.create());
    
        // 모든 Http 요청에 헤더 추가한다고 할 때
        Interceptor interceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
           Request request = chain.request(); // 현재 Request를 가져온다.
    	  // 첫번째 방법
          Headers headers = new Headers.Builder()
    	                        .add(MyConstant.PARAM.KEY_ACCESS_TOKEN, PreferencesUtil.getAccessToken(context))
                                        .add(MyConstant.PARAM.HEADER_APP_VERSION, CommonUtil.localAppVersion(context))
     		            .add("Content-Type", "application/json;charset=utf-8") // GET, POST 일 경우
                        //.add("Content-Type", "multipart/form-data; boundary=" + MyConstant.FORM_DATA_BOUNDARY) // multipart 인 경우
                       .build();
    	   Request newRequest = request.newBuilder().headers(headers).build(); // 새로운 Request를 만든다. (헤더를 추가한)
    
    	// 두번째 방법
    	// Request newRequest = request.newBuilder()
      	// .addHeader(SHPConstant.PARAM.KEY_ACCESS_TOKEN, PreferencesUtil.getAccessToken(context))
    	// .add(MyConstant.PARAM.HEADER_APP_VERSION, CommonUtil.localAppVersion(context))
     	// .add("Content-Type", "application/json;charset=utf-8") // GET, POST 일 경우
        // //.add("Content-Type", "multipart/form-data; boundary=" + MyConstant.FORM_DATA_BOUNDARY) // multipart 인 경우
        // .build();
    
     okhttp3.Response response = chain.proceed(newRequest); // 새로운 Request로 통신을 하여 Response를 받는다.
     return response;
    	}
      };
      OkHttpClient.Builder builder = new OkHttpClient.Builder();
      builder.interceptors().add(interceptor);
      OkHttpClient client = builder.build();
      retrofitBuilder.client(client);
    
      Retrofit retrofit = retrofitBuilder.build();
      apiService = retrofit.create(ApiService.class);


API 문서 상에서 Request들이 하나의 Map 또는 ObjectList에 들어가는 동급 데이터라고 하더라도 이미지 같은 File은 RequestBody에 넣고 MultipartBody.Part로 한번 더 감싸서 따로 @Part 부여해야 한다.

모든 데이터들은 RequestBody에 들어갈 때 MediaType.parse()로 파싱하는데, 
이때 일반적인 String이나 int같은 값들은 “text/plain”으로 하면 되지만
ex. `RequestBody.create(MediaType.parse(“text/plain”), data);`

이미지 같은 File들은 “multipart/form-data”로 지정한다.
ex. `RequestBody.create(MediaType.parse(“multipart/form-data”), data);`
(물론 “image”로 넘겨도 되지만,
**“multipart/form-data” :** 데이터의 크기가 클 경우에 사용
**“image” 또는 “text/plain” :** 데이터의 크기가 작을 경우에 사용
이므로 “multipart/form-data” 로 넘기는 것이 좋다.)

ex. 

    @Multipart
    @POST(MyConstant.Url.POST_PROFILE_IMAGE_UPLOAD)
    Call<String> getUpdateProfileInfo(@Part MultipartBody.Part file, @PartMap Map<String, RequestBody> info);

인 경우

    /*Request 모델*/
    public static class RQ {
        /**
         * 프로필 사진 File
         */
        private MultipartBody.Part mpFile;
    
        /**
         * Request에 쓰이는 Map
         */
         private Map<String, RequestBody> rqMap;
    
    
      public RQ(final String path, final Bitmap bitmap, final UserProfileInfoModel profileUpdateModel) {
         super(); 
         rqMap = new HashMap<>();
         // Bitmap(이미지) -> File
         if (path != null) {
           File file = new File(path);
             try { 
              OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
               bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
               os.close();
     
              RequestBody rqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
    mpFile = MultipartBody.Part.createFormData(MyConstant.PARAM.PROFILE_IMAGE, file.getName(), rqFile); // 킷값, 파일 이름, 데이터
                 } catch (FileNotFoundException e) {
                    e.printStackTrace();
                     } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
    
      RequestBody rqGender = RequestBody.create(MediaType.parse("text/plain"), profileUpdateModel.getGender());
      RequestBody rqNickName = RequestBody.create(MediaType.parse("text/plain"), profileUpdateModel.getNickName());
    
    rqMap.put(SHPConstant.PARAM.GENDER, rqGender);
    rqMap.put(SHPConstant.PARAM.NICK_NAME, rqNickName);
    
        public MultipartBody.Part getMpFile() {
                return mpFile;
            }
    
            public Map<String, RequestBody> getRqMap() {
                   return rqMap;
            }
        }



*** Content Type**

**1. [Get의 ContentType]**
key : "Content-Type", value : "application/json"

**2. [POST의 ContentType]**
key : "Content-Type", value : "application/json;charset=UTF-8" // content가 UTF-8로 인코딩 되어있다는 뜻 
(JSON의 기본 인코딩은 UTF-8이다. 
그래서 만약 ScalarsConverterFactory를 사용하는 경우 JSON Object인 RequestBody를 사용해야 한다. (컨버터가 없으므로)
반면에 GsonConvertFactory인 경우엔 일반 Data 클래스도 사용이 가능하다. (Gson 즉, 컨버터에 따라 변환되므로)

참고로 RequestBody에는 String값만 들어갈 수 있으므로 
만약 ScalarsConverterFactory를 사용할 경우, 그냥 String 값을 RequestBody에 넣거나
Data 클래스를 사용할 경우(JSON Object) Gson을 이용해서(toJson) String으로 바꾼 후 RequestBody에 넣고 통신 한다.

**3. [Multipart의 ContentType]**
key : "Content-Type", value : "multipart/form-data; boundary=" + MyConstant.FORM_DATA_BOUNDARY"



*** 통신 중 발생하는 로그보는 법**
Request/Response 즉, 통신 중 일어나는 로그를 보고 싶을 때
  HttpLoggingInterceptor를 사용한다.
ex. 

    HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

즉, 
ex. 

    private void initApiSetting(Context context) {
            Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                    .baseUrl(MyConstant.Url.BASE_URL + "/")
                    .addConverterFactory(ScalarsConverterFactory.create());
    
    	 // 통신 중 일어나는 로그를 인터셉트하는 Interceptor
                HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
                logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    
               OkHttpClient.Builder builder = new OkHttpClient.Builder();
               builder.interceptors().add(logInterceptor);
    
              OkHttpClient client = builder.build();
              retrofitBuilder.client(client);
    
              Retrofit retrofit = retrofitBuilder.build();
              apiService = retrofit.create(ApiService.class);
    }





