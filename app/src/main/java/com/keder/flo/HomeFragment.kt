package com.keder.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.keder.flo.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // 특정 버튼 클릭 시 AlbumFragment로 이동
        binding.mainAlbumIv.setOnClickListener {
            val albumTitle = binding.mainAlbumTitleTv.text.toString()
            val singerName = binding.mainAlbumSinglerTv.text.toString()
            val albumImageRes = R.drawable.img_album_exp2

            val bundle = Bundle().apply{
                putString("albumTitle", albumTitle)
                putString("singerName", singerName)
                putInt("albumImageRes", albumImageRes)
            }
            findNavController().navigate(R.id.albumFragment, bundle)
        }

        return binding.root
    }
}
