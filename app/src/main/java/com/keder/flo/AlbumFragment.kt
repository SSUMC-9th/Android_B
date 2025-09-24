package com.keder.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.keder.flo.databinding.FragmentAlbumBinding


class AlbumFragment : Fragment() {
    lateinit var binding : FragmentAlbumBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumTitle = arguments?.getString("albumTitle") ?: "앨범 제목 없음"
        val singerName = arguments?.getString("singerName") ?: "가수 없음"
        val albumImageRes = arguments?.getInt("albumImageRes") ?: R.drawable.img_album_exp2

        binding.albumMusicTitleTv.text = albumTitle
        binding.albumSingerNameTv.text = singerName
        binding.albumAlbumIv.setImageResource(albumImageRes)

        binding.albumBackIv.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        return binding.root
    }
}