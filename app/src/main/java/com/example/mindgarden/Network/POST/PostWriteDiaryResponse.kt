package com.example.mindgarden.Network.POST

import com.example.mindgarden.Data.WriteDiaryData


data class PostWriteDiaryResponse (
    val status : Int,
    val success : Boolean,
    val message : String,
    val data: ArrayList<WriteDiaryData>
)