package com.example.mindgarden.Network.POST

import com.example.mindgarden.Data.PlantData

data class PostPlantResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: ArrayList<PlantData>?
)