package com.example.mindgarden.network.GET

import com.example.mindgarden.data.DiaryListData

data class GetDiaryListResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: ArrayList<DiaryListData>?
)