package com.example.mindgarden.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast

import com.example.mindgarden.Layout.CustomGridViewLayout
import com.example.mindgarden.R

class GridViewAdapter(private val getContext : Context, private val CustomGridViewID:Int, private val customItem:ArrayList<CustomGridViewLayout>):
    ArrayAdapter<CustomGridViewLayout>(getContext,CustomGridViewID,customItem) {

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    class ViewHolder{
        internal var img: ImageView?= null
        internal var position:Int?=null
    }
}