package com.example.mindgarden.ui.inventory

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mindgarden.data.GridData
import com.example.mindgarden.R
import com.example.mindgarden.setDefaultTreeImage
import com.example.mindgarden.setSpringTreeImage
import kotlin.collections.ArrayList

class GridRecyclerViewAdapter(private val clickEvent : (position : Int)->Unit):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        val selectedStatus = SparseBooleanArray(0)
    }
    private val data = ArrayList<GridData>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            GridData.defaultType-> DefaultGridRecyclerViewHolder(clickEvent,viewGroup)

            GridData.lakeType-> LakeGridRecyclerViewHolder(viewGroup)

            GridData.alreadyExistType-> AETypeRecyclerViewHolder(viewGroup)

            else-> throw RuntimeException("알 수 없는 뷰타입 에러")
        }
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = data[position].type


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        data[position].let {
            when(it.type) {
                GridData.lakeType -> {
                    holder as LakeGridRecyclerViewHolder
                }
                GridData.defaultType -> {
                    holder as DefaultGridRecyclerViewHolder
                    data[position].img?.let { img->
                        setTreeImage(data[position].season, holder.gridImg, img)
                    }
                    when(selectedStatus.get(position)){
                        true->{
                            holder.gridImg.setBackgroundResource(R.drawable.grid_border_selected)
                        }
                        else->  {
                            holder.gridImg.setBackgroundResource(R.drawable.grid_border)
                            holder.gridImg.setDefaultTreeImage(-1)
                        }

                    }

                }
                GridData.alreadyExistType->{
                    holder as AETypeRecyclerViewHolder
                    data[position].img?.let { img->
                        setTreeImage(data[position].season, holder.gridImg, img)
//                        holder.gridImg.setDefaultTreeImage(img)
                    }
                }
                else-> throw RuntimeException("알 수 없는 뷰타입 에러")
            }
        }
    }

    private fun setTreeImage(season: Int, iv : ImageView, img : Int){
        when(season){
            0-> iv.setSpringTreeImage(img)
            else -> iv.setDefaultTreeImage(img)
        }
    }

    fun setData(newData : ArrayList<GridData>){
        newData?.let {
            data.clear()
            data.addAll(newData)
            notifyDataSetChanged()
        }
    }

    class DefaultGridRecyclerViewHolder(private val clickEvent: (position: Int) -> Unit, viewGroup:ViewGroup) :
        RecyclerView.ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.rv_item_grid, viewGroup,false))
    {
        val gridImg = itemView.findViewById<ImageView>(R.id.img_rv_item_grid)

        init {
            gridImg.setOnClickListener { clickEvent(adapterPosition) }
        }
    }
    class LakeGridRecyclerViewHolder(viewGroup: ViewGroup):
        RecyclerView.ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.rv_item_grid_lake, viewGroup, false))

    class AETypeRecyclerViewHolder(viewGroup: ViewGroup) :
        RecyclerView.ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.rv_item_grid, viewGroup,false))
    {
        val gridImg = itemView.findViewById<ImageView>(R.id.img_rv_item_grid)
    }
}