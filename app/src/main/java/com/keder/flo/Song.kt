package com.keder.flo

import androidx.room.PrimaryKey

class Song (
    val title : String = "",
    val singer : String = "",
    var coverImg: Int? = null,
    var second : Int = 0,
    var playTime : Int = 0,
    var isPlaying : Boolean = false
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}