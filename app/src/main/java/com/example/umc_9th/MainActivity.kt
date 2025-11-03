package com.example.umc_9th

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_9th.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), OnAlbumPlayClickListener {

    private lateinit var binding: ActivityMainBinding
    private val gson = Gson()

    // 현재 재생 곡 상태
    private var song: Song = Song(
        title = "Lilac",
        singer = "아이유(IU)",
        second = 0,
        playTime = 180,
        isPlaying = false,
        music = "music_lilac"
    )

    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FLO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()
        loadCurrentSong()
        setMiniPlayer(song)
        initMiniPlayerButtons()

        // 미니플레이어 클릭 → SongActivity 실행
        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java).apply {
                putExtra("title", song.title)
                putExtra("singer", song.singer)
                putExtra("second", song.second)
                putExtra("playTime", song.playTime)
                putExtra("isPlaying", song.isPlaying)
                putExtra("music", song.music)
            }
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        startMiniPlayerSync()
    }

    override fun onPause() {
        super.onPause()
        handler?.removeCallbacksAndMessages(null)
    }

    /** ▶ / Ⅱ 버튼 리스너 */
    private fun initMiniPlayerButtons() {
        binding.mainMiniplayerBtn.setOnClickListener {
            updatePlayPauseState(true)
        }

        binding.mainPauseBtn.setOnClickListener {
            updatePlayPauseState(false)
        }
    }

    private fun updatePlayPauseState(isPlaying: Boolean) {
        song.isPlaying = isPlaying
        saveCurrentSong()
        updatePlayPauseIcon(isPlaying)
    }

    private fun updatePlayPauseIcon(isPlaying: Boolean) {
        if (isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }
    }

    /** ✅ 미니플레이어 실시간 동기화 */
    private fun startMiniPlayerSync() {
        handler = Handler(Looper.getMainLooper())
        handler?.post(object : Runnable {
            override fun run() {
                val prefs = getSharedPreferences("song", MODE_PRIVATE)
                val songJson = prefs.getString("songData", null)
                if (songJson != null) {
                    val updatedSong = gson.fromJson(songJson, Song::class.java)
                    song = updatedSong
                    setMiniPlayer(song)
                    updatePlayPauseIcon(song.isPlaying)
                }

                // ✅ 0.1초마다 SeekBar 업데이트
                handler?.postDelayed(this, 100)
            }
        })
    }

    /** ✅ 미니플레이어 UI 갱신 */
    private fun setMiniPlayer(s: Song) {
        binding.mainMiniplayerTitleTv.text = s.title
        binding.mainMiniplayerSingerTv.text = s.singer

        val progress = ((s.second.toFloat() / s.playTime.toFloat()) * 100)
            .toInt()
            .coerceIn(0, 100)
        binding.mainMiniplayerProgressSb.progress = progress
    }

    /** ✅ HomeFragment에서 ▶ 눌렀을 때 */
    override fun onAlbumPlay(album: Album) {
        song = Song(
            title = album.title ?: "제목 없음",
            singer = album.singer ?: "가수 없음",
            second = 0,
            playTime = 180,
            isPlaying = true,
            music = album.music ?: "music_lilac"
        )
        setMiniPlayer(song)
        saveCurrentSong()
        updatePlayPauseIcon(true)
    }

    /** 현재 곡 저장 */
    private fun saveCurrentSong() {
        val songJson = gson.toJson(song)
        val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
        editor.putString("songData", songJson)
        editor.apply()
    }

    /** 현재 곡 불러오기 */
    private fun loadCurrentSong() {
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData", null)
        song = if (songJson == null) {
            Song("Lilac", "아이유(IU)", 0, 180, false, "music_lilac")
        } else {
            gson.fromJson(songJson, Song::class.java)
        }
    }

    /** 하단 네비게이션 */
    private fun initBottomNavigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    true
                }

                else -> false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacksAndMessages(null)
    }

}
interface OnAlbumPlayClickListener {
    fun onAlbumPlay(album: Album)
}