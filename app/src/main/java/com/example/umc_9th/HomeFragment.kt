package com.example.umc_9th

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.umc_9th.databinding.FragmentHomeBinding
import me.relex.circleindicator.CircleIndicator3

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bannerAdapter: BannerVPAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var bannerPosition = 0
    private val slideDelay = 3000L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupTodayAlbumRv()
        setupBannerViewPager()

        return binding.root
    }

    /** 오늘 발매 음악 RecyclerView 세팅 */
    private fun setupTodayAlbumRv() {
        val albumDatas = arrayListOf(
            Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp),
            Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2),
            Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3),
            Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4),
            Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5),
            Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6)
        )

        val albumAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.homeTodayMusicAlbumRv.adapter = albumAdapter
    }

    /** 배너 ViewPager + CircleIndicator3 설정 */
    private fun setupBannerViewPager() {
        bannerAdapter = BannerVPAdapter(this).apply {
            addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
            addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
            addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        }

        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // ✅ CircleIndicator3 연결
        binding.homeBannerIndicator.setViewPager(binding.homeBannerVp)
        bannerAdapter.registerAdapterDataObserver(binding.homeBannerIndicator.adapterDataObserver)

        // ✅ 자동 슬라이드
        val runnable = object : Runnable {
            override fun run() {
                bannerPosition = (bannerPosition + 1) % bannerAdapter.itemCount
                binding.homeBannerVp.currentItem = bannerPosition
                handler.postDelayed(this, slideDelay)
            }
        }

        handler.postDelayed(runnable, slideDelay)

        // ✅ ViewPager 페이지 변화 감지
        binding.homeBannerVp.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bannerPosition = position
            }
        })
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed({
            bannerPosition = binding.homeBannerVp.currentItem
            handler.postDelayed({
                bannerPosition = (bannerPosition + 1) % bannerAdapter.itemCount
                binding.homeBannerVp.currentItem = bannerPosition
            }, slideDelay)
        }, slideDelay)
    }
}
