package com.omnyom.yumyum.interfaces

import com.omnyom.yumyum.model.login.LoginResponse
import com.omnyom.yumyum.model.myinfo.UserModel
import com.omnyom.yumyum.model.signup.SignUpResponse
import com.omnyom.yumyum.model.signup.UploadProfileResponse
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

    @GET("user/login/{email}")
    fun login(@Path("email") email: String): Call<LoginResponse>

    @POST("user/signup")
    fun signup(@Body parameters: HashMap<String, String>): Call<SignUpResponse>

    @Multipart
    @POST("user/profile")
    fun uploadProfile(@Part image: MultipartBody.Part?): Call<UploadProfileResponse>
}