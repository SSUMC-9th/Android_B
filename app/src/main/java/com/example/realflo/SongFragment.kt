package com.example.realflo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realflo.databinding.FragmentDetailBinding
import com.example.realflo.databinding.FragmentSongBinding
import data.local.FloDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongFragment : Fragment() {

    private lateinit var binding: FragmentSongBinding
    private lateinit var db: FloDatabase

    private val songs = ArrayList<Song>()
    private lateinit var adapter: SongRVAdapter

    // AlbumVPAdapter에서 번들로 전달됨
    private val albumId: Int by lazy { arguments?.getInt("albumId") ?: -1 }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongBinding.inflate(inflater, container, false)
        db = FloDatabase.getInstance(requireContext())

        setupRecyclerView()
        loadSongs() // 최초 로드

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // SongActivity 다녀온 뒤 좋아요/진행도 반영을 위해 재조회
        loadSongs()
    }

    private fun setupRecyclerView() {
        adapter = SongRVAdapter(songs)

        // 곡 클릭 → SongActivity
        adapter.setOnItemClickListener { song ->
            val intent = Intent(requireContext(), SongActivity::class.java)
            intent.putExtra("songId", song.id) // 인텐트 키 통일!
            startActivity(intent)
        }

        // 하트 클릭 → DB 토글 + UI 업데이트
        adapter.setOnLikeClickListener { song, position ->
            lifecycleScope.launch {
                val newLike = !song.isLike
                withContext(Dispatchers.IO) {
                    db.songDao().updateLike(song.id, newLike)
                }
                // 로컬 리스트 갱신
                songs[position] = song.copy(isLike = newLike)
                adapter.notifyItemChanged(position)
            }
        }

        binding.songRv.layoutManager = LinearLayoutManager(requireContext())
        binding.songRv.adapter = adapter
    }

    private fun loadSongs() {
        viewLifecycleOwner.lifecycleScope.launch {
            val list = withContext(Dispatchers.IO) {
                if (albumId == -1) db.songDao().getSongs()
                else db.songDao().getSongsInAlbum(albumId)
            }
            songs.clear()
            songs.addAll(list)
            adapter.notifyDataSetChanged()
        }
    }
}





