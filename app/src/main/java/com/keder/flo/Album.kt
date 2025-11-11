package com.keder.flo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
class Album (
    var title : String? = "",
    var singer : String? = "",
    var coverImg : Int? = null,
    var isLike : Boolean = false,
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}