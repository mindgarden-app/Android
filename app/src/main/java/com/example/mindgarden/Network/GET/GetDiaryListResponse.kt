package com.example.mindgarden.Network.GET

import com.example.mindgarden.Data.DiaryListData

data class GetDiaryListResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: ArrayList<DiaryListData>?
)