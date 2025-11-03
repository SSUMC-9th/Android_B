package com.example.week2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")
data class Song(
    val title: String = "",
    val singer: String = ""
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}


