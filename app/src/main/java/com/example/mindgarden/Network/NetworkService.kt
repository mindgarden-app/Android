package com.example.mindgarden.Network

import com.example.mindgarden.Network.Get.GetDiaryListResponse
import com.example.mindgarden.Network.POST.PostWriteDiaryResponse
import com.example.mindgarden.Network.PUT.PutReadDiaryResponse
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {
    @GET("/diarylist")
    fun getDiaryListResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Call<GetDiaryListResponse>

    //일기 등록
    @Multipart
    @POST("/diary/save")
    fun postWriteDiaryResponse(
        @Header("Content-Type") content_type: RequestBody,
        @Part("diary_content") diary_content : RequestBody,
        @Part("userIdx") userIdx : Int,
        @Part diary_img : MultipartBody.Part,
        @Part("weatherIdx") weatherIdx : Int
    ): Call<PostWriteDiaryResponse>

    //일기 수정
    @Multipart
    @PUT("/garden/complete")
    fun putReadDiaryResponse(
        @Header("Content-Type") content_type: RequestBody,
        @Part("userIdx") userIdx: Int,
        @Part diary_img: MultipartBody.Part,
        @Part("weatherIdx") weatherIdx: Int,
        @Part("date") date : RequestBody
    ) : Call<PutReadDiaryResponse>
}