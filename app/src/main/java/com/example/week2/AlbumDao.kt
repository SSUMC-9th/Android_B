package com.example.week2

import androidx.room.*

@Dao
interface AlbumDao {

    @Insert
    fun insert(album: Album)

    @Insert
    fun insert(albums: List<Album>)

    @Update
    fun update(album: Album)

    @Delete
    fun delete(album: Album)

    @Query("SELECT * FROM AlbumTable")
    fun getAlbums(): List<Album>

    @Query("SELECT * FROM AlbumTable WHERE albumIdx = :albumIdx")
    fun getAlbum(albumIdx: Int): Album?
}
