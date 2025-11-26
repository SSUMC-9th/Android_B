package com.example.umctest1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.umctest1.databinding.FragmentHomeBinding
import com.example.umctest1.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    private var _binding: FragmentLockerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLockerBinding.inflate(inflater, container, false)

        _binding!!.lockerLoginTv.setOnClickListener{
            startActivity(Intent(activity, LoginActivity::class.java))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLockerBinding.bind(view)

        // 1. ViewPager2에 Adapter 연결
        val pagerAdapter = LockerVPAdapter(this)
        binding.lockerContentVp.adapter = pagerAdapter

        // 2. TabLayout과 ViewPager2 동기화 (TabLayoutMediator)
        val tabTitles = arrayOf("저장한 곡", "음악파일", "저장앨범")

        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}