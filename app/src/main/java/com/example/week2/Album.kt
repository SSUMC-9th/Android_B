package com.example.week2

data class Album(
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null,
    var song: ArrayList<Song>? = null
)
