package com.example.umctest1

data class LoginResponse(
    val status: Boolean,
    val code: String,
    val message: String,
    val data: LoginData?
)

data class LoginData(
    val name: String,
    val memberId: Int,
    val accessToken: String
)

