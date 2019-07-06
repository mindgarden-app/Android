package com.example.mindgarden_2.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.mindgarden_2.Fragment.DiaryListFragment

class MainPagerAdapter(fm: FragmentManager, private val num_fragment: Int): FragmentStatePagerAdapter(fm) {
    override fun getItem(p0: Int): Fragment? {
        return when(p0){
            0 -> DiaryListFragment()
            1 -> DiaryListFragment()
            2 -> DiaryListFragment()
            else -> null
        }
    }

    override fun getCount(): Int {
        return num_fragment
    }
}