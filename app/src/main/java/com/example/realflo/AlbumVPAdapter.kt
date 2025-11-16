package com.example.realflo

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumVPAdapter(fragment: Fragment, private val albumId: Int) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> SongFragment()
            1 -> DetailFragment()
            else -> VideoFragment()
        }
        val bundle = Bundle()
        bundle.putInt("albumId", albumId)
        fragment.arguments = bundle
        return fragment
    }
}
