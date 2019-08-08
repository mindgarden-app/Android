package com.example.mindgarden.Network.GET

import com.example.mindgarden.Data.TokenData

data class GetAccessTokenResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: ArrayList<TokenData>?
)