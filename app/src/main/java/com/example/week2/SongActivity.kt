package com.example.week2

import android.adservices.adid.AdId
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.week2.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    lateinit var binding : ActivitySongBinding
    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private val seekbarUpdater = object : Runnable {
        override fun run() {
            mediaPlayer?.let { mp ->
                val pos = mp.currentPosition
                binding.songSeekbar.progress = pos
                binding.songCurrentTimeTv.text = formatTime(pos)

                // 현재 위치 저장
                val sp = getSharedPreferences(SP_NAME, MODE_PRIVATE).edit()
                sp.putInt(SP_KEY_POS, pos)
                sp.apply()

                handler.postDelayed(this, 500)
            }
        }

    }
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()

        preparePlayer(songs[nowPos])
        setPlayer(songs[nowPos])

        if (intent.hasExtra("title") && intent.hasExtra("singer")){
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }

        binding.songDownIb.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.songMiniplayerIv.setOnClickListener{
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener{
            setPlayerStatus(false)
        }

        binding.songPreviousIv.setOnClickListener { moveSong(-1) }
        binding.songNextIv.setOnClickListener { moveSong(1) }

        binding.songSeekbar.max = 16000

        val sp = getSharedPreferences(SP_NAME, MODE_PRIVATE)
        val lastPos = sp.getInt(SP_KEY_POS, 0)
        val lastPlay = sp.getBoolean(SP_KEY_PLAY, false)

        mediaPlayer?.seekTo(lastPos)
        binding.songSeekbar.progress = lastPos

        setPlayerStatus(lastPlay)
    }

    override fun onPause() {
        super.onPause()
        val sp = getSharedPreferences(SP_NAME, MODE_PRIVATE).edit()
        sp.putInt(SP_KEY_POS, mediaPlayer?.currentPosition ?: 0)
        sp.putBoolean(SP_KEY_PLAY, isPlaying)
        sp.apply()
    }

    private fun initSong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)
        Log.d("now song id", songs[nowPos].id.toString())
        //startTimer()
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size){
            if(songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)
        songs.addAll(songDB.songDao().getSongs())
    }

    fun setPlayerStatus(play: Boolean){
        isPlaying = play

        if (play) {
            mediaPlayer?.start()
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            handler.post(seekbarUpdater)
        } else {
            mediaPlayer?.pause()
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            handler.removeCallbacks(seekbarUpdater)
        }
    }

    private fun setPlayer(song: Song) {
        binding.songMusicTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer

        mediaPlayer?.let { mp ->
            binding.songTotalTimeTv.text = formatTime(16000)
            binding.songCurrentTimeTv.text = formatTime(mp.currentPosition)
        }

    }

    private fun moveSong(direction: Int) {
        if (songs.isEmpty()) return

        if (songs.size == 1) {
            mediaPlayer?.seekTo(0)
            setPlayerStatus(true)
            return
        }

        nowPos += direction
        if (nowPos < 0) nowPos = songs.lastIndex
        if (nowPos > songs.lastIndex) nowPos = 0

        preparePlayer(songs[nowPos])
        setPlayer(songs[nowPos])
        setPlayerStatus(true)
    }

    private fun preparePlayer(song: Song) {
        mediaPlayer?.release()
        mediaPlayer = null

        val resId = when (song.title.lowercase()) {
            "drama" -> R.raw.face_talk_wait_0
            "ex1" -> R.raw.face_talk_wait_0
            else -> R.raw.face_talk_wait_0
        }

        mediaPlayer = MediaPlayer.create(this, resId).apply {
            setOnCompletionListener {
                moveSong(1)
            }
        }
    }

    private fun formatTime(ms: Int): String {
        val totalSec = ms / 1000
        val min = totalSec / 60
        val sec = totalSec % 60
        return String.format("%d:%02d", min, sec)
    }

}