package com.example.realflo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongRVAdapter(
    private val items: MutableList<Song>
) : RecyclerView.Adapter<SongRVAdapter.VH>() {

    /** 아이템 전체 클릭 (곡 상세/재생 이동 등에 사용) */
    private var onItemClick: ((Song) -> Unit)? = null
    fun setOnItemClickListener(listener: (Song) -> Unit) {
        onItemClick = listener
    }

    /** 하트만 클릭 (원하면 다음 턴에 DB 토글 연결) */
    private var onLikeClick: ((Song, Int) -> Unit)? = null
    fun setOnLikeClickListener(listener: (Song, Int) -> Unit) {
        onLikeClick = listener
    }

    fun replaceAll(newItems: List<Song>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun updateItem(position: Int, newSong: Song) {
        if (position in items.indices) {
            items[position] = newSong
            notifyItemChanged(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val song = items[position]
        holder.bind(song)

        // 아이템 전체 클릭
        holder.itemView.setOnClickListener { onItemClick?.invoke(song) }

        // 하트만 클릭 (다음 턴에 DB 토글 연결 예정)
        holder.likeIv.setOnClickListener {
            onLikeClick?.invoke(song, position)
        }
    }

    override fun getItemCount(): Int = items.size

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTv: TextView = view.findViewById(R.id.songTitleTv)
        private val singerTv: TextView = view.findViewById(R.id.songSingerTv)
        val likeIv: ImageView = view.findViewById(R.id.likeIv)

        fun bind(song: Song) {
            titleTv.text = song.title
            singerTv.text = song.singer

            // isLike 상태에 맞춰 하트 표시
            if (song.isLike) {
                likeIv.setImageResource(R.drawable.ic_my_like_on)
            } else {
                likeIv.setImageResource(R.drawable.ic_my_like_off)
            }
        }
    }
}