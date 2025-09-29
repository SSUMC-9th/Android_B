package com.keder.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.keder.flo.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    lateinit var binding : FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val requestKey = "songData"
        parentFragmentManager.setFragmentResultListener(requestKey, viewLifecycleOwner){
            key, bundle ->
            val title = bundle.getString("albumTitle")
            val singer = bundle.getString("singerName")
            binding.detailTitleTv.text = "이 앨범의 이름은 $title 입니다."
            binding.detailSingerTv.text = "이 앨범의 가수는 $singer 입니다."
        }

        return binding.root
    }
}