package com.example.realflo

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realflo.databinding.FragmentLockerSavedalbumBinding
import data.local.FloDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedAlbumFragment : Fragment() {

    private lateinit var binding: FragmentLockerSavedalbumBinding
    private lateinit var db: FloDatabase
    private lateinit var adapter: AlbumLockerRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerSavedalbumBinding.inflate(inflater, container, false)
        db = FloDatabase.getInstance(requireContext())

        adapter = AlbumLockerRVAdapter()
        binding.lockerSavedSongRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.lockerSavedSongRecyclerView.adapter = adapter

        // (…) 버튼 클릭 콜백
        adapter.setOnMoreClickListener { album ->
            showDeleteDialog(album)
        }

        loadLikedAlbums()

        return binding.root
    }

    private fun loadLikedAlbums() {
        viewLifecycleOwner.lifecycleScope.launch {
            val likedList = withContext(Dispatchers.IO) {
                db.albumDao().getLikedAlbums()
            }
            adapter.submitList(likedList)
        }
    }

    private fun showDeleteDialog(album: Album) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("앨범 삭제")
        builder.setMessage("이 앨범을 저장 목록에서 삭제하시겠습니까?")

        builder.setPositiveButton("삭제") { _, _ ->
            deleteAlbum(album)
        }

        builder.setNegativeButton("취소", null)
        builder.show()
    }

    private fun deleteAlbum(album: Album) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {

            // DB에서 삭제
            db.albumDao().deleteAlbum(album.id)

            // 또는 isLike만 false로 변경하고 싶으면 ↓
            // db.albumDao().updateAlbumLike(album.id, false)

            // 삭제 후 리스트 다시 불러오기
            val updatedList = db.albumDao().getLikedAlbums()

            withContext(Dispatchers.Main) {
                adapter.submitList(updatedList)
            }
        }
    }
}
