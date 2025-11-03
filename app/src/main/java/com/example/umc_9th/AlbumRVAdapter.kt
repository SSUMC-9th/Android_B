package com.example.umc_9th

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_9th.databinding.ItemAlbumBinding

class AlbumRVAdapter (private val albumList : ArrayList<Album>): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    interface MyItemClickerListener {
        fun onItemClick(album: Album)
        fun onPlayClick(album: Album)
    }

    private lateinit var myItemClickerListener: MyItemClickerListener
    fun setMyItemClickListener(itemClickListener: MyItemClickerListener) {
        myItemClickerListener = itemClickListener
    }

    fun addItem(album: Album) {
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    interface OnItemCLickListener {
        fun onItemClick(album: Album)
        fun onPlayAlbum(album: Album)
    }


    interface CommunicationInterface {
        fun sendData(album: Album)
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): AlbumRVAdapter.ViewHolder {
        val binding: ItemAlbumBinding =
            ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])


    }

    inner class ViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }
        init {
            // 1. 재생 버튼 클릭 시 -> onPlayClick 호출 (미니플레이어 변경)
            binding.itemAlbumPlayImgIv.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) { // 안전장치
                    myItemClickerListener.onPlayClick(albumList[position])
                }
            }

            // 2. 앨범 커버 이미지 클릭 시 -> onItemClick 호출 (수록곡 화면으로 전환)
            binding.itemAlbumCoverImgIv.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) { // 안전장치
                    myItemClickerListener.onItemClick(albumList[position])
                }
            }
        }
    }

    override fun getItemCount(): Int = albumList.size
}