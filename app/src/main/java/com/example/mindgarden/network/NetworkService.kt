package com.example.mindgarden.network

import com.example.mindgarden.data.vo.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {
    //일기 상세보기
    @GET("/diarylist/click/{diaryIdx}")
    fun getDiary(
        @Header("token") token: String,
        @Path("diaryIdx") diaryIdx : Int
    ): Call<DiaryResponse>

    //일기 등록
    @Multipart
    @POST("/diary/save")
    fun postDiary(
        @Header("token") token: String,
        @Part("diary_content") diary_content : RequestBody,
        @Part("weatherIdx") weatherIdx : Int,
        @Part diary_img : MultipartBody.Part?
    ): Call<DiaryIdx>

    //일기 수정
    @Multipart
    @PUT("/diary/complete")
    fun putDiary(
        @Header("token") token: String,
        @Part("diary_content") diary_content : RequestBody,
        @Part("weatherIdx") weatherIdx: Int,
        @Part("diaryIdx") diaryIdx: Int,
        @Part diary_img: MultipartBody.Part?
    ) : Call<PostDiaryResponse>

    //일기 목록 조회
    @GET("/diarylist/{date}")
    fun getDiaryList(
        @Header("token") token: String,
        @Path("date") date: String
    ): Call<DiaryListResponse>

    //일기 삭제
    @DELETE("/diarylist/delete/{diaryIdx}")
    fun deleteDiaryList(
        @Header("token") token: String,
        @Path("diaryIdx") date: Int
    ): Call<Unit>

    //나무심기
    @POST("/garden/plant")
    fun postPlant(
        @Header("token") token: String,
        @Body() body: JsonObject
    ): Call<PlantResponse>

    //가든
    @GET("/garden/{date}")
    fun getPlant(
        @Header("token") token: String,
        @Path("date") date: String
    ): Call<GardenResponse>

    //토큰 재발금
    @POST("/user/refresh")
    fun postRenewAccessToken(
        @Header("refreshtoken") token:String,
        @Body() body:JsonObject
    ): Call<RenewAccessTokenResponse>

    @DELETE("/user/delete")
    fun deleteUser(
        @Header("token") token:String
    ):Call<DeleteUserResponse>

    //이메일 회원가입
    @POST("user/signup")
    fun postEmailSignUp(
        @Body() body: JsonObject
    ):Call<EmailSignUpResponse>

    //이메일 로그인
    @POST("user/signin")
    fun postEmailSignIn(
        @Body() body: JsonObject
    ):Call<EmailSignInResponse>

    @GET("/auth/mail")
    fun getForgetPassword(
        @Header("token") token: String
    ):Call<ForgetPasswordResponse>
}