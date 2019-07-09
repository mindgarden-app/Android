package com.example.mindgarden.Network.PUT

import com.example.mindgarden.Data.ReadDiaryData

data class PutReadDiaryResponse (
    val status : Int,
    val success : Boolean,
    val message : String,
    val data : ArrayList<ReadDiaryData>
)