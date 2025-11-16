package com.example.realflo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realflo.databinding.FragmentLockerBinding
import data.local.FloDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LockerFragment : Fragment() {

    private lateinit var binding: FragmentLockerBinding
    private lateinit var db: FloDatabase
    private lateinit var adapter: LockerAdapter   // 좋아요한 곡 리스트 어댑터

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)
        db = FloDatabase.getInstance(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadLikedSongs()
    }

    override fun onResume() {
        super.onResume()
        // SongActivity에서 하트 토글하고 돌아왔을 때 갱신
        loadLikedSongs()
    }

    private fun setupRecyclerView() {
        adapter = LockerAdapter { song ->
            val intent = Intent(requireContext(), SongActivity::class.java)
            intent.putExtra("songId", song.id)
            startActivity(intent)
        }

        binding.lockerRecyclerview.adapter = adapter
        binding.lockerRecyclerview.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadLikedSongs() {
        viewLifecycleOwner.lifecycleScope.launch {
            val likedSongs = withContext(Dispatchers.IO) {
                db.songDao().getLikedSongs()   // 좋아요만 조회
            }

            adapter.submitList(likedSongs)

            binding.lockerEmptyTv.visibility =
                if (likedSongs.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}
