package com.example.realflo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.realflo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import data.local.FloDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumFragment : Fragment() {

    private lateinit var binding: FragmentAlbumBinding
    private lateinit var db: FloDatabase
    private val information = arrayListOf("수록곡","상세정보","영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        db = FloDatabase.getInstance(requireContext())

        // 뒤로가기
        binding.albumBackIv.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // 1) HomeFragment에서 넘긴 albumId 받기
        val albumId = arguments?.getInt("albumId") ?: -1

        // 2) 앨범 헤더 채우기 (제목/가수/커버)
        viewLifecycleOwner.lifecycleScope.launch {
            val album = withContext(Dispatchers.IO) { db.albumDao().getAlbum(albumId) }
            album?.let {
                binding.albumMusicTitleTv.text = it.title
                binding.albumSingerNameTv.text = it.singer
                it.coverImg?.let { resId -> binding.albumAlbumIv.setImageResource(resId) }
            }
        }

        // 3) ViewPager에 albumId 넘겨주기
        val albumAdapter = AlbumVPAdapter(this, albumId)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }
}
