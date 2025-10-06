package com.example.realflo

import androidx.annotation.DrawableRes

data class LockerData(
    @DrawableRes val profileImage: Int,
    val name: String,
    val email: String
)
