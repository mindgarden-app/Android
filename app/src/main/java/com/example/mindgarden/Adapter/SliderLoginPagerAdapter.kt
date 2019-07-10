package com.example.mindgarden.Adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.mindgarden.Fragment.SliderLoginFragment

class SliderLoginPagerAdapter (fm: FragmentManager?,val num_fragment:Int): FragmentPagerAdapter(fm) {
    override fun getItem(p0: Int): Fragment? {
        var fragment: SliderLoginFragment = SliderLoginFragment()
        var bundle: Bundle = Bundle(1)

       when(p0){
            //음 여기 어떻게 넣어야 될지 잘 모르겠음
           0-> bundle.putString("background_url", "https://1.bp.blogspot.com/-SyJkzqzp8hQ/XSWWV0a6oHI/AAAAAAAAAO4/WkHZjaJz0W4mTECiXc1qV45_6WZ6CEQ9wCK4BGAYYCw/s1600/img_log_in_1.png")
           1-> bundle.putString("background_url", "https://2.bp.blogspot.com/-f24o2ana2-g/XSWWV5LjuDI/AAAAAAAAAO8/ipK1m9GfWFgJYY4YUq7ZFk6eDU3GtLhtACK4BGAYYCw/s1600/img_log_in_2.png")
           2-> bundle.putString("background_url", "https://3.bp.blogspot.com/-0HMIu1_bHPo/XSWWVxoExkI/AAAAAAAAAPE/ePCerwoW5nUblTsjYfzl0k_foefOAr_fACK4BGAYYCw/s1600/Img_log_in_3.png")
           3-> bundle.putString("background_url", "https://3.bp.blogspot.com/-JMJe5rVQdo8/XSWWV2fo3fI/AAAAAAAAAPA/H2OAjgrQdvMi55uKIeHSSJ2gAv9UVrgOACK4BGAYYCw/s1600/img_log_in_4.png")
       }
        fragment.arguments=bundle
        return fragment
    }

    override fun getCount(): Int {
       return num_fragment
    }
}