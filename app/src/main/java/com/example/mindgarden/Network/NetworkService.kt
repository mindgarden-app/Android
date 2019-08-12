package com.example.mindgarden.Network

import com.example.mindgarden.Network.Delete.DeleteDiaryListResponse
import com.example.mindgarden.Network.GET.*
import com.example.mindgarden.Network.POST.PostAccessTokenResponse
import com.example.mindgarden.Network.POST.PostPlantResponse
import com.example.mindgarden.Network.POST.PostWriteDiaryResponse
import com.example.mindgarden.Network.PUT.PutModifyDiaryResponse
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.*

interface NetworkService {
    //일기 목록 조회
    @GET("/diarylist/{date}")
    fun getDiaryListResponse(
        @Header("token") token: String,
        @Path("date") date: String
    ): Call<GetDiaryListResponse>

    //일기 삭제
    @DELETE("/diarylist/delete/{date}")
    fun deleteDiaryListResponse(
        @Header("token") token: String,
        @Path("date") date: String
    ): Call<DeleteDiaryListResponse>

    //일기 상세보기
    @GET("/diarylist/click/{date}")
    fun getDiaryResponse(
        @Header("token") token: String,
        @Path("date") date: String
    ): Call<GetDiaryResponse>

    //일기 등록
    @Multipart
    @POST("/diary/save")
    fun postWriteDiaryResponse(
        @Header("token") token: String,
        @Part("diary_content") diary_content : RequestBody,
        @Part("weatherIdx") weatherIdx : Int,
        @Part diary_img : MultipartBody.Part?

    ): Call<PostWriteDiaryResponse>

    //일기 수정
    @Multipart
    @PUT("/diary/complete")
    fun putModifyDiaryResponse(
        @Header("token") token: String,
        @Part("diary_content") diary_content : RequestBody,
        @Part("weatherIdx") weatherIdx: Int,
        @Part("date") date : RequestBody,
        @Part diary_img: MultipartBody.Part?
    ) : Call<PutModifyDiaryResponse>

    //메인
    @GET("/garden/{date}")
    fun getMainResponse(
        @Header("token") token: String,
        @Path("date") date: String
    ): Call<GetMainResponse>

    @GET("/auth/mail/{userIdx}")
    fun getForgetPasswordResponse(
        @Header("Content-Type") content_type: String,
        @Path("accessToken") accessToken: String
    ):Call<GetForgetPasswordResponse>

    //나무심기
    @POST("/garden/plant")
    fun postPlantResponse(
        @Header("token") token: String,
        @Body() body:JsonObject
    ): Call<PostPlantResponse>

    //나무심기 창 가든
    @GET("/garden/{date}")
    fun getPlantResponse(
        @Header("token") token: String,
        @Path("date") date: String
    ): Call<GetPlantResponse>

    //토큰 재발금
    @POST("/user/refresh")
    fun postRenewAccessToken(
        @Header("token") token:String
    ): Call<PostAccessTokenResponse>
}