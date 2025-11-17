package com.keder.flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
// (Firebase 임포트 삭제)
import com.google.gson.Gson
import com.keder.flo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(){
    private lateinit var binding : ActivityMainBinding

    private var song : Song = Song()
    private var gson: Gson = Gson()

    private var songs = ArrayList<Song>()
    private var nowPos = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Flo)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        inputDummySongs() // Room DB에 더미 데이터 삽입
        inputDummyAlbums()

        binding.minibarPreviousIv.setOnClickListener { moveSong(-1) }
        binding.minibarNextIv.setOnClickListener { moveSong(+1) }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        binding.mainPlayerCl.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()

            // ⬇️ 3. Int ID를 SharedPreferences에 저장
            editor.putInt("songId", song.id) // putString -> putInt
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        binding.mainBnv.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()

        // ⬇️ 4. SharedPreferences에서 Int ID 가져오기
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0) // getString -> getInt

        val songDB = SongDatabase.getInstance(this)!!

        song = if(songId == 0){
            songDB.songDao().getSong(1)
        }else{
            songDB.songDao().getSong(songId)
        }
        Log.d("song ID", song.id.toString())
        setMiniPlayer(song)
    }

    // ⬇️ 7. Int ID를 받도록 수정
    private fun getPlayingSongPosition(songId : Int) : Int{
        for(i in 0 until songs.size){
            if(songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    // ⬇️ 8. SharedPreferences에 Int ID를 저장하도록 수정
    private fun moveSong(direct : Int){
        if (songs.isEmpty()) {
            Log.e("moveSong", "Songs list is empty, cannot move.")
            return
        }

        val nextPos = nowPos + direct

        if (nextPos < 0 || nextPos >= songs.size) {
            Log.d("moveSong", "Out of bounds, returning.")
            return
        }

        nowPos = nextPos
        song = songs[nowPos]

        setMiniPlayer(song) // 미니 플레이어 UI 업데이트

        val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
        editor.putInt("songId", song.id) // putString -> putInt
        editor.apply()
    }

    private fun setMiniPlayer(song : Song){
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainMiniplayerProgressSb.progress = (song.second*100000)/song.playTime
    }

    // ⬇️ 9. Room DB에 insert하는 코드로 원복
    private fun inputDummySongs(){
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if(songs.isNotEmpty()) return

        // (Firebase 코드 전체 삭제)

        Log.d("DB", "Inserting dummy songs...")
        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false
            )
        )

        songDB.songDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_flu",
                R.drawable.img_album_exp2,
                false
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false
            )
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }

    private fun inputDummyAlbums(){

        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if(albums.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(0, "IU 5th Album 'LILAC'", "아이유(IU)", R.drawable.img_album_exp2)
        )

        songDB.albumDao().insert(
            Album(1, "Butter", "방탄소년단(BTS)", R.drawable.img_album_exp)
        )

        songDB.albumDao().insert(
            Album(2, "IU 5th Album 'LILAC'22", "아이유(IU)", R.drawable.img_album_exp2)
        )

    }
}