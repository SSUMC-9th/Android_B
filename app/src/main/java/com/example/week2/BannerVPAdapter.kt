package com.example.week2

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> HomeBanner1Fragment()
            1 -> HomeBanner2Fragment()
            2 -> HomeBanner3Fragment()
            else -> HomeBanner1Fragment()
        }
    }
}