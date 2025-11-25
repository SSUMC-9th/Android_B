package com.example.realflo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
data class Album(
    var title: String = "",
    var singer: String = "",
    var coverImg: Int = 0,
    var releaseDate: String = "",


    var isLike: Boolean = false
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
