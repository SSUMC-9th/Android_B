package com.example.week2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week2.databinding.FragmentSaveSong3Binding

class SaveSongFragment3 : Fragment() {

    private lateinit var binding: FragmentSaveSong3Binding
    private lateinit var adapter: SaveSongRV3Adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveSong3Binding.inflate(inflater, container, false)

        val albumList = arrayListOf(
            SaveSong3(coverImg = R.drawable.img_album_drama, title = "Drama", singer = "aespa"),
            SaveSong3(coverImg = R.drawable.img_album_supernova, title = "Supernova", singer = "aespa"),
            SaveSong3(coverImg = R.drawable.img_album_lovewinsall, title = "라일락", singer = "아이유"),
            SaveSong3(coverImg = R.drawable.img_album_exp2, title = "제목4", singer = "가수4"),
            SaveSong3(coverImg = R.drawable.img_album_exp, title = "제목5", singer = "가수5"),
            SaveSong3(coverImg = R.drawable.img_album_exp5, title = "제목6", singer = "가수6"),
            SaveSong3(coverImg = R.drawable.img_album_exp6, title = "제목7", singer = "가수7")
        )

        adapter = SaveSongRV3Adapter(albumList)

        binding.songListRv.adapter = adapter
        binding.songListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return binding.root
    }
}
