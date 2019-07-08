package com.example.mindgarden.Adapter

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mindgarden.Activity.ReadDiaryActivity
import com.example.mindgarden.Data.DiaryListData

import com.example.mindgarden.R
import org.w3c.dom.Text
import org.jetbrains.anko.startActivity

class DiaryListRecyclerViewAdapter(var ctx: Context, var dataList:ArrayList<DiaryListData>): RecyclerView.Adapter<DiaryListRecyclerViewAdapter.Holder>() {
    var context : Context = ctx

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_diary_list, viewGroup, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size

    var isPressed = false
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.day_num.text = dataList[position].day_num.toString()
        holder.day_text.text = dataList[position].day_text
        holder.content.text = dataList[position].content

        holder.container.setOnLongClickListener {
            isPressed = !isPressed
            notifyDataSetChanged()
            false
        }

        holder.content.setOnClickListener {
            ctx.startActivity<ReadDiaryActivity>()
        }

        if (isPressed) {
            holder.lay1.visibility = View.VISIBLE

            holder.icn_delete.setOnClickListener {
                //var dialogView: View = View.inflate(context, R.layout.diary_list_dialog, null)
                var dlg = AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                dlg.setTitle("삭제")
                dlg.setMessage("삭제하시겠습니까?")

                fun do_p() {
                    dataList.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                    notifyItemRangeChanged(holder.adapterPosition, dataList.size)
                }

                val dlg_listener = DialogInterface.OnClickListener { dialog, which ->
                    when(which) {
                        DialogInterface.BUTTON_POSITIVE -> do_p()
                    }
                }

                dlg.setPositiveButton("네", dlg_listener)
                dlg.setNegativeButton("아니오", null)

                var dlgNew: AlertDialog = dlg.show()
                var messageText:TextView? = dlgNew.findViewById(android.R.id.message)
                messageText!!.gravity = Gravity.CENTER

                dlgNew.show()
            }
        } else {
            holder.lay1.visibility = View.GONE
        }
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var lay1 = itemView.findViewById(R.id.lay1) as LinearLayout
        var icn_delete = itemView.findViewById(R.id.icn_delete) as ImageView
        var container = itemView.findViewById(R.id.ll_rv_item_diary_list_container) as LinearLayout
        var day_num = itemView.findViewById(R.id.txt_rv_item_diary_list_day_num) as TextView
        var day_text = itemView.findViewById(R.id.txt_rv_item_diary_list_day_text) as TextView
        var content = itemView.findViewById(R.id.txt_rv_item_diary_list_content) as TextView
    }
}