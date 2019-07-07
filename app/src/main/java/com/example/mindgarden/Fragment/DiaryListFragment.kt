package com.example.mindgarden.Fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mindgarden.Activity.MypageActivity
import com.example.mindgarden.Adapter.DiaryListRecyclerViewAdapter
import com.example.mindgarden.Data.DiaryListData

import com.example.mindgarden.R
import kotlinx.android.synthetic.main.fragment_diary_list.*
import kotlinx.android.synthetic.main.toolbar_diary_list.*
import org.jetbrains.anko.support.v4.startActivity
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class DiaryListFragment : Fragment() {
    lateinit var diaryListRecyclerViewAdapter: DiaryListRecyclerViewAdapter
    private var ascending = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diary_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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

        btn_updown.setOnClickListener {
            if (ascending) {
                dataList.sortBy { data -> data.day_num }
                diaryListRecyclerViewAdapter.notifyDataSetChanged()
            } else {
                dataList.sortByDescending { data -> data.day_num }
                diaryListRecyclerViewAdapter.notifyDataSetChanged()
            }

            ascending = !ascending
        }

        btn_setting.setOnClickListener {
            startActivity<MypageActivity>()
           // 환경설정 페이지로 넘어감
        }

        diaryListRecyclerViewAdapter = DiaryListRecyclerViewAdapter(context!!, dataList)
        rv_diary_list.adapter = diaryListRecyclerViewAdapter
        rv_diary_list.addItemDecoration(DividerItemDecoration(context!!, 1))
        rv_diary_list.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
    }
}
