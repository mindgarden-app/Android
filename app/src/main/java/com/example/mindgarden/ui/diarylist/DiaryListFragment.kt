package com.example.mindgarden.ui.diarylist


import android.content.Context
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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mindgarden.ui.mypage.MypageActivity
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.fragment_diary_list.*
import kotlinx.android.synthetic.main.toolbar_diary_list.*
import java.util.*
import com.example.mindgarden.data.MindgardenRepository
import com.example.mindgarden.data.vo.DiaryListResponse.*
import com.example.mindgarden.db.RenewAcessTokenController
import com.example.mindgarden.db.TokenController
import com.example.mindgarden.ui.diary.DiaryDate
import com.example.mindgarden.ui.login.LoginActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import org.koin.android.ext.android.inject
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
    private val repository : MindgardenRepository by inject()

    //lateinit var diaryListRecyclerViewAdapter: DiaryListRecyclerViewAdapter
    //Adapter
    val diaryListRecyclerViewAdapter: DiaryListRecyclerViewAdapter by lazy {
        DiaryListRecyclerViewAdapter { clickEventCallback(it) }
    }
    private var ascending = true

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
            loadData()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        txt_year.setText(year)
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_month.setText(month)

        btn_left.setOnClickListener {
            if (month.toInt() == 1) {
                leftYearChange()
            } else {
                leftMonthChange()
            }

            if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_year.text.toString() + "-" + txt_month.text.toString())) {
                loadData()
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
                loadData()
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

    private fun rightYearChange() {
        month = (month.toInt() - 11).toString()
        year = (year.toInt() + 1).toString()
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_year.setText(year)
        txt_month.setText(month)
    }

    private fun rightMonthChange() {
        month = (month.toInt() + 1).toString()
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_month.setText(month)
    }

    private fun configureRecyclerView() {
        var dataList: ArrayList<DiaryListData> = ArrayList()

        //정렬
        /*dataList.sortByDescending { data ->
            data.date.substring(8, 10).toInt() }*/
        //dataList.sortedWith(compareByDescending<DiaryListData> { getDay(it.date) }.thenByDescending { getTime(it.date) })
        dataList.sortByDescending { data -> data.date }

        btn_updown.setOnClickListener {
            if (ascending) {
                //diaryListRecyclerViewAdapter.dataList.sortBy { data ->  data.date.substring(8, 10).toInt() }
                //diaryListRecyclerViewAdapter.dataList.sortBy { getDay(it.date) }
                diaryListRecyclerViewAdapter.dataList.sortBy { data -> data.date }
                diaryListRecyclerViewAdapter.notifyDataSetChanged()
            } else {
                //정렬
                //diaryListRecyclerViewAdapter.dataList.sortByDescending { data ->  data.date.substring(8, 10).toInt() }
                //diaryListRecyclerViewAdapter.dataList.sortedWith(compareByDescending<DiaryListData> { getDay(it.date) }.thenByDescending { getTime(it.date) })
                diaryListRecyclerViewAdapter.dataList.sortByDescending { data -> data.date }
                diaryListRecyclerViewAdapter.notifyDataSetChanged()
            }

            ascending = !ascending
        }

        btn_setting.setOnClickListener {
            startActivity(Intent(activity!!.applicationContext, MypageActivity::class.java))
        }

        rv_diary_list.adapter = diaryListRecyclerViewAdapter
        rv_diary_list.addItemDecoration(DividerItemDecoration(context!!, 1))
        rv_diary_list.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)

        if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_year.text.toString() + "-" + txt_month.text.toString())) {
           loadData()
        }
    }

    fun isValid(accessToken: String, date: String): Boolean {
        val toast: Toast = Toast(activity!!.applicationContext)
        val inflater: LayoutInflater = LayoutInflater.from(activity!!.applicationContext)
        val toastView: View = inflater.inflate(R.layout.toast, null)
        val toastText: TextView = toastView.findViewById(R.id.toastText)

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

    private fun loadData(){
        if (!TokenController.isValidToken(activity!!.applicationContext)) {
            RenewAcessTokenController.postRenewAccessToken(activity!!.applicationContext,repository)
        }
        repository
            .getDiaryList(
                TokenController.getAccessToken(activity!!.applicationContext),
                txt_year.text.toString() + "-" + txt_month.text.toString(),
                { response ->
                    Log.e("diaryList", "일기 조회 성공")

                    val tmp: ArrayList<DiaryListData> = response.data!!

                    if (tmp.isEmpty()) {
                        ll_list_zero.visibility = View.VISIBLE
                    } else {
                        ll_list_zero.visibility = View.GONE

                        diaryListRecyclerViewAdapter.dataList = tmp
                        diaryListRecyclerViewAdapter.dataList.sortByDescending { data: DiaryListData ->  data.date.substring(8, 10).toInt() }
                        diaryListRecyclerViewAdapter.notifyDataSetChanged()
                    }
                },
                {})
    }

    private fun clickEventCallback(position: Int) {
        if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_year.text.toString() + "-" + txt_month.text.toString())) {
            deleteDiary(diaryListRecyclerViewAdapter.dataList[position].diaryIdx)
        }
    }

    private fun deleteDiary(diaryIdx: Int){
        if (!TokenController.isValidToken(activity!!.applicationContext)) {
            RenewAcessTokenController.postRenewAccessToken(activity!!.applicationContext,repository)
        }
        repository
            .deleteDiaryList(TokenController.getAccessToken(activity!!.applicationContext), diaryIdx,
                {
                    val iterator: MutableIterator<DiaryListData> = diaryListRecyclerViewAdapter.dataList.iterator()

                    while (iterator.hasNext()) {
                        if (iterator.next().diaryIdx == diaryIdx) {
                            iterator.remove()
                            diaryListRecyclerViewAdapter.notifyDataSetChanged()
                        }
                    }

                    Log.e("diaryList", "일기 삭제 성공")
                },
                {
                    //에러처리
                })
    }
}