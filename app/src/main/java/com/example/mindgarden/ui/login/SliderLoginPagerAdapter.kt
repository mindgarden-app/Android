package com.example.mindgarden.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SliderLoginPagerAdapter (fm: FragmentManager, val num_fragment:Int): FragmentPagerAdapter(fm) {
    override fun getItem(p0: Int): Fragment {
        var fragment: SliderLoginFragment =
            SliderLoginFragment()
        var bundle: Bundle = Bundle(1)

        when(p0){
            /*0-> bundle.putString("background_url", "https://1.bp.blogspot.com/-SyJkzqzp8hQ/XSWWV0a6oHI/AAAAAAAAAO4/WkHZjaJz0W4mTECiXc1qV45_6WZ6CEQ9wCK4BGAYYCw/s1600/img_log_in_1.png")
            1-> bundle.putString("background_url", "https://2.bp.blogspot.com/-f24o2ana2-g/XSWWV5LjuDI/AAAAAAAAAO8/ipK1m9GfWFgJYY4YUq7ZFk6eDU3GtLhtACK4BGAYYCw/s1600/img_log_in_2.png")
            2-> bundle.putString("background_url", "https://3.bp.blogspot.com/-0HMIu1_bHPo/XSWWVxoExkI/AAAAAAAAAPE/ePCerwoW5nUblTsjYfzl0k_foefOAr_fACK4BGAYYCw/s1600/Img_log_in_3.png")
            3-> bundle.putString("background_url", "https://3.bp.blogspot.com/-JMJe5rVQdo8/XSWWV2fo3fI/AAAAAAAAAPA/H2OAjgrQdvMi55uKIeHSSJ2gAv9UVrgOACK4BGAYYCw/s1600/img_log_in_4.png")*/
            0-> bundle.putString("background_url", "https://1.bp.blogspot.com/-NHHQAX6ex6s/XmM1EQRlL1I/AAAAAAAAARw/C1mzKKSb8WAU1HAeQA5JX13OQ1oW8D44ACLcBGAsYHQ/s1600/1583559782703-3.png")
            1-> bundle.putString("background_url", "https://1.bp.blogspot.com/-y8EWg5LqlZg/XmM1GmLu67I/AAAAAAAAAR0/rccnWGu5nbE3osaTiWa_2eeHgAa59F3aACLcBGAsYHQ/s1600/1583559782703-2.png")
            2-> bundle.putString("background_url", "https://1.bp.blogspot.com/-Ry6IPZfYgio/XmM1IY_-VtI/AAAAAAAAAR4/600KzCMA6nsS5WCuZKaGUNFxm_dEYsNiACLcBGAsYHQ/s1600/1583559782703-1.png")
            3-> bundle.putString("background_url", "https://1.bp.blogspot.com/-yDVRHKlGQZo/XmM1Js5kk5I/AAAAAAAAAR8/F1x3UgmTFOon1yaRpLzxvIjhG8og6fYTwCLcBGAsYHQ/s1600/1583559782703-0.png")
        }

        fragment.arguments=bundle
        return fragment
    }

    override fun getCount(): Int {
        return num_fragment
    }
}