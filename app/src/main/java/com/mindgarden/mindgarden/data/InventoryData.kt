package com.mindgarden.mindgarden.data

data class InventoryData(var treeIcn : Int, var treeIdx : Int, var type : Int, var season: Int){
    companion object{
        val default = 0
        val click = 1

    }
}