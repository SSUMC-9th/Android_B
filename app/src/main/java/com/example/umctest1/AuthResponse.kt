package com.example.umctest1

data class AuthResponse(
    val status: Boolean,
    val code: String,
    val message: String,
    val data: AuthData?
)

data class AuthData(
    val memberId: Int
)