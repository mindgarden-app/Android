package com.example.mindgarden.ui.diarylist


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mindgarden.ui.mypage.MypageActivity
import com.example.mindgarden.DB.TokenController
import com.example.mindgarden.Data.DiaryListData

import com.example.mindgarden.R
import kotlinx.android.synthetic.main.fragment_diary_list.*
import kotlinx.android.synthetic.main.toolbar_diary_list.*
import java.util.*
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.GET.GetDiaryListResponse
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.DB.RenewAcessTokenController
import com.example.mindgarden.Network.Delete.DeleteDiaryListResponse
import com.example.mindgarden.ui.diary.DiaryDate
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
class DiaryListFragment : androidx.fragment.app.Fragment(), DiaryDate {
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }
    //lateinit var diaryListRecyclerViewAdapter : DiaryListRecyclerViewAdapter
    //Adapter
    val diaryListRecyclerViewAdapter: DiaryListRecyclerViewAdapter by lazy {
        DiaryListRecyclerViewAdapter { clickEventCallback(it) }
    }
    private var ascending = true

    //수정중
    val cal = Calendar.getInstance()
    var year = cal.get(Calendar.YEAR).toString()
    var month = (cal.get(Calendar.MONTH) + 1).toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diary_list, container, false)
    }

    override fun onResume() {
        super.onResume()

        if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_year.text.toString() + "-" + txt_month.text.toString())) {
            getDiaryListResponse()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //수정중
        /*val cal = Calendar.getInstance()
        var year = cal.get(Calendar.YEAR).toString()
        var month = (cal.get(Calendar.MONTH) + 1).toString()*/

        txt_year.setText(year)
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_month.setText(month)

        //수정중
        btn_left.setOnClickListener {
            if (month.toInt() == 1) {
                leftYearChange()
            } else {
                leftMonthChange()
            }

            if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_year.text.toString() + "-" + txt_month.text.toString())) {
                getDiaryListResponse()
            }
        }

        //수정중
        btn_right.setOnClickListener {
            if (month.toInt() == 12) {
                rightYearChange()
            } else {
                rightMonthChange()
            }

            if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_year.text.toString() + "-" + txt_month.text.toString())) {
                getDiaryListResponse()
            }
        }

        configureRecyclerView()
    }

    //수정중
    private fun leftYearChange() {
        month = (month.toInt() + 11).toString()
        year = (year.toInt() - 1).toString()
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_year.setText(year)
        txt_month.setText(month)
    }

    //수정중
    private fun leftMonthChange() {
        month = (month.toInt() - 1).toString()
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_month.setText(month)
    }

    //수정중
    private fun rightYearChange() {
        month = (month.toInt() - 11).toString()
        year = (year.toInt() + 1).toString()
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_year.setText(year)
        txt_month.setText(month)
    }

    //수정중
    private fun rightMonthChange() {
        month = (month.toInt() + 1).toString()
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_month.setText(month)
    }

    private fun configureRecyclerView() {
        var dataList: ArrayList<DiaryListData> = ArrayList()

        dataList.sortByDescending { data ->
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
            startActivity(Intent(activity!!.applicationContext, MypageActivity::class.java))
        }

        //diaryListRecyclerViewAdapter = DiaryListRecyclerViewAdapter(context!!, dataList)
        //Adapter
        rv_diary_list.adapter = diaryListRecyclerViewAdapter
        rv_diary_list.addItemDecoration(DividerItemDecoration(context!!, 1))
        //수정중
        //LinearLayoutManager.VERTICAL ->
        rv_diary_list.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)

        if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_year.text.toString() + "-" + txt_month.text.toString())) {
            getDiaryListResponse()
        }
    }

    fun isValid(accessToken: String, date: String): Boolean {
        val toast: Toast = Toast(activity!!.applicationContext)
        val inflater: LayoutInflater = LayoutInflater.from(activity!!.applicationContext)
        val toastView: View = inflater.inflate(R.layout.toast, null)
        val toastText: TextView = toastView.findViewById(R.id.toastText)

        //수정
        //.toString() 삭제
        if (accessToken == "") {
            toastText.setText("로그인하세요")
            toastText.gravity = Gravity.CENTER
            toast.view = toastView
            toast.show()
        }

        else if (date == "") {
            toastText.setText("보고 싶은 달을 선택하세요")
            toastText.gravity = Gravity.CENTER
            toast.view = toastView
            toast.show()
        }

        else return true

        return false
    }

    private fun getDiaryListResponse() {
        if (!TokenController.isValidToken(activity!!.applicationContext)) {
            RenewAcessTokenController.postRenewAccessToken(activity!!.applicationContext)
        }

        val getDiaryListResponse = networkService.getDiaryListResponse(
            TokenController.getAccessToken(activity!!.applicationContext), txt_year.text.toString() + "-" + txt_month.text.toString())
        getDiaryListResponse.enqueue(object: Callback<GetDiaryListResponse> {
            override fun onFailure(call: Call<GetDiaryListResponse>, t: Throwable) {
                Log.e("garden select fail", t.toString())
            }

            override fun onResponse(call: Call<GetDiaryListResponse>, response: Response<GetDiaryListResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        Log.e("diaryList", "일기 조회 성공")

                        val tmp: ArrayList<DiaryListData> = response.body()!!.data!!

                        if (tmp.isEmpty()) {
                            ll_list_zero.visibility = View.VISIBLE
                        } else {
                            ll_list_zero.visibility = View.GONE

                            diaryListRecyclerViewAdapter.dataList = tmp
                            diaryListRecyclerViewAdapter.dataList.sortByDescending { data ->  data.date.substring(8, 10).toInt() }
                            diaryListRecyclerViewAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })
    }

    fun clickEventCallback(position: Int) {
        if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_year.text.toString() + "-" + txt_month.text.toString())) {
            deleteDiaryListResponse(position)
        }
    }

    fun deleteDiaryListResponse(diaryIdx: Int) {
        if (!TokenController.isValidToken(activity!!.applicationContext)) {
            RenewAcessTokenController.postRenewAccessToken(activity!!.applicationContext)
        }

        val deleteDiaryListResponse = networkService.deleteDiaryListResponse(
            TokenController.getAccessToken(activity!!.applicationContext), diaryIdx
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

                        diaryListRecyclerViewAdapter.dataList.removeAt(diaryIdx)
                        diaryListRecyclerViewAdapter.notifyItemRemoved(diaryIdx)
                        diaryListRecyclerViewAdapter.notifyItemRangeChanged(diaryIdx, diaryListRecyclerViewAdapter.dataList.size)

                        Log.e("diaryList", "일기 삭제 성공")
                    }
                }
            }
        })
    }
}
