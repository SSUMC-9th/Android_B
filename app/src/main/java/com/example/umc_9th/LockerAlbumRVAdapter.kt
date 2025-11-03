package com.example.umc_9th

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_9th.databinding.ItemLockerAlbumBinding

class LockerAlbumRVAdapter (private val albumList: ArrayList<Album>) : RecyclerView.Adapter<LockerAlbumRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LockerAlbumRVAdapter.ViewHolder {
        val binding: ItemLockerAlbumBinding = ItemLockerAlbumBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LockerAlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(albumList[position])
        }

        holder.binding.itemLockerAlbumMoreIv.setOnClickListener {
            albumList[position].isPlaying = !albumList[position].isPlaying
            notifyItemChanged(position)
        }
        holder.binding.itemLockerAlbumMoreIv.setOnClickListener {
            // isPlaying 관련 코드를 제거하고, 원래의 삭제 이벤트를 호출하도록 합니다.
            itemClickListener.onRemoveAlbum(position)
        }
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemLockerAlbumBinding): RecyclerView.ViewHolder(binding.root){
        init {
            // [핵심 1] 재생 버튼을 눌렀을 때의 동작
            binding.itemLockerAlbumPlayImgIv.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) { // 안전장치
                    val album = albumList[position]
                    album.isPlaying = !album.isPlaying // isPlaying 상태를 반전 (true <=> false)
                    notifyItemChanged(position)      // 현재 아이템의 UI만 새로고침 -> bind() 함수 다시 호출
                }
            }

            // [핵심 2] 아이템 전체를 눌렀을 때의 동작
            // 재생 버튼을 누르면 이 코드는 실행되지 않습니다.
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(albumList[position]) // 화면 전환
                }
            }

            // 더보기(...) 버튼을 눌렀을 때의 동작
            binding.itemLockerAlbumMoreIv.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onRemoveAlbum(position)
                }
            }
        }
        fun bind(album: Album){
            binding.itemLockerAlbumTitleTv.text = album.title
            binding.itemLockerAlbumSingerTv.text = album.singer
            binding.itemLockerAlbumCoverImgIv.setImageResource(album.coverImg!!)

            if (album.isPlaying) {
                // isPlaying이 true이면 '일시정지' 이미지로 변경
                binding.itemLockerAlbumPlayImgIv.setImageResource(R.drawable.btn_miniplay_pause)
            } else {
                // isPlaying이 false이면 '재생' 이미지로 변경
                binding.itemLockerAlbumPlayImgIv.setImageResource(R.drawable.btn_miniplayer_play)
            }
        }
    }
    interface OnItemClickListener {
        fun onItemClick(album : Album)
        fun onRemoveAlbum(position: Int)
        fun onPlayClick(album: Album)
    }

    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    fun addItem(album: Album){
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }
}