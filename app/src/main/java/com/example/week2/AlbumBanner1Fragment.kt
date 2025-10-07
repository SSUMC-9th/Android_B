package com.example.week2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week2.databinding.FragmentAlbumBanner1Binding

class AlbumBanner1Fragment : Fragment() {

    private lateinit var binding: FragmentAlbumBanner1Binding
    private lateinit var adapter: AlbumBanner1RVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBanner1Binding.inflate(inflater, container, false)

        val albumList = arrayListOf(
            AlbumBanner1(title = "Drama", singer = "aespa"),
            AlbumBanner1(title = "Supernova", singer = "aespa"),
            AlbumBanner1(title = "라일락", singer = "아이유"),
            AlbumBanner1(title = "제목4", singer = "가수4"),
            AlbumBanner1(title = "제목5", singer = "가수5"),
            AlbumBanner1(title = "제목6", singer = "가수6"),
            AlbumBanner1(title = "제목7", singer = "가수7")
        )

        adapter = AlbumBanner1RVAdapter(albumList)

        binding.songListRv.adapter = adapter
        binding.songListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return binding.root
    }
}
