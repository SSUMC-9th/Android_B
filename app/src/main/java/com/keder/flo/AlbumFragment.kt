package com.keder.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.keder.flo.databinding.FragmentAlbumBinding
import kotlin.math.sin


class AlbumFragment : Fragment() {
    lateinit var binding : FragmentAlbumBinding
    private val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumTitle = arguments?.getString("albumTitle") ?: "앨범 제목 없음"
        val singerName = arguments?.getString("singerName") ?: "가수 없음"

        binding.albumMusicTitleTv.text = albumTitle
        binding.albumSingerNameTv.text = singerName

        val requestKey = "songData"
        val dataBundle = Bundle().apply{
            putString("albumTitle", albumTitle)
            putString("singerName", singerName)
        }

        childFragmentManager.setFragmentResult(requestKey, dataBundle)

        binding.albumBackIv.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        val albumAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){
            tab, position ->
            tab.text = information[position]
        }.attach()
        return binding.root
    }
}