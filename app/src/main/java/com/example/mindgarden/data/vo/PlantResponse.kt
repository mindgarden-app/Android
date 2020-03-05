package com.example.mindgarden.data.vo

data class PlantResponse(
    val data: ArrayList<Plant>?
){
    data class Plant(
        var ballon: Int,
        var check : Int
    )
}