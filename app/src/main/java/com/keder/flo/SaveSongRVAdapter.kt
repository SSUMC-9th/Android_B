package com.keder.flo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keder.flo.databinding.ItemSongBinding

class SaveSongRVAdapter(private val isAbumView : Boolean = false) : RecyclerView.Adapter<SaveSongRVAdapter.ViewHolder>() {
    private val songs = ArrayList<Song>()
    interface MyItemClickListener{
        fun onRemoveSong(songId : String)
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
        holder.bind(songs[position],position+1, isAbumView)
        holder.binding.itemSongMoreIv.setOnClickListener {
            mItemClickListener.onRemoveSong(songs[position].id)
            removeSong(position)
        }
    }

    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs : ArrayList<Song>){
        this.songs.clear()
        this.songs.addAll(songs)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeSong(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding : ItemSongBinding) : RecyclerView.ViewHolder(binding.root){
        // ⬇️ 4. (수정) bind 함수가 position을 받도록 수정 (기존 코드 유지)
        fun bind(song : Song, position : Int, isAlbumView : Boolean){
            if(isAlbumView){
                binding.itemSongOrderTv.visibility = View.GONE
                binding.itemSongImgIv.visibility = View.VISIBLE

                // ⬇️ 5. (수정) NPE 방지 (!! -> ?.let)
                song.coverImg?.let {
                    binding.itemSongImgIv.setImageResource(it)
                }
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