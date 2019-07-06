package com.example.mindgarden

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.example.mindgarden.R


class GridViewAdapter(private val getContext : Context, private val CustomGridViewLayoutID:Int, private val customItem:ArrayList<CustomGridViewLayout>):
    ArrayAdapter<CustomGridViewLayout>(getContext,CustomGridViewLayoutID,customItem){

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

        var row = convertView
        val Holder: ViewHolder

        if (row == null) {
            val inflater = (getContext as Activity).layoutInflater

            row = inflater.inflate(CustomGridViewLayoutID, parent, false)

            Holder = ViewHolder()
            Holder.img= row.findViewById(R.id.gridItemImg) as ImageView
            row.setTag(Holder)
        }else{
            Holder=row.getTag() as ViewHolder
        }
        val item=customItem[position]
        Holder.img!!.setImageResource(item.Image)

        return row
    }
    class ViewHolder{
        internal var img: ImageView?= null

    }
}