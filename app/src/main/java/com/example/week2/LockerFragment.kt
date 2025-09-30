package com.example.week2

import LockerVPAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.week2.databinding.FragmentHomeBinding
import com.example.week2.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {
    private lateinit var binding: FragmentLockerBinding
    private val tabTitles = arrayListOf("저장한 곡", "음악파일", "저장앨범")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerVPAdapter = LockerVPAdapter(this)
        binding.lockerVp.adapter = lockerVPAdapter

        TabLayoutMediator(binding.lockerContentTb, binding.lockerVp){
                tab, position -> tab.text = tabTitles[position]
        }.attach()

        return binding.root
    }

}