package com.example.mindgarden.ui.diary

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.ContextCompat
import com.example.mindgarden.data.MoodChoiceData
import com.example.mindgarden.R

interface Mood {

    private fun initDataList(ctx: Context, dataList : ArrayList<MoodChoiceData>){
        dataList.add(MoodChoiceData(0, drawableToBitmap(ctx, R.drawable.img_weather1_good), "좋아요"))
        dataList.add(MoodChoiceData(1, drawableToBitmap(ctx,R.drawable.img_weather2_excited), "신나요"))
        dataList.add(MoodChoiceData(2, drawableToBitmap(ctx,R.drawable.img_weather3_soso), "그냥 그래요"))
        dataList.add(MoodChoiceData(3, drawableToBitmap(ctx,R.drawable.img_weather4_bored), "심심해요"))
        dataList.add(MoodChoiceData(4, drawableToBitmap(ctx,R.drawable.img_weather5_funny), "재미있어요"))
        dataList.add(MoodChoiceData(5, drawableToBitmap(ctx,R.drawable.img_weather6_rainbow), "설레요"))
        dataList.add(MoodChoiceData(6, drawableToBitmap(ctx,R.drawable.img_weather7_notgood), "별로에요"))
        dataList.add(MoodChoiceData(7, drawableToBitmap(ctx,R.drawable.img_weather8_sad), "우울해요"))
        dataList.add(MoodChoiceData(8, drawableToBitmap(ctx,R.drawable.img_weather9_annoying), "짜증나요"))
        dataList.add(MoodChoiceData(9, drawableToBitmap(ctx,R.drawable.img_weather10_lightning), "화가나요"))
        dataList.add(MoodChoiceData(10, drawableToBitmap(ctx,R.drawable.img_weather11_none), "기분없음"))
    }

    fun getMoodList(ctx: Context, dataList : ArrayList<MoodChoiceData>) : ArrayList<MoodChoiceData>{
        initDataList(ctx, dataList)
        return dataList
    }

    fun drawableToBitmap(ctx : Context, icnName : Int) : Bitmap  = convertDrawable(ctx, icnName).bitmap

    private fun convertDrawable(ctx : Context, icnName : Int) : BitmapDrawable = ContextCompat.getDrawable(ctx, icnName) as BitmapDrawable

}