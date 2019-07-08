package com.example.mindgarden.Adapter

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.mindgarden.Activity.WriteDiaryActivity
import com.example.mindgarden.Fragment.BlankFragment

import com.example.mindgarden.Fragment.DiaryListFragment
import com.example.mindgarden.Fragment.MainFragment
import com.example.mindgarden.R

class MainPagerAdapter(fm: FragmentManager, private val num_fragment: Int): FragmentStatePagerAdapter(fm) {
    override fun getItem(p0: Int): Fragment? {
        return when(p0){
            0 -> MainFragment()
            1 -> BlankFragment()
            2 -> DiaryListFragment()
            else -> null
        }
    }

    override fun getCount(): Int {
        return num_fragment
    }
}