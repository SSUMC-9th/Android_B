package com.example.androidfloclone

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment)  {

    private val fragmentlist : ArrayList<Fragment> = ArrayList()


    override fun createFragment(position: Int): Fragment = fragmentlist[position]//0,1,2,3

    override fun getItemCount(): Int = fragmentlist.size

    fun addFragment(fragment: Fragment) {
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size - 1)

    }
}