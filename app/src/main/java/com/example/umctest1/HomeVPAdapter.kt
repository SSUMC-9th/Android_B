package com.example.umctest1

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> HomeBanner1Fragment()
            1 -> HomeBanner2Fragment()
            2 -> HomeBanner3Fragment()
            else -> HomeBanner3Fragment()
        }
    }

}