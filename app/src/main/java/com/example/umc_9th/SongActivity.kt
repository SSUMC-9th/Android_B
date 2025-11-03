package com.example.umc_9th

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_9th.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding
    private lateinit var song: Song
    private var timer: Timer? = null
    private var mediaPlayer: MediaPlayer? = null
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()          // ✅ song 객체를 먼저 만들고
        setPlayer(song)     // ✅ UI/플레이어 세팅
        startTimer()        // ✅ 타이머 반드시 시작

        initClickListeners()
        initSeekBarListener()
    }

    /** 인텐트 → song 객체 생성 (없으면 기본값) */
    private fun initSong() {
        song = Song(
            title = intent.getStringExtra("title") ?: "Lilac",
            singer = intent.getStringExtra("singer") ?: "아이유(IU)",
            second = intent.getIntExtra("second", 0),
            playTime = intent.getIntExtra("playTime", 180).coerceAtLeast(1),
            isPlaying = intent.getBooleanExtra("isPlaying", false),
            music = intent.getStringExtra("music") ?: "music_lilac"
        )
    }

    /** 현재 song 기준으로 UI와 MediaPlayer 세팅 */
    private fun setPlayer(s: Song) {
        binding.songMusicTitleTv.text = s.title
        binding.songSingerNameTv.text = s.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d", s.second / 60, s.second % 60)
        binding.songEndTimeTv.text   = String.format("%02d:%02d", s.playTime / 60, s.playTime % 60)

        // SeekBar 0~100 퍼센트로 사용
        binding.songProgressSb.max = 100
        binding.songProgressSb.progress =
            ((s.second.toFloat() / s.playTime.toFloat()) * 100).toInt().coerceIn(0, 100)

        val resId = resources.getIdentifier(s.music, "raw", packageName)
        if (resId == 0) {
            Log.e("SongActivity", "음원 리소스가 없습니다: ${s.music} (res/raw/${s.music}.mp3 필요)")
            return
        }
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, resId).apply {
            seekTo(s.second * 1000)
            if (s.isPlaying) start()
        }

        setPlayerStatus(s.isPlaying, touchTimer = false) // UI 버튼 상태만 맞춰줌
    }

    /** 버튼 리스너 */
    private fun initClickListeners() {
        binding.songDownIb.setOnClickListener { finish() }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        // 이전/다음은 일단 처음부터 재시작
        binding.songPreviousIv.setOnClickListener { restartSong() }
        binding.songNextIv.setOnClickListener     { restartSong() }
    }

    /** 드래그로 위치 이동 */
    private fun initSeekBarListener() {
        binding.songProgressSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                timer?.isPlaying = false
                mediaPlayer?.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val percent = (seekBar?.progress ?: 0).coerceIn(0, 100)
                val targetSec = (percent / 100f) * song.playTime
                song.second = targetSec.toInt()
                binding.songStartTimeTv.text =
                    String.format("%02d:%02d", song.second / 60, song.second % 60)

                mediaPlayer?.seekTo((targetSec * 1000).toInt())
                if (song.isPlaying) mediaPlayer?.start()
                timer?.apply {
                    setBase(song.second)
                    isPlaying = song.isPlaying
                }
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val sec = ((progress / 100f) * song.playTime).toInt()
                    binding.songStartTimeTv.text = String.format("%02d:%02d", sec / 60, sec % 60)
                }
            }
        })
    }

    /** 재생/일시정지 + UI 동기화 */
    private fun setPlayerStatus(isPlaying: Boolean, touchTimer: Boolean = true) {
        song.isPlaying = isPlaying

        if (isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            mediaPlayer?.pause()
        }

        if (touchTimer) {
            timer?.isPlaying = isPlaying
            if (timer == null) startTimer()
        }
    }

    /** 곡 처음부터 */
    private fun restartSong() {
        song.second = 0
        binding.songProgressSb.progress = 0
        binding.songStartTimeTv.text = "00:00"
        mediaPlayer?.seekTo(0)
        setPlayerStatus(true)
        startTimer()
    }

    /** 진행/시간 갱신용 타이머 */
    private fun startTimer() {
        timer?.interrupt()
        timer = Timer(song.playTime, song.isPlaying, song.second).also { it.start() }
    }

    inner class Timer(
        private val playTimeSec: Int,
        @Volatile var isPlaying: Boolean,
        startSecond: Int
    ) : Thread() {

        private var second = startSecond
        private var mills = (startSecond * 1000).toFloat()

        fun setBase(sec: Int) {
            second = sec
            mills = (sec * 1000).toFloat()
        }

        override fun run() {
            try {
                while (second < playTimeSec) {
                    if (!isPlaying) {
                        sleep(50); continue
                    }

                    sleep(50)
                    mills += 50f

                    // 진행률(%) = 경과밀리초 / 전체밀리초 * 100
                    val progress =
                        ((mills / (playTimeSec * 1000f)) * 100f).toInt().coerceIn(0, 100)

                    runOnUiThread {
                        binding.songProgressSb.progress = progress
                        binding.songStartTimeTv.text =
                            String.format("%02d:%02d", second / 60, second % 60)
                    }

                    if (mills >= ((second + 1) * 1000f)) {
                        second++
                        runOnUiThread {
                            binding.songStartTimeTv.text =
                                String.format("%02d:%02d", second / 60, second % 60)
                        }

                        // ✅ 초가 갱신될 때마다 상태 저장
                        song.second = second
                        saveSongState()
                    }

                }
            } catch (_: InterruptedException) { /* no-op */ }
        }
    }

    override fun onPause() {
        super.onPause()
        // 현재 위치 저장
        saveSongState()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun saveSongState() {
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val songJson = gson.toJson(song)
        editor.putString("songData", songJson)
        editor.apply()
    }

}
