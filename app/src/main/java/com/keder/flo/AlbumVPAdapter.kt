package com.keder.flo

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.gson.Gson

class AlbumVPAdapter(fragment: Fragment, private val album: Album) : FragmentStateAdapter(fragment) {
    private val gson = Gson()
    private val albumJson = gson.toJson(album)
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val args = Bundle().apply {
            putString("albumJson", albumJson)
        }
        return when(position){
            0-> SongFragment()
            1-> DetailFragment().apply { arguments = args }
            else->VideoFragment()
        }
    }
}