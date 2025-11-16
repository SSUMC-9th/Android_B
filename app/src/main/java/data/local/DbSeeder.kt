package data.local

import android.content.Context
import com.example.realflo.Album
import com.example.realflo.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DbSeeder {
    suspend fun seedIfNeeded(context: Context) = withContext(Dispatchers.IO) {
        val db = FloDatabase.getInstance(context)
        val albumDao = db.albumDao()
        val songDao = db.songDao()

        // Album 테이블에 데이터가 이미 있는지 확인
        if (albumDao.getAlbums().isNotEmpty()) {
            return@withContext
        }

        // 앨범 더미
        albumDao.insertAll(
            listOf(
                Album(
                    title = "NewJeans",
                    singer = "NewJeans",
                    description = "NewJeans의 다양한 음악이 담긴 EP"
                ),
                Album(
                    title = "IU Special",
                    singer = "IU",
                    description = "아이유 스페셜 앨범 모음"
                )
            )
        )

        val albums = albumDao.getAlbums()
        val newJeansId = albums.first { it.title == "NewJeans" }.id
        val iuId = albums.first { it.title == "IU Special" }.id

        // 노래 더미 (raw에 music_lilac.mp3 이미 있으니 예시로 사용)
        songDao.insertAll(
            listOf(
                Song(
                    title = "Hype Boy",
                    singer = "NewJeans",
                    playTime = 210,
                    albumIdx = newJeansId!!,
                    music = "music_hypeboy"
                ),
                Song(title = "Ditto",    singer = "NewJeans", playTime = 190, albumIdx = newJeansId!!),
                Song(title = "LILAC",    singer = "IU",       playTime = 230, albumIdx = iuId!!, music = "music_lilac")
            )
        )
    }
}
