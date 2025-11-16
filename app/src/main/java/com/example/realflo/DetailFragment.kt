package com.example.realflo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.realflo.databinding.FragmentDetailBinding
import data.local.FloDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var db: FloDatabase

    // AlbumVPAdapter에서 번들로 전달됨
    private val albumId: Int by lazy { arguments?.getInt("albumId") ?: -1 }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        db = FloDatabase.getInstance(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAlbumDetail()
    }

    private fun loadAlbumDetail() {
        if (albumId == -1) return

        viewLifecycleOwner.lifecycleScope.launch {
            val album = withContext(Dispatchers.IO) {
                db.albumDao().getAlbum(albumId)
            }

            album?.let {
                binding.detailTitleTv.text = it.title
                binding.detailSingerTv.text = it.singer
                // 설명은 별도 필드가 없으니 임시로 구성
                binding.detailDescTv.text = "이 앨범은 ${it.singer}의 \"${it.title}\" 입니다."
                // 만약 fragment_detail.xml에 cover 이미지뷰가 있다면 주석 해제
                // it.coverImg?.let { resId -> binding.detailCoverIv.setImageResource(resId) }
            }
        }
    }
}