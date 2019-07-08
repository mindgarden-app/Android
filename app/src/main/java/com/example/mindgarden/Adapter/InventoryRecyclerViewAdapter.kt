package com.example.mindgarden.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.mindgarden.Activity.InventoryActivity
import com.example.mindgarden.Data.InventoryData
import com.example.mindgarden.R

class InventoryRecyclerViewAdapter(var ctx: Context, var dataList: ArrayList<InventoryData>): RecyclerView.Adapter<InventoryRecyclerViewAdapter.Holder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_inventory, viewGroup, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Glide.with(ctx)
            .load(dataList[position].icn_tree)
            .into(holder.img_inventory)

        val btm = dataList[position].icn_tree

        holder.container.isSelected = true
        holder.container.setOnClickListener{
            if (holder.container.isSelected) {
                holder.container.setBackgroundResource(R.drawable.inventory_click_border)
            } else {
                holder.container.setBackgroundResource(R.drawable.inventory_border)
            }
            holder.container.isSelected = !holder.container.isSelected
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var container = itemView.findViewById(R.id.ll_rv_item_inventory_container) as LinearLayout
        var img_inventory = itemView.findViewById(R.id.img_rv_item_inventory) as ImageView
    }
}