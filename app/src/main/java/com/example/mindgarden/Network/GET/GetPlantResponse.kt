package com.example.mindgarden.Network.GET

import com.example.mindgarden.Data.MainData

data class GetPlantResponse(
    val status : Int,
    val success : Boolean,
    val message: String,
    val data : ArrayList<MainData>?
)