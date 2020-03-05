package com.example.mindgarden.ui.inventory

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.example.mindgarden.data.GridData
import com.example.mindgarden.network.NetworkService
import com.example.mindgarden.R
import kotlin.collections.ArrayList

class GridRecyclerViewAdapter(var ctx: Context, var gridDataList:ArrayList<GridData>): androidx.recyclerview.widget.RecyclerView.Adapter<GridRecyclerViewAdapter.Holder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_grid, viewGroup, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = gridDataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Glide.with(ctx)
            .load(gridDataList[position].img)
            .into(holder.grid_img)

        var num:Int = 0

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
                    for (i in 0..InventoryActivity.locationList.size - 1) {
                        if (holder.adapterPosition == InventoryActivity.fromServerToUs[InventoryActivity.locationList[i]]) {
                            num = num + 1
                        }
                    }
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    } else if (num > 0) {
                        holder.container.setBackgroundResource(R.drawable.grid_border)
                    }
                    else if (num == 0) {
                        holder.grid_img.setImageResource(R.drawable.img_selcted011)
                    }
                } else if (InventoryActivity.inventoryIdx == 1) {
                    for (i in 0..InventoryActivity.locationList.size - 1) {
                        if (holder.adapterPosition == InventoryActivity.fromServerToUs[InventoryActivity.locationList[i]]) {
                            num = num + 1
                        }
                    }
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    } else if (num > 0) {
                        holder.container.setBackgroundResource(R.drawable.grid_border)
                    }
                    else if (num == 0) {holder.grid_img.setImageResource(R.drawable.img_selcted012)}
                } else if (InventoryActivity.inventoryIdx == 2) {
                    for (i in 0..InventoryActivity.locationList.size - 1) {
                        if (holder.adapterPosition == InventoryActivity.fromServerToUs[InventoryActivity.locationList[i]]) {
                            num = num + 1
                        }
                    }
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    } else if (num > 0) {
                        holder.container.setBackgroundResource(R.drawable.grid_border)
                    }
                    else if (num == 0) {holder.grid_img.setImageResource(R.drawable.img_selcted015)}
                } else if (InventoryActivity.inventoryIdx == 3) {
                    for (i in 0..InventoryActivity.locationList.size - 1) {
                        if (holder.adapterPosition == InventoryActivity.fromServerToUs[InventoryActivity.locationList[i]]) {
                            num = num + 1
                        }
                    }
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    } else if (num > 0) {
                        holder.container.setBackgroundResource(R.drawable.grid_border)
                    }
                    else if (num == 0) {holder.grid_img.setImageResource(R.drawable.img_selcted016)}
                } else if (InventoryActivity.inventoryIdx == 4) {
                    for (i in 0..InventoryActivity.locationList.size - 1) {
                        if (holder.adapterPosition == InventoryActivity.fromServerToUs[InventoryActivity.locationList[i]]) {
                            num = num + 1
                        }
                    }
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    } else if (num > 0) {
                        holder.container.setBackgroundResource(R.drawable.grid_border)
                    }
                    else if (num == 0) {holder.grid_img.setImageResource(R.drawable.img_selcted007)}
                } else if (InventoryActivity.inventoryIdx == 5) {
                    for (i in 0..InventoryActivity.locationList.size - 1) {
                        if (holder.adapterPosition == InventoryActivity.fromServerToUs[InventoryActivity.locationList[i]]) {
                            num = num + 1
                        }
                    }
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    } else if (num > 0) {
                        holder.container.setBackgroundResource(R.drawable.grid_border)
                    }
                    else if (num == 0) {holder.grid_img.setImageResource(R.drawable.img_selcted013)}
                } else if (InventoryActivity.inventoryIdx == 6) {
                    for (i in 0..InventoryActivity.locationList.size - 1) {
                        if (holder.adapterPosition == InventoryActivity.fromServerToUs[InventoryActivity.locationList[i]]) {
                            num = num + 1
                        }
                    }
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    } else if (num > 0) {
                        holder.container.setBackgroundResource(R.drawable.grid_border)
                    }
                    else if (num == 0) {holder.grid_img.setImageResource(R.drawable.img_selcted014)}
                } else if (InventoryActivity.inventoryIdx == 7) {
                    for (i in 0..InventoryActivity.locationList.size - 1) {
                        if (holder.adapterPosition == InventoryActivity.fromServerToUs[InventoryActivity.locationList[i]]) {
                            num = num + 1
                        }
                    }
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    } else if (num > 0) {
                        holder.container.setBackgroundResource(R.drawable.grid_border)
                    }
                    else if (num == 0) {holder.grid_img.setImageResource(R.drawable.img_selcted008)}
                } else if (InventoryActivity.inventoryIdx == 8) {
                    for (i in 0..InventoryActivity.locationList.size - 1) {
                        if (holder.adapterPosition == InventoryActivity.fromServerToUs[InventoryActivity.locationList[i]]) {
                            num = num + 1
                        }
                    }
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    } else if (num > 0) {
                        holder.container.setBackgroundResource(R.drawable.grid_border)
                    }
                    else if (num == 0) {holder.grid_img.setImageResource(R.drawable.img_selcted010)}
                } else if (InventoryActivity.inventoryIdx == 9) {
                    for (i in 0..InventoryActivity.locationList.size - 1) {
                        if (holder.adapterPosition == InventoryActivity.fromServerToUs[InventoryActivity.locationList[i]]) {
                            num = num + 1
                        }
                    }
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    } else if (num > 0) {
                        holder.container.setBackgroundResource(R.drawable.grid_border)
                    }
                    else if (num == 0) {holder.grid_img.setImageResource(R.drawable.img_selcted004)}
                } else if (InventoryActivity.inventoryIdx == 10) {
                    for (i in 0..InventoryActivity.locationList.size - 1) {
                        if (holder.adapterPosition == InventoryActivity.fromServerToUs[InventoryActivity.locationList[i]]) {
                            num = num + 1
                        }
                    }
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    } else if (num > 0) {
                        holder.container.setBackgroundResource(R.drawable.grid_border)
                    }
                    else if (num == 0) {holder.grid_img.setImageResource(R.drawable.img_selcted005)}
                } else if (InventoryActivity.inventoryIdx == 11) {
                    for (i in 0..InventoryActivity.locationList.size - 1) {
                        if (holder.adapterPosition == InventoryActivity.fromServerToUs[InventoryActivity.locationList[i]]) {
                            num = num + 1
                        }
                    }
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    } else if (num > 0) {
                        holder.container.setBackgroundResource(R.drawable.grid_border)
                    }
                    else if (num == 0) {holder.grid_img.setImageResource(R.drawable.img_selcted003)}
                } else if (InventoryActivity.inventoryIdx == 12) {
                    for (i in 0..InventoryActivity.locationList.size - 1) {
                        if (holder.adapterPosition == InventoryActivity.fromServerToUs[InventoryActivity.locationList[i]]) {
                            num = num + 1
                        }
                    }
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    } else if (num > 0) {
                        holder.container.setBackgroundResource(R.drawable.grid_border)
                    }
                    else if (num == 0) {holder.grid_img.setImageResource(R.drawable.img_selcted009)}
                } else if (InventoryActivity.inventoryIdx == 13) {
                    for (i in 0..InventoryActivity.locationList.size - 1) {
                        if (holder.adapterPosition == InventoryActivity.fromServerToUs[InventoryActivity.locationList[i]]) {
                            num = num + 1
                        }
                    }
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    } else if (num > 0) {
                        holder.container.setBackgroundResource(R.drawable.grid_border)
                    }
                    else if (num == 0) {holder.grid_img.setImageResource(R.drawable.img_selcted002)}
                } else if (InventoryActivity.inventoryIdx == 14) {
                    for (i in 0..InventoryActivity.locationList.size - 1) {
                        if (holder.adapterPosition == InventoryActivity.fromServerToUs[InventoryActivity.locationList[i]]) {
                            num = num + 1
                        }
                    }
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    } else if (num > 0) {
                        holder.container.setBackgroundResource(R.drawable.grid_border)
                    }
                    else if (num == 0) {holder.grid_img.setImageResource(R.drawable.img_selcted006)}
                } else if (InventoryActivity.inventoryIdx == 15) {
                    for (i in 0..InventoryActivity.locationList.size - 1) {
                        if (holder.adapterPosition == InventoryActivity.fromServerToUs[InventoryActivity.locationList[i]]) {
                            num = num + 1
                        }
                    }
                    if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                        holder.grid_img.setImageResource(R.drawable.img_small_lake)
                    } else if (num > 0) {
                        holder.container.setBackgroundResource(R.drawable.grid_border)
                    }
                    else if (num == 0) {holder.grid_img.setImageResource(R.drawable.img_selcted001)}
                }
            } else {
                for (i in 0..InventoryActivity.locationList.size - 1) {
                    if (holder.adapterPosition == InventoryActivity.fromServerToUs[InventoryActivity.locationList[i]]) {
                        num = num + 1
                    }
                }
                if (holder.adapterPosition == 14 || holder.adapterPosition == 15 || holder.adapterPosition == 20 || holder.adapterPosition == 21) {
                    holder.grid_img.setImageResource(R.drawable.img_small_lake)
                } else if (num > 0) {
                    holder.container.setBackgroundResource(R.drawable.grid_border)
                }
                else if (num == 0) {holder.grid_img.setImageResource(R.drawable.tree_size)}
            }

            holder.container.isSelected = !holder.container.isSelected
        }
    }

    inner class Holder(itemView: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var container = itemView.findViewById(R.id.ll_rv_item_grid_container) as LinearLayout
        var grid_img = itemView.findViewById(R.id.img_rv_item_grid) as ImageView
    }
}