package com.keder.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.keder.flo.databinding.FragmentAlbumBinding
import kotlin.math.sin


class AlbumFragment : Fragment() {
    lateinit var binding : FragmentAlbumBinding
    private var gson: Gson = Gson()
    private val information = arrayListOf("수록곡", "상세정보", "영상")

    private var isLiked : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumJson = arguments?.getString("albumJson")

        val album = gson.fromJson(albumJson, Album::class.java)
        isLiked = isLikedAlbum(album.id)
        setInit(album)
        setOnClickListeners(album)

        binding.albumBackIv.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        val albumAdapter = AlbumVPAdapter(this, album)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){
            tab, position ->
            tab.text = information[position]
        }.attach()
        return binding.root
    }

    private fun setInit(album : Album){
        binding.albumAlbumIv.setImageResource(album.coverImg ?: 0)
        binding.albumMusicTitleTv.text = album.title.toString()
        binding.albumSingerNameTv.text = album.singer.toString()
        if(isLiked){
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun getJwt():Int{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }

    private fun likeAlbum(userId : Int, albumId:Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.albumDao().likeAlbum(like)
    }

    private fun isLikedAlbum(albumId : Int) : Boolean{
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userID = getJwt()

        val likeId : Int? = songDB.albumDao().isLikedAlbum(userID, albumId)

        return likeId != null
    }

    private fun disLikedAlbum(albumId : Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userID = getJwt()

        songDB.albumDao().disLikedAlbum(userID, albumId)
    }

    private fun setOnClickListeners(album : Album){
        val userId = getJwt()
        binding.albumLikeIv.setOnClickListener {
            if(isLiked){
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(album.id)
            }else{
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.id)
            }
        }
    }
}