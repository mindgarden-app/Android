package com.example.mindgarden.Network

import com.example.mindgarden.Network.Delete.DeleteDiaryListResponse
import com.example.mindgarden.Network.GET.GetDiaryListResponse
import com.example.mindgarden.Network.GET.GetDiaryResponse
import com.example.mindgarden.Network.Get.GetLoginResponse
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
    @GET("/diarylist/{userIdx}/{date}")
    fun getDiaryListResponse(
        @Header("Content-Type") content_type: String,
        @Path("userIdx") userIdx: Int,
        @Path("date") date: String
        //@Body() body: JsonObject
    ): Call<GetDiaryListResponse>

    //일기 삭제
    @DELETE("/diarylist/delete/{userIdx}/{date}")
    fun deleteDiaryListResponse(
        @Header("Content-Type") content_type: String,
        @Path("userIdx") userIdx: Int,
        @Path("date") date: String
    ): Call<DeleteDiaryListResponse>

    /*
     @GET("/diarylist/click/{userIdx}/{date}")
    fun getDiaryListClickResponse(
        @Header("Content-Type") content_type: String,
        @Path("userIdx") userIdx: Int,
        @Path("date") date: String
    ): Call<GetDiaryListClickResponse>
     */
    //일기 상세보기
    @GET("/diarylist/click/{userIdx}/{date}")
    fun getDiaryResponse(
        @Header("Content-Type") content_type: String,
        @Path("userIdx") userIdx: Int,
        @Path("date") date: String
    ): Call<GetDiaryResponse>

    //일기 등록
    @Multipart
    @POST("/diary/save")
    fun postWriteDiaryResponse(
        @Part("diary_content") diary_content : RequestBody,
        @Part("userIdx") userIdx : Int,
        @Part("weatherIdx") weatherIdx : Int,
        @Part diary_img : MultipartBody.Part?

    ): Call<PostWriteDiaryResponse>

    //일기 수정
    @Multipart
    @PUT("/garden/complete")
    fun putReadDiaryResponse(
        @Header("Content-Type") content_type: String,
        @Part("userIdx") userIdx: Int,
        @Part("weatherIdx") weatherIdx: Int,
        @Part("date") date : RequestBody,
        @Part diary_img: MultipartBody.Part?
    ) : Call<PutModifyDiaryResponse>

    /*
    //메인
    @GET("/garden/{userIdx}/{date}")
    fun getMainResponse(
        @Header("Content-Type") content_type: String,
        @Path("userIdx") userIdx: Int,
        @Path("date") date: String
    ) : Call<>
     */


    @GET("/auth/login/success")
    fun getLoginResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ):Call<GetLoginResponse>
}