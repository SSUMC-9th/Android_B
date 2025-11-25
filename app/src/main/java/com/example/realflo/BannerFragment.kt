package com.example.realflo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.realflo.databinding.FragmentBannerBinding

class BannerFragment : Fragment() {

    private lateinit var binding: FragmentBannerBinding

    // onCreate에서 arguments 받기
    private var imgRes: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imgRes = arguments?.getInt(ARG_IMG_RES) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBannerBinding.inflate(inflater, container, false)
        binding.bannerImageIv.setImageResource(imgRes)
        return binding.root
    }

    companion object {

        private const val ARG_IMG_RES = "imgRes"

        fun newInstance(imgRes: Int): BannerFragment {
            val fragment = BannerFragment()
            val args = Bundle()
            args.putInt(ARG_IMG_RES, imgRes)
            fragment.arguments = args
            return fragment
        }
    }
}
