package com.keder.flo

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.gson.Gson
import com.keder.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySongBinding
    lateinit var timer : Timer
    private var mediaPlayer : MediaPlayer? = null
    private var gson : Gson = Gson()

    val songs = arrayListOf<Song>()
    //lateinit var songDB : SongDatabase
    var nowPos = 0
    private val database = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        //initSong()
        initClickListener()

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

    override fun onPause() {
        super.onPause()
        songs[nowPos].second = ((binding.songProgressSb.progress * songs[nowPos].playTime)/100)/1000
        songs[nowPos].isPlaying = false
        setPlayerStatus(false)

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()


        editor.putString("songId", songs[nowPos].id)

        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun initPlayList(){
//        songDB = SongDatabase.getInstance(this)!!
//        songs.addAll(songDB.songDao().getSongs())
        val songsRef = database.child("songs")
        songsRef.get().addOnSuccessListener { dataSnapshot ->
            songs.clear()
            for (snapshot in dataSnapshot.children){
                val song = snapshot.getValue(Song::class.java)
                if(song != null){
                    songs.add(song)
                }
            }
            Log.d("Firebase", "Songs loaded : ${songs.size}")
            initSong()
        }.addOnFailureListener { Log.e("Firebase", "Failed to load songs", it) }
    }

    private fun initClickListener(){
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
            moveSong(-1)
        }
        binding.songNextIv.setOnClickListener {
            moveSong(+1)
        }
        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }

    private fun initSong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        //val songId = spf.getInt("songId", 0)
        val songIdSpf = spf.getString("songId", "")
        val songId : String = songIdSpf ?: ""
        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song Id", songs[nowPos].id)
        startTimer()
        setPlayer(songs[nowPos])


    }

    private fun setLike(isLike : Boolean){
        val songId = songs[nowPos].id
        val newIsLike = !isLike
        songs[nowPos].isLike = newIsLike
//        songDB.songDao().updateIsLikeById(!isLike, songs[nowPos].id)

        database.child("songs").child(songId).child("isLike").setValue(newIsLike).addOnSuccessListener {
            Log.d("Firebase", "Like status update for $songId")
        }.addOnFailureListener { Log.e("Firebase", "FAILED TO UPDATE LIKE STATUS", it) }


        if(newIsLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun moveSong(direct : Int){
        if(nowPos + direct < 0){
            resetSong()
            return
        }
        if(nowPos + direct >= songs.size){
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return
        }
        nowPos += direct

        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer = null

        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId : String) : Int{
        for(i in 0 until songs.size){
            if(songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun setPlayer(song:Song){
        binding.songMusicTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text =String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        binding.songProgressSb.progress = (((song.second * 1000f) / song.playTime)*100f).toInt()
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        if(song.isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
        setPlayerStatus(song.isPlaying)
    }

    fun setPlayerStatus(isPlaying : Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying
        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        }else{
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if (mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }
    }

    private fun resetSong(){
        songs[nowPos].second = 0
        timer.second = 0
        timer.mills = 0f

        binding.songStartTimeTv.text = "00:00"
        binding.songProgressSb.progress = 0

        setPlayerStatus(true)
    }

    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime : Int, var isPlaying : Boolean=true): Thread(){
        var second : Int = songs[nowPos].second
        var mills : Float = songs[nowPos].second * 1000f

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
