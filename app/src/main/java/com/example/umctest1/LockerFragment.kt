package com.example.umctest1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.umctest1.databinding.FragmentHomeBinding
import com.example.umctest1.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.kakao.sdk.user.UserApiClient


class LockerFragment : Fragment() {

    private var _binding: FragmentLockerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLockerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. 로그인 텍스트 클릭 시 이동
        binding.lockerLoginTv.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        // 2. ViewPager2 및 TabLayout 설정
        val pagerAdapter = LockerVPAdapter(this)
        binding.lockerContentVp.adapter = pagerAdapter

        val tabTitles = arrayOf("저장한 곡", "음악파일", "저장앨범")
        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        // 3. 로그아웃 버튼 클릭 리스너
        binding.lockerLogoutTv.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e("KakaoLogin", "로그아웃 실패", error)
                    // 프래그먼트에서는 requireContext() 사용
                    Toast.makeText(requireContext(), "로그아웃 실패", Toast.LENGTH_SHORT).show()
                } else {
                    Log.i("KakaoLogin", "로그아웃 성공")
                    Toast.makeText(requireContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()

                    // 로그인 화면으로 이동 및 스택 클리어
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }
}