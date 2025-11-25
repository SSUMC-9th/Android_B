package com.example.realflo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.realflo.databinding.ItemSongBinding
import java.util.Locale

class LockerAdapter(
    private val onSongClicked: (Song) -> Unit,
    private val onLikeClicked: (Song) -> Unit      // 보관함에서 하트 클릭 콜백
) : ListAdapter<Song, LockerAdapter.SongViewHolder>(SongDiffCallback()) {

    inner class SongViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song, order: Int) {
            // 순번, 제목, 가수
            binding.songOrderTv.text = String.format(Locale.US, "%02d", order)
            binding.songTitleTv.text = song.title
            binding.songSingerTv.text = song.singer

            // 좋아요 아이콘
            binding.likeIv.setImageResource(
                if (song.isLike) R.drawable.ic_my_like_on
                else R.drawable.ic_my_like_off
            )

            // 곡 클릭
            binding.root.setOnClickListener { onSongClicked(song) }
            binding.songPlayIv.setOnClickListener { onSongClicked(song) }

            // 하트 클릭 → Fragment 로 이벤트만 전달
            binding.likeIv.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onLikeClicked(song)
                }
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
