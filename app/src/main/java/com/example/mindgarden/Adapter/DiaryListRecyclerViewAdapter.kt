package com.example.mindgarden.Adapter

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mindgarden.Activity.ReadDiaryActivity
import com.example.mindgarden.Data.DiaryListData
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.Delete.DeleteDiaryListResponse
import com.example.mindgarden.Network.NetworkService

import com.example.mindgarden.R
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiaryListRecyclerViewAdapter(var ctx: Context, var dataList:ArrayList<DiaryListData>): RecyclerView.Adapter<DiaryListRecyclerViewAdapter.Holder>() {
    var context : Context = ctx
    val networkService: NetworkService by lazy{
        ApplicationController.instance.networkService
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_diary_list, viewGroup, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size

    var isPressed = false
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.day_num.text = dataList[position].date.substring(8, 10)
        holder.day_text.text = dataList[position].date.substring(11, 14)
        holder.content.text = dataList[position].diary_content

        holder.container.setOnLongClickListener {
            isPressed = !isPressed
            notifyDataSetChanged()
            false
        }

        holder.content.setOnClickListener {
            val date = dataList[position].date.substring(0, 10)
            //intent로 date값 넘기기
            ctx.startActivity<ReadDiaryActivity>("date" to date)
        }

        if (isPressed) {
            holder.lay1.visibility = View.VISIBLE

            holder.icn_delete.setOnClickListener {
                //var dialogView: View = View.inflate(context, R.layout.diary_list_dialog, null)
                var dlg = AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                dlg.setTitle("삭제")
                dlg.setMessage("삭제하시겠습니까?")

                fun do_p() {
                    deleteDiaryListResponse(dataList[position].date.substring(0, 10),holder.adapterPosition)
                    //밑에는 서버 통신하고 삭제
                    dataList.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                    notifyItemRangeChanged(holder.adapterPosition, dataList.size)
                }

                val dlg_listener = DialogInterface.OnClickListener { dialog, which ->
                    when(which) {
                        DialogInterface.BUTTON_NEUTRAL -> do_p()
                    }
                }

                dlg.setNeutralButton("네", dlg_listener)
                dlg.setPositiveButton("아니오", null)

                var dlgNew: AlertDialog = dlg.show()
                var messageText:TextView? = dlgNew.findViewById(android.R.id.message)
                messageText!!.gravity = Gravity.CENTER
                dlgNew.window.setBackgroundDrawableResource(R.drawable.round_layout_border)

                dlgNew.show()
            }
        } else {
            holder.lay1.visibility = View.GONE
        }
    }


    private fun deleteDiaryListResponse(deleteDate: String, deleteIndex: Int){
        //var jsonObject = JSONObject()
        //jsonObject.put("date", deleteDate)
        //userIdx 넣어야

        //val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject
        val deleteDiaryListResponse = networkService.deleteDiaryListResponse(
            "application/json", deleteDate, 5)
        deleteDiaryListResponse.enqueue(object: Callback<DeleteDiaryListResponse> {
            override fun onFailure(call: Call<DeleteDiaryListResponse>, t: Throwable) {
                Log.e("일기 삭제 실패", t.toString())
            }

            override fun onResponse(call: Call<DeleteDiaryListResponse>, response: Response<DeleteDiaryListResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        dataList.removeAt(deleteIndex)
                        notifyItemRemoved(deleteIndex)
                        notifyItemRangeChanged(deleteIndex, dataList.size)
                    }
                }
            }
        })
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