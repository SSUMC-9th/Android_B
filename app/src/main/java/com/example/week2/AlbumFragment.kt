package com.example.week2

import LockerVPAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.week2.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment : Fragment() {
    private lateinit var binding: FragmentAlbumBinding
    private val tabTitles = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumVPAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumVPAdapter

        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){
                tab, position -> tab.text = tabTitles[position]
        }.attach()

        binding.albumBackIv.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fraimContainer, HomeFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

}