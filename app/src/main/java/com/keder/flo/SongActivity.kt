package com.keder.flo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.keder.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(true)
        }
        if (intent.hasExtra("title") && intent.hasExtra("singer")){
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }
        binding.songRepeatIv.setOnClickListener { setRepeatStatus(true) }
        binding.songSelectedRepeatIv.setOnClickListener { setRepeatStatus(false) }
        binding.songRandomIv.setOnClickListener { setRandomStatus(true) }
        binding.songSelectedRandomIv.setOnClickListener { setRandomStatus(false) }
    }

    fun setPlayerStatus(isPlaying : Boolean){
        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }else{
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }
    }

    fun setRandomStatus(isRandom : Boolean){
        if(isRandom){
            binding.songRandomIv.visibility = View.GONE
            binding.songSelectedRandomIv.visibility = View.VISIBLE
        }else{
            binding.songRandomIv.visibility = View.VISIBLE
            binding.songSelectedRandomIv.visibility = View.GONE
        }
    }

    fun setRepeatStatus(isRepeat : Boolean){
        if(isRepeat){
            binding.songRepeatIv.visibility = View.GONE
            binding.songSelectedRepeatIv.visibility = View.VISIBLE
        }else{
            binding.songRepeatIv.visibility = View.VISIBLE
            binding.songSelectedRepeatIv.visibility = View.GONE
        }
    }
}
