package com.example.mindgarden.data.vo

data class DiaryListResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: ArrayList<DiaryListData>?
){
    data class DiaryListData(
        var diaryIdx: Int,
        var date: String,
        var diary_content: String,
        var weatherIdx: Int,
        var userIdx: Int,
        var diary_img: String)
}