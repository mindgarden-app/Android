package com.example.mindgarden.Network.POST

import com.example.mindgarden.Data.TokenData

data class PostRenewAccessTokenResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: ArrayList<TokenData>?
)