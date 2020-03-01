package com.example.mindgarden.ui.diarylist

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mindgarden.ui.diary.ReadDiaryActivity
import com.example.mindgarden.DB.TokenController
import com.example.mindgarden.Data.DiaryListData
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.Delete.DeleteDiaryListResponse
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.R
import com.example.mindgarden.DB.RenewAcessTokenController
import com.example.mindgarden.ui.diary.DiaryDate
import kotlinx.android.synthetic.main.dialog_diary_list_delete.view.*
import kotlinx.android.synthetic.main.rv_item_diary_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList
import kotlin.coroutines.coroutineContext

class DiaryListRecyclerViewAdapter(private val clickEvent: (position: Int) -> Unit): RecyclerView.Adapter<DiaryListRecyclerViewAdapter.Holder>(), DiaryDate {
    //Adapter
    //class DiaryListRecyclerViewAdapter(var ctx: Context, var dataList: ArrayList<DiaryListData>): RecyclerView.Adapter<DiaryListRecyclerViewAdapter.Holder>(), DiaryDate
    //context도 없앰
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }
    //Adapter
    var dataList = ArrayList<DiaryListData>()
    var isPressed = false

    //수정중
    lateinit var dlgNew : AlertDialog

    //Adapter
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Holder = Holder(clickEvent, viewGroup)
    /*override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_diary_list, viewGroup, false)
        return Holder(view)
    }*/

    override fun getItemCount(): Int = dataList.size

    //Adapter
    fun getDataAt(position: Int) = dataList[position]

    //Adapter
    fun setData(newData: ArrayList<DiaryListData>) {
        newData?.let {
            dataList.clear()
            dataList.addAll(newData)
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        //binding
        Log.e("인터페이스1", dataList[position].date)
        Log.e("인터페이스2", getDay(dataList[position].date))
        Log.e("인터페이스3", getDayOfWeek(dataList[position].date))

        holder.itemView.txt_rv_item_diary_list_day_num.text = getDay(dataList[position].date)
        holder.itemView.txt_rv_item_diary_list_day_text.text = getDayOfWeek(dataList[position].date)
        holder.itemView.txt_rv_item_diary_list_content.text = dataList[position].diary_content

        holder.itemView.ll_rv_item_diary_list_container.setOnLongClickListener {
            isPressed = !isPressed
            notifyDataSetChanged()
            false
        }

        //인터페이스 작업 필요
        holder.itemView.txt_rv_item_diary_list_content.setOnClickListener {
            var dateText = dataList[position].date.substring(2, 4) + "." + dataList[position].date.substring(5, 7) + "." + dataList[position].date.substring(8, 10) + ". (" + dataList[position].date.substring(11, 14) + ")"
            //Adapter
            //context ->
            Intent(holder.itemView.context, ReadDiaryActivity::class.java).apply {
                putExtra("from",300)
                putExtra("userIdx" ,7)
                putExtra("dateText",  dateText)
                putExtra("dateValue", dataList[position].date.substring(0, 10))
                //Adapter
                //context.startActivity(this) ->
                holder.itemView.context.startActivity(this)
            }
        }

        if (isPressed) {
            holder.itemView.lay1.visibility = View.VISIBLE

            holder.itemView.icn_delete.setOnClickListener {
                //수정중
                //Adapter
                //context ->
                val builder = AlertDialog.Builder(holder.itemView.context, R.style.MyAlertDialogStyle)
                val dlgView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.dialog_diary_list_delete, null)
                builder.setView(dlgView)

                dlgNew = builder.show()
                dlgNew.window.setBackgroundDrawableResource(R.drawable.round_layout_border)
                dlgNew.show()

                val display = WindowManager.LayoutParams()
                display.copyFrom(dlgNew.window.attributes)
                display.width = 1000
                display.height = 750

                val window = dlgNew.window
                window.attributes = display

                dlgView.txt_diary_list_yes.setOnClickListener {
                    Log.e("index:", dataList[holder.adapterPosition].diaryIdx.toString())
                    Log.e("adapter:", holder.adapterPosition.toString())

                    clickEvent(holder.adapterPosition)
                    notifyDataSetChanged()
                    dlgNew.dismiss()
                }

                dlgView.txt_diary_list_no.setOnClickListener {
                    dlgNew.dismiss()
                }

                notifyDataSetChanged()
            }
        } else {
            holder.itemView.lay1.visibility = View.INVISIBLE
        }
    }
    /*override fun onBindViewHolder(holder: Holder, position: Int) {
        //holder.day_num.text = dataList[position].date.substring(8, 10)
        //holder.day_text.text = dataList[position].date.substring(11, 14)
        //인터페이스
        Log.e("인터페이스1", dataList[position].date)
        Log.e("인터페이스2", getDay(dataList[position].date))
        Log.e("인터페이스3", getDayOfWeek(dataList[position].date))
        holder.day_num.text = getDay(dataList[position].date)
        holder.day_text.text = getDayOfWeek(dataList[position].date)
        holder.content.text = dataList[position].diary_content

        holder.container.setOnLongClickListener {
            isPressed = !isPressed
            notifyDataSetChanged()
            false
        }

        holder.content.setOnClickListener {
            //var dateText = dataList[position].date.substring(2, 4) + "." + dataList[position].date.substring(5, 7) + "." + dataList[position].date.substring(8, 10) + ". (" + dataList[position].date.substring(11, 14) + ")"
            //인터페이스
            var dateText = getDiaryDate(dataList[position].date)
            Intent(ctx, ReadDiaryActivity::class.java).apply {
                putExtra("from",300)
                putExtra("userIdx" ,7)
                putExtra("dateText",  dateText)
                putExtra("dateValue", dataList[position].date.substring(0, 10))
                ctx.startActivity(this)
            }
        }

        if (isPressed) {
            holder.lay1.visibility = View.VISIBLE

            holder.icn_delete.setOnClickListener {
                //수정중
                val builder = AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                val dlgView = LayoutInflater.from(context).inflate(R.layout.dialog_diary_list_delete, null)
                builder.setView(dlgView)

                dlgNew = builder.show()
                dlgNew.window.setBackgroundDrawableResource(R.drawable.round_layout_border)
                dlgNew.show()

                val display = WindowManager.LayoutParams()
                display.copyFrom(dlgNew.window.attributes)
                display.width = 1000
                display.height = 750

                val window = dlgNew.window
                window.attributes = display

                dlgView.txt_diary_list_yes.setOnClickListener {
                    if (isValid(TokenController.getAccessToken(ctx), dataList[position].date.substring(0, 10))) {
                        deleteDiaryListResponse(dataList[position].date.substring(0, 10), holder.adapterPosition)
                        dlgNew.dismiss()
                    }
                }

                dlgView.txt_diary_list_no.setOnClickListener {
                   dlgNew.dismiss()
                }

                /*var dlg = AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
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

                dlg.setNeutralButton("         네", dlg_listener)
                dlg.setPositiveButton("아니오     ", null)

                var dlgNew: AlertDialog = dlg.show()
                var messageText:TextView? = dlgNew.findViewById(android.R.id.message)
                messageText!!.gravity = Gravity.CENTER
                dlgNew.window.setBackgroundDrawableResource(R.drawable.round_layout_border)

                dlgNew.show()*/
            }
        } else {
            holder.lay1.visibility = View.INVISIBLE
        }
    }*/

    /*fun isValid(token: String, date: String): Boolean {
        if (token == "")
            Log.e("login fail", "login value null")

        else if (date == "")
            Log.e("date fail", "date value null")

        else return true

        return false
    }*/

    /*private fun deleteDiaryListResponse(deleteIndex: Int) {
        if (!TokenController.isValidToken(ctx)) {
            RenewAcessTokenController.postRenewAccessToken(ctx)
        }

        val deleteDiaryListResponse = networkService.deleteDiaryListResponse(
            TokenController.getAccessToken(ctx), deleteIndex
        )
        Log.e("diaryList_delete", "delete1")
        deleteDiaryListResponse.enqueue(object : Callback<DeleteDiaryListResponse> {
            override fun onFailure(call: Call<DeleteDiaryListResponse>, t: Throwable) {
                Log.e("일기 삭제 실패", t.toString())
            }

            override fun onResponse(call: Call<DeleteDiaryListResponse>, response: Response<DeleteDiaryListResponse>) {
                Log.e("diaryList_delete", "delete2")
                if (response.isSuccessful) {
                    Log.e("diaryList_delete", "delete3")
                    if (response.body()!!.status == 200) {
                        Log.e("diaryList", "일기 삭제 성공")

                        dataList.removeAt(deleteIndex)
                        notifyItemRemoved(deleteIndex)
                        notifyItemRangeChanged(deleteIndex, dataList.size)
                    }
                }
            }
        })
    }*/

    class Holder(private val clickEvent: (position: Int) -> Unit, parent: ViewGroup): RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item_diary_list, parent, false)) {
        init {
            //binding
            var lay1 = itemView.findViewById(R.id.lay1) as LinearLayout
            var icn_delete = itemView.findViewById(R.id.icn_delete) as ImageView
            var container = itemView.findViewById(R.id.ll_rv_item_diary_list_container) as LinearLayout
            var day_num = itemView.findViewById(R.id.txt_rv_item_diary_list_day_num) as TextView
            var day_text = itemView.findViewById(R.id.txt_rv_item_diary_list_day_text) as TextView
            var content = itemView.findViewById(R.id.txt_rv_item_diary_list_content) as TextView

            //var dl1 = itemView.findViewById(R.id.dl1) as LinearLayout
            //var dl2 = itemView.findViewById(R.id.dl2) as LinearLayout
            //var delete = itemView.findViewById(R.id.txt_diary_list_yes) as TextView?

            //click
            /*delete?.setOnClickListener {
                clickEvent(adapterPosition)
            }*/
        }
    }

    /*inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var lay1 = itemView.findViewById(R.id.lay1) as LinearLayout
        var icn_delete = itemView.findViewById(R.id.icn_delete) as ImageView
        var container = itemView.findViewById(R.id.ll_rv_item_diary_list_container) as LinearLayout
        var day_num = itemView.findViewById(R.id.txt_rv_item_diary_list_day_num) as TextView
        var day_text = itemView.findViewById(R.id.txt_rv_item_diary_list_day_text) as TextView
        var content = itemView.findViewById(R.id.txt_rv_item_diary_list_content) as TextView
    }*/
}