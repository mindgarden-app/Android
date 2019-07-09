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

        // InventoryActivity에서 isClickAvailable 변수를 caompanion object해서 담아둠 -> 하나의 객체로 해서 사용하겠다는
        // 이제 여기서 리사이클러뷰 선택시 작동하도록 안에 코드 작성
        // 리워드 하나 선택 시, 리사이클러뷰가 존재하는 뷰(InventoryActivity)가 클릭 가능하다면, 이 상태에서 내가 리워드 하나 클릭했다(holder.container.isSelected=true). 그러면 테두리 생김
        // 근데 그 상태에서 다른 리워드는 선택 못하도록 해야 하니깐 isClickAvailable을 false로 바꿈
        // else if -> 테두리 생긴 애만 누를 수 있다를 의미함
        holder.container.setOnClickListener{
            if (InventoryActivity.isClickAvailable) {
                holder.container.isSelected = true
                InventoryActivity.isClickAvailable = false
            }

            else if (!InventoryActivity.isClickAvailable && holder.container.isSelected) {
                holder.container.isSelected = false
                InventoryActivity.isClickAvailable = true
            }

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