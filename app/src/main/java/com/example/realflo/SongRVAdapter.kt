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

    /** 하트 클릭 콜백: (현재 Song, position) */
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

        // 곡 전체 클릭
        holder.itemView.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onItemClick?.invoke(items[pos])
            }
        }

        // 하트 클릭 → Fragment 에게 Song + position 전달
        holder.likeIv.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onLikeClick?.invoke(items[pos], pos)
            }
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
            likeIv.setImageResource(
                if (song.isLike) R.drawable.ic_my_like_on
                else R.drawable.ic_my_like_off
            )
        }
    }
}
