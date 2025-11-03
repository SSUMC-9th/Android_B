package com.example.umc_9th

import com.example.umc_9th.Song

data class Album(
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null,
    var songs: ArrayList<Song> = ArrayList(),
    var isPlaying: Boolean = false,
    var music: String? = ""

)
