package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication6.databinding.FragmentAlbumBinding
import com.example.myapplication6.R // R 클래스 import 수정

class AlbumFragment : Fragment() {
    lateinit var binding: FragmentAlbumBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment()) // R.id.main_frm은 com.example.myapplication6.R에서 가져올 것입니다.
                .commitAllowingStateLoss()
        }
        val albumAdapter = AlbumVPAdapter(this)
        // albumContentVp를 albumContentViewPager로 수정 (정확한 ID로 변경 필요)
        binding.albumContentVp.adapter = albumAdapter 

        return binding.root
    }
}