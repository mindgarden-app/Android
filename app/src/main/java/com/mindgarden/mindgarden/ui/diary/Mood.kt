package com.mindgarden.mindgarden.ui.diary

import com.mindgarden.mindgarden.data.MoodChoiceData
import com.mindgarden.mindgarden.R

interface Mood {

    private fun initDataList(dataList : ArrayList<MoodChoiceData>){
        dataList.add(MoodChoiceData(0, R.drawable.img_weather1_good, "좋아요"))
        dataList.add(MoodChoiceData(1, R.drawable.img_weather2_excited, "신나요"))
        dataList.add(MoodChoiceData(2, R.drawable.img_weather3_soso, "그냥 그래요"))
        dataList.add(MoodChoiceData(3, R.drawable.img_weather4_bored, "심심해요"))
        dataList.add(MoodChoiceData(4, R.drawable.img_weather5_funny, "재미있어요"))
        dataList.add(MoodChoiceData(5, R.drawable.img_weather6_rainbow, "설레요"))
        dataList.add(MoodChoiceData(6, R.drawable.img_weather7_notgood, "별로에요"))
        dataList.add(MoodChoiceData(7, R.drawable.img_weather8_sad, "우울해요"))
        dataList.add(MoodChoiceData(8, R.drawable.img_weather9_annoying, "짜증나요"))
        dataList.add(MoodChoiceData(9, R.drawable.img_weather10_lightning, "화가나요"))
        dataList.add(MoodChoiceData(10,R.drawable.img_weather11_none, "기분없음"))
    }

    fun getMoodList( dataList : ArrayList<MoodChoiceData>) : ArrayList<MoodChoiceData>{
        initDataList(dataList)
        return dataList
    }
}