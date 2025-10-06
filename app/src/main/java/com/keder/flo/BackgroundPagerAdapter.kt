package com.keder.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BackgroundPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){
    private val fragmentList : ArrayList<Fragment> = ArrayList()

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    override fun getItemCount(): Int = fragmentList.size

    fun addFragment(fragment: Fragment){
        fragmentList.add(fragment)
        notifyItemInserted(fragmentList.size-1)
    }
}
