package com.example.week2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week2.databinding.FragmentSaveSongBinding

class SaveSongFragment : Fragment() {

    private lateinit var binding: FragmentSaveSongBinding
    private lateinit var adapter: SaveSongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveSongBinding.inflate(inflater, container, false)

        val albumList = arrayListOf(
            SaveSong(no = 1, title = "Drama", singer = "aespa"),
            SaveSong(no = 2, title = "Supernova", singer = "aespa"),
            SaveSong(no = 3, title = "라일락", singer = "아이유"),
            SaveSong(no = 4, title = "제목4", singer = "가수4"),
            SaveSong(no = 5, title = "제목5", singer = "가수5"),
            SaveSong(no = 6, title = "제목6", singer = "가수6"),
            SaveSong(no = 7, title = "제목7", singer = "가수7")
        )

        adapter = SaveSongRVAdapter(albumList)

        binding.songListRv.adapter = adapter
        binding.songListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return binding.root
    }
}
