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
import com.example.mindgarden.Activity.InventoryActivity
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

        holder.container.setOnClickListener{
            if (InventoryActivity.isGridClick) {
                holder.container.isSelected = true
                InventoryActivity.isGridClick = false
            }

            else if (!InventoryActivity.isGridClick && holder.container.isSelected) {
                holder.container.isSelected = false
                InventoryActivity.isGridClick = true
            }

            if (holder.container.isSelected) {
                if (InventoryActivity.inventoryIdx == 0) {
                    holder.grid_img.setImageResource(R.drawable.img_selcted011)
                } else if (InventoryActivity.inventoryIdx == 1) {
                    holder.grid_img.setImageResource(R.drawable.img_selcted012)
                } else if (InventoryActivity.inventoryIdx == 2) {
                    holder.grid_img.setImageResource(R.drawable.img_selcted015)
                } else if (InventoryActivity.inventoryIdx == 3) {
                    holder.grid_img.setImageResource(R.drawable.img_selcted016)
                } else if (InventoryActivity.inventoryIdx == 4) {
                    holder.grid_img.setImageResource(R.drawable.img_selcted007)
                } else if (InventoryActivity.inventoryIdx == 5) {
                    holder.grid_img.setImageResource(R.drawable.img_selcted013)
                } else if (InventoryActivity.inventoryIdx == 6) {
                    holder.grid_img.setImageResource(R.drawable.img_selcted014)
                } else if (InventoryActivity.inventoryIdx == 7) {
                    holder.grid_img.setImageResource(R.drawable.img_selcted008)
                } else if (InventoryActivity.inventoryIdx == 8) {
                    holder.grid_img.setImageResource(R.drawable.img_selcted010)
                } else if (InventoryActivity.inventoryIdx == 9) {
                    holder.grid_img.setImageResource(R.drawable.img_selcted004)
                } else if (InventoryActivity.inventoryIdx == 10) {
                    holder.grid_img.setImageResource(R.drawable.img_selcted005)
                } else if (InventoryActivity.inventoryIdx == 11) {
                    holder.grid_img.setImageResource(R.drawable.img_selcted003)
                } else if (InventoryActivity.inventoryIdx == 12) {
                    holder.grid_img.setImageResource(R.drawable.img_selcted009)
                } else if (InventoryActivity.inventoryIdx == 13) {
                    holder.grid_img.setImageResource(R.drawable.img_selcted002)
                } else if (InventoryActivity.inventoryIdx == 14) {
                    holder.grid_img.setImageResource(R.drawable.img_selcted006)
                } else if (InventoryActivity.inventoryIdx == 15) {
                    holder.grid_img.setImageResource(R.drawable.img_selcted001)
                }
            } else {
                holder.grid_img.setImageResource(R.drawable.tree_size)
            }
            holder.container.isSelected = !holder.container.isSelected
        }
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var container = itemView.findViewById(R.id.ll_rv_item_grid_container) as LinearLayout
        var grid_img = itemView.findViewById(R.id.img_rv_item_grid) as ImageView
    }
}