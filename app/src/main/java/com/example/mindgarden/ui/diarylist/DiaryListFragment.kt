package com.example.mindgarden.ui.diarylist


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
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
import com.example.mindgarden.db.TokenController
import com.example.mindgarden.ui.MyObserver
import com.example.mindgarden.ui.diary.DiaryDate
import com.example.mindgarden.ui.diary.WriteDiaryActivity
import com.example.mindgarden.ui.main.MainCalendarActivity
import kotlinx.android.synthetic.main.layout_data_load_fail.*
import kotlinx.android.synthetic.main.toolbar_diary_list.btn_left
import kotlinx.android.synthetic.main.toolbar_diary_list.btn_right
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class DiaryListFragment : androidx.fragment.app.Fragment(), DiaryDate, MyObserver {
    private val repository : MindgardenRepository by inject()
    val diaryListRecyclerViewAdapter: DiaryListRecyclerViewAdapter by lazy {
        DiaryListRecyclerViewAdapter { clickEventCallback(it) }
    }
    private var ascending = true

    var cal = Calendar.getInstance()

    val TOOLBAR_DATE_REQUEST_CODE = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diary_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    override fun onAppForegrounded() {
        init()
    }
    
    override fun onResume() {
        super.onResume()
        if(WriteDiaryActivity.CHECK){
            init()
        }
    }

    private fun init() {
        configureRecyclerView()
        initToolbarTextCurrent()
        diaryListFragmentClick()
        getData()
    }

    private fun configureRecyclerView() {
        var dataList: ArrayList<DiaryListData> = ArrayList()

        dataList.sortByDescending { data -> data.date }

        rv_diary_list.adapter = diaryListRecyclerViewAdapter
        rv_diary_list.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
    }

    private fun diaryListFragmentClick() {
        btnToolbarClick()
        btnUpdownClick()
        btnSettingClick()
    }

    private fun btnUpdownClick() {
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
    }

    private fun btnSettingClick() {
        btn_setting.setOnClickListener {
            startActivity(Intent(activity!!.applicationContext, MypageActivity::class.java))
        }
    }

    private fun initToolbarTextCurrent() {
        txt_date_toolbar_diary_list.text = getToolbarDate(Calendar.getInstance())
        //cal = Calendar.getInstance()
    }

    private fun isCurrentToolbarDate(): Boolean {
        return txt_date_toolbar_diary_list.text == getToolbarDate(Calendar.getInstance(Locale.KOREA))
    }

    private fun btnToolbarClick() {
        btn_left.setOnClickListener {
            txt_date_toolbar_diary_list.text = setDateMoveControl(1)
            getData()
        }

        btn_right.setOnClickListener {
            txt_date_toolbar_diary_list.text = setDateMoveControl(0)
            getData()
        }

        txt_date_toolbar_diary_list.setOnClickListener {
            Intent(activity!!.applicationContext, MainCalendarActivity::class.java).apply {
                putExtra("toolbarDate", txt_date_toolbar_diary_list.text)
                startActivityForResult(this, TOOLBAR_DATE_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TOOLBAR_DATE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    cal.set(Calendar.MONTH, data.getIntExtra("month", - 1))
                    cal.set(Calendar.YEAR, data.getIntExtra("year", - 1))
                    txt_date_toolbar_diary_list.text = getToolbarDate(cal)
                    getData()
                } else {
                    Log.e("diaryList", "onActivityResult Failed")
                }
            }
        }
    }

    private fun setDateMoveControl(i: Int): String {
        when (i) {
            0 -> {
                //cal.add(Calendar.MONTH, 1)
                if (!isCurrentToolbarDate()) {
                    cal.add(Calendar.MONTH, 1)
                }
            }
            1 -> {
                val mindgardenStartDate = Calendar.getInstance()
                mindgardenStartDate.set(2019, 6, 31)
                if (txt_date_toolbar_diary_list.text != getToolbarDate(mindgardenStartDate)) cal.add(Calendar.MONTH, -1)
            }
        }

        return getToolbarDate(cal)
    }

    private fun getToolbarDate(calendar: Calendar): String {
        val f = SimpleDateFormat("yyyy년 MM월", Locale.getDefault())
        return f.format(calendar.time)
    }

    private fun getServerDate(calendar: Calendar): String {
        val f = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        return f.format(calendar.time)
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
        TokenController.isValidToken(activity!!.applicationContext, repository)

        val date = getServerDate(cal)
        Log.e("diaryList date_loadData():", date)

        repository
            .getDiaryList(
                TokenController.getAccessToken(activity!!.applicationContext), date,
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
        val date = getServerDate(cal)

        if (isValid(TokenController.getAccessToken(activity!!.applicationContext), date)) {
            deleteDiary(diaryListRecyclerViewAdapter.dataList[position].diaryIdx)
        }
    }

    private fun deleteDiary(diaryIdx: Int) {
        TokenController.isValidToken(activity!!.applicationContext, repository)

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
        val date = getServerDate(cal)

        if (isValid(TokenController.getAccessToken(activity!!.applicationContext), date)) {
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