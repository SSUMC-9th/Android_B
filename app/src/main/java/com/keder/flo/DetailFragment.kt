package com.keder.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.keder.flo.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    lateinit var binding : FragmentDetailBinding
    private var gson: Gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val albumJson = arguments?.getString("albumJson")

        if (albumJson != null) {
            val album = gson.fromJson(albumJson, Album::class.java)
            val title = album.title
            val singer = album.singer

            binding.detailTitleTv.text = "이 앨범의 이름은 $title 입니다."
            binding.detailSingerTv.text = "이 앨범의 가수는 $singer 입니다."
        } else {
            binding.detailTitleTv.text = "앨범 정보를 불러올 수 없습니다."
            binding.detailSingerTv.text = "가수 정보를 불러올 수 없습니다."
        }

        return binding.root
    }
}