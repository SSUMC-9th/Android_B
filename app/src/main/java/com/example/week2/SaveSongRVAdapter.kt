package com.example.week2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week2.databinding.ItemSaveSongBinding

class SaveSongRVAdapter(private var albumList: ArrayList<SaveSong>): RecyclerView.Adapter<SaveSongRVAdapter.ViewHolder>(){
    interface MyItemClickListener{
        fun onItemClick()
    }

    private lateinit var mItemClickListener: com.example.week2.SaveSongRVAdapter.MyItemClickListener
    fun setMyItemClickListener(itemClickListener: com.example.week2.SaveSongRVAdapter.MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveSongRVAdapter.ViewHolder {
        val binding: ItemSaveSongBinding = ItemSaveSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SaveSongRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener{mItemClickListener.onItemClick()}

        holder.binding.btnMore.setOnClickListener{
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                removeAt(pos)
            }
        }
    }

    fun removeAt(position: Int) {
        if (position !in albumList.indices) return
        albumList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemSaveSongBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album: SaveSong){
            binding.tvTitle.text = album.title
            binding.tvArtist.text = album.singer
        }
    }
}