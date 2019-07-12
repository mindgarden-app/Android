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
    val fromServerToUs =arrayOf(100,0,6,1,12,7,2,18,13,8,3,24,19,9,4,30,25,10,5,31,26,16,11,32,27,22,17,33,28,23,34,29,35)
    val fromUsToServer = arrayOf(1,3,6,10,14,18,2,5,9,13,17,22,4,8,100,101,21,26,7,12,102,103,25,29,11,16,20,24,28,31,15,19,23,27,30,32)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_grid, viewGroup, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = gridDataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Glide.with(ctx)
            .load(gridDataList[position].img)
            .into(holder.grid_img)

        /*gridDataList[14].img = R.drawable.img_small_lake
        gridDataList[15].img = R.drawable.img_small_lake
        gridDataList[20].img = R.drawable.img_small_lake
        gridDataList[21].img = R.drawable.img_small_lake*/

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
                InventoryActivity.gridIdx = holder.adapterPosition
                Log.e("grid", InventoryActivity.gridIdx.toString())
                if (InventoryActivity.inventoryIdx == 0) {
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    }
                    else holder.grid_img.setImageResource(R.drawable.img_selcted011)
                } else if (InventoryActivity.inventoryIdx == 1) {
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    }
                    else holder.grid_img.setImageResource(R.drawable.img_selcted012)
                } else if (InventoryActivity.inventoryIdx == 2) {
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    }
                    else holder.grid_img.setImageResource(R.drawable.img_selcted015)
                } else if (InventoryActivity.inventoryIdx == 3) {
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    }
                    else holder.grid_img.setImageResource(R.drawable.img_selcted016)
                } else if (InventoryActivity.inventoryIdx == 4) {
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    }
                    else holder.grid_img.setImageResource(R.drawable.img_selcted007)
                } else if (InventoryActivity.inventoryIdx == 5) {
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    }
                    else holder.grid_img.setImageResource(R.drawable.img_selcted013)
                } else if (InventoryActivity.inventoryIdx == 6) {
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    }
                    else holder.grid_img.setImageResource(R.drawable.img_selcted014)
                } else if (InventoryActivity.inventoryIdx == 7) {
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    }
                    else holder.grid_img.setImageResource(R.drawable.img_selcted008)
                } else if (InventoryActivity.inventoryIdx == 8) {
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    }
                    else holder.grid_img.setImageResource(R.drawable.img_selcted010)
                } else if (InventoryActivity.inventoryIdx == 9) {
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    }
                    else holder.grid_img.setImageResource(R.drawable.img_selcted004)
                } else if (InventoryActivity.inventoryIdx == 10) {
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    }
                    else holder.grid_img.setImageResource(R.drawable.img_selcted005)
                } else if (InventoryActivity.inventoryIdx == 11) {
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    }
                    else holder.grid_img.setImageResource(R.drawable.img_selcted003)
                } else if (InventoryActivity.inventoryIdx == 12) {
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    }
                    else holder.grid_img.setImageResource(R.drawable.img_selcted009)
                } else if (InventoryActivity.inventoryIdx == 13) {
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    }
                    else holder.grid_img.setImageResource(R.drawable.img_selcted002)
                } else if (InventoryActivity.inventoryIdx == 14) {
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    }
                    else holder.grid_img.setImageResource(R.drawable.img_selcted006)
                } else if (InventoryActivity.inventoryIdx == 15) {
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    }
                    else holder.grid_img.setImageResource(R.drawable.img_selcted001)
                }
            } else {
                if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                    holder.grid_img.setImageResource(R.drawable.img_small_lake)
                }
                else {holder.grid_img.setImageResource(R.drawable.tree_size)}
            }

            holder.container.isSelected = !holder.container.isSelected
        }
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var container = itemView.findViewById(R.id.ll_rv_item_grid_container) as LinearLayout
        var grid_img = itemView.findViewById(R.id.img_rv_item_grid) as ImageView
    }
}