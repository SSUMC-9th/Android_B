package com.example.realflo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.realflo.databinding.ActivityMainBinding
import data.local.FloDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    // 미니플레이어에 표시할 현재 곡 (DB에 있는 실제 Song)
    private var currentSong: Song? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Realflo)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 기본 화면 = 홈
        replaceFragment(HomeFragment())
        binding.mainBnv.selectedItemId = R.id.homeFragment

        // 하단 탭
        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.lockerFragment -> {
                    replaceFragment(LockerFragment())
                    true
                }
                R.id.searchFragment -> {
                    // 검색 프래그먼트 쓰면 여기서 교체
                    true
                }
                else -> false
            }
        }

        // ⭐ 미니플레이어 클릭 → SongActivity 실행
        binding.mainPlayerCl.setOnClickListener {
            val sp = getSharedPreferences("song", MODE_PRIVATE)
            var songId = sp.getInt("songId", -1)

            if (songId <= 0) {
                // prefs 에 저장된 게 없으면 currentSong 기준으로라도 열어줌(라일락 포함)
                val fallback = currentSong
                if (fallback == null || fallback.id <= 0) {
                    android.util.Log.w("MainActivity", "열 수 있는 곡이 없습니다.")
                    return@setOnClickListener
                }
                songId = fallback.id
                sp.edit().putInt("songId", songId).apply()
            }

            openSong(songId)
        }

        // 미니플레이어 재생/일시정지 버튼 (UI만 토글)
        binding.mainMiniplayerBtn.setOnClickListener { togglePlay(true) }
        binding.mainPauseBtn.setOnClickListener { togglePlay(false) }
    }

    override fun onStart() {
        super.onStart()

        val sp = getSharedPreferences("song", MODE_PRIVATE)
        val savedSongId = sp.getInt("songId", -1)

        lifecycleScope.launch {
            val db = FloDatabase.getInstance(this@MainActivity)

            val song = withContext(Dispatchers.IO) {
                val dao = db.songDao()

                // 1) DB에서 전체 곡 조회
                val all = dao.getSongs()

                // 1-1) DB에 곡이 하나도 없으면 → 라일락을 직접 DB에 집어넣는다
                if (all.isEmpty()) {
                    val lilac = Song(
                        title = "라일락",
                        singer = "아이유(IU)",
                        playTime = 214,
                        music = "music_lilac",  // res/raw/music_lilac.mp3 기준
                        albumIdx = 0,
                        isLike = false
                    )
                    dao.insert(lilac)

                    // 방금 넣은 라일락 다시 읽어오기 (id가 자동생성되니까)
                    dao.getSongs().firstOrNull()
                } else {
                    // 2) DB에 곡은 있는데, 저장된 songId가 있으면 그 곡 우선
                    if (savedSongId > 0) {
                        dao.getSongById(savedSongId) ?: all.first()
                    } else {
                        // 3) songId 없으면 라일락 우선 선택, 없으면 첫 곡
                        all.firstOrNull {
                            it.title == "라일락" || it.title.equals("LILAC", true)
                        } ?: all.first()
                    }
                }
            }

            currentSong = song

            if (song != null) {
                // prefs에 songId가 아직 없었다면 여기서 한 번 저장 (라일락 포함)
                if (savedSongId <= 0 && song.id > 0) {
                    sp.edit().putInt("songId", song.id).apply()
                }
                setMiniPlayer(song)
            } else {
                // 진짜로 DB에 곡이 아무것도 없을 때만 (거의 없겠지만)
                setEmptyMiniPlayer()
            }
        }
    }

    // SongActivity 열기
    private fun openSong(songId: Int) {
        if (songId <= 0) return
        val intent = Intent(this, SongActivity::class.java)
        intent.putExtra("songId", songId)
        startActivity(intent)
    }

    // 프래그먼트 전환
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, fragment)
            .commit()
    }

    // ✅ 실제 곡 있을 때 미니플레이어 UI
    private fun setMiniPlayer(song: Song) {
        binding.mainPlayerCl.visibility = View.VISIBLE

        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer

        val progress = if (song.playTime == 0) 0 else (song.second * 100 / song.playTime)
        binding.mainMiniplayerProgressSb.progress = progress

        if (song.isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }
    }

    // ✅ DB에 곡이 아예 없을 때만 호출 (거의 안 쓰일 것)
    private fun setEmptyMiniPlayer() {
        binding.mainPlayerCl.visibility = View.VISIBLE

        binding.mainMiniplayerTitleTv.text = "재생할 곡이 없습니다"
        binding.mainMiniplayerSingerTv.text = "앱에 곡 데이터를 추가해 주세요"
        binding.mainMiniplayerProgressSb.progress = 0

        binding.mainMiniplayerBtn.visibility = View.VISIBLE
        binding.mainPauseBtn.visibility = View.GONE
    }

    // 미니플레이어 재생/일시정지 토글 (UI만)
    private fun togglePlay(isPlaying: Boolean) {
        if (isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }
    }
}
