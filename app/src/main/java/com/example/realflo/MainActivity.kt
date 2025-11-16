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

    // 미니플레이어에서 표시할 현재 곡(간단 캐시)
    private var currentSong: Song? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Realflo)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1) 최초 화면: 홈
        replaceFragment(HomeFragment())

        // 2) BottomNavigation 탭 전환
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
                    // 검색 탭이 있다면 여기에 프래그먼트 연결
                    // replaceFragment(SearchFragment())
                    true
                }
                else -> false
            }
        }
        // 기본 선택 탭
        binding.mainBnv.selectedItemId = R.id.homeFragment

        // 3) 미니플레이어 클릭 → SongActivity로 이동
        binding.mainPlayerCl.setOnClickListener {
            // 현재 저장된 songId 기반으로 이동
            val sp = getSharedPreferences("song", MODE_PRIVATE)
            val songId = sp.getInt("songId", -1)
            val intent = Intent(this, SongActivity::class.java)
            if (songId != -1) {
                intent.putExtra("songId", songId)
            } else {
                // songId가 없을 때를 대비해 제목/가수만 최소 전달(선택)
                currentSong?.let {
                    intent.putExtra("songId", it.id)
                    intent.putExtra("title", it.title)
                    intent.putExtra("singer", it.singer)
                }
            }
            startActivity(intent)
        }

        // 미니플레이어 재생/일시정지 버튼(있으면) 동작 예시
        binding.mainMiniplayerBtn.setOnClickListener { togglePlay(false) }
        binding.mainPauseBtn.setOnClickListener { togglePlay(true) }
    }

    override fun onStart() {
        super.onStart()
        // 저장된 songId로 DB에서 현재 곡 로드 → 미니플레이어 렌더링
        val sp = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sp.getInt("songId", -1)

        lifecycleScope.launch {
            val song = withContext(Dispatchers.IO) {
                if (songId != -1) {
                    FloDatabase.getInstance(this@MainActivity).songDao().getSongById(songId)
                } else null
            }
            currentSong = song ?: currentSong
            currentSong?.let { setMiniPlayer(it) }
        }
    }

    // 프래그먼트 전환 헬퍼
    private fun replaceFragment(Fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, Fragment)
            .commit()
    }

    // 미니플레이어 UI 렌더링
    private fun setMiniPlayer(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer

        // 진행바는 저장된 second/playTime 기반으로 계산 (0 나눔 방지)
        val progress = if (song.playTime == 0) 0 else (song.second * 100 / song.playTime)
        binding.mainMiniplayerProgressSb.progress = progress
    }

    // 미니플레이어 버튼 샘플 동작(재생/일시정지 토글 시 아이콘만 교체)
    private fun togglePlay(toPlay: Boolean) {
        // 여기서는 UI 아이콘만 토글 (실재생 제어는 SongActivity에서 함)
        if (toPlay) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }
        // 필요하면 isPlaying을 SharedPreferences로 저장해서 SongActivity와 동기화 가능
        // getSharedPreferences("song", MODE_PRIVATE).edit().putBoolean("isPlaying", toPlay).apply()
    }
}