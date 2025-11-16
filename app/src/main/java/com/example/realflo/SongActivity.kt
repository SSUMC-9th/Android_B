package com.example.realflo

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.realflo.databinding.ActivitySongBinding
import com.google.gson.Gson
import data.local.FloDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding
    private lateinit var db: FloDatabase

    private lateinit var song: Song
    private lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FloDatabase.getInstance(this)

        // songId만 받아서 DB에서 곡 로드
        val songId = intent.getIntExtra("songId", -1)

        lifecycleScope.launch {
            song = withContext(Dispatchers.IO) {
                db.songDao().getSongById(songId)
            } ?: Song(title = "라일락", singer = "아이유(IU)", playTime = 214, music = "music_lilac")

            setPlayer(song)
            startTimer()
            attachSeekBarListener()   // ✅ SeekBar 스크러빙 연결
        }

        // 좋아요 토글
        binding.songLikeIv.setOnClickListener {
            song.isLike = !song.isLike
            lifecycleScope.launch(Dispatchers.IO) { db.songDao().updateLike(song.id, song.isLike) }
            renderLikeIcon(song.isLike)
        }

        // 상단 닫기
        binding.songDownIb.setOnClickListener { finish() }

        // 재생/일시정지
        binding.songMiniplayerIv.setOnClickListener { setPlayerStatus(false) }
        binding.songPauseIv.setOnClickListener { setPlayerStatus(true) }

        // 이전/다음 곡 버튼 (xml id 다르면 변경)
        binding.songPreviousIv.setOnClickListener {
            lifecycleScope.launch { getAdjacent(delta = -1)?.let { playNewSong(it) } }
        }
        binding.songNextIv.setOnClickListener {
            lifecycleScope.launch { getAdjacent(delta = +1)?.let { playNewSong(it) } }
        }
    }

    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)

        // 진행도 저장
        song.second = (binding.songProgressSb.progress * song.playTime) / 100

        // 현재 곡 동기화(songId만 저장)
        getSharedPreferences("song", MODE_PRIVATE).edit()
            .putInt("songId", song.id)
            .apply()

        // 진행 상태 DB 반영(선택)
        lifecycleScope.launch(Dispatchers.IO) { db.songDao().update(song) }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::timer.isInitialized) timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    /** 재생/일시정지 UI & MediaPlayer 반영 */
    private fun setPlayerStatus(isPlaying: Boolean) {
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
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }

    /** 같은 앨범에서 delta(-1:이전, +1:다음) 곡을 구한다. 없으면 null */
    private suspend fun getAdjacent(delta: Int): Song? = withContext(Dispatchers.IO) {
        val list = db.songDao().getSongsInAlbum(song.albumIdx)
        if (list.isEmpty()) return@withContext null
        val idx = list.indexOfFirst { it.id == song.id }.coerceAtLeast(0)
        val nextIndex = (idx + delta + list.size) % list.size
        list[nextIndex]
    }

    /** 곡이 끝나면 자동으로 다음 곡으로 넘어가도록 타이머/플레이어 세팅 */
    private fun attachAutoNextListener() {
        mediaPlayer?.setOnCompletionListener {
            lifecycleScope.launch {
                getAdjacent(delta = +1)?.let { playNewSong(it) }
            }
        }
    }

    /** ✅ SeekBar 드래그로 재생 위치 이동 */
    private fun attachSeekBarListener() {
        binding.songProgressSb.setOnSeekBarChangeListener(object :
            android.widget.SeekBar.OnSeekBarChangeListener {

            private var userSeeking = false

            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {
                userSeeking = true
            }

            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {
                userSeeking = false
            }

            override fun onProgressChanged(
                seekBar: android.widget.SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (!fromUser) return
                val newSecond = (progress * song.playTime) / 100
                song.second = newSecond

                // 타이머 내부 위치도 동기화
                if (this@SongActivity::timer.isInitialized) {
                    timer.setPosition(newSecond)
                }

                // 미디어플레이어 시킹 (ms)
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

                // 현재 위치 텍스트 즉시 반영
                binding.songStartTimeTv.text = String.format(
                    Locale.getDefault(), "%02d:%02d", newSecond / 60, newSecond % 60
                )
            }
        })
    }

    /** 새로운 곡으로 전환(타이머/플레이어/UI/저장 동기화) */
    private fun playNewSong(newSong: Song) {
        // 타이머 교체
        if (this::timer.isInitialized) timer.interrupt()

        // MediaPlayer 교체
        mediaPlayer?.release()
        mediaPlayer = null

        song = newSong.copy(second = 0, isPlaying = true) // 다음/이전 곡은 처음부터 재생
        setPlayer(song)          // UI + MediaPlayer 재설정
        startTimer()             // 새 타이머 시작

        // 현재 곡 저장 (미니플레이어 동기화)
        getSharedPreferences("song", MODE_PRIVATE).edit()
            .putInt("songId", song.id)
            .apply()

        // (선택) 상태 DB 반영
        lifecycleScope.launch(Dispatchers.IO) { db.songDao().update(song) }
    }

    /** 타이머 스레드 */
    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {
        // 외부에서 위치 변경 가능하도록 메서드 제공
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

    /** 좋아요 아이콘 렌더링 */
    private fun renderLikeIcon(isLike: Boolean) {
        if (isLike) binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        else binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
    }

    /** 곡 정보/플레이어 UI 렌더링 + MediaPlayer 생성 */
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

        // MediaPlayer 재생 준비
        mediaPlayer?.release()
        val musicResId = resources.getIdentifier(song.music, "raw", packageName)
        if (musicResId == 0) {
            Log.e("SongActivity", "음악 리소스를 찾을 수 없습니다: ${song.music}")
            return
        }
        mediaPlayer = MediaPlayer.create(this, musicResId)
        // 시작 위치 반영(예: 재방문 시 이어듣기)
        if (song.second > 0) {
            try {
                mediaPlayer?.seekTo((song.second * 1000).coerceAtLeast(0))
            } catch (e: Exception) {
                Log.e("SongActivity", "초기 seek 실패: ${e.message}")
            }
        }
        attachAutoNextListener()           // 곡 종료 시 다음 곡 자동 재생
        setPlayerStatus(song.isPlaying)    // 재생/일시정지 상태 반영
    }
}
