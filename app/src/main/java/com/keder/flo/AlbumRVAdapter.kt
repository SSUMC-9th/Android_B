package com.keder.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keder.flo.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumList : ArrayList<Album>) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun onItemClick(album : Album)
    }
    interface OnPlayButtonClickListener {
        fun onPlayButtonClick(title: String, singer: String)
    }

    private lateinit var mItemClickListener : MyItemClickListener
    private lateinit var mPlayButtonClickListener : OnPlayButtonClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    fun setOnPlayButtonClickListener(playButtonClickListener: OnPlayButtonClickListener){
        mPlayButtonClickListener = playButtonClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): AlbumRVAdapter.ViewHolder {
        val binding : ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        val currentAlbum = albumList[position]
        holder.bind(currentAlbum)
        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(albumList[position])
        }
        holder.binding.itemAlbumPlayImgIv.setOnClickListener {
            val firstSongTitle = currentAlbum.title
            val firstSongSinger = currentAlbum.singer
            mPlayButtonClickListener.onPlayButtonClick(firstSongTitle!!, firstSongSinger!!)

        }

    }

    override fun getItemCount(): Int  = albumList.size

    inner class ViewHolder(val binding : ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(album : Album){
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }
    }

}