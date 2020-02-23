package com.example.mindgarden.ui.main


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
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
import com.example.mindgarden.DB.TokenController
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.GET.GetMainResponse
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.DB.RenewAcessTokenController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 *
 */
class MainFragment : androidx.fragment.app.Fragment() {
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    val REQUEST_CODE_SET_TOOLBAR_DATE = 1005
    var toolbarYear : String = ""
    var toolbarMonth : String = ""
    var year : String =""
    var month : String = ""
    val cal = Calendar.getInstance()
    var userIdx : Int = 0
    var treeNum = 0 //트리수
    var balloon = 0 //나무 심기 여부
    var check = 0   //일기 작성 여부
    var dataPasser : OnDataPass? = null


    lateinit var treeList : List<Bitmap>
    lateinit var locationList : List<ImageView>

    interface OnDataPass {
        fun checkPass(bal: Int)
    }

//    override fun onAttach(context: Context?) {
//        super.onAttach(context)
//        dataPasser = context as OnDataPass
//    }

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //ImageResource setting
        setTree()
        setLocation()

        //현재 년,월로 setting
        year = cal.get(Calendar.YEAR).toString()
        month = (cal.get(Calendar.MONTH) + 1).toString()

        //툴바 년,월 설정
        txt_main_year.setText(year)
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_main_month.setText(month)

        canBeFuture()

        if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_main_year.text.toString() + "-" + txt_main_month.text.toString())) {
            getMainResponse()
        }

        btn_reward.setOnClickListener {
            var intent: Intent = Intent(context, InventoryActivity::class.java)
            //intent.putExtra("balloon", balloon)
            //intent.putExtra("check", check)
            startActivity(intent)
        }

        //환경설정 페이지로 넘어감
        btn_main_setting.setOnClickListener {
            startActivity(Intent(activity!!.applicationContext, MypageActivity::class.java))
        }

        //툴바 년/월 설정(MainCalendar로 전달)
        toolbarYear = txt_main_year.text.toString()
        toolbarMonth = txt_main_month.text.toString()

        //툴바 날짜 클릭했을 때 -> 팝업 띄우기
        ll_date_toolbar_main.setOnClickListener {
            Intent(activity!!.applicationContext, MainCalendarActivity::class.java).apply {
                putExtra("year", toolbarYear)
                putExtra("month" , toolbarMonth)
                startActivityForResult(this,REQUEST_CODE_SET_TOOLBAR_DATE)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_main_year.text.toString() + "-" + txt_main_month.text.toString())) {
            getMainResponse()
        }

        //툴바 년/월 설정(MainCalendar로 전달)
        toolbarYear = txt_main_year.text.toString()
        toolbarMonth = txt_main_month.text.toString()

        canBeFuture()

        btn_left.setOnClickListener {
            canBeFuture()

            //1월로 갔을때 년도 바뀜
            if (month.toInt() == 1) {
                month = (month.toInt() + 11).toString() //1->12월로 가도록
                year = (year.toInt() - 1).toString()    //12월로 가면 년도 바뀜
                if (month.toInt() < 10) {   //한자리수면 0붙여주기
                    month = "0$month"
                }
                txt_main_year.setText(year)
                txt_main_month.setText(month)

                //글씨 안보이게
                txt_main_day_num_word.visibility = View.INVISIBLE
                txt_main_day_num.visibility = View.INVISIBLE
                txt_main_day_text.visibility = View.INVISIBLE

                //이거 왜 두번 반복해줘야 하는지?
                if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_main_year.text.toString() + "-" + txt_main_month.text.toString())) {
                    getMainResponse()
                }

                canBeFuture()

                btn_reward.setOnClickListener {
                    var intent: Intent = Intent(context, InventoryActivity::class.java)
                    startActivity(intent)
                }

                //툴바 년/월 설정(MainCalendar로 전달)
                toolbarYear = txt_main_year.text.toString()
                toolbarMonth = txt_main_month.text.toString()

            } else {
                month = (month.toInt() - 1).toString()
                if (month.toInt() < 10) {
                    month = "0$month"
                }
                txt_main_month.setText(month)

                if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_main_year.text.toString() + "-" + txt_main_month.text.toString())) {
                    getMainResponse()
                }

                btn_reward.setOnClickListener {
                    var intent: Intent = Intent(context, InventoryActivity::class.java)
                    startActivity(intent)
                }

                //툴바 월 설정(MainCalendar로 전달)
                toolbarMonth = txt_main_month.text.toString()

                canBeFuture()
            }
        }

        btn_right.setOnClickListener {
            canBeFuture()

            if (month.toInt() == 12) {
                month = (month.toInt() - 11).toString()
                year = (year.toInt() + 1).toString()
                if (month.toInt() < 10) {
                    month = "0$month"
                }
                txt_main_year.setText(year)
                txt_main_month.setText(month)

                if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_main_year.text.toString() + "-" + txt_main_month.text.toString())) {
                    getMainResponse()
                }

                canBeFuture()

                btn_reward.setOnClickListener {
                    var intent: Intent = Intent(context, InventoryActivity::class.java)
                    startActivity(intent)
                }

                //툴바 년/월 설정(MainCalendar로 전달)
                toolbarYear = txt_main_year.text.toString()
                toolbarMonth = txt_main_month.text.toString()
            } else {
                month = (month.toInt() + 1).toString()
                if (month.toInt() < 10) {
                    month = "0$month"
                }
                txt_main_month.setText(month)

                if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_main_year.text.toString() + "-" + txt_main_month.text.toString())) {
                    getMainResponse()
                }

                canBeFuture()

                btn_reward.setOnClickListener {
                    var intent: Intent = Intent(context, InventoryActivity::class.java)
                    startActivity(intent)
                }

                //툴바 월 설정(MainCalendar로 전달)
                toolbarMonth = txt_main_month.text.toString()
            }

            //툴바 날짜 클릭했을 때 -> 팝업 띄우기
            ll_date_toolbar_main.setOnClickListener {
                Intent(activity!!.applicationContext,MainCalendarActivity::class.java).apply {
                    putExtra("year", toolbarYear)
                    putExtra("month", toolbarMonth)
                    startActivityForResult(this,REQUEST_CODE_SET_TOOLBAR_DATE)
                }
            }
        }
    }

    //액티비티 이동했다가 돌아오면 현재 년, 달로 바뀌어있음
    override fun onStop() {
        super.onStop()

        //현재 년 월 다시 셋팅
        year = cal.get(Calendar.YEAR).toString()
        month = (cal.get(Calendar.MONTH) + 1).toString()

        txt_main_year.setText(year)
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_main_month.setText(month)

        canBeFuture()

        if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_main_year.text.toString() + "-" + txt_main_month.text.toString())) {
            getMainResponse()
        }

        btn_reward.setOnClickListener {
            var intent: Intent = Intent(context, InventoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE_SET_TOOLBAR_DATE) {
            if(resultCode == Activity.RESULT_OK) {
                year = data!!.getStringExtra("year")
                month = data!!.getStringExtra("month")

                if (month.toInt() < 10) {
                    month = "0$month"
                }
                txt_main_month.setText(month)
                txt_main_year.setText(year)

                canBeFuture()

                if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_main_year.text.toString() + "-" + txt_main_month.text.toString())) {
                    getMainResponse()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_main_year.text.toString() + "-" + txt_main_month.text.toString())) {
            getMainResponse()
        }

        //툴바 년/월 설정(MainCalendar로 전달)
        toolbarYear = txt_main_year.text.toString()
        toolbarMonth = txt_main_month.text.toString()

        canBeFuture()

        btn_left.setOnClickListener {
            canBeFuture()

            if (month.toInt() == 1) {
                month = (month.toInt() + 11).toString()
                year = (year.toInt() - 1).toString()
                if (month.toInt() < 10) {
                    month = "0$month"
                }
                txt_main_year.setText(year)
                txt_main_month.setText(month)

                txt_main_day_num_word.visibility = View.INVISIBLE
                txt_main_day_num.visibility = View.INVISIBLE
                txt_main_day_text.visibility = View.INVISIBLE

                if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_main_year.text.toString() + "-" + txt_main_month.text.toString())) {
                    getMainResponse()
                }

                canBeFuture()

                //툴바 년/월 설정(MainCalendar로 전달)
                toolbarYear = txt_main_year.text.toString()
                toolbarMonth = txt_main_month.text.toString()

                btn_reward.setOnClickListener {
                    var intent: Intent = Intent(context, InventoryActivity::class.java)
                    startActivity(intent)
                }
            } else {
                month = (month.toInt() - 1).toString()
                if (month.toInt() < 10) {
                    month = "0$month"
                }
                txt_main_month.setText(month)

                if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_main_year.text.toString() + "-" + txt_main_month.text.toString())) {
                    getMainResponse()
                }

                btn_reward.setOnClickListener {
                    var intent: Intent = Intent(context, InventoryActivity::class.java)
                    startActivity(intent)
                }

                //툴바 월 설정(MainCalendar로 전달)
                toolbarMonth = txt_main_month.text.toString()

                canBeFuture()
            }

            btn_right.setOnClickListener {
                canBeFuture()

                if (month.toInt() == 12) {
                    month = (month.toInt() - 11).toString()
                    year = (year.toInt() + 1).toString()
                    if (month.toInt() < 10) {
                        month = "0$month"
                    }
                    txt_main_year.setText(year)
                    txt_main_month.setText(month)

                    if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_main_year.text.toString() + "-" + txt_main_month.text.toString())) {
                        getMainResponse()
                    }

                    canBeFuture()

                    btn_reward.setOnClickListener {
                        var intent: Intent = Intent(context, InventoryActivity::class.java)
                        startActivity(intent)
                    }

                    //툴바 년/월 설정(MainCalendar로 전달)
                    toolbarYear = txt_main_year.text.toString()
                    toolbarMonth = txt_main_month.text.toString()
                } else {
                    month = (month.toInt() + 1).toString()
                    if (month.toInt() < 10) {
                        month = "0$month"
                    }
                    txt_main_month.setText(month)

                    if (isValid(TokenController.getAccessToken(activity!!.applicationContext), txt_main_year.text.toString() + "-" + txt_main_month.text.toString())) {
                        getMainResponse()
                    }

                    //툴바 월 설정(MainCalendar로 전달)
                    toolbarMonth = txt_main_month.text.toString()

                    btn_reward.setOnClickListener {
                        var intent: Intent = Intent(context, InventoryActivity::class.java)
                        startActivity(intent)
                    }

                    canBeFuture()

                    //툴바 날짜 클릭했을 때 -> 팝업 띄우기
                    ll_date_toolbar_main.setOnClickListener {
                        Intent(activity!!.applicationContext,MainCalendarActivity::class.java).apply {
                            putExtra("year", toolbarYear)
                            putExtra("month" , toolbarMonth)
                            startActivityForResult(this,REQUEST_CODE_SET_TOOLBAR_DATE )
                        }
                    }
                }
            }
        }
    }

    fun isValid(accessToken: String, date: String): Boolean {
        val toast: Toast = Toast(activity!!.applicationContext)
        val inflater: LayoutInflater = LayoutInflater.from(activity!!.applicationContext)
        val toastView: View = inflater.inflate(R.layout.toast, null)
        val toastText: TextView = toastView.findViewById(R.id.toastText)

        if(accessToken.toString() == "") {
            toastText.setText("로그인하세요")
            toastText.gravity = Gravity.CENTER
            toast.view = toastView
            toast.show()
        }

        else if(date == "") {
            toastText.setText("보고 싶은 달을 선택하세요")
            toastText.gravity = Gravity.CENTER
            toast.view = toastView
            toast.show()
        }

        else return true

        return false
    }

    private fun getMainResponse() {
        if(!TokenController.isValidToken(activity!!.applicationContext)){
            RenewAcessTokenController.postRenewAccessToken(activity!!.applicationContext)
        }

        val getMainResponse = networkService.getMainResponse(
            TokenController.getAccessToken(activity!!.applicationContext), txt_main_year.text.toString() + "-" + txt_main_month.text.toString())
        Log.e("year" , txt_main_year.text.toString())
        Log.e("month", txt_main_month.text.toString())
        getMainResponse.enqueue(object: Callback<GetMainResponse> {
            override fun onFailure(call: Call<GetMainResponse>, t: Throwable) {
                Log.e("garden select fail", t.toString())
            }

            override fun onResponse(call: Call<GetMainResponse>, response: Response<GetMainResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        initializeTree()

                        balloon = response.body()!!.data!![0].balloon
                        Log.e("balloon", "" + balloon);
                        check = response.body()!!.data!![0].check
                        Log.e("check", "" + check)
                        dataPasser?.checkPass(check)

                        var mmonth = (cal.get(Calendar.MONTH) + 1).toString()
                        if (mmonth.toInt() < 10) {
                            mmonth = "0$mmonth"
                        }

                        if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == mmonth) {
                            btn_reward.visibility = View.VISIBLE

                            if (balloon == 1) {
                                img_balloon.visibility = View.VISIBLE
                                btn_reward.setImageResource(R.drawable.btn_plus_redbdg)
                                Log.e("balloon", balloon.toString())
                                Log.e("img_ballon_visibility", img_balloon.visibility.toString())
                            } else {
                                img_balloon.visibility = View.INVISIBLE
                                btn_reward.setImageResource(R.drawable.btn_reward)
                            }
                        } else {
                            img_balloon.visibility=View.INVISIBLE
                            btn_reward.visibility = View.INVISIBLE
                        }

                        for(i in 0..(response.body()!!.data!!.size - 1)) {
                            var treeIdx = 0
                            var location = 0

                            treeIdx = response.body()!!.data!![i].treeIdx
                            location = response.body()!!.data!![i].location

                            //잡초만 있을 경우
                            if (response.body()!!.data!![i].treeIdx == 16) {
                                locationList.get(location - 1).setImageBitmap(drawableToBitmap(R.drawable.android_weeds))
                            } else {
                                locationList.get(location - 1).setImageBitmap(treeList.get(treeIdx))
                            }

                            //요일 설정
                            if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == mmonth) {
                                txt_main_day_num_word.visibility = View.VISIBLE
                                txt_main_day_num.visibility = View.VISIBLE
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
                            } else {
                                txt_main_day_num_word.visibility = View.INVISIBLE
                                txt_main_day_num.visibility = View.INVISIBLE
                                txt_main_day_text.visibility = View.INVISIBLE
                            }

                            //문구 설정
                            treeNum = response.body()!!.data!![i].treeNum
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
                }
            }
        })
    }

    fun canBeFuture() {
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

    fun initializeTree() {
        val initTree = drawableToBitmap(R.drawable.tree_size)
        for(i in 0..31) locationList.get(i).setImageBitmap(initTree)
    }

    fun setLocation() {
        locationList = listOf(img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11,
            img12, img13, img14, img15, img16, img17, img18, img19, img20, img21_weed, img22,
            img23, img24, img25, img26, img27, img28, img29, img30_weed, img31, img32)

    }

    fun setTree() {
        val tree1 = drawableToBitmap(R.drawable.android_tree1)
        val tree2 = drawableToBitmap(R.drawable.android_tree2)
        val tree3 = drawableToBitmap(R.drawable.android_tree3)
        val tree4 = drawableToBitmap(R.drawable.android_tree4)
        val tree5 = drawableToBitmap(R.drawable.android_tree5)
        val tree6 = drawableToBitmap(R.drawable.android_tree6)
        val tree7 = drawableToBitmap(R.drawable.android_tree7)
        val tree8 = drawableToBitmap(R.drawable.android_tree8)
        val tree9 = drawableToBitmap(R.drawable.android_tree9)
        val tree10 = drawableToBitmap(R.drawable.android_tree10)
        val tree11 = drawableToBitmap(R.drawable.android_tree11)
        val tree12 = drawableToBitmap(R.drawable.android_tree12)
        val tree13 = drawableToBitmap(R.drawable.android_tree13)
        val tree14 = drawableToBitmap(R.drawable.android_tree14)
        val tree15 = drawableToBitmap(R.drawable.android_tree15)
        val tree16 = drawableToBitmap(R.drawable.android_tree16)

        treeList = listOf(tree1,tree2,tree3,tree4, tree5,tree6,tree7,tree8,tree9,
            tree10,tree11,tree12,tree13,tree14, tree15,tree16)
    }

    private fun drawableToBitmap(icnName: Int): Bitmap {
        val drawable = resources.getDrawable(icnName) as BitmapDrawable
        val bitmap = drawable.bitmap
        return bitmap
    }
}