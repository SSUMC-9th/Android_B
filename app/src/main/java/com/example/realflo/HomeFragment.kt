package com.example.realflo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.realflo.databinding.FragmentHomeBinding
import data.local.FloDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var db: FloDatabase

    private val albumDatas = ArrayList<Album>()
    private lateinit var albumRVAdapter: AlbumRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // DB 인스턴스
        db = FloDatabase.getInstance(requireContext())

        // 어댑터/리사이클러 설정
        albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.apply {
            adapter = albumRVAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        // 앨범 클릭 → albumId 번들로 전달
        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener {
            override fun onItemClick(album: Album) {
                val fragment = AlbumFragment().apply {
                    arguments = Bundle().apply { putInt("albumId", album.id) }
                }
                (requireActivity() as MainActivity).supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_frm, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        })

        // 배너(그대로 유지)
        val bannerAdapter = BannerVPAdapter(this).apply {
            addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
            addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        }
        binding.homeBannerVp.apply {
            adapter = bannerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }

        // 🔥 더미 add(...) 제거하고 DB에서 로드
        loadAlbumsFromDb()

        return binding.root
    }

    private fun loadAlbumsFromDb() {
        viewLifecycleOwner.lifecycleScope.launch {
            val albums = withContext(Dispatchers.IO) { db.albumDao().getAlbums() }
            albumDatas.clear()
            albumDatas.addAll(albums)
            albumRVAdapter.notifyDataSetChanged()
        }
    }
}