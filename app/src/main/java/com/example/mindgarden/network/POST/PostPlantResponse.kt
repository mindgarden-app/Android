package com.example.mindgarden.network.POST

import com.example.mindgarden.data.PlantData

data class PostPlantResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: ArrayList<PlantData>?
)