package com.example.mindgarden.Network.Get

import com.example.mindgarden.Data.LoginData

class GetLoginResponse (
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: LoginData?
)