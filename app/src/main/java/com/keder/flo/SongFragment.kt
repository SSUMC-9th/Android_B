package com.keder.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.keder.flo.databinding.FragmentDetailBinding
import com.keder.flo.databinding.FragmentSongBinding

class SongFragment : Fragment() {
    lateinit var binding : FragmentSongBinding
    private var songData = ArrayList<Song>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        binding.songMixonTg.setOnClickListener { setMixStatus(false) }
        binding.songMixoffTg.setOnClickListener { setMixStatus(true) }

        songData.apply{
            add(Song("Butter", "방탄소년단 (BTS)"))
            add(Song("Lilac", "아이유 (IU)"))
        }

        val songRVAdapter = SaveSongRVAdapter( false)
        binding.albumSongRv.adapter = songRVAdapter
        binding.albumSongRv.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)


        return binding.root
    }

    fun setMixStatus(isMixing : Boolean){
        if(isMixing){
            binding.songMixonTg.visibility = View.VISIBLE
            binding.songMixoffTg.visibility = View.GONE
        }else{
            binding.songMixonTg.visibility = View.GONE
            binding.songMixoffTg.visibility = View.VISIBLE
        }
    }
}