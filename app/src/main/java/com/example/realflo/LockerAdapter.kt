package com.example.realflo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.realflo.databinding.ItemLockerBinding


class LockerAdapter(private var LockerList: MutableList<LockerData>,
                    private val onVisitClicked: (LockerData) -> Unit)
    : RecyclerView.Adapter<LockerAdapter.FriendViewHolder>() {

    inner class FriendViewHolder(val binding: ItemLockerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: LockerData) {

            binding.friendTvName.text = friend.name
            binding.friendTvStatus.text = friend.email
            binding.friendVisitBtn.setOnClickListener {
                onVisitClicked(friend)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding = ItemLockerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return FriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LockerAdapter.FriendViewHolder, position: Int) {
        val nowFriend = LockerList[position]
        holder.bind(nowFriend)
    }

    override fun getItemCount(): Int {
        return LockerList.size
    }

}