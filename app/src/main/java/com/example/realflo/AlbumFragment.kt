package com.example.realflo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.realflo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import data.local.FloDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumFragment : Fragment() {

    private lateinit var binding: FragmentAlbumBinding
    private lateinit var db: FloDatabase
    private val information = arrayListOf("수록곡", "상세정보", "영상")

    private var currentAlbum: Album? = null
    private var albumId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        db = FloDatabase.getInstance(requireContext())

        // 뒤로가기
        binding.albumBackIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // HomeFragment 에서 넘어온 앨범 ID
        albumId = arguments?.getInt("albumId") ?: -1

        // 1) 앨범 정보 로드
        loadAlbumHeader()

        // 2) ViewPager 설정
        val albumAdapter = AlbumVPAdapter(this, albumId)
        binding.albumContentVp.adapter = albumAdapter

        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }

    private fun loadAlbumHeader() {
        viewLifecycleOwner.lifecycleScope.launch {
            val album = withContext(Dispatchers.IO) {
                db.albumDao().getAlbumById(albumId)
            }

            currentAlbum = album

            album?.let {
                binding.albumMusicTitleTv.text = it.title
                binding.albumSingerNameTv.text = it.singer
                it.coverImg?.let { resId -> binding.albumAlbumIv.setImageResource(resId) }

                setLikeUI(it.isLike)

                // 좋아요 버튼 동작
                binding.albumLikeIv.setOnClickListener {
                    toggleAlbumLike()
                }
            }
        }
    }

    private fun toggleAlbumLike() {
        val album = currentAlbum ?: return
        val newValue = !album.isLike

        // UI 즉시 변경
        setLikeUI(newValue)

        // DB 업데이트
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            db.albumDao().updateAlbumLike(album.id, newValue)
        }

        // 메모리도 갱신
        album.isLike = newValue
    }

    private fun setLikeUI(isLike: Boolean) {
        if (isLike) {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }
}
