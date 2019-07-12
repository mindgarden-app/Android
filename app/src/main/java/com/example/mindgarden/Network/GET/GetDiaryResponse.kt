package com.example.mindgarden.Network.GET

import com.example.mindgarden.Data.DiaryData

data class GetDiaryResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: ArrayList<DiaryData>?
)