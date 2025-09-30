package com.example.week2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.week2.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val bannerAdapter = BannerVPAdapter(this)
        binding.homeBannerVp2.adapter = bannerAdapter

        TabLayoutMediator(binding.homeBannerTabLayout, binding.homeBannerVp2){
            tab, position ->
        }.attach()

        binding.imgAlbum.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fraimContainer, AlbumFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

}