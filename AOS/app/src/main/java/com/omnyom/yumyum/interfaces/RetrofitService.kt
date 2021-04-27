package com.omnyom.yumyum.interfaces

import com.omnyom.yumyum.model.feed.*
import com.omnyom.yumyum.model.login.LoginResponse
import com.omnyom.yumyum.model.maps.KeywordSearchResponse
import com.omnyom.yumyum.model.myinfo.UserModel
import com.omnyom.yumyum.model.signup.SignUpResponse
import com.omnyom.yumyum.model.signup.UploadProfileResponse
import com.omnyom.yumyum.model.place.GetPlaceDataResponse
import okhttp3.MultipartBody
//import com.omnyom.yumyum.model.myinfo.UserSampleModel
import retrofit2.Call
import retrofit2.http.*



interface RetrofitService {
//    예시
//    @GET("/member")
//    fun getMemberList(): Call<List<MemberModel>>
//    @GET("/additional-info/program")
//    fun getProgramList(): Call<List<ProgramModel>>
//    @GET("/additional-info/position")
//    fun getPositionList(): Call<List<PositionModel>>
//    @GET("/additional-info/program/?")
//    fun getProgramListByPosition(@Query("position") position: String?): Call<ArrayList<ProgramModel>>
//
//    @GET("o/oauth2/auth/oauthchooseaccount?")
//    fun getUser(@Query("client_id") client_id: String, @Query("redirect_uri") redirect_uri: String, @Query("response_type") response_type: String, @Query("scope") scope: String): Call<UserModel>
//
    @POST("user/login")
    fun loginSample(@Body parameters: HashMap<String, String>): Call<LoginResponse>

<<<<<<< AOS/app/src/main/java/com/omnyom/yumyum/interfaces/RetrofitService.kt
    @GET("user/login/{email}")
    fun login(@Path("email") email: String): Call<LoginResponse>

    @POST("user/signup")
    fun signup(@Body parameters: HashMap<String, String>): Call<SignUpResponse>

    @Multipart
    @POST("user/profile")
    fun uploadProfile(@Part image: MultipartBody.Part?): Call<UploadProfileResponse>
}
=======
    // 모든 피드 불러오기
    @GET("feed/list/{userId}")
    fun getAllFeeds(@Path("userId") userId: Long): Call<AllFeedResponse>

    // 비디오 데이터 보내기
    @Multipart
    @POST("feed/video")
    fun sendVideo(@Part file: MultipartBody.Part ): Call<SendVideoResponse>

    // 피드 작성하기
    @POST("feed/")
    fun createFeed(@Body parameters: HashMap<String, Any>): Call<CreateFeedResponse>

    // 식당ID로 정보 불러오기
    @GET("place/{placeId}")
    fun getPlaceData(@Path("placeId") placeId: Long) : Call<GetPlaceDataResponse>
}

interface KakaoApiService {
    @GET("/v2/local/search/keyword.json")
    fun placeSearch(
            @Header("Authorization") key: String,
            @Query("query") query: String,
            @Query("x")x: Double,
            @Query("y")y: Double,
            @Query("page")page: Int,
            @Query("size")size: Int,
    ) : Call<KeywordSearchResponse>
}

>>>>>>> AOS/app/src/main/java/com/omnyom/yumyum/interfaces/RetrofitService.kt
