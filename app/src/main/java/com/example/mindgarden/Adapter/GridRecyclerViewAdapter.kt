package com.example.mindgarden.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
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

        gridDataList[14].img = R.drawable.img_small_lake
        gridDataList[15].img = R.drawable.img_small_lake
        gridDataList[20].img = R.drawable.img_small_lake
        gridDataList[21].img = R.drawable.img_small_lake
        Log.e("grid", gridDataList[14].product_id.toString())
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var container = itemView.findViewById(R.id.ll_rv_item_grid_container) as LinearLayout
        var grid_img = itemView.findViewById(R.id.img_rv_item_grid) as ImageView
    }
}