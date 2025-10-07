package com.keder.flo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keder.flo.databinding.ItemSongBinding

class SaveSongRVAdapter(private val songList : ArrayList<Song>, private val isAbumView : Boolean = false) : RecyclerView.Adapter<SaveSongRVAdapter.ViewHolder>() {
    interface MyItemClickListener{
        fun onRemoveSong(songId : Int)
    }
    private lateinit var mItemClickListener : MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): SaveSongRVAdapter.ViewHolder {
        val binding : ItemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SaveSongRVAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(songList[position], position + 1, isAbumView)
        holder.binding.itemSongMoreIv.setOnClickListener {
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

    inner class ViewHolder(val binding : ItemSongBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(song : Song, position : Int, isAlbumView : Boolean){
            if(isAlbumView){
                binding.itemSongOrderTv.visibility = View.GONE
                binding.itemSongImgIv.visibility = View.VISIBLE
                binding.itemSongImgIv.setImageResource(song.coverImg!!)
            }else{
                binding.itemSongOrderTv.visibility = View.VISIBLE
                binding.itemSongImgIv.visibility = View.GONE
                binding.itemSongOrderTv.text = position.toString()
            }
            binding.itemSongTitleTv.text = song.title
            binding.itemSongSingerTv.text = song.singer
        }
    }
}