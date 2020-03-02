package com.example.mindgarden.ui.main

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.SparseArray
import androidx.core.content.ContextCompat
import com.example.mindgarden.R

interface Tree {
    private fun initDataArray(ctx: Context,array: SparseArray<Bitmap>){
        array.append(0, drawableToBitmap(ctx, R.drawable.android_tree1))
        array.append(1,drawableToBitmap(ctx, R.drawable.android_tree2))
        array.append(2, drawableToBitmap(ctx, R.drawable.android_tree3))
        array.append(3, drawableToBitmap(ctx, R.drawable.android_tree4))
        array.append(4,drawableToBitmap(ctx, R.drawable.android_tree5))
        array.append(5, drawableToBitmap(ctx, R.drawable.android_tree6))
        array.append(6, drawableToBitmap(ctx, R.drawable.android_tree7))
        array.append(7, drawableToBitmap(ctx, R.drawable.android_tree8))
        array.append(8, drawableToBitmap(ctx, R.drawable.android_tree9))
        array.append(9, drawableToBitmap(ctx, R.drawable.android_tree10))
        array.append(10, drawableToBitmap(ctx, R.drawable.android_tree11))
        array.append(11, drawableToBitmap(ctx, R.drawable.android_tree12))
        array.append(12, drawableToBitmap(ctx, R.drawable.android_tree13))
        array.append(13,drawableToBitmap(ctx, R.drawable.android_tree14))
        array.append(14, drawableToBitmap(ctx, R.drawable.android_tree15))
        array.append(15,drawableToBitmap(ctx, R.drawable.android_tree16))
    }

    fun getTreeArray(ctx: Context, array: SparseArray<Bitmap>): SparseArray<Bitmap>{
        initDataArray(ctx, array)
        return array
    }

    fun drawableToBitmap(ctx : Context, icnName : Int) : Bitmap = convertDrawable(ctx, icnName).bitmap

    private fun convertDrawable(ctx : Context, icnName : Int) : BitmapDrawable = ContextCompat.getDrawable(ctx, icnName) as BitmapDrawable
}