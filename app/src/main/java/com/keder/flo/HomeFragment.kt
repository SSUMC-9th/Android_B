package com.keder.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.keder.flo.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // 특정 버튼 클릭 시 AlbumFragment로 이동
        binding.mainAlbumIv.setOnClickListener {
            val albumTitle = binding.mainAlbumTitleTv.text.toString()
            val singerName = binding.mainAlbumSingerTv.text.toString()

            val bundle = Bundle().apply{
                putString("albumTitle", albumTitle)
                putString("singerName", singerName)
            }
            findNavController().navigate(R.id.albumFragment, bundle)
        }

        val backgroundAdapter = BackgroundPagerAdapter(this)
        backgroundAdapter.addFragment(BackgroundFragment(R.drawable.img_first_album_default))
        backgroundAdapter.addFragment(BackgroundFragment(R.drawable.img_first_album_default))
        backgroundAdapter.addFragment(BackgroundFragment(R.drawable.img_first_album_default))
        binding.homePannelBackgroundVp.adapter = backgroundAdapter
        binding.homePannelBackgroundVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.homePannelIndicator.setViewPager(binding.homePannelBackgroundVp)

        val bannerAdapter = BackgroundPagerAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        return binding.root
    }
}
