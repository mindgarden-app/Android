package com.example.mindgarden.data

data class InventoryData(var treeIcn : Int, var treeIdx : Int, var type : Int){
    companion object{
        val default = 0
        val click = 1
        val block = 2
    }
}