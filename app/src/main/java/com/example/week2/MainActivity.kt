package com.example.week2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.week2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val song = Song(binding.title.text.toString(), binding.singer.text.toString())
        Log.d("Song", song.title + song.singer)

        binding.miniPlayer.setOnClickListener{
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            startActivity(intent)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fraimContainer, HomeFragment())
            .commit()

        binding.mainBnv.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.homeFragment_bt -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fraimContainer, HomeFragment())
                        .commit()
                    true
                }
                R.id.lookFragment_bt -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fraimContainer, LookFragment())
                        .commit()
                    true
                }
                R.id.searchFragment_bt -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fraimContainer, SearchFragment())
                        .commit()
                    true
                }
                R.id.lockerFragment_bt -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fraimContainer, LockerFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}