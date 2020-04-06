package com.mindgarden.mindgarden.ui.inventory

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.mindgarden.mindgarden.data.InventoryData
import com.mindgarden.mindgarden.R
import com.mindgarden.mindgarden.setDefaultTreeImage
import com.mindgarden.mindgarden.setSpringTreeImage

class InventoryRecyclerViewAdapter(private val clickEvent : (position : Int)->Unit):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data = ArrayList<InventoryData>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        return when(viewType){
            InventoryData.default-> DefaultInventoryRecyclerViewHolder(clickEvent, viewGroup)
            InventoryData.click-> ClickInventoryRecyclerViewHolder(clickEvent,viewGroup)
           // InventoryData.block-> BlockInventoryRecyclerViewHolder(viewGroup)
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
                    setTreeImage(data[position].season, holder.imgInventory, data[position].treeIcn)
                }
                InventoryData.click->{
                    holder as ClickInventoryRecyclerViewHolder
                    setTreeImage(data[position].season, holder.imgInventory, data[position].treeIcn)
                }

                else-> throw RuntimeException("알 수 없는 뷰타입 에러")
            }
        }
    }

    private fun setTreeImage(season: Int, iv : ImageView, treeIcn : Int){
        when(season){
            0-> iv.setSpringTreeImage(treeIcn)
            else -> iv.setDefaultTreeImage(treeIcn)
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