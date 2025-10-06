package com.keder.flo

import androidx.room.PrimaryKey

class Song (
    val title : String = "",
    val singer : String = "",
    var coverImg: Int? = null,
    ){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}