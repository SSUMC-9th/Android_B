package com.example.week2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week2.AlbumRVAdapter.MyItemClickListener
import com.example.week2.databinding.ItemAlbumBanner1Binding
import com.example.week2.databinding.ItemAlbumBinding

class AlbumBanner3RVAdapter(private var albumList: ArrayList<AlbumBanner1>): RecyclerView.Adapter<AlbumBanner3RVAdapter.ViewHolder>(){
    interface MyItemClickListener{
        fun onItemClick()
    }

    private lateinit var mItemClickListener: com.example.week2.AlbumRVAdapter.MyItemClickListener
    fun setMyItemClickListener(itemClickListener: com.example.week2.AlbumRVAdapter.MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumBanner3RVAdapter.ViewHolder {
        val binding: ItemAlbumBanner1Binding = ItemAlbumBanner1Binding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumBanner3RVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener{mItemClickListener.onItemClick()}
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemAlbumBanner1Binding): RecyclerView.ViewHolder(binding.root){
        fun bind(album: AlbumBanner1){
            binding.tvTitle.text = album.title
            binding.tvArtist.text = album.singer
        }
    }
}