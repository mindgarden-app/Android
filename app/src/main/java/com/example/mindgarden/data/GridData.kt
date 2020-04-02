package com.example.mindgarden.data

data class GridData(var type : Int,var gridId: Int, var img: Int?, var season: Int) {
    companion object {
        const val defaultType = 0
        const val lakeType = 1
        const val alreadyExistType = 2
        const val clickType=3
    }
}