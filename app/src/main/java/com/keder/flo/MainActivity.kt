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
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.gson.Gson
import com.keder.flo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(){
    private lateinit var binding : ActivityMainBinding

    private var song : Song = Song()
    private var gson: Gson = Gson()

    private var songs = ArrayList<Song>()
    private var nowPos = 0
    //private lateinit var songDB : SongDatabase
    private val database = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Flo)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()
        binding.minibarPreviousIv.setOnClickListener { moveSong(-1) }
        binding.minibarNextIv.setOnClickListener { moveSong(+1) }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        binding.mainPlayerCl.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putString("songId", song.id)
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
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songJson = sharedPreferences.getString("songData", null)
//
//        song = if(songJson == null){
//            Song("라일락", "아이유(IU)", 0, 0,60, false, "music_lilac")
//        }else{
//            gson.fromJson(songJson, Song::class.java)
//        }
        //songDB = SongDatabase.getInstance(this)!!
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        //val songId = spf.getInt("songId", 0)
        val songIdspf = spf.getString("songId", "")
        val songId : String = songIdspf ?: ""

        database.child("songs").get().addOnSuccessListener { dataSnapshot ->
            songs.clear()
            var currentSong : Song? = null

            for(snapshot in dataSnapshot.children){
                val song = snapshot.getValue(Song::class.java)
                if(song != null){
                    songs.add(song)
                    if(song.id == songId){
                        currentSong = song
                        nowPos = songs.size -1
                    }
                }
            }
            if(currentSong == null && songs.isNotEmpty()){
                currentSong = songs[0]
                nowPos = 0
            }
            if(currentSong != null){
                song = currentSong!!
                Log.d("Song ID", song.id)
                setMiniPlayer(this.song)
                binding.minibarPreviousIv.isEnabled = true
                binding.minibarNextIv.isEnabled = true
            }
        }.addOnFailureListener { Log.e("Firebase", "Failed to load song for miniplayer", it) }


    }
    private fun getPlayingSongPosition(songId : String) : Int{
        for(i in 0 until songs.size){
            if(songs[i].id == songId){
                return i
            }
        }
        return 0
    }

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
        editor.putString("songId", song.id) // putInt -> putString
        editor.apply()
    }

    private fun setMiniPlayer(song : Song){
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainMiniplayerProgressSb.progress = (song.second*100000)/song.playTime
    }

    private fun inputDummySongs(){
//        val songDB = SongDatabase.getInstance(this)!!
//        val songs = songDB.songDao().getSongs()

//        if(songs.isNotEmpty()) return

        val songsRef = database.child("songs")
        songsRef.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.childrenCount == 0L){
                Log.d("Firebase", "Inserting dummy")

                val dummySong1 = Song(
                    "Lilac",
                    "아이유 (IU)",
                    0,
                    200,
                    false,
                    "music_lilac",
                    R.drawable.img_album_exp2,
                    false,
                )

                val newKey1 = songsRef.push().key
                if(newKey1 != null){
                    dummySong1.id = newKey1
                    songsRef.child(newKey1).setValue(dummySong1)
                }

                val dummySong2 = Song(
                    "Flu",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_flu",
                R.drawable.img_album_exp2,
                false,
                )

                val newKey2 = songsRef.push().key
                if(newKey2 != null){
                    dummySong1.id = newKey2
                    songsRef.child(newKey2).setValue(dummySong2)
                }

                val dummySong3 = Song(
                    "Butter",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false,
                )

                val newKey3 = songsRef.push().key
                if(newKey3 != null){
                    dummySong1.id = newKey3
                    songsRef.child(newKey3).setValue(dummySong3)
                }
            }else{
                Log.d("Firebase", "Dummy data already exits")
            }
        }.addOnFailureListener { Log.e("Firebase", "Failed to check dummy datas", it) }
//        songDB.songDao().insert(
//            Song(
//                "Lilac",
//                "아이유 (IU)",
//                0,
//                200,
//                false,
//                "music_lilac",
//                R.drawable.img_album_exp2,
//                false,
//            )
//        )
//
//        songDB.songDao().insert(
//            Song(
//                "Flu",
//                "아이유 (IU)",
//                0,
//                200,
//                false,
//                "music_flu",
//                R.drawable.img_album_exp2,
//                false,
//            )
//        )
//
//        songDB.songDao().insert(
//            Song(
//                "Butter",
//                "방탄소년단 (BTS)",
//                0,
//                190,
//                false,
//                "music_butter",
//                R.drawable.img_album_exp,
//                false,
//            )
//        )
//
//        val _songs = songDB.songDao().getSongs()
//        Log.d("DB data", _songs.toString())

    }


}