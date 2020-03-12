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
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.fragment_diary_list.*
import kotlinx.android.synthetic.main.toolbar_diary_list.*
import java.util.*
import com.example.mindgarden.data.MindgardenRepository
import com.example.mindgarden.data.vo.DiaryListResponse.*
import com.example.mindgarden.db.RenewAcessTokenController
import com.example.mindgarden.db.TokenController
import com.example.mindgarden.ui.diary.DiaryDate
import kotlinx.android.synthetic.main.data_load_fail.*
import org.koin.android.ext.android.inject
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

        getData()
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

            getData()
        }

        btn_right.setOnClickListener {
            if (month.toInt() == 12) {
                rightYearChange()
            } else {
                rightMonthChange()
            }

            getData()
        }

        configureRecyclerView()
    }

    private fun leftYearChange() {
        month = (month.toInt() + 11).toString()
        year = (year.toInt() - 1).toString()
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_year.setText(year)
        txt_month.setText(month)
    }

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

        dataList.sortByDescending { data -> data.date }

        btn_updown.setOnClickListener {
            if (ascending) {
                diaryListRecyclerViewAdapter.dataList.sortBy { data -> data.date }
                diaryListRecyclerViewAdapter.notifyDataSetChanged()
            } else {
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

        getData()
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

    private fun loadData() {
        if (!TokenController.isValidToken(activity!!.applicationContext)) {
            RenewAcessTokenController.postRenewAccessToken(activity!!.applicationContext, repository)
        }
        repository
            .getDiaryList(
                TokenController.getAccessToken(activity!!.applicationContext),
                txt_year.text.toString() + "-" + txt_month.text.toString(),
                { response ->
                    hideErrorView()

                    when (response.status) {
                        200 -> {
                            val tmp: ArrayList<DiaryListData> = response.data!!

                            if (tmp.isEmpty()) {
                                ll_list_zero.visibility = View.VISIBLE
                            } else {
                                ll_list_zero.visibility = View.GONE

                                diaryListRecyclerViewAdapter.setData(tmp)
                                //diaryListRecyclerViewAdapter.dataList = tmp
                                diaryListRecyclerViewAdapter.dataList.sortByDescending { data -> data.date }
                                diaryListRecyclerViewAdapter.notifyDataSetChanged()
                            }
                        }
                        else -> Log.e("diaryList", response.message)
                    }
                },
                {
                    showErrorView()
                    btnRetryDataLoad()
                })
    }

    private fun clickEventCallback(position: Int) {
        if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_year.text.toString() + "-" + txt_month.text.toString())) {
            deleteDiary(diaryListRecyclerViewAdapter.dataList[position].diaryIdx)
        }
    }

    private fun deleteDiary(diaryIdx: Int) {
        if (!TokenController.isValidToken(activity!!.applicationContext)) {
            RenewAcessTokenController.postRenewAccessToken(activity!!.applicationContext, repository)
        }
        repository
            .deleteDiaryList(TokenController.getAccessToken(activity!!.applicationContext), diaryIdx,
                {
                    hideErrorView()

                    when (it.status) {
                        200 -> {
                            val iterator: MutableIterator<DiaryListData> = diaryListRecyclerViewAdapter.dataList.iterator()

                            while (iterator.hasNext()) {
                                if (iterator.next().diaryIdx == diaryIdx) {
                                    iterator.remove()
                                    diaryListRecyclerViewAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                        else -> Log.e("diaryList", it.message)
                    }
                },
                {
                    showErrorView()
                    btnRetryDataLoad()
                })
    }

    private fun getData() {
        if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_year.text.toString() + "-" + txt_month.text.toString())) {
            loadData()
        }
    }

   private fun showErrorView() {
        dataLoadFailDiaryList.visibility = View.VISIBLE
    }

    private fun hideErrorView() {
        dataLoadFailDiaryList.visibility = View.GONE
    }

    private fun btnRetryDataLoad() {
        btnRetryDataLoadFail.setOnClickListener {
            loadData()
        }
    }
}