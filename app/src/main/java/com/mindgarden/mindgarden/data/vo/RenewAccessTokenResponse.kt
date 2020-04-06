package com.mindgarden.mindgarden.data.vo

import com.mindgarden.mindgarden.data.TokenData

data class RenewAccessTokenResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: ArrayList<TokenData>?
)