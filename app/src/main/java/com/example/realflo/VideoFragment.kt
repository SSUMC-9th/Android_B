package com.example.realflo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.realflo.databinding.FragmentDetailBinding
import com.example.realflo.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {

    private lateinit var binding: FragmentVideoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoBinding.inflate(inflater, container, false)

        // AlbumVPAdapter에서 albumId 받기
        val albumId = arguments?.getInt("albumId") ?: -1

        // 디버그용
        requireActivity().title = "영상 탭 (albumId=$albumId)"

        return binding.root
    }
}