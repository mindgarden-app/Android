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
import com.example.mindgarden.DB.TokenController
import com.example.mindgarden.Data.DiaryListData
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.Delete.DeleteDiaryListResponse
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.R
import com.example.mindgarden.RenewAcessTokenController
import org.jetbrains.anko.ctx
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiaryListRecyclerViewAdapter(var ctx: Context, var dataList: ArrayList<DiaryListData>): RecyclerView.Adapter<DiaryListRecyclerViewAdapter.Holder>() {
    var context : Context = ctx
    val networkService: NetworkService by lazy {
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
            var dateText = dataList[position].date.substring(2, 4) + "." + dataList[position].date.substring(5, 7) + "." + dataList[position].date.substring(8, 10) + ". (" + dataList[position].date.substring(11, 14) + ")"
            ctx.startActivity<ReadDiaryActivity>("from" to 300, "userIdx" to 7, "dateText" to  dateText,"dateValue" to dataList[position].date.substring(0, 10))
        }

        if (isPressed) {
            holder.lay1.visibility = View.VISIBLE

            holder.icn_delete.setOnClickListener {
                var dlg = AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                dlg.setTitle("삭제")
                dlg.setMessage("삭제하시겠습니까?")

                fun do_p() {
                    if (isValid(TokenController.getAccessToken(ctx), dataList[position].date.substring(0, 10))) {
                        deleteDiaryListResponse(dataList[position].date.substring(0, 10), holder.adapterPosition)
                    }
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
            holder.lay1.visibility = View.INVISIBLE
        }
    }

    fun isValid(token: String, date: String): Boolean {
        if(token == "")
            Log.e("login fail", "login value null")

        else if(date == "")
            Log.e("date fail", "date value null")

        else return true

        return false
    }

    private fun deleteDiaryListResponse(deleteDate: String, deleteIndex: Int) {
        if(!TokenController.isValidToken(ctx)){
            RenewAcessTokenController.postRenewAccessToken(ctx)
        }

        val deleteDiaryListResponse = networkService.deleteDiaryListResponse(
             TokenController.getAccessToken(ctx), deleteDate)
        Log.e("delete", "delete1")
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
                        Log.e("delete", "delete2")
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