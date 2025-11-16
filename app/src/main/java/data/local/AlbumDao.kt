package data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.realflo.Album
import com.example.realflo.Song

@Dao
interface AlbumDao {
    @Insert
    suspend fun insert(album: Album)

    @Insert
    suspend fun insertAll(albums: List<Album>)

    @Update
    suspend fun update(album: Album)

    @Query("SELECT * FROM AlbumTable")
    suspend fun getAlbums(): List<Album>

    @Query("SELECT * FROM AlbumTable WHERE id = :albumId")
    suspend fun getAlbum(albumId: Int): Album?
}