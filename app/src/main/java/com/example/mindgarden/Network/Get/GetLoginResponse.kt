package com.example.mindgarden.Network.Get

class GetLoginResponse (
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: ArrayList<Int>?
)