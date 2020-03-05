package com.example.mindgarden.data.vo

import com.example.mindgarden.data.TokenData

data class RenewAccessTokenResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: ArrayList<TokenData>?
)