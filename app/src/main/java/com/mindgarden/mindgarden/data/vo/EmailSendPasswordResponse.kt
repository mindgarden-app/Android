package com.mindgarden.mindgarden.data.vo

data class EmailSendPasswordResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data:String
)