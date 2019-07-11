package com.example.mindgarden.Network.GET

import com.example.mindgarden.Data.MainData

data class GetMainResponse(
    val stasut : Int,
    val success : String,
    val data : ArrayList<MainData>
)
