package com.example.mindgarden.Adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.example.mindgarden.Data.GridData
import com.example.mindgarden.R

class GridRecyclerViewAdapter(var ctx: Context, var gridDataList:ArrayList<GridData>): RecyclerView.Adapter<GridRecyclerViewAdapter.Holder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_grid, viewGroup, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = gridDataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Glide.with(ctx)
            .load(gridDataList[position].img)
            .into(holder.grid_img)
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var container = itemView.findViewById(R.id.ll_rv_item_grid_container) as LinearLayout
        var grid_img = itemView.findViewById(R.id.img_rv_item_grid) as ImageView
    }
}