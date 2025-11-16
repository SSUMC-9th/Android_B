package com.example.realflo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")
data class Song(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var singer: String = "",
    var second: Int = 0,      // 현재 재생 위치(초)
    var playTime: Int = 0,    // 총 길이(초)
    var isPlaying: Boolean = false,
    var music: String = "",   // raw 파일명 등
    var coverImg: Int? = null,
    var isLike: Boolean = false,
    var albumIdx: Int = 0
)
