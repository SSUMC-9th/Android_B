package data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.realflo.Album
import com.example.realflo.Song

@Database(
    entities = [Song::class, Album::class],
    version = 3,
    exportSchema = false
)
abstract class FloDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao
    abstract fun albumDao(): AlbumDao

    companion object {
        @Volatile private var INSTANCE: FloDatabase? = null

        fun getInstance(context: Context): FloDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    FloDatabase::class.java,
                    "flo.db"              // DB 파일명
                )
                    .fallbackToDestructiveMigration() // 스키마 바꾸다 깨지면 초기화
                    .build().also { INSTANCE = it }
            }
    }
}