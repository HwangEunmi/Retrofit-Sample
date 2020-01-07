
**프로젝트 소개**
-------

Retrofit2는 안드로이드 REST API 통신 라이브러리입니다.
통신 라이브러리 중 가장 많이 사용되는 라이브러리로써 꼭 알아야 한다는 생각에 정리해보았습니다.

내용중에 Retrofit2과 상관 없는 주제가 있을 수도 있습니다. 그런 내용은 제가 공부하면서 궁금한 점이 꼬리에 꼬리를 물었기때문에 같이 정리하였습니다.
그런 내용은 Skip 하셔도 됩니다!

프로젝트는 지속적으로 추가 될 예정입니다. 상단의 Star, Watching 버튼을 클릭하시면 구독 알림을 받으실 수 있습니다 :)


----------


**목차**
--

 - [프로젝트 소개](#프로젝트-소개)
 - [목차](#목차)
 - [전체 플로우](#전체-플로우)
  - [Retrofit 정의](#retrofit-정의)
  - [Retrofit 작동방식 이해](#Retrofit-작동방식-이해) 
  -  [Retrofit의 Converter](#retrofit의-컨버터)
     - [GsonConverterFactory.create()](#GsonConverterFactory.create())
     - [ScalarsConverterFactory.create()](#ScalarsConverterFactory.create())
  - [Retrofit에서 동기/비동기 방식](#retrofit에서-동기/비동기-방식)
     - [통신결과를 Listener로 받기](#통신결과를-Listener로-받기)
     - [cancel()로 요청 취소하기](#cancel()로-요청-취소하기)
  - [Retrofit의 어노테이션](#retrofit의-어노테이션)
     - [@Query](#1.-@Query)
     - [@Path](#@Path)
     - [@Field](#@Field)
     - [@FieldMap](#@FieldMap)
     - [@Body](#@Body)
     - [@Part](#@Part)
     - [@PartMap](#@PartMap)
- [Retrofit에서 Multipart 통신하기](#retrofit에서-멀티파트-통신하기)
- [Retrofit에서 헤더 설정하는 법](#retrofit에서-헤더-설정하는법)
     - [@Header 어노테이션 사용](#@Header-어노테이션-사용)
     - [OkHttp의 Interceptor 사용](#OkHttp의-Interceptor-사용)
   - [통신 ContentType](#통신-컨텐트타입)
     - [Get의 ContentType](#Get의-ContentType)
     - [Post의 ContentType](#Post의-ContentType)
     - [Multipart의 ContentType](#Multipart의-ContentType)
- [Retrofit을 사용하면서 로그찍는 법](#retrofit을-사용하면서로그찍는-법) 


----------

**전체 플로우**
----------





**Retrofit 정의**
------
![1_wvpjbw4kezkwp0yvclzwlg](https://user-images.githubusercontent.com/21076910/51786123-8b2ed980-21a3-11e9-9d43-750fea261c0d.jpeg)

**Retrofit 이란?**

Retrofit은 REST API 통신을 위해 구현된 라이브러리이다. 
AsyncTask 없이 Background Thread에서 실행되며 callback을 통해 Main Thread에서의 UI 업데이트를 간단하게 할 수 있도록 제공하고 있다.

Retrofit 문서 : [http://devflow.github.io/retrofit-kr/](http://devflow.github.io/retrofit-kr/)




**Retrofit을 사용하는 이유?**

Retrofit 이외에 다른 라이브러리도 있지만 Retrofit을 사용하기로 한 이유는 성능과 간단한 구현방법 때문이다.
Retrofit은 AsyncTask로 구현된 통신이나 Volley에 비해 응답속도가 매우 빠른것으로 나와있다.
또한 동기/비동기 방식을 선택할 수 있으며 Call의 요청을 취소할 수도 있다. 

참고 : [http://instructure.github.io/blog/2013/12/09/volley-vs-retrofit/](http://instructure.github.io/blog/2013/12/09/volley-vs-retrofit/)



**#Retrofit2 는 기본적으로 OkHttp를 네트워킹 계층으로 활용하며 그 위에 구축된다.**

----------

**Retrofit 작동방식 이해**
------

네트워킹은 Android 애플리케이션에서 가장 중요한 부분중 하나이다. 

초기에는 네트워킹을 처리하기위해 자체 HTTP클래스를 작성했지만, 시간이 지남에 따라 라이브러리에 의존하게 되었다. (작업속도를 높이기 위해)

Retrofit은 인기있는 라이브러리 중 하나이다. 

먼저 Retrofit은 Android 및 Java 용 HTTP 클라이언트 라이브러리인데

Retrofit을 사용하면서 Android 앱에서 네트워킹이 더 쉬워졌다. 

사용자 지정 헤더 및 요청 유형을 쉽게 추가할 수 있는 기능(Converter)등 많은 기능이 있으므로 쉽게 사용할 수 있다. 


Retrofit 내에서 처리되는 방식을 살펴보기전에 사용법을 살펴보자.

----------

Retrofit을 사용하려면 다음 세가지 클래스가 필요하다.

1. JSON 형태의 모델 클래스

2. HTTP 작업을 정의하는(onSuccess/onFail) 인터페이스

3. Retrofit.Builder를 선언한 클래스 (baseUrl과 Converter등을 선언한다. Interceptor를 추가하여 응답을 가공할수도 있다.)

사용방법은 다음과 같다.

**1. build.gradle에 추가한다.**

```java
implementation ‘com.squareup.retrofit2:retrofit:2.3.0’
implementation ‘com.squareup.retrofit2:converter-gson:2.3.0’
```  

**2. JSON 형태의 모델 클래스를 생성한다.**

**3. HTTP 요청을 수행하는 Call 메소드가 있는 API 인터페이스(APIService)를  생성한다.**


**(Retrofit은 @GET, @POST 등과 같은 어노테이션 리스트를 제공한다.)**
 
 ```java
@GET("/users/")
Call<User> getInfo(@Query("name") String name);
```  

**4. Retrofit.Builder 클래스를 생성한다.**
```java
final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
           .baseUrl(MyConstant.Url.BASE_URL + "/") // 뒤에 / 를 꼭 붙여야 한다.
           .addConverterFactory(ScalarsConverterFactory.create());
```  

**5. APIService의 Call 메소드 객체를 선언하고 동기/비동기로 실행한다.**
 ```java
Retrofit retrofit = retrofitBuilder.build();
APIService apiService = retrofit.create(ApiService.class);
Call<User> call1 = apiService.getInfo("홍길동").enqueue();
```  

**6. 이제 서버에서 Response를 받아온 후 원하는 작업을 수행한다.**


----------

여기까지가 Retrofit의 작동방식이다. 이제 뒤에 어떤 일이 일어나는지 분석해본다.

```java
APIService apiService = retrofit.create(ApiService.class);
```  
우리가 APIService의 객체를 만들때 내부에서는 다음과 같이 동작한다.

```java
 public <T> T create(final Class<T> service) {
    Utils.validateServiceInterface(service);
    if (validateEagerly) {
      eagerlyValidateMethods(service);
    }
    return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service },
        new InvocationHandler() {
          private final Platform platform = Platform.get();
          private final Object[] emptyArgs = new Object[0];

          @Override public @Nullable Object invoke(Object proxy, Method method,
              @Nullable Object[] args) throws Throwable {
            // If the method is a method from Object then defer to normal invocation.
            if (method.getDeclaringClass() == Object.class) {
              return method.invoke(this, args);
            }
            if (platform.isDefaultMethod(method)) {
              return platform.invokeDefaultMethod(method, service, proxy, args);
            }
            return loadServiceMethod(method).invoke(args != null ? args : emptyArgs);
          }
        });
  }
```  
먼저 validateServiceInterface() 메소드를 호출하여 현 인터페이스가 유효한것인지를 판단한다.

```java
 static <T> void validateServiceInterface(Class<T> service) {
    if (!service.isInterface()) {
      throw new IllegalArgumentException("API declarations must be interfaces.");
    }
    // Prevent API interfaces from extending other interfaces. This not only avoids a bug in
    // Android (http://b.android.com/58753) but it forces composition of API declarations which is
    // the recommended pattern.
    if (service.getInterfaces().length > 0) {
      throw new IllegalArgumentException("API interfaces must not extend other interfaces.");
    }
  }
```  

만약 유효하지 않은 경우 IllegalArgumentException을 호출한다.

그런다음 eagerlyValidateMethods() 메소드를 호출하여 플랫폼 유형을 얻는다.

```java
private void eagerlyValidateMethods(Class<?> service) {
    Platform platform = Platform.get();
    for (Method method : service.getDeclaredMethods()) {
      if (!platform.isDefaultMethod(method) && !Modifier.isStatic(method.getModifiers())) {
        loadServiceMethod(method);
      }
    }
  }
```  

```java
class Platform {
  private static final Platform PLATFORM = findPlatform();

  static Platform get() {
    return PLATFORM;
  }

  private static Platform findPlatform() {
    try {
      Class.forName("android.os.Build");
      if (Build.VERSION.SDK_INT != 0) {
        return new Android();
      }
    } catch (ClassNotFoundException ignored) {
    }
    try {
      Class.forName("java.util.Optional");
      return new Java8();
    } catch (ClassNotFoundException ignored) {
    }
    return new Platform();
  }
  /* More methods of this class
  ........
  ........
  ......... 
  */
}
```

플랫폼 유형을 얻은 후 eagerlyValidateMethods()내에서 service.getDeclaredMethods()를 호출하여

인터페이스에 선언된 모든 메소드 객체를 포함하는 배열을 리턴한다. (여기서 인터페이스란 ApiService 클래스)
```java
public Method[] getDeclaredMethods() throws SecurityException {
    Method[] result = getDeclaredMethodsUnchecked(false);
    for (Method m : result) {
        // Throw NoClassDefFoundError if types cannot be resolved.
        m.getReturnType();
        m.getParameterTypes();
    }
    return result;
}
```  

그후 loadServiceMethod()내에서 ServiceMethodCache라는 Map에 메소드를 put하고 필요할 경우 get하는데

```java
private final Map<Method, ServiceMethod<?>> serviceMethodCache = new ConcurrentHashMap<>();
ServiceMethod<?> loadServiceMethod(Method method) {
  ServiceMethod<?> result = serviceMethodCache.get(method);
  if (result != null) return result;

  synchronized (serviceMethodCache) {
    result = serviceMethodCache.get(method);
    if (result == null) {
      result = ServiceMethod.parseAnnotations(this, method);
      serviceMethodCache.put(method, result);
    }
  }
  return result;
}
```  

이때 put 즉 저장할때, 메소드의 Annotation이나 매개변수타입등을 파싱하여 같이 저장한다.

```java
abstract class ServiceMethod<T> {
  static <T> ServiceMethod<T> parseAnnotations(Retrofit retrofit, Method method) {
    RequestFactory requestFactory = RequestFactory.parseAnnotations(retrofit, method);

    Type returnType = method.getGenericReturnType();
    if (Utils.hasUnresolvableType(returnType)) {
      throw methodError(method,
          "Method return type must not include a type variable or wildcard: %s", returnType);
    }
    if (returnType == void.class) {
      throw methodError(method, "Service methods cannot return void.");
    }

    return HttpServiceMethod.parseAnnotations(retrofit, method, requestFactory);
  }

  abstract @Nullable T invoke(Object[] args);
}
}
```  

```java
Builder(Retrofit retrofit, Method method) {
  this.retrofit = retrofit;
  this.method = method;
  this.methodAnnotations = method.getAnnotations();
  this.parameterTypes = method.getGenericParameterTypes();
  this.parameterAnnotationsArray = method.getParameterAnnotations();
}
```  

이로인해 인터페이스에 있는 모든 메소드를 사용할 수 있는 것이다.


![image](/image/image.PNG)

OkHttp는 Retrofit 아래에 있다. OkHttp는 소켓에 연결하여 HTTP요청을 한다. 

Retrofit과 OkHttp는 RequestBody와 ResponseBody 타입을 이용하여 통신을 한다. 

순서는 ApiService > Retrofit > OkHttp 이다. 

![structure](/image/structure.PNG)

----------

**Retrofit의 컨버터**
------
**Retrofit 에는 Converter가 여러가지가 있다.**

> **1)** **GsonConverterFactory.create() :** 결과값을 Gson으로 자동으로 파싱해서 JSON형태로 받을 수 있다. 
> 
> **2)** **ScalarsConverterFactory.create() :** 결과값을 String으로 받는다.
>                                              그래서 Gson으로 내가 직접 파싱해야 한다.

대표적인 두가지를 적었다. 

Retrofit2은 Converter를 다중추가 가능하지만, GsonConverterFactory는 항상 마지막에 추가해야 한다.

Call에 대해 Converter를 할 수 있냐는 물음에 항상 YES를 리턴하기 때문이다.

```java
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://api.github.com")
    .addConverterFactory(ProtoConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .build();
```  

**나의 생각)** 만약 Response의 규격이 정해져 있다면
ex. int code
    String message
    Object data
GsonConverterFactory.create() 를 사용하는 것이 좋고, API에 따라 가지각색이라면 ScalarsConverterFactory.create()를 사용하는 것이 좋다.
또, 확실하진 않지만 두개 동시에는 사용이 불가능한듯 하다.


----------

**Retrofit에서 동기/비동기 방식**
---------------
**Retrofit은 동기/비동기 방식을 선택할 수 있다.**

**동기식**

```java
Call<User> call = 
    apiService.getUser("홍길동");
Response<User> response = call.execute(); 

// This will throw IllegalStateException:
Response<User> response = call.execute();

Call<User> call2 = call.clone();
// This will not throw:
Response<User> response = call2.execute();
```   

excute()는 한번만 가능하다. 두 번 execute를 시도하면 실패하게 된다.
하지만 clone()메소드로 인스턴스를 복제할 수 있으며 비용은 매우 적다.

**비동기식**

```java
Call<User> call = 
    apiService.getUser("홍길동");
Response<User> response = call.enqueue(); 
```   

**통신결과를 Listener로 받기**
---------------
또한 통신결과를 Listener로 받을 수 있다. 

```java
call.enqueue(new Callback<User>() {
  @Override void onResponse(/* ... */) {
    // ...
  }

  @Override void onFailure(Throwable t) {
    // ...
  }
}); 

```   

**cancel()로 요청 취소하기**
---------------
동기/비동기 방식으로 요청을 한 후 cancel()로 통신을 취소할 수도 있다. 

```java
Call<User> call = 
    apiService.getUser("홍길동");
Response<User> response = call.enqueue(); 
// Call cancel
call.cancel();
```   
     
----------

**Retrofit의-어노테이션**
---------------
**Retrofit에는 크게 7개의 어노테이션이 있다.**

**1. @Query**
```java
@GET("/posts") 
Call<List<ResponseGet>> getSecond(@Query("userId") String id);
```           
일 때, url은 http://jsonplaceholder.typicode.com/posts?userId=1 가 된다.


**2. @Path** 

```java
@GET("/posts/{userId}")
Call<ResponseGet> getFirst(@Path("userId") String id);
```
   일 때, id로 들어간 String 값을 {userId} 로 넘겨준다. 
   즉, url은 http://jsonplaceholder.typicode.com/posts/1 가 된다.

**3. @Field**        
     서버에 데이터를 보낼때 Request 데이터를 하나씩 지정해서 보내려면 사용한다.
    ContentType을 form-encoded로 지정하여 데이터를 전송해야 하므로 @FormUrlEncoded 어노테이션을 지정해줘야 한다. (안하면 오류 발생)            
```java
@FormUrlEncoded    
@POST("/login_url/")
Call<User> login(@field("email")String email,@field("password")String password); 
```
**4. @FieldMap**

   Field 형식을 통해 넘겨주는 값들이 여러개일 경우 FieldMap 사용한다. 참고로 Retrofit에서는 Map보다 HashMap 형식을 쓰기 권장한다.
ContentType을 form-encoded로 지정하여 데이터를 전송해야 하므로 @FormUrlEncoded 어노테이션 지정해줘야 한다. (안하면 오류 발생)
```java
@FormUrlEncoded 
@POST("/posts") 
Call<ResponseGet> postFirst(@FieldMap HashMap<String, Object> parameters);
```
           
여기서 String은 키값, Object는 데이터이다.

**5. @Body**
   
   Request로 넘겨주는 값이 Json형식일 경우 사용한다. 
   (ex. ActivitySearchModel.class)
   
```java
@POST("/users/")
Call<User> signUp(@Body User user);
```
 **나의 생각)** 어차피 보통 Data클래스 만드니까 @Field 보단 @Body 사용해도 될 듯 하다.) 

**6. @Part**

   Multipart 요청시 사용한다. (POST/PUT)
   서버에 데이터를 보낼때 Request 데이터를 하나씩 지정해서 보내려면 사용한다.
   이 때 @Multipart 어노테이션 사용함으로써 multipart 라는것을 지정해줘야 한다.
   
```java
@Multipart
@POST(MyConstant.Url.POST_PROFILE_IMAGE_UPLOAD)
Call<String> getUpdateProfileInfo(@Part(“imageFile”) RequestBody file);
```


**7. @PartMap**
 
 Multipart 요청시 사용한다. (POST/PUT)
 Part 형식을 통해 넘겨주는 값들이 여러개일 경우 PartMap을 사용한다. 참고로 Retrofit에서는 Map보다 HashMap 형식을 쓰기 권장한다.
이 때 @Multipart 어노테이션 사용함으로써 multipart 라는것을 지정해줘야 한다.
```java
@Multipart
@POST(MyConstant.Url.POST_PROFILE_IMAGE_UPLOAD)
Call<String> getUpdateProfileInfo(@Part MultipartBody.Part file, @PartMap Map<String, RequestBody> info);
```

이 때 image같은 File 객체는 RequestBody를 그냥 사용하는것 보다는 MultipartBody.Part로 한번 더 감싸는 것이 좋다.

```java 
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
```
----------

**Retrofit에서 멀티파트 통신하기**
-------------------------
API 문서 상에서 Request들이 하나의 Map 또는 ObjectList에 들어가는 동급 데이터라고 하더라도 이미지 같은 File은 RequestBody에 넣고 MultipartBody.Part로 한번 더 감싸서 따로 @Part 부여해야 한다.

모든 데이터들은 RequestBody에 들어갈 때 MediaType.parse()로 파싱하는데, 
이때 일반적인 String이나 int같은 값들은 “text/plain”으로 하면 되지만
```java
RequestBody.create(MediaType.parse(“text/plain”), data);`
```

이미지 같은 File들은 “multipart/form-data”로 지정한다.
```java
RequestBody.create(MediaType.parse(“multipart/form-data”), data);`
```

(물론 “image”로 넘겨도 되지만,

> **1) “multipart/form-data” :** 데이터의 크기가 클 경우에 사용
> 
> **2) “image” 또는 “text/plain” :** 데이터의 크기가 작을 경우에 사용 이므로 “multipart/form-data” 로 넘기는 것이 좋다.)

```java

@Multipart
@POST(MyConstant.Url.POST_PROFILE_IMAGE_UPLOAD)
Call<String> getUpdateProfileInfo(@Part MultipartBody.Part file, @PartMap Map<String, RequestBody> info);
```

인 경우
```java
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
    
rqMap.put(MyConstant.PARAM.GENDER, rqGender);
rqMap.put(MyConstant.PARAM.NICK_NAME, rqNickName);

public MultipartBody.Part getMpFile() {
 return mpFile;
 }
 
public Map<String, RequestBody> getRqMap() {
 return rqMap;
  }
 }
```

----------


**Retrofit에서 헤더 설정하는법**
-------------------

**1) @Header 어노테이션 사용**

```java
@Headers("Cache-Control: max-age=640000")
@GET("/widget/list")
Call<List<Widget>> widgetList();
```
나 

```java
@Headers({
 "Accept: application/vnd.github.v3.full+json",
 "User-Agent: Retrofit-Sample-App"})
@GET("/users/{username}")
Call<User> getUser(@Path("username") String username);
```

**2) OkHttp의 Interceptor 사용**
이런 식으로 annotation을 달아주면 되지만, 사실 모든 API에 적용하려고 할때는 번거로우므로 
okHttp의 Interceptor로 header를 지정해주는 것이 좋다.

```java
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
// .addHeader(MyConstant.PARAM.KEY_ACCESS_TOKEN, PreferencesUtil.getAccessToken(context))
// .add(MyConstant.PARAM.HEADER_APP_VERSION, CommonUtil.localAppVersion(context))
// .add("Content-Type", "application/json;charset=utf-8") // GET, POST 일 경우
//.add("Content-Type", "multipart/form-data; boundary=" + MyConstant.FORM_DATA_BOUNDARY) // multipart 인 경우
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
```

----------


**통신 컨텐트타입**
--------------

> **1) Get의 ContentType**
> 
> key : "Content-Type", value : "application/json"
> 
> **2) POST의 ContentType**
> 
> key : "Content-Type", value : "application/json;charset=UTF-8" //
> content가 UTF-8로 인코딩 되어있다는 뜻

(JSON의 기본 인코딩은 UTF-8이다. 

그래서 만약 ScalarsConverterFactory를 사용하는 경우 JSON Object인 RequestBody를 사용해야 한다. (컨버터가 없으므로)

반면에 GsonConvertFactory인 경우엔 일반 Data 클래스도 사용이 가능하다. (Gson 즉, 컨버터에 따라 변환되므로)

참고로 RequestBody에는 String값만 들어갈 수 있으므로 
만약 ScalarsConverterFactory를 사용할 경우, 그냥 String 값을 RequestBody에 넣거나
Data 클래스를 사용할 경우(JSON Object) Gson을 이용해서(toJson) String으로 바꾼 후 RequestBody에 넣고 통신 한다.

> **3) Multipart의 ContentType**
> 
> key : "Content-Type", value : "multipart/form-data; boundary=" +
> MyConstant.FORM_DATA_BOUNDARY"


----------


**Retrofit을 사용하면서로그찍는 법**
---------------------
Request/Response 즉, 통신 중 일어나는 로그를 보고 싶을 때
  HttpLoggingInterceptor를 사용한다.
```java
HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
```
즉, 

```java
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
```
