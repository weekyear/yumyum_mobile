package com.omnyom.yumyum.interfaces

import com.omnyom.yumyum.model.feed.AllFeedResponse
import com.omnyom.yumyum.model.feed.CreateFeedResponse
import com.omnyom.yumyum.model.feed.SendVideoResponse
import com.omnyom.yumyum.model.like.LikeResponse
import com.omnyom.yumyum.model.login.LoginResponse
import com.omnyom.yumyum.model.place.GetPlaceDataResponse
import com.omnyom.yumyum.model.place.SearchPlaceListResponse
import com.omnyom.yumyum.model.signup.SignUpResponse
import com.omnyom.yumyum.model.signup.UploadProfileResponse
import com.omnyom.yumyum.model.userInfo.UserResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface RetrofitService {
    // User
    @POST("user/login")
    fun loginSample(@Body parameters: HashMap<String, String>): Call<LoginResponse>

    @GET("user/login/{email}")
    fun login(@Path("email") email: String): Call<LoginResponse>

    @POST("user/signup")
    fun signup(@Body parameters: HashMap<String, String>): Call<SignUpResponse>

    @Multipart
    @POST("user/profile")
    fun uploadProfile(@Part image: MultipartBody.Part?): Call<UploadProfileResponse>

    // userId로 유저정보 불러오기
    @GET("user/{userId}")
    fun getUserData(@Path("userId") userId: Long) : Call<UserResponse>

    // Feed
    // 모든 피드 불러오기
    @GET("feed/list/{userId}")
    fun getAllFeeds(@Path("userId") userId: Long): Call<AllFeedResponse>

    // 특정 유저 피드 불러오기
    @GET("feed/list/{authorId}/{userId}")
    fun getUserFeeds(@Path("userId") userId: Long, @Path("authorId")authorId: Long ) : Call<AllFeedResponse>

    // 비디오 데이터 보내기
    @Multipart
    @POST("feed/video")
    fun sendVideo(@Part file: MultipartBody.Part ): Call<SendVideoResponse>

    // 피드 작성하기
    @POST("feed/")
    fun createFeed(@Body parameters: HashMap<String, Any>): Call<CreateFeedResponse>

    // 피드 좋아요!
    @POST("feed/like")
    fun feedLike(@Body parameters: HashMap<String, Int>) : Call<LikeResponse>

    // 피드 좋아요 취소!
    @DELETE("feed/like/{feedId}/{userId}")
    fun cancelFeedLike(@Path("feedId") feedId: Long, @Path("userId") userId: Long) : Call<LikeResponse>

    // 좋아요 피드 불러오기
    @GET("feed/list/like/{userId}")
    fun getLikedFeed(@Path("userId") userId: Long) : Call<AllFeedResponse>

    // Place
    // 식당ID로 정보 불러오기
    @GET("place/{placeId}")
    fun getPlaceData(@Path("placeId") placeId: Long) : Call<GetPlaceDataResponse>

    // 키워드로 장소, 주소 검색
    @GET("place/list/{type}/{keyword}")
    fun getSearchPlaceList(@Path("type") type: String, @Path("keyword") keyword: String) : Call <SearchPlaceListResponse>
}
