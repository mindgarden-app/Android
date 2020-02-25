package com.example.mindgarden.data

data class DiaryData(
    var diaryIdx : Int,
    var date : String,
    var diary_content : String,
    var weatherIdx : Int,
    var userIdx : Int,
    var diary_img : String?
)