package com.example.mindgarden.ui.main

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

import com.example.mindgarden.ui.diarylist.DiaryListFragment

class MainPagerAdapter(fm: FragmentManager, private val num_fragment: Int): FragmentStatePagerAdapter(fm) {
    override fun getItem(p0: Int): Fragment {
        return when(p0){
            0 -> MainFragment()
            1 -> DiaryListFragment()
            else -> MainFragment()
        }
    }

    override fun getCount(): Int {
        return num_fragment
    }


}