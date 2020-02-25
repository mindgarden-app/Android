package com.example.mindgarden.network.POST

import com.example.mindgarden.data.TokenData

data class PostRenewAccessTokenResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: ArrayList<TokenData>?
)