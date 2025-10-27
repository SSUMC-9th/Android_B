package com.keder.flo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.keder.flo.databinding.ItemLockerAlbumBinding

class SaveAlbumRVAdapter(private val songList : ArrayList<Song>, private val isAlbumView : Boolean = false) : RecyclerView.Adapter<SaveAlbumRVAdapter.ViewHolder>() {
    interface MyItemClickListener{
        fun onRemoveSong(songId : Int)
    }
    private lateinit var mItemClickListener : MyItemClickListener

    fun setMyItemClickListener(itemClickListener : MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): SaveAlbumRVAdapter.ViewHolder {
        val binding : ItemLockerAlbumBinding = ItemLockerAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SaveAlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(songList[position], position + 1)
        holder.binding.itemAlbumMoreIv.setOnClickListener {
            mItemClickListener.onRemoveSong(songList[position].id)
            removeSong(position)
        }
    }

    override fun getItemCount(): Int = songList.size

    @SuppressLint("NotifyDataSetChanged")
    private fun removeSong(position: Int){
        songList.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding : ItemLockerAlbumBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(song : Song, position : Int){
            binding.itemAlbumImgIv.setImageResource(song.coverImg!!)
            binding.itemAlbumTitleTv.text = song.title
            binding.itemAlbumSingerTv.text = song.singer
        }
    }
}