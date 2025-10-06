package com.keder.flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.keder.flo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), OnSongItemClickListener{
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString())


        binding.mainPlayerCl.setOnClickListener {
            // MiniPlayer에 표시된 현재 노래 정보를 가져와 SongActivity로 전달
            val currentTitle = binding.mainMiniplayerTitleTv.text.toString()
            val currentSinger = binding.mainMiniplayerSingerTv.text.toString()

            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", currentTitle)
            intent.putExtra("singer", currentSinger)
            startActivity(intent)
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        binding.mainBnv.setupWithNavController(navController)
    }

    // 💡 MiniPlayer에 노래 정보를 업데이트하는 함수 구현
    override fun onSongItemClick(title: String, singer: String) {
        binding.mainMiniplayerTitleTv.text = title
        binding.mainMiniplayerSingerTv.text = singer
        // 필요하다면 MiniPlayer의 재생 버튼을 멈춤 버튼으로 변경하는 로직도 여기에 추가
    }
}

interface OnSongItemClickListener {
    fun onSongItemClick(title: String, singer: String)
}