package com.example.realflo

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.realflo.databinding.ActivitySongBinding
import data.local.FloDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding
    private lateinit var db: FloDatabase

    private lateinit var song: Song
    private var songLoaded = false

    private lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FloDatabase.getInstance(this)

        val songId = intent.getIntExtra("songId", -1)
        Log.d("SongActivity", "onCreate() songId = $songId")

        // ❗ songId가 0 이하이면 잘못 진입한 것 → 바로 종료
        if (songId <= 0) {
            Log.e("SongActivity", "잘못된 songId로 SongActivity 진입: $songId")
            finish()
            return
        }

        // DB에서 곡 로드
        lifecycleScope.launch {
            val loaded: Song? = withContext(Dispatchers.IO) {
                db.songDao().getSongById(songId)
            }

            if (loaded == null) {
                Log.e("SongActivity", "DB에서 songId=$songId 를 찾지 못했습니다.")
                finish()
                return@launch
            }

            song = loaded
            songLoaded = true

            Log.d(
                "SongActivity",
                "loaded song id=${song.id}, title=${song.title}, isLike=${song.isLike}"
            )

            setPlayer(song)
            startTimer()
            attachSeekBarListener()
        }

        // 좋아요 토글
        binding.songLikeIv.setOnClickListener {
            if (!songLoaded) {
                Log.w("SongActivity", "좋아요 클릭했지만 song 이 아직 로드 안 됨")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val newLike = !song.isLike

                withContext(Dispatchers.IO) {
                    db.songDao().updateLike(song.id, newLike)
                }

                val refreshed = withContext(Dispatchers.IO) {
                    db.songDao().getSongById(song.id)
                }

                song = refreshed ?: song.copy(isLike = newLike)
                renderLikeIcon(song.isLike)
                Log.d("SongActivity", "좋아요 변경: songId=${song.id}, isLike=${song.isLike}")
            }
        }

        // 상단 닫기
        binding.songDownIb.setOnClickListener { finish() }

        // 재생/일시정지
        binding.songMiniplayerIv.setOnClickListener { setPlayerStatus(false) }
        binding.songPauseIv.setOnClickListener { setPlayerStatus(true) }

        // 이전/다음 곡
        binding.songPreviousIv.setOnClickListener {
            if (!songLoaded) return@setOnClickListener
            lifecycleScope.launch { getAdjacent(-1)?.let { playNewSong(it) } }
        }
        binding.songNextIv.setOnClickListener {
            if (!songLoaded) return@setOnClickListener
            lifecycleScope.launch { getAdjacent(+1)?.let { playNewSong(it) } }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!songLoaded) return

        lifecycleScope.launch {
            val updated = withContext(Dispatchers.IO) {
                db.songDao().getSongById(song.id)
            }
            if (updated != null) song = updated
            renderLikeIcon(song.isLike)
            Log.d("SongActivity", "onResume sync isLike=${song.isLike}")
        }
    }

    override fun onPause() {
        super.onPause()

        if (songLoaded) {
            setPlayerStatus(false)

            song.second = (binding.songProgressSb.progress * song.playTime) / 100

            getSharedPreferences("song", MODE_PRIVATE).edit()
                .putInt("songId", song.id)
                .apply()

            lifecycleScope.launch(Dispatchers.IO) { db.songDao().update(song) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::timer.isInitialized) timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    /** 재생/일시정지 UI & MediaPlayer 반영 */
    private fun setPlayerStatus(isPlaying: Boolean) {
        if (!songLoaded) return

        song.isPlaying = isPlaying
        if (this::timer.isInitialized) timer.isPlaying = isPlaying

        if (isPlaying) {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            mediaPlayer?.start()
        } else {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            if (mediaPlayer?.isPlaying == true) mediaPlayer?.pause()
        }
    }

    private fun startTimer() {
        if (!songLoaded) return
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }

    /** 같은 앨범에서 이전/다음 곡 */
    private suspend fun getAdjacent(delta: Int): Song? = withContext(Dispatchers.IO) {
        val list = db.songDao().getSongsInAlbum(song.albumIdx)
        if (list.isEmpty()) return@withContext null

        val idx = list.indexOfFirst { it.id == song.id }.coerceAtLeast(0)
        val nextIndex = (idx + delta + list.size) % list.size
        list[nextIndex]
    }

    private fun attachAutoNextListener() {
        mediaPlayer?.setOnCompletionListener {
            lifecycleScope.launch {
                getAdjacent(+1)?.let { playNewSong(it) }
            }
        }
    }

    /** SeekBar 드래그 이동 */
    private fun attachSeekBarListener() {
        binding.songProgressSb.setOnSeekBarChangeListener(object :
            android.widget.SeekBar.OnSeekBarChangeListener {

            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) = Unit

            override fun onProgressChanged(
                seekBar: android.widget.SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (!fromUser || !songLoaded) return
                val newSecond = (progress * song.playTime) / 100
                song.second = newSecond

                if (this@SongActivity::timer.isInitialized) {
                    timer.setPosition(newSecond)
                }

                mediaPlayer?.let {
                    try {
                        val newMs = newSecond * 1000
                        val duration = it.duration.coerceAtLeast(1)
                        val clamped = newMs.coerceIn(0, duration - 1)
                        it.seekTo(clamped)
                    } catch (e: Exception) {
                        Log.e("SongActivity", "seekTo 실패: ${e.message}")
                    }
                }

                binding.songStartTimeTv.text = String.format(
                    Locale.getDefault(), "%02d:%02d", newSecond / 60, newSecond % 60
                )
            }
        })
    }

    /** 새로운 곡 재생 */
    private fun playNewSong(newSong: Song) {
        if (this::timer.isInitialized) timer.interrupt()

        mediaPlayer?.release()
        mediaPlayer = null

        song = newSong.copy(second = 0, isPlaying = true)
        songLoaded = true

        setPlayer(song)
        startTimer()

        getSharedPreferences("song", MODE_PRIVATE).edit()
            .putInt("songId", song.id)
            .apply()

        lifecycleScope.launch(Dispatchers.IO) { db.songDao().update(song) }
    }

    /** 타이머 스레드 */
    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {

        @Volatile private var second: Int = song.second
        @Volatile private var mills: Float = song.second * 1000f

        fun setPosition(newSecond: Int) {
            second = newSecond
            mills = newSecond * 1000f
        }

        override fun run() {
            try {
                while (true) {
                    if (second >= playTime) break
                    if (isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress =
                                ((mills / (playTime * 1000)) * 100).toInt()
                        }

                        if (mills % 1000 == 0f) {
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format(
                                    Locale.getDefault(), "%02d:%02d", second / 60, second % 60
                                )
                            }
                            second++
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("Song", "타이머 종료: ${e.message}")
            }
        }
    }

    private fun renderLikeIcon(isLike: Boolean) {
        if (isLike) binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        else binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
    }

    private fun setPlayer(song: Song) {
        binding.songMusicTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer

        binding.songStartTimeTv.text =
            String.format(Locale.getDefault(), "%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text =
            String.format(Locale.getDefault(), "%02d:%02d", song.playTime / 60, song.playTime % 60)

        renderLikeIcon(song.isLike)

        val progress = if (song.playTime == 0) 0 else (song.second * 100 / song.playTime)
        binding.songProgressSb.progress = progress

        mediaPlayer?.release()
        val musicResId = resources.getIdentifier(song.music, "raw", packageName)
        if (musicResId == 0) {
            Log.e("SongActivity", "음악 리소스 없음: ${song.music}")
            return
        }

        mediaPlayer = MediaPlayer.create(this, musicResId)

        if (song.second > 0) {
            try {
                mediaPlayer?.seekTo((song.second * 1000).coerceAtLeast(0))
            } catch (e: Exception) {
                Log.e("SongActivity", "초기 seek 실패: ${e.message}")
            }
        }

        attachAutoNextListener()
        setPlayerStatus(song.isPlaying)
    }
}
