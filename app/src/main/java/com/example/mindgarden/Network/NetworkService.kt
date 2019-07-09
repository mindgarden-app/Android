package com.example.mindgarden.Network

import com.example.mindgarden.Network.Delete.DeleteDiaryListResponse
import com.example.mindgarden.Network.Get.GetDiaryListClickResponse
import com.example.mindgarden.Network.Get.GetDiaryListResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header

interface NetworkService {
    @GET("/api/diarylist")
    fun getDiaryListResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Call<GetDiaryListResponse>

    @GET("/api/diarylist/click")
    fun getDiaryListClickResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Call<GetDiaryListClickResponse>

    @DELETE("/api/diarylist/delete")
    fun deleteDiaryListResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Call<DeleteDiaryListResponse>
}