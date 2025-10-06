package com.keder.flo

import MusicFileFragment
import SaveAlbumFragment
import SaveSongFragment
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> SaveSongFragment()
            1-> MusicFileFragment()
            else->SaveAlbumFragment()
        }
    }
}