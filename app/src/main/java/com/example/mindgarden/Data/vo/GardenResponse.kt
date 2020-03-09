package com.example.mindgarden.data.vo

data class GardenResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data : ArrayList<GardenData>
){
    data class  GardenData(
        var date : String,
        var location : Int,
        var treeIdx : Int,
        var balloon : Int,
        var treeNum : Int
    )
}
