package com.example.realflo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.realflo.databinding.ItemLockerAlbumBinding

class AlbumLockerRVAdapter :
    ListAdapter<Album, AlbumLockerRVAdapter.ViewHolder>(AlbumDiffCallback()) {

    private var onMoreClickListener: ((Album) -> Unit)? = null

    fun setOnMoreClickListener(listener: (Album) -> Unit) {
        onMoreClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLockerAlbumBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemLockerAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumImgIv.setImageResource(album.coverImg)

            // (…) 버튼 클릭 시
            binding.itemAlbumMoreIv.setOnClickListener {
                onMoreClickListener?.invoke(album)
            }
        }
    }
}

class AlbumDiffCallback : DiffUtil.ItemCallback<Album>() {
    override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem == newItem
    }
}
