package com.example.realflo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.realflo.databinding.ItemSongBinding

class LockerAdapter(
    private val onSongClicked: (Song) -> Unit
) : ListAdapter<Song, LockerAdapter.SongViewHolder>(SongDiffCallback()) {

    inner class SongViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song, order: Int) {
            // 순번, 제목, 가수
            binding.songOrderTv.text = String.format("%02d", order)
            binding.songTitleTv.text = song.title
            binding.songSingerTv.text = song.singer

            // 좋아요 아이콘
            binding.likeIv.setImageResource(
                if (song.isLike) R.drawable.ic_my_like_on
                else R.drawable.ic_my_like_off
            )

            // 클릭 동작
            binding.root.setOnClickListener { onSongClicked(song) }
            binding.songPlayIv.setOnClickListener { onSongClicked(song) }

            // 하트 토글(화면상만 변경; DB 반영은 이후 단계에서 처리)
            binding.likeIv.setOnClickListener {
                // Note: This only changes the UI state, not the database.
                // The database update should be handled in the Fragment/ViewModel.
                val currentSong = getItem(bindingAdapterPosition)
                currentSong.isLike = !currentSong.isLike
                notifyItemChanged(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ItemSongBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(getItem(position), position + 1)
    }

    private class SongDiffCallback : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem == newItem
        }
    }
}
