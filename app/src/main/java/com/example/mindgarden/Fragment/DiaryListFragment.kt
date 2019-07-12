package com.example.mindgarden.Fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mindgarden.Activity.MypageActivity
import com.example.mindgarden.Adapter.DiaryListRecyclerViewAdapter
import com.example.mindgarden.DB.SharedPreferenceController
import com.example.mindgarden.Data.DiaryListData

import com.example.mindgarden.R
import kotlinx.android.synthetic.main.fragment_diary_list.*
import kotlinx.android.synthetic.main.toolbar_diary_list.*
import org.jetbrains.anko.support.v4.startActivity
import java.util.*
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.GET.GetDiaryListResponse
import com.example.mindgarden.Network.NetworkService
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class DiaryListFragment : Fragment() {
    val networkService: NetworkService by lazy{
        ApplicationController.instance.networkService
    }
    lateinit var diaryListRecyclerViewAdapter: DiaryListRecyclerViewAdapter
    private var ascending = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diary_list, container, false)
    }

    override fun onResume() {
        super.onResume()

        if (isValid(SharedPreferenceController.getUserID(ctx), txt_year.text.toString() + "-" + txt_month.text.toString())) {
            getDiaryListResponse()
        }
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

            if (isValid(SharedPreferenceController.getUserID(ctx), txt_year.text.toString() + "-" + txt_month.text.toString())) {
                getDiaryListResponse()
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

            if (isValid(SharedPreferenceController.getUserID(ctx), txt_year.text.toString() + "-" + txt_month.text.toString())) {
                getDiaryListResponse()
            }
        }

        configureRecyclerView()
    }

    private fun configureRecyclerView() {
        var dataList: ArrayList<DiaryListData> = ArrayList()

        dataList.sortBy { data ->
            data.date.substring(8, 10).toInt() }

        btn_updown.setOnClickListener {
            if (ascending) {
                diaryListRecyclerViewAdapter.dataList.sortBy { data ->  data.date.substring(8, 10).toInt() }
                diaryListRecyclerViewAdapter.notifyDataSetChanged()
            } else {
                diaryListRecyclerViewAdapter.dataList.sortByDescending { data ->  data.date.substring(8, 10).toInt() }
                diaryListRecyclerViewAdapter.notifyDataSetChanged()
            }

            ascending = !ascending
        }

        btn_setting.setOnClickListener {
            startActivity<MypageActivity>()
        }

        diaryListRecyclerViewAdapter = DiaryListRecyclerViewAdapter(context!!, dataList)
        rv_diary_list.adapter = diaryListRecyclerViewAdapter
        rv_diary_list.addItemDecoration(DividerItemDecoration(context!!, 1))
        rv_diary_list.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)

        if (isValid(SharedPreferenceController.getUserID(ctx), txt_year.text.toString() + "-" + txt_month.text.toString())) {
            getDiaryListResponse()
        }
    }

    fun isValid(userIdx: Int, date: String): Boolean {
        if(userIdx.toString() == "")
            toast("로그인하세요")

        else if(date == "")
            toast("보고 싶은 달을 선택하세요")

        else return true

        return false
    }

    private fun getDiaryListResponse(){
        val getDiaryListResponse = networkService.getDiaryListResponse(
            "application/json", SharedPreferenceController.getUserID(ctx), txt_year.text.toString() + "-" + txt_month.text.toString())
        getDiaryListResponse.enqueue(object: Callback<GetDiaryListResponse> {
            override fun onFailure(call: Call<GetDiaryListResponse>, t: Throwable) {
                Log.e("garden select fail", t.toString())
            }

            override fun onResponse(call: Call<GetDiaryListResponse>, response: Response<GetDiaryListResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        val tmp: ArrayList<DiaryListData> = response.body()!!.data!!
                        diaryListRecyclerViewAdapter.dataList = tmp
                        diaryListRecyclerViewAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}
