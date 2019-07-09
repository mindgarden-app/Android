package com.example.mindgarden.Network

import com.example.mindgarden.Network.Get.GetDiaryListResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface NetworkService {
    @GET("/diarylist")
    fun getDiaryListResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Call<GetDiaryListResponse>


}