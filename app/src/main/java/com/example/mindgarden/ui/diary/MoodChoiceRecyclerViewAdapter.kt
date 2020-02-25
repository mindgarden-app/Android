package com.example.mindgarden.ui.diary

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.mindgarden.data.MoodChoiceData

import com.example.mindgarden.R

class MoodChoiceRecyclerViewAdapter (var ctx: Context, var dataList : ArrayList<MoodChoiceData>) : RecyclerView.Adapter<MoodChoiceRecyclerViewAdapter.Holder>(){
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_mood_choice, viewGroup , false)
        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Glide.with(ctx)
            .load(dataList[position].moodIcn)
            .into(holder.moodIcn)

        val btm = dataList[position].moodIcn

        val weatherIdx = dataList[position].weatherIdx

        holder.moodTxt.text = dataList[position].moodTxt

        holder.container.setOnClickListener{
            Intent(ctx, ModifyDiaryActivity::class.java).apply {
                putExtra("weatherIdx", dataList[position].weatherIdx)
                (ctx as Activity).setResult(Activity.RESULT_OK, this)
                (ctx as Activity).finish()
            }
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var container = itemView.findViewById(R.id.ll_rv_item_mood_choice_container) as LinearLayout
        var moodIcn = itemView.findViewById(R.id.img_icon_rv_item_mood_choice) as ImageView
        var moodTxt = itemView.findViewById(R.id.txt_rv_item_mood_choice) as TextView
    }
}