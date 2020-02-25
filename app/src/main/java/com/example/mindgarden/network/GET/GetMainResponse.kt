package com.example.mindgarden.network.GET

import com.example.mindgarden.data.MainData

data class GetMainResponse(
    val status : Int,
    val success : Boolean,
    val message: String,
    val data : ArrayList<MainData>?
)
