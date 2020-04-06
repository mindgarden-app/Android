package com.mindgarden.mindgarden.data.vo

data class EmailSignInResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data:ArrayList<EmailSignInData>?
){
    data class EmailSignInData(
        val token:String,
        val refreshToken:String,
        val email:String,
        val name:String,
        val expires_in:Long
    )
}