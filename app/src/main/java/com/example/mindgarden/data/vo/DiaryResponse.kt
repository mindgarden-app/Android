package com.example.mindgarden.data.vo

import com.google.gson.annotations.SerializedName

data class DiaryResponse(
        val status: Int,
        val success: Boolean,
        val message: String,
        @SerializedName("data")val diaryResponse : ArrayList<Diary>
){
        data class Diary(
                var diaryIdx : Int,
                var date : String,
                var diary_content : String,
                var weatherIdx : Int,
                var userIdx : Int,
                var diary_img : String?
        )
}

data class DiaryIdx(
        @SerializedName("data") val diaryIdx: Int
)

data class PostDiaryResponse(
        val status: Int,
        val success: Boolean,
        val message: String
)
