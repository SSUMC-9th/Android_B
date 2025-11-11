package com.example.week2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
data class Album(
    @PrimaryKey(autoGenerate = true)
    val albumIdx: Int = 0,
    val title: String = "",
    val singer: String = "",
    val coverImg: Int? = null
)
