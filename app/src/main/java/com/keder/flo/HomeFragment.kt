package com.keder.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.keder.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()

    private lateinit var songDB : SongDatabase

    private val backgroundHandler = Handler(Looper.getMainLooper())
    private lateinit var backgroundSlideRunnable: Runnable
    private val BACKGROUND_SLIDE_DELAY: Long = 3000 // 3초 간격

    private val bannerHandler = Handler(Looper.getMainLooper())
    private lateinit var bannerSlideRunnable: Runnable
    private val BANNER_SLIDE_DELAY: Long = 5000 // 5초 간격

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!
        albumDatas.addAll(songDB.albumDao().getAlbums())

        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener {
            override fun onItemClick(album: Album) {
                val gson = Gson()
                val albumJson = gson.toJson(album)
                val bundle = Bundle().apply {
                    putString("albumJson", albumJson)
                }
                findNavController().navigate(R.id.albumFragment, bundle)
            }
        })

        val backgroundAdapter = BackgroundPagerAdapter(this)
        backgroundAdapter.addFragment(BackgroundFragment(R.drawable.img_first_album_default))
        backgroundAdapter.addFragment(BackgroundFragment(R.drawable.img_first_album_default))
        backgroundAdapter.addFragment(BackgroundFragment(R.drawable.img_first_album_default))
        binding.homePannelBackgroundVp.adapter = backgroundAdapter
        binding.homePannelBackgroundVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.homePannelIndicator.setViewPager(binding.homePannelBackgroundVp)

        backgroundSlideRunnable = Runnable {
            val viewPager = binding.homePannelBackgroundVp
            val adapter = viewPager.adapter ?: return@Runnable

            val nextPosition = viewPager.currentItem + 1
            if (nextPosition >= adapter.itemCount) {
                viewPager.setCurrentItem(0, false)
            } else {
                viewPager.setCurrentItem(nextPosition, true)
            }

            // 중복 방지: 기존 콜백 제거 후 재등록
            backgroundHandler.removeCallbacks(backgroundSlideRunnable)
            backgroundHandler.postDelayed(backgroundSlideRunnable, BACKGROUND_SLIDE_DELAY)
        }

        binding.homePannelBackgroundVp.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                when (state) {
                    ViewPager2.SCROLL_STATE_DRAGGING -> {
                        backgroundHandler.removeCallbacks(backgroundSlideRunnable)
                    }

                    ViewPager2.SCROLL_STATE_IDLE -> {
                        backgroundHandler.removeCallbacks(backgroundSlideRunnable)
                        backgroundHandler.postDelayed(backgroundSlideRunnable, BACKGROUND_SLIDE_DELAY)
                    }
                }
            }
        })

        val bannerAdapter = BackgroundPagerAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        bannerSlideRunnable = Runnable {
            val viewPager = binding.homeBannerVp
            val adapter = viewPager.adapter ?: return@Runnable

            val nextPosition = viewPager.currentItem + 1
            if (nextPosition >= adapter.itemCount) {
                viewPager.setCurrentItem(0, false)
            } else {
                viewPager.setCurrentItem(nextPosition, true)
            }

            bannerHandler.removeCallbacks(bannerSlideRunnable)
            bannerHandler.postDelayed(bannerSlideRunnable, BANNER_SLIDE_DELAY)
        }

        binding.homeBannerVp.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                when (state) {
                    ViewPager2.SCROLL_STATE_DRAGGING -> {
                        bannerHandler.removeCallbacks(bannerSlideRunnable)
                    }

                    ViewPager2.SCROLL_STATE_IDLE -> {
                        bannerHandler.removeCallbacks(bannerSlideRunnable)
                        bannerHandler.postDelayed(bannerSlideRunnable, BANNER_SLIDE_DELAY)
                    }
                }
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        backgroundHandler.removeCallbacks(backgroundSlideRunnable)
        bannerHandler.removeCallbacks(bannerSlideRunnable)
        backgroundHandler.postDelayed(backgroundSlideRunnable, BACKGROUND_SLIDE_DELAY)
        bannerHandler.postDelayed(bannerSlideRunnable, BANNER_SLIDE_DELAY)
    }

    override fun onPause() {
        super.onPause()
        backgroundHandler.removeCallbacks(backgroundSlideRunnable)
        bannerHandler.removeCallbacks(bannerSlideRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backgroundHandler.removeCallbacks(backgroundSlideRunnable)
        bannerHandler.removeCallbacks(bannerSlideRunnable)
    }
}
