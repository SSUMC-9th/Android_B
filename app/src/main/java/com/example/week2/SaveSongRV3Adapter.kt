package com.example.week2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week2.databinding.ItemSaveSong3Binding

class SaveSongRV3Adapter(private var albumList: ArrayList<SaveSong3>): RecyclerView.Adapter<SaveSongRV3Adapter.ViewHolder>(){
    interface MyItemClickListener{
        fun onItemClick()
    }

    private lateinit var mItemClickListener: com.example.week2.SaveSongRV3Adapter.MyItemClickListener
    fun setMyItemClickListener(itemClickListener: com.example.week2.SaveSongRV3Adapter.MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveSongRV3Adapter.ViewHolder {
        val binding: ItemSaveSong3Binding = ItemSaveSong3Binding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SaveSongRV3Adapter.ViewHolder, position: Int) {
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

    inner class ViewHolder(val binding: ItemSaveSong3Binding): RecyclerView.ViewHolder(binding.root){
        fun bind(album: SaveSong3){
            album.coverImg?.let { binding.ivAlbum.setImageResource(it) }
            binding.tvTitle.text = album.title
            binding.tvArtist.text = album.singer
        }
    }
}