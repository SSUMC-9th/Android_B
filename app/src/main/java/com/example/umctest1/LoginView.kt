package com.example.umctest1

interface LoginView {
    fun onLoginSuccess(code: String, data: LoginData)
    fun onLoginFailure(message: String)
}

