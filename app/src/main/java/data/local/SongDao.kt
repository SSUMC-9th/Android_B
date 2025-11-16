package data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.realflo.Song

@Dao
interface SongDao {
    @Insert
    suspend fun insert(song: Song)

    @Insert
    suspend fun insertAll(songs: List<Song>)

    @Update
    suspend fun update(song: Song)

    @Query("SELECT * FROM SongTable")
    suspend fun getSongs(): List<Song>

    @Query("SELECT * FROM SongTable WHERE albumIdx = :albumId")
    suspend fun getSongsInAlbum(albumId: Int): List<Song>

    @Query("SELECT * FROM SongTable WHERE id = :songId")
    suspend fun getSongById(songId: Int): Song?

    @Query("UPDATE SongTable SET isLike = :isLike WHERE id = :songId")
    suspend fun updateLike(songId: Int, isLike: Boolean)

    // ✅ 추가: 좋아요한 곡만 가져오기
    @Query("SELECT * FROM SongTable WHERE isLike = 1")
    suspend fun getLikedSongs(): List<Song>
}