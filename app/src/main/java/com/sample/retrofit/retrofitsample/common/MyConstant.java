package com.sample.retrofit.retrofitsample.common;

/*공통 상수*/
public class MyConstant {

    // Multipart Boundary
    public static final String FORM_DATA_BOUNDARY = "983xjdf83hsd2kdfldf";

    // Url value
    public class Url {

        public static final String BASE_URL = "http://test.sample";

        public static final String GET_ACTIVITY_EXERCISE_SEARCH = "api/activity/exercise/search";

        public static final String POST_ACTIVITY_EXERCISE_RECORD_INPUT = "api/activity/exercise/input/save";

        public static final String POST_PROFILE_IMAGE_UPLOAD = "api/profile/update";

    }

    // 파라메터 value
    public class PARAM {

        public static final String PROFILE_IMAGE = "profile_image";

        public static final String GENDER = "gender";

        public static final String NICK_NAME = "nick_name";

        public static final String WEIGHT = "weight";

        public static final String HEIGHT = "height";


        public static final String HEADER_MOBILE_PLATFORM = "header_mobile_platform";

        public static final String HEADER_APP_VERSION = "header_app_version";

    }
}
