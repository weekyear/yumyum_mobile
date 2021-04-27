package com.omnyom.yumyum.interfaces

//import com.omnyom.yumyum.model.myinfo.UserSampleModel
import com.omnyom.yumyum.model.feed.AllFeedResponse
import com.omnyom.yumyum.model.feed.CreateFeedResponse
import com.omnyom.yumyum.model.feed.SendVideoResponse
import com.omnyom.yumyum.model.login.LoginResponse
import com.omnyom.yumyum.model.place.GetPlaceDataResponse
import com.omnyom.yumyum.model.signup.SignUpResponse
import com.omnyom.yumyum.model.signup.UploadProfileResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface RetrofitService {
    @POST("user/login")
    fun loginSample(@Body parameters: HashMap<String, String>): Call<LoginResponse>

    @GET("user/login/{email}")
    fun login(@Path("email") email: String): Call<LoginResponse>

    @POST("user/signup")
    fun signup(@Body parameters: HashMap<String, String>): Call<SignUpResponse>

    @Multipart
    @POST("user/profile")
    fun uploadProfile(@Part image: MultipartBody.Part?): Call<UploadProfileResponse>

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
