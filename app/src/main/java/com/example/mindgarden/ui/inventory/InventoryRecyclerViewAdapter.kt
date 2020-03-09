package com.example.mindgarden.ui.inventory

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.mindgarden.data.InventoryData
import com.example.mindgarden.R

class InventoryRecyclerViewAdapter(private val clickEvent : (position : Int)->Unit):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data = ArrayList<InventoryData>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        return when(viewType){
            InventoryData.default-> DefaultInventoryRecyclerViewHolder(clickEvent, viewGroup)
            InventoryData.click-> ClickInventoryRecyclerViewHolder(clickEvent,viewGroup)
            InventoryData.block-> BlockInventoryRecyclerViewHolder(viewGroup)
            else-> throw RuntimeException("알 수 없는 뷰타입 에러")
        }
    }


    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = data[position].type

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        data[position].let {
            when (it.type) {
                InventoryData.default->{
                    holder as DefaultInventoryRecyclerViewHolder
                    setInventoryImage(holder.itemView.context, data[position].treeIcn, holder.imgInventory)
                }
                InventoryData.click->{
                    holder as ClickInventoryRecyclerViewHolder
                    setInventoryImage(holder.itemView.context, data[position].treeIcn, holder.imgInventory)
                }
                InventoryData.block->{
                    holder as BlockInventoryRecyclerViewHolder
                    setInventoryImage(holder.itemView.context, data[position].treeIcn, holder.imgInventory)
                }
                else-> throw RuntimeException("알 수 없는 뷰타입 에러")
            }
        }
    }

    fun getDataAt(position: Int) = data[position]

    fun setData(newData : ArrayList<InventoryData>){
        newData?.let {
            data.clear()
            data.addAll(newData)
            notifyDataSetChanged()
        }
    }

    private fun setInventoryImage(ctx : Context, img :Int, iv : ImageView){
        Glide.with(ctx)
            .load(img)
            .into(iv)
    }

    class DefaultInventoryRecyclerViewHolder(clickEvent: (position: Int) -> Unit, viewGroup: ViewGroup) :
        RecyclerView.ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.rv_item_inventory, viewGroup, false)) {

        val imgInventory = itemView.findViewById(R.id.img_rv_item_inventory) as ImageView

        init {
            imgInventory.setBackgroundResource(R.drawable.inventory_border)
            imgInventory.isClickable = true
            imgInventory.setOnClickListener {
                clickEvent(adapterPosition)
            }
        }
    }

    class ClickInventoryRecyclerViewHolder(private val clickEvent: (position: Int) -> Unit, viewGroup:ViewGroup) :
        RecyclerView.ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.rv_item_inventory, viewGroup, false)){

        val imgInventory = itemView.findViewById(R.id.img_rv_item_inventory) as ImageView

        init {
            imgInventory.setBackgroundResource(R.drawable.grid_border)
            imgInventory.isClickable = true
            imgInventory.setOnClickListener {
                clickEvent(adapterPosition)
            }
        }
    }

    class BlockInventoryRecyclerViewHolder(viewGroup: ViewGroup) :
        RecyclerView.ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.rv_item_inventory, viewGroup, false)) {
        val imgInventory = itemView.findViewById(R.id.img_rv_item_inventory) as ImageView

        init {
                imgInventory.isClickable = false
            }
    }
}

