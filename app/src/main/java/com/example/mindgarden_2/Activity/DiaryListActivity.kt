package com.example.mindgarden_2.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.example.mindgarden_2.Data.DiaryListData
import com.example.mindgarden_2.Adapter.DiaryListRecyclerViewAdapter
import com.example.mindgarden_2.R
import kotlinx.android.synthetic.main.activity_diary_list.*
import kotlinx.android.synthetic.main.toolbar_diary_list.*
import java.util.*

class DiaryListActivity : AppCompatActivity() {
    lateinit var diaryListRecyclerViewAdapter: DiaryListRecyclerViewAdapter
    private var ascending = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_list)

        val cal = Calendar.getInstance()
        var year = cal.get(Calendar.YEAR).toString()
        var month = (cal.get(Calendar.MONTH) + 1).toString()

        txt_year.setText(year)
        if (month.toInt() < 10) {
          month = "0$month"
        }
        txt_month.setText(month)

        btn_left.setOnClickListener {
            if (month.toInt() == 1) {
                month = (month.toInt() + 11).toString()
                year = (year.toInt() - 1).toString()
                if (month.toInt() < 10) {
                    month = "0$month"
                }
                txt_year.setText(year)
                txt_month.setText(month)
            } else {
                month = (month.toInt() - 1).toString()
                if (month.toInt() < 10) {
                    month = "0$month"
                }
                txt_month.setText(month)
            }
        }

        btn_right.setOnClickListener {
            if (month.toInt() == 12) {
                month = (month.toInt() - 11).toString()
                year = (year.toInt() + 1).toString()
                if (month.toInt() < 10) {
                    month = "0$month"
                }
                txt_year.setText(year)
                txt_month.setText(month)
            } else {
                month = (month.toInt() + 1).toString()
                if (month.toInt() < 10) {
                    month = "0$month"
                }
                txt_month.setText(month)
            }
        }

        configureRecyclerView()
    }

    private fun configureRecyclerView() {
        var dataList: ArrayList<DiaryListData> = ArrayList()
        dataList.add(
            DiaryListData(
                0, 2019, 6, 25, "월",
                "hi"
            )
        )
        dataList.add(
            DiaryListData(
                1, 2019, 6, 18, "일",
                "good good good good good"
            )
        )
        dataList.add(
            DiaryListData(
                2, 2019, 6, 20, "목",
                "bad bad"
            )
        )

        dataList.sortBy { data -> data.day_num }

        icn_listing.setOnClickListener {
            if (ascending) {
                dataList.sortBy { data -> data.day_num }
                diaryListRecyclerViewAdapter.notifyDataSetChanged()
            } else {
                dataList.sortByDescending { data -> data.day_num }
                diaryListRecyclerViewAdapter.notifyDataSetChanged()
            }

            ascending = !ascending
        }

        /*icn_setting.setOnClickListener {
            val intent = Intent(this, MypageActivituy::class.java)
            startActivity(intent)
            환경설정 페이지로 넘어감
        }*/

        diaryListRecyclerViewAdapter = DiaryListRecyclerViewAdapter(this, dataList)
        rv_diary_list.adapter = diaryListRecyclerViewAdapter
        rv_diary_list.addItemDecoration(DividerItemDecoration(this, 1))
        rv_diary_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}
