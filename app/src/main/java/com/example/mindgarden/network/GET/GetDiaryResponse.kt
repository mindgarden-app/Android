package com.example.mindgarden.network.GET

import com.example.mindgarden.data.DiaryData

data class GetDiaryResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: ArrayList<DiaryData>?
)