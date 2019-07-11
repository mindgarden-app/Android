package com.example.mindgarden.Network.GET

data class GetForgetPasswordResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: String
)