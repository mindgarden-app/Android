package com.example.mindgarden.Adapter
import android.app.Activity
import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.mindgarden_2.Data.MoodChoiceData
import com.example.mindgarden_2.R
import org.jetbrains.anko.startActivity

class MoodChoiceRecyclerViewAdapter (var ctx: Context, var dataList : ArrayList<MoodChoiceData>) : RecyclerView.Adapter<MoodChoiceRecyclerViewAdapter.Holder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MoodChoiceRecyclerViewAdapter.Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_mood_choice, viewGroup , false)
        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Glide.with(ctx)
            .load(dataList[position].moodIcn)
            .into(holder.moodIcn)

        val btm = dataList[position].moodIcn

        holder.moodTxt.text = dataList[position].moodTxt


        holder.container.setOnClickListener{
            var intent: Intent = Intent()
            intent.putExtra("moodTxt", dataList[position].moodTxt)
            intent.putExtra("moodIcn", btm)
            (ctx as Activity).setResult(Activity.RESULT_OK, intent)
            (ctx as Activity).finish()

        }

    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var container = itemView.findViewById(R.id.ll_rv_item_mood_choice_container) as LinearLayout
        var moodIcn = itemView.findViewById(R.id.img_icon_rv_item_mood_choice) as ImageView
        var moodTxt = itemView.findViewById(R.id.txt_rv_item_mood_choice) as TextView
    }



}