package com.example.mindgarden.ui.main


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.util.SparseArray
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mindgarden.ui.inventory.InventoryActivity
import com.example.mindgarden.ui.mypage.MypageActivity
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.toolbar_diary_list.btn_left
import kotlinx.android.synthetic.main.toolbar_diary_list.btn_right
import kotlinx.android.synthetic.main.toolbar_main.*
import java.util.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.mindgarden.data.MindgardenRepository
import com.example.mindgarden.data.vo.GardenResponse
import com.example.mindgarden.db.RenewAcessTokenController
import com.example.mindgarden.db.TokenController
import com.example.mindgarden.ui.diary.DiaryDate
import com.example.mindgarden.ui.login.LoginActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.data_load_fail.*
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 *
 */
class MainFragment : Fragment(), DiaryDate, Tree {
    private val repository: MindgardenRepository by inject()

    val REQUEST_CODE_SET_TOOLBAR_DATE = 1005
    var toolbarYear: String = ""
    var toolbarMonth: String = ""

    val cal = Calendar.getInstance()
    var year = cal.get(Calendar.YEAR).toString()
    var month = (cal.get(Calendar.MONTH) + 1).toString()

    //var userIdx: Int = 0
    var treeNum = 0 //트리수
    //var balloon = 0 //나무 심기 여부

    private val treeArray = SparseArray<Bitmap>()
    lateinit var locationList: List<ImageView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //ImageResource setting
        getTreeArray(activity!!.applicationContext, treeArray)
        setLocation()

        //툴바 년,월 설정
        txt_main_year.setText(year)
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_main_month.setText(month)

        getData()

        popUp()

        cantBeFuture()

        btn_left.setOnClickListener {
            if (month.toInt() == 1) {
                leftYearChange()
                cantBeFuture()
            } else {
                leftMonthChange()
                cantBeFuture()
            }

            popUp()

            getData()
        }

        btn_right.setOnClickListener {
            if (month.toInt() == 12) {
                rightYearChange()
                cantBeFuture()
            } else {
                rightMonthChange()
                cantBeFuture()
            }

            popUp()

            getData()
        }

        btn_reward.setOnClickListener {
            var intent: Intent = Intent(context, InventoryActivity::class.java)
            startActivity(intent)
        }

        btn_main_setting.setOnClickListener {
            startActivity(Intent(activity!!.applicationContext, MypageActivity::class.java))
        }
    }

    //수정중
    //onStart()

    //수정중
    override fun onResume() {
        super.onResume()

        getData()
    }

    //수정중
    //onStop

    //액티비티 이동했다가 돌아오면 현재 년, 달로 바뀌어있음
    /*override fun onStop() {
        super.onStop()

        //현재 년 월 다시 셋팅
        year = cal.get(Calendar.YEAR).toString()
        month = (cal.get(Calendar.MONTH) + 1).toString()

        txt_main_year.setText(year)
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_main_month.setText(month)

        cantBeFuture()

        if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_main_year.text.toString() + "-" + txt_main_month.text.toString())) {
            loadData()
        }

        btn_reward.setOnClickListener {
            var intent: Intent = Intent(context, InventoryActivity::class.java)
            startActivity(intent)
        }
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SET_TOOLBAR_DATE) {
            if (resultCode == Activity.RESULT_OK) {
                year = data!!.getStringExtra("year")
                month = data!!.getStringExtra("month")

                txt_main_year.setText(year)
                if (month.toInt() < 10) {
                    month = "0$month"
                }
                txt_main_month.setText(month)

                getData()

                cantBeFuture()
            }
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

    //봐야 하는 부분
    fun postRenewAccessToken(ctx: Context){
        var jsonObject = JSONObject()
        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject
        Log.e("Renew token","in Renew access token")

        repository
            .postRenewAccessToken(TokenController.getRefreshToken(ctx),gsonObject,
                {
                    //리프레시 토큰 유효
                    if (it.status == 200) {
                        if (it.success) {
                            //엑세스 토큰 재발급
                            Log.e("Renew token", it.message)
                            val temp = it.data!![0].token
                            Log.e("Renew token: ", temp)

                            //재발급 받은 토큰을 저장
                            TokenController.setAccessToken(ctx, temp)
                            //재발급 받은 시간을 저장
                            TokenController.setStartTimeAccessToken(ctx, System.currentTimeMillis())
                        }//리프레시 토큰 만료
                        else{
                            //리프레시 토큰 값 지워주고
                            TokenController.clearRefreshToken(ctx)

                            //로그인 화면으로 이동
                            var loginIntent= Intent(ctx, LoginActivity::class.java)
                            loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            ContextCompat.startActivity(ctx, loginIntent, Bundle())
                        }
                    }
                },
                {
                    Log.e("Renew token failed", it)
                })
    }

    private fun loadData() {
        if (!TokenController.isValidToken(activity!!.applicationContext)) {
            RenewAcessTokenController.postRenewAccessToken(activity!!.applicationContext, repository)
        }

        val date = txt_main_year.text.toString() + "-" + txt_main_month.text.toString()

        repository
            .getGarden(TokenController.getAccessToken(activity!!.applicationContext), date,
                {
                    /*if (it.status == 200) {
                        Log.e("mainFragment load", it.message)
                    }else{
                        Log.e("mainFragment load", it.message)
                    }*/

                    hideErrorView()

                    when (it.status) {
                        200 -> {
                            initializeTree()

                            var mmonth = (cal.get(Calendar.MONTH) + 1).toString()
                            if (mmonth.toInt() < 10) {
                                mmonth = "0$mmonth"
                            }

                            if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == mmonth) {
                                txt_main_day_num.visibility = View.VISIBLE
                                txt_main_day_num_word.visibility = View.VISIBLE
                                txt_main_day_text.visibility = View.VISIBLE

                                var date = SimpleDateFormat("dd")
                                var intDate = SimpleDateFormat("u")
                                var date2: String = ""
                                when (intDate.format(Date()).toInt()) {
                                    1->date2="Mon"
                                    2->date2="Tue"
                                    3->date2="Wed"
                                    4->date2="Thu"
                                    5->date2="Fri"
                                    6->date2="Sat"
                                    7->date2="Sun"
                                }

                                txt_main_day_num.setText(date.format(Date()).toString())
                                txt_main_day_text.setText(date2)

                                btn_reward.visibility = View.VISIBLE

                                Log.e("balloon", it.data?.get(0)?.balloon.toString())
                                Log.e("size", it.data?.size.toString())

                                if (it.data?.get(0)?.balloon == 1) {
                                    img_balloon.visibility = View.VISIBLE
                                    btn_reward.setImageResource(R.drawable.btn_plus_redbdg)
                                } else {
                                    img_balloon.visibility = View.INVISIBLE
                                    btn_reward.setImageResource(R.drawable.btn_reward)
                                }
                            } else {
                                txt_main_day_num.visibility = View.INVISIBLE
                                txt_main_day_num_word.visibility = View.INVISIBLE
                                txt_main_day_text.visibility = View.INVISIBLE

                                img_balloon.visibility = View.INVISIBLE
                                btn_reward.visibility = View.INVISIBLE
                            }

                            for (i in 0..(it.data!!.size - 1)) {
                                var treeIdx = 0
                                var location = 0

                                treeIdx = it.data[i].treeIdx
                                location = it.data[i].location

                                //잡초만 있을 경우
                                if (it.data[i].treeIdx == 16) {
                                    locationList.get(location - 1).setImageBitmap(drawableToBitmap(activity!!.applicationContext, R.drawable.android_weeds))
                                } else {
                                    locationList.get(location - 1).setImageBitmap(treeArray.get(treeIdx))
                                }

                                //문구 설정
                                treeNum = it.data[i].treeNum
                                Log.e("treeNum", treeNum.toString())

                                //현재달이고, 심은 나무가 없을 경우(초기상태) -> 정원을 꾸며보아요 문구
                                if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == mmonth) {
                                    if (treeNum < 1) {
                                        txt_main_exp1.setText(getString(R.string.treeNumTextCurrent0))
                                        txt_main_exp1.visibility = View.VISIBLE
                                    } else if (treeNum < 11) {
                                        txt_main_exp1.setText(getString(R.string.treeNumTextCurrent10))
                                        txt_main_exp1.visibility = View.VISIBLE
                                    } else if (treeNum < 21) {
                                        txt_main_exp1.setText(getString(R.string.treeNumTextCurrent20))
                                        txt_main_exp1.visibility = View.VISIBLE
                                    } else {
                                        txt_main_exp1.setText(getString(R.string.treeNumTextCurrent21))
                                        txt_main_exp1.visibility = View.VISIBLE
                                    }
                                } else {
                                    if (treeNum < 1) {
                                        txt_main_exp1.setText(getString(R.string.treeNumText0))
                                        txt_main_exp1.visibility = View.VISIBLE
                                    } else if (treeNum < 11) {
                                        val text = treeNum.toString() + getString( R.string.treeNumText10)
                                        txt_main_exp1.setText(text)
                                        txt_main_exp1.visibility = View.VISIBLE
                                    } else if (treeNum < 21) {
                                        val text = treeNum.toString() + getString(R.string.treeNumText20)
                                        txt_main_exp1.setText(text)
                                        txt_main_exp1.visibility = View.VISIBLE
                                    } else {
                                        val text = treeNum.toString() + getString(R.string.treeNumText21)
                                        txt_main_exp1.setText(text)
                                        txt_main_exp1.visibility = View.VISIBLE
                                    }
                                }
                            }
                        }
                        else -> Log.e("mainFragment", it.message)
                    }

                    /*initializeTree()

                    var mmonth = (cal.get(Calendar.MONTH) + 1).toString()
                    if (mmonth.toInt() < 10) {
                        mmonth = "0$mmonth"
                    }

                    if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == mmonth) {
                        txt_main_day_num.visibility = View.VISIBLE
                        txt_main_day_num_word.visibility = View.VISIBLE
                        txt_main_day_text.visibility = View.VISIBLE

                        var date = SimpleDateFormat("dd")
                        var intDate = SimpleDateFormat("u")
                        var date2: String = ""
                        when (intDate.format(Date()).toInt()) {
                            1->date2="Mon"
                            2->date2="Tue"
                            3->date2="Wed"
                            4->date2="Thu"
                            5->date2="Fri"
                            6->date2="Sat"
                            7->date2="Sun"
                        }

                        txt_main_day_num.setText(date.format(Date()).toString())
                        txt_main_day_text.setText(date2)

                        btn_reward.visibility = View.VISIBLE

                        Log.e("balloon", it.data?.get(0)?.balloon.toString())
                        Log.e("size", it.data?.size.toString())

                        if (it.data?.get(0)?.balloon == 1) {
                            img_balloon.visibility = View.VISIBLE
                            btn_reward.setImageResource(R.drawable.btn_plus_redbdg)
                        } else {
                            img_balloon.visibility = View.INVISIBLE
                            btn_reward.setImageResource(R.drawable.btn_reward)
                        }
                    } else {
                        txt_main_day_num.visibility = View.INVISIBLE
                        txt_main_day_num_word.visibility = View.INVISIBLE
                        txt_main_day_text.visibility = View.INVISIBLE

                        img_balloon.visibility = View.INVISIBLE
                        btn_reward.visibility = View.INVISIBLE
                    }

                    for (i in 0..(it.data!!.size - 1)) {
                        var treeIdx = 0
                        var location = 0

                        treeIdx = it.data[i].treeIdx
                        location = it.data[i].location

                        //잡초만 있을 경우
                        if (it.data[i].treeIdx == 16) {
                            locationList.get(location - 1).setImageBitmap(drawableToBitmap(activity!!.applicationContext, R.drawable.android_weeds))
                        } else {
                            locationList.get(location - 1).setImageBitmap(treeArray.get(treeIdx))
                        }

                        //문구 설정
                        treeNum = it.data[i].treeNum
                        Log.e("treeNum", treeNum.toString())

                        //현재달이고, 심은 나무가 없을 경우(초기상태) -> 정원을 꾸며보아요 문구
                        if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == mmonth) {
                            if (treeNum < 1) {
                                txt_main_exp1.setText(getString(R.string.treeNumTextCurrent0))
                                txt_main_exp1.visibility = View.VISIBLE
                            } else if (treeNum < 11) {
                                txt_main_exp1.setText(getString(R.string.treeNumTextCurrent10))
                                txt_main_exp1.visibility = View.VISIBLE
                            } else if (treeNum < 21) {
                                txt_main_exp1.setText(getString(R.string.treeNumTextCurrent20))
                                txt_main_exp1.visibility = View.VISIBLE
                            } else {
                                txt_main_exp1.setText(getString(R.string.treeNumTextCurrent21))
                                txt_main_exp1.visibility = View.VISIBLE
                            }
                        } else {
                            if (treeNum < 1) {
                                txt_main_exp1.setText(getString(R.string.treeNumText0))
                                txt_main_exp1.visibility = View.VISIBLE
                            } else if (treeNum < 11) {
                                val text = treeNum.toString() + getString( R.string.treeNumText10)
                                txt_main_exp1.setText(text)
                                txt_main_exp1.visibility = View.VISIBLE
                            } else if (treeNum < 21) {
                                val text = treeNum.toString() + getString(R.string.treeNumText20)
                                txt_main_exp1.setText(text)
                                txt_main_exp1.visibility = View.VISIBLE
                            } else {
                                val text = treeNum.toString() + getString(R.string.treeNumText21)
                                txt_main_exp1.setText(text)
                                txt_main_exp1.visibility = View.VISIBLE
                            }
                        }
                    }*/
                },
                {
                    showErrorView()
                    btnRetryDataLoad()
                })
    }

    private fun popUp() {
        toolbarYear = txt_main_year.text.toString()
        toolbarMonth = txt_main_month.text.toString()

        ll_date_toolbar_main.setOnClickListener {
            Intent(activity!!.applicationContext, MainCalendarActivity::class.java).apply {
                putExtra("year", toolbarYear)
                putExtra("month", toolbarMonth)
                startActivityForResult(this, REQUEST_CODE_SET_TOOLBAR_DATE)
            }
        }
    }

    private fun getData() {
        if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_main_year.text.toString() + "-" + txt_main_month.text.toString())) {
            loadData()
        }
    }

    private fun leftYearChange() {
        month = (month.toInt() + 11).toString()
        year = (year.toInt() - 1).toString()
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_main_year.setText(year)
        txt_main_month.setText(month)
    }

    private fun leftMonthChange() {
        month = (month.toInt() - 1).toString()
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_main_month.setText(month)
    }

    private fun rightYearChange() {
        month = (month.toInt() - 11).toString()
        year = (year.toInt() + 1).toString()
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_main_year.setText(year)
        txt_main_month.setText(month)
    }

    private fun rightMonthChange() {
        month = (month.toInt() + 1).toString()
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_main_month.setText(month)
    }

    fun cantBeFuture() {
        //미래로 못가게
        var mmonth = (cal.get(Calendar.MONTH) + 1).toString()
        if (mmonth.toInt() < 10) {
            mmonth = "0$mmonth"
        }

        if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == mmonth) {
            btn_right.isEnabled = false
        } else {
            btn_right.isEnabled = true
        }
    }

    private fun initializeTree() {
        val initTree = activity?.applicationContext?.let { drawableToBitmap(it, R.drawable.tree_size) }
        for (i in 0..31) locationList.get(i).setImageBitmap(initTree)
    }

    fun setLocation() {
        locationList = listOf(img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11,
            img12, img13, img14, img15, img16, img17, img18, img19, img20, img21_weed, img22,
            img23, img24, img25, img26, img27, img28, img29, img30_weed, img31, img32)

    }

    /*private fun setLocation() {
        for (i in 0..31) {
            locationArray.append(i, cl_fragment_main.findViewWithTag("area$i"))
        }
    }*/

    private fun showErrorView() {
        dataLoadFailMain.visibility = View.VISIBLE
    }

    private fun hideErrorView() {
        dataLoadFailMain.visibility = View.GONE
    }

    private fun btnRetryDataLoad() {
        btnRetryDataLoadFail.setOnClickListener {
            loadData()
        }
    }
}