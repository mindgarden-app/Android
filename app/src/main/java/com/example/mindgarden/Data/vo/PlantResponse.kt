package com.example.mindgarden.data.vo

data class PlantResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: ArrayList<Plant>
){
    data class Plant(
        var balloon: Int
    )
}