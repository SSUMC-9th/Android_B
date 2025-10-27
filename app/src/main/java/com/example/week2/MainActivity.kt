package com.example.week2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.week2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()

        val song = Song(binding.title.text.toString(), binding.singer.text.toString())
        Log.d("Song", song.title + song.singer)

        binding.miniPlayer.setOnClickListener{
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songID", song.id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fraimContainer, HomeFragment())
            .commit()

        binding.mainBnv.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.homeFragment_bt -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fraimContainer, HomeFragment())
                        .commit()
                    true
                }
                R.id.lookFragment_bt -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fraimContainer, LookFragment())
                        .commit()
                    true
                }
                R.id.searchFragment_bt -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fraimContainer, SearchFragment())
                        .commit()
                    true
                }
                R.id.lockerFragment_bt -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fraimContainer, LockerFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!
        val song = if(songId == 0){
            songDB.songDao().getSong(1)
        }else{
            songDB.songDao().getSong(songId)
        }

        Log.d("song ID", song.id.toString())
        setMiniPlayer(song)
    }

    private var isPlaying = false

    private fun setMiniPlayer(song: Song) {
        binding.title.text = song.title
        binding.singer.text = song.singer

        binding.mainMiniplayerBtn.setOnClickListener {
            isPlaying = !isPlaying
            val icon = if (isPlaying) R.drawable.btn_miniplay_pause else R.drawable.btn_player_play
            binding.mainMiniplayerBtn.setImageResource(icon)
        }
    }



    private fun inputDummySongs(){
        val songDB = SongDatabase.getInstance(this)
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return

        songDB.songDao().insert(
            Song(
                "drama",
                "aespa"
            )
        )

        songDB.songDao().insert(
            Song(
                "ex0",
                "아이유"
            )
        )

        songDB.songDao().insert(
            Song(
                "ex1",
                "방탄"
            )
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }
}