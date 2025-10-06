package com.example.week2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week2.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()

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

//        binding.imgAlbum.setOnClickListener{
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.main_fraimContainer, AlbumFragment())
//                .addToBackStack(null)
//                .commit()
//        }

        // 데이터 리스트 생성 더미 데이터
        albumDatas.apply {
            add(Album("drama", "에스파", R.drawable.img_album_drama))
            add(Album("supernova", "에스파", R.drawable.img_album_supernova))
            add(Album("라일락", "아이유", R.drawable.img_album_exp2))
            add(Album("앨범 제목", "가수 이름", R.drawable.img_album_exp3))
            add(Album("앨범 제목", "가수 이름", R.drawable.img_album_exp))
            add(Album("앨범 제목", "가수 이름", R.drawable.img_album_exp4))
        }

        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbum.adapter = albumRVAdapter
        binding.homeTodayMusicAlbum.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick() {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_fraimContainer, AlbumFragment())
                    .addToBackStack(null)
                    .commit()
            }
        })

        return binding.root
    }

}