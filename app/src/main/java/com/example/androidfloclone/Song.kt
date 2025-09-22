package com.example.androidfloclone

data class Song(
    val title : String = "",
    val singer : String = "",
    var isPlaying : Boolean = false,
    var second : Int = 0,
    var minute : Int = 0,
    var playTime : Int = 0
)
