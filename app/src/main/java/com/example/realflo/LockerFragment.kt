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
        setupSavedAlbumButton()
        loadLikedSongs()
    }

    override fun onResume() {
        super.onResume()
        loadLikedSongs()
    }

    private fun setupSavedAlbumButton() {
        // 저장앨범 버튼 클릭 시 → 저장앨범 목록 Fragment 로 이동
        binding.lockerSavedAlbumBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, SavedAlbumFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupRecyclerView() {
        adapter = LockerAdapter(
            onSongClicked = { song ->
                // 곡 클릭하면 SongActivity 이동
                val intent = Intent(requireContext(), SongActivity::class.java)
                intent.putExtra("songId", song.id)
                startActivity(intent)
            },

            onLikeClicked = { song ->
                // 좋아요 해제
                viewLifecycleOwner.lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        db.songDao().updateLike(song.id, false)
                    }
                    loadLikedSongs()
                }
            }
        )

        binding.lockerRecyclerview.adapter = adapter
        binding.lockerRecyclerview.layoutManager =
            LinearLayoutManager(requireContext())
    }

    private fun loadLikedSongs() {
        viewLifecycleOwner.lifecycleScope.launch {
            val likedSongs = withContext(Dispatchers.IO) {
                db.songDao().getLikedSongs()
            }

            adapter.submitList(likedSongs)

            binding.lockerEmptyTv.visibility =
                if (likedSongs.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}
