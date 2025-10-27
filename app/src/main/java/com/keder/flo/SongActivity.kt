package com.keder.flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.keder.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySongBinding
    lateinit var song : Song
    lateinit var timer : Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)

        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songRepeatIv.setOnClickListener { setRepeatStatus(true) }
        binding.songSelectedRepeatIv.setOnClickListener { setRepeatStatus(false) }
        binding.songRandomIv.setOnClickListener { setRandomStatus(true) }
        binding.songSelectedRandomIv.setOnClickListener { setRandomStatus(false) }

        binding.songPreviousIv.setOnClickListener {
            resetSong()
        }
        binding.songNextIv.setOnClickListener {
            resetSong()
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

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
    }

    private fun initSong(){
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("coverImg", 0),
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false),
            )
        }
        startTimer()
    }

    private fun setPlayer(song:Song){
        binding.songMusicTitleTv.text = intent.getStringExtra("title")!!
        binding.songSingerNameTv.text = intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text =String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (((song.second * 1000f) / song.playTime)*100f).toInt()

        setPlayerStatus(song.isPlaying)
    }

    fun setPlayerStatus(isPlaying : Boolean){
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying
        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }else{
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }
    }

    private fun resetSong(){
        song.second = 0
        timer.second = 0
        timer.mills = 0f

        binding.songStartTimeTv.text = "00:00"
        binding.songProgressSb.progress = 0

        setPlayerStatus(true)
    }

    private fun startTimer(){
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime : Int, var isPlaying : Boolean=true): Thread(){
        var second : Int = song.second
        var mills : Float = song.second * 1000f

        override fun run() {
            super.run()
            try {
                while(true){
                    if(second >= playTime){
                        break
                    }
                    if(isPlaying){
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills/playTime)*100).toInt()
                        }
                        if(mills % 1000 == 0f){
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            }catch (e : InterruptedException){
                Log.d("Song", "쓰레드가 죽었습니다. ${e.message}")
            }

        }
    }
}
