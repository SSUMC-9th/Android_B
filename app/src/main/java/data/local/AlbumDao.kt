package data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.realflo.Album

@Dao
interface AlbumDao {

    // 모든 앨범 가져오기
    @Query("SELECT * FROM AlbumTable")
    fun getAllAlbums(): List<Album>

    // 특정 앨범 가져오기
    @Query("SELECT * FROM AlbumTable WHERE id = :albumId")
    fun getAlbumById(albumId: Int): Album?

    // 좋아요 업데이트
    @Query("UPDATE AlbumTable SET isLike = :isLike WHERE id = :albumId")
    fun updateAlbumLike(albumId: Int, isLike: Boolean)

    // 좋아요한 앨범만 가져오기
    @Query("SELECT * FROM AlbumTable WHERE isLike = 1")
    fun getLikedAlbums(): List<Album>

    // 초기 데이터 넣기
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlbums(albums: List<Album>)

    @Query("DELETE FROM AlbumTable WHERE id = :albumId")
    suspend fun deleteAlbum(albumId: Int)

    @Query("SELECT * FROM AlbumTable WHERE id = :albumId LIMIT 1")
    suspend fun getAlbums(albumId: Int): Album?
}
