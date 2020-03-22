package com.example.mindgarden.ui.diarylist

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.provider.FontsContract
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.mindgarden.ui.diary.ReadDiaryActivity
import com.example.mindgarden.R
import com.example.mindgarden.data.vo.DiaryListResponse.*
import com.example.mindgarden.ui.diary.DiaryDate
import com.example.mindgarden.ui.diary.ReadDiaryActivity.Companion.DIARY_IDX
import kotlinx.android.synthetic.main.dialog_diary_list_delete.view.*
import kotlinx.android.synthetic.main.rv_item_diary_list.view.*
import kotlin.collections.ArrayList

class DiaryListRecyclerViewAdapter(private val clickEvent: (position: Int) -> Unit): RecyclerView.Adapter<DiaryListRecyclerViewAdapter.Holder>(), DiaryDate {
    var dataList = ArrayList<DiaryListData>()
    var isPressed = false

    lateinit var dlgNew : AlertDialog

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Holder = Holder(viewGroup)

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = dataList.size

    fun getDataAt(position: Int) = dataList[position]

    fun setData(newData: ArrayList<DiaryListData>) {
        newData?.let {
            dataList.clear()
            dataList.addAll(newData)
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        //binding
        holder.itemView.txt_rv_item_diary_list_day_num.text = getDay(dataList[position].date)
        holder.itemView.txt_rv_item_diary_list_day_text.text = getDayOfWeek(dataList[position].date)
        holder.itemView.txt_rv_item_diary_list_content.text = dataList[position].diary_content

        var weatherIdx: Int = dataList[position].weatherIdx
        when (weatherIdx) {
            0 -> holder.itemView.diary_list_weather_img.setImageResource(R.drawable.icn_weather_list_ver_01)
            1 -> holder.itemView.diary_list_weather_img.setImageResource(R.drawable.icn_weather_list_ver_02)
            2 -> holder.itemView.diary_list_weather_img.setImageResource(R.drawable.icn_weather_list_ver_03)
            3 -> holder.itemView.diary_list_weather_img.setImageResource(R.drawable.icn_weather_list_ver_04)
            4 -> holder.itemView.diary_list_weather_img.setImageResource(R.drawable.icn_weather_list_ver_05)
            5 -> holder.itemView.diary_list_weather_img.setImageResource(R.drawable.icn_weather_list_ver_06)
            6 -> holder.itemView.diary_list_weather_img.setImageResource(R.drawable.icn_weather_list_ver_07)
            7 -> holder.itemView.diary_list_weather_img.setImageResource(R.drawable.icn_weather_list_ver_08)
            8 -> holder.itemView.diary_list_weather_img.setImageResource(R.drawable.icn_weather_list_ver_09)
            9 -> holder.itemView.diary_list_weather_img.setImageResource(R.drawable.icn_weather_list_ver_10)
            10 -> holder.itemView.diary_list_weather_img.setImageResource(R.drawable.icn_weather_list_ver_11)
        }

        holder.itemView.ll_rv_item_diary_list_container.setOnLongClickListener {
            isPressed = !isPressed
            notifyDataSetChanged()
            false
        }

        holder.itemView.ll_diary_list_content.setOnClickListener {
            Intent(holder.itemView.context, ReadDiaryActivity::class.java).apply {
                putExtra(DIARY_IDX, dataList[position].diaryIdx)
                holder.itemView.context.startActivity(this)
            }
        }

        if (isPressed) {
            holder.itemView.lay1.visibility = View.VISIBLE

            holder.itemView.icn_delete.setOnClickListener {
                var builder = AlertDialog.Builder(holder.itemView.context, R.style.MyAlertDialogStyle)
                builder.setTitle("삭제").setMessage("삭제하시겠습니까?")
                    .setNegativeButton("네") {
                        dlgInterface: DialogInterface?, which: Int ->
                        clickEvent(holder.adapterPosition)
                        notifyDataSetChanged()
                        null
                    }
                    .setPositiveButton("아니오", null)

                var dlg: AlertDialog = builder.show()

                var messageText: TextView? = dlg.findViewById(android.R.id.message)
                messageText!!.gravity = Gravity.CENTER
                messageText!!.typeface = ResourcesCompat.getFont(holder.itemView.context, R.font.notosanscjkr_regular)
                messageText!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)

                dlg.window.setBackgroundDrawableResource(R.drawable.round_layout_border)

                dlg.show()

                val btnNegative = dlg.getButton(AlertDialog.BUTTON_NEGATIVE);
                val btnPositive = dlg.getButton(AlertDialog.BUTTON_POSITIVE);

                btnNegative.gravity = Gravity.CENTER
                btnNegative.typeface = ResourcesCompat.getFont(holder.itemView.context, R.font.notosanscjkr_medium)
                btnNegative.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)

                btnPositive.gravity = Gravity.CENTER
                btnPositive.typeface = ResourcesCompat.getFont(holder.itemView.context, R.font.notosanscjkr_medium)
                btnPositive.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)

                val layoutParams : LinearLayout.LayoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
                layoutParams.weight = 10f;
                btnNegative.setLayoutParams(layoutParams);
                btnPositive.setLayoutParams(layoutParams);

                /*val builder = AlertDialog.Builder(holder.itemView.context, R.style.NewDialogStyle)
                val dlgView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.dialog_diary_list_delete, null)
                builder.setView(dlgView)

                dlgNew = builder.show()

                dlgView.txt_diary_list_yes.setOnClickListener {
                    Log.e("diary_index:", dataList[holder.adapterPosition].diaryIdx.toString())
                    Log.e("adapter:", holder.adapterPosition.toString())

                    clickEvent(holder.adapterPosition)
                    notifyDataSetChanged()
                    dlgNew.dismiss()
                }

                dlgView.txt_diary_list_no.setOnClickListener {
                    Log.e("diary_index:", dataList[holder.adapterPosition].diaryIdx.toString())
                    Log.e("adapter:", holder.adapterPosition.toString())

                    dlgNew.dismiss()
                }

                notifyDataSetChanged()*/
            }
        } else {
            holder.itemView.lay1.visibility = View.GONE
        }
    }

    class Holder(viewGroup: ViewGroup): RecyclerView.ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.rv_item_diary_list, viewGroup, false))
}