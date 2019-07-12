package com.example.mindgarden.Fragment


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mindgarden.Activity.InventoryActivity
import com.example.mindgarden.Activity.MainCalendarActivity
import com.example.mindgarden.Activity.MypageActivity
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.toolbar_diary_list.btn_left
import kotlinx.android.synthetic.main.toolbar_diary_list.btn_right
import kotlinx.android.synthetic.main.toolbar_main.*
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.startActivityForResult
import java.util.*
import android.widget.ImageView
import com.example.mindgarden.DB.SharedPreferenceController
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.GET.GetMainResponse
import com.example.mindgarden.Network.NetworkService
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 *
 */
class MainFragment : Fragment() {
    val networkService: NetworkService by lazy{
        ApplicationController.instance.networkService
    }
    var mainList: ArrayList<com.example.mindgarden.Data.MainData> = ArrayList()
    lateinit var inventoryActivity: InventoryActivity

    val REQUEST_CODE_SET_TOOLBAR_DATE = 1005
    var toolbarYear : String = ""
    var toolbarMonth : String = ""
    var year : String =""
    var month : String = ""
    val cal = Calendar.getInstance()
    var userIdx : Int = 0
    var dayOfWeek = ""      //요일
    var day = ""            //날짜
    var treeNum = 0 //트리수
    var writeDiary = false //일기 작성 가능 여부

    lateinit var treeList : List<Bitmap>
    lateinit var locationList : List<ImageView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setTree()
        setLocation()

        year = cal.get(Calendar.YEAR).toString()
        month = (cal.get(Calendar.MONTH) + 1).toString()

        //텍스트뷰 일수
        txt_main_day_num.setText(cal.get(Calendar.DAY_OF_MONTH).toString())

        txt_main_year.setText(year)
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_main_month.setText(month)
        if (isValid(
                SharedPreferenceController.getUserID(ctx),
                txt_main_year.text.toString() + "-" + txt_main_month.text.toString()
            )
        ) {
            getMainResponse()
        }



        if (btn_reward.isEnabled) {
            btn_reward.setOnClickListener {
                var intent: Intent = Intent(context, InventoryActivity::class.java)
                startActivity(intent)
            }
        }


        btn_main_setting.setOnClickListener {
            startActivity<MypageActivity>()
            // 환경설정 페이지로 넘어감
        }


        //툴바 년/월 설정(MainCalendar로 전달)
        toolbarYear = txt_main_year.text.toString()
        toolbarMonth = txt_main_month.text.toString()

        //툴바 날짜 클릭했을 때 -> 팝업 띄우기
        ll_date_toolbar_main.setOnClickListener {
            startActivityForResult<MainCalendarActivity>(
                REQUEST_CODE_SET_TOOLBAR_DATE, "year" to toolbarYear, "month" to toolbarMonth)
        }
    }

    override fun onStart() {
        super.onStart()

        if (isValid(
                SharedPreferenceController.getUserID(ctx),
                txt_main_year.text.toString() + "-" + txt_main_month.text.toString()
            )
        ) {
            getMainResponse()
        }

        //툴바 년/월 설정(MainCalendar로 전달)
        toolbarYear = txt_main_year.text.toString()
        toolbarMonth = txt_main_month.text.toString()
        var mmonth = (cal.get(Calendar.MONTH)).toString()
        if (mmonth.toInt() < 10) {
            mmonth = "0$mmonth"
        }
        if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == mmonth) {
            btn_right.isEnabled = false
        } else {
            btn_right.isEnabled = true
        }

        btn_left.setOnClickListener {
            //미래로 못가게
            var mmonth = (cal.get(Calendar.MONTH)).toString()
            if (mmonth.toInt() < 10) {
                mmonth = "0$mmonth"
            }

            if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == mmonth) {
                btn_right.isEnabled = false
            } else {
                btn_right.isEnabled = true
            }

            //1월로 갔을때 년도 바뀜
            if (month.toInt() == 1) {
                month = (month.toInt() + 11).toString() //1->12월로 가도록
                year = (year.toInt() - 1).toString()    //12월로 가면 년도 바뀜
                if (month.toInt() < 10) {   //한자리수면 0붙여주기
                    month = "0$month"
                }
                txt_main_year.setText(year)
                txt_main_month.setText(month)
                txt_main_day_num_word.visibility = View.INVISIBLE
                txt_main_day_num.visibility = View.INVISIBLE
                txt_main_day_text.visibility = View.INVISIBLE

                if (isValid(
                        SharedPreferenceController.getUserID(ctx),
                        txt_main_year.text.toString() + "-" + txt_main_month.text.toString()
                    )
                ) {
                    getMainResponse()
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

                if (isValid(
                        SharedPreferenceController.getUserID(ctx),
                        txt_main_year.text.toString() + "-" + txt_main_month.text.toString()
                    )
                ) {
                    getMainResponse()
                }

                if (btn_reward.isEnabled) {
                    btn_reward.setOnClickListener {
                        var intent: Intent = Intent(context, InventoryActivity::class.java)
                        startActivity(intent)

                    }
                }
                //툴바 월 설정(MainCalendar로 전달)
                toolbarMonth = txt_main_month.text.toString()
            }
        }

        btn_right.setOnClickListener {
            //미래로 못가게
            var mmonth = (cal.get(Calendar.MONTH)).toString()
            if (mmonth.toInt() < 10) {
                mmonth = "0$mmonth"
            }

            if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == mmonth) {
                btn_right.isEnabled = false
            } else {
                btn_right.isEnabled = true
            }

            if (month.toInt() == 12) {
                month = (month.toInt() - 11).toString()
                year = (year.toInt() + 1).toString()
                if (month.toInt() < 10) {
                    month = "0$month"
                }
                txt_main_year.setText(year)
                txt_main_month.setText(month)
                if (isValid(
                        SharedPreferenceController.getUserID(ctx),
                        txt_main_year.text.toString() + "-" + txt_main_month.text.toString()
                    )
                ) {
                    getMainResponse()
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
                if (isValid(
                        SharedPreferenceController.getUserID(ctx),
                        txt_main_year.text.toString() + "-" + txt_main_month.text.toString()
                    )
                ) {
                    getMainResponse()
                }


                //툴바 월 설정(MainCalendar로 전달)
                toolbarMonth = txt_main_month.text.toString()
            }

            //툴바 날짜 클릭했을 때 -> 팝업 띄우기
            ll_date_toolbar_main.setOnClickListener {
                startActivityForResult<MainCalendarActivity>(
                    REQUEST_CODE_SET_TOOLBAR_DATE, "year" to toolbarYear, "month" to toolbarMonth
                )
            }

        }
    }
    //액티비티 이동했다가 돌아오면 현재 년, 달로 바뀌어있음
    override fun onStop() {
        super.onStop()

        if (isValid(
                SharedPreferenceController.getUserID(ctx),
                txt_main_year.text.toString() + "-" + txt_main_month.text.toString()
            )
        ) {
            getMainResponse()
        }

        //현재 년 월 다시 셋팅
        year = cal.get(Calendar.YEAR).toString()
        month = (cal.get(Calendar.MONTH) + 1).toString()

        txt_main_year.setText(year)
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_main_month.setText(month)

        if (isValid(
                SharedPreferenceController.getUserID(ctx),
                txt_main_year.text.toString() + "-" + txt_main_month.text.toString()
            )
        ) {
            getMainResponse()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_SET_TOOLBAR_DATE){
            if(resultCode == Activity.RESULT_OK){
                year = data!!.getStringExtra("year")
                month = data!!.getStringExtra("month")

                if (month.toInt() < 10) {
                    month = "0$month"
                }
                txt_main_month.setText(month)
                txt_main_year.setText(year)

                if (isValid(
                        SharedPreferenceController.getUserID(ctx),
                        txt_main_year.text.toString() + "-" + txt_main_month.text.toString()
                    )
                ) {
                    getMainResponse()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (isValid(
                SharedPreferenceController.getUserID(ctx),
                txt_main_year.text.toString() + "-" + txt_main_month.text.toString()
            )
        ) {
            getMainResponse()
        }

        //툴바 년/월 설정(MainCalendar로 전달)
        toolbarYear = txt_main_year.text.toString()
        toolbarMonth = txt_main_month.text.toString()

        btn_left.setOnClickListener {
            //미래로 못가게
            var mmonth = (cal.get(Calendar.MONTH)).toString()
            if (mmonth.toInt() < 10) {
                mmonth = "0$mmonth"
            }

            if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == mmonth) {
                btn_right.isEnabled = false
            } else {
                btn_right.isEnabled = true
            }

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

                if (isValid(
                        SharedPreferenceController.getUserID(ctx),
                        txt_main_year.text.toString() + "-" + txt_main_month.text.toString()
                    )
                ) {
                    getMainResponse()
                }

                //툴바 년/월 설정(MainCalendar로 전달)
                toolbarYear = txt_main_year.text.toString()
                toolbarMonth = txt_main_month.text.toString()

                //현재 년,월 (숫자만) , 년도가 현재인지 월이 현재 달인지
                if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == "0" + (cal.get(
                        Calendar.MONTH
                    ) + 1).toString()
                ) {
                    btn_reward.isEnabled = true
                } else {
                    btn_reward.isEnabled = false
                }
            } else {
                month = (month.toInt() - 1).toString()
                if (month.toInt() < 10) {
                    month = "0$month"
                }
                txt_main_month.setText(month)

                if (isValid(
                        SharedPreferenceController.getUserID(ctx),
                        txt_main_year.text.toString() + "-" + txt_main_month.text.toString()
                    )
                ) {
                    getMainResponse()
                }

                if (btn_reward.isEnabled) {
                    btn_reward.setOnClickListener {
                        var intent: Intent = Intent(context, InventoryActivity::class.java)
                        startActivity(intent)

                    }
                }

                //툴바 월 설정(MainCalendar로 전달)
                toolbarMonth = txt_main_month.text.toString()
            }

            btn_right.setOnClickListener {
                //미래로 못가게
                var mmonth = (cal.get(Calendar.MONTH)).toString()
                if (mmonth.toInt() < 10) {
                    mmonth = "0$mmonth"
                }

                if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == mmonth) {
                    btn_right.isEnabled = false
                } else {
                    btn_right.isEnabled = true
                }

                if (month.toInt() == 12) {
                    month = (month.toInt() - 11).toString()
                    year = (year.toInt() + 1).toString()
                    if (month.toInt() < 10) {
                        month = "0$month"
                    }
                    txt_main_year.setText(year)
                    txt_main_month.setText(month)

                    if (isValid(
                            SharedPreferenceController.getUserID(ctx),
                            txt_main_year.text.toString() + "-" + txt_main_month.text.toString()
                        )
                    ) {
                        getMainResponse()
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

                    if (isValid(
                            SharedPreferenceController.getUserID(ctx),
                            txt_main_year.text.toString() + "-" + txt_main_month.text.toString()
                        )
                    ) {
                        getMainResponse()
                    }

                    //툴바 월 설정(MainCalendar로 전달)
                    toolbarMonth = txt_main_month.text.toString()

                    //툴바 날짜 클릭했을 때 -> 팝업 띄우기
                    ll_date_toolbar_main.setOnClickListener {
                        startActivityForResult<MainCalendarActivity>(
                            REQUEST_CODE_SET_TOOLBAR_DATE, "year" to toolbarYear, "month" to toolbarMonth
                        )
                    }
                }
            }

        }
    }

    fun isValid(userIdx: Int, date: String): Boolean {
        if (userIdx.toString() == "")
            toast("로그인하세요")
        else if (date == "")
            toast("보고 싶은 달을 선택하세요")
        else return true

        return false
    }

    private fun getMainResponse(){
        val getMainResponse = networkService.getMainResponse(
            "application/json", SharedPreferenceController.getUserID(ctx), txt_main_year.text.toString() + "-" + txt_main_month.text.toString())
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

                        val balloon = response.body()!!.data!![0].balloon

                        if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == "0" + (cal.get(Calendar.MONTH) + 1).toString()) {
                            if(balloon==1) {
                                btn_reward.isEnabled = true
                                img_balloon.visibility=View.VISIBLE
                            }
                            else btn_reward.isEnabled=false
                            img_balloon.visibility=View.INVISIBLE
                        }
                        else {
                            //btn_reward.isEnabled = false
                            //img_balloon.visibility=View.INVISIBLE
                            if(balloon==1){
                                btn_reward.isEnabled=true
                                img_balloon.visibility=View.VISIBLE
                                writeDiary = false

                            }else
                            {
                                btn_reward.isEnabled = false
                                writeDiary = true
                                img_balloon.visibility=View.INVISIBLE
                            }
                        }

                        for(i in 0..(response.body()!!.data!!.size-1)) {
                            var treeIdx = 0
                            var location = 0

                            treeIdx = response.body()!!.data!![i].treeIdx
                            location = response.body()!!.data!![i].location

                            dayOfWeek = response.body()!!.data!![i].date.substring(8,10)
                            day = response.body()!!.data!![i].date.substring(10,14)

                            //잡초만 있을 경우
                            if(response.body()!!.data!![i].treeIdx==16){
                                locationList.get(location-1).setImageBitmap(drawableToBitmap(R.drawable.android_weeds))
                            }else{
                                locationList.get(location-1).setImageBitmap(treeList.get(treeIdx-1))
                            }

                            //요일 설정
                            if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == "0" + (cal.get(Calendar.MONTH) + 1).toString()) {
                                txt_main_day_num_word.visibility = View.VISIBLE
                                txt_main_day_num.visibility = View.VISIBLE
                                txt_main_day_text.visibility = View.VISIBLE

                                txt_main_day_num.setText(dayOfWeek)
                                txt_main_day_text.setText(day)
                            }else{
                                txt_main_day_num_word.visibility = View.INVISIBLE
                                txt_main_day_num.visibility = View.INVISIBLE
                                txt_main_day_text.visibility = View.INVISIBLE
                            }

                            //문구 설정
                            treeNum = response.body()!!.data!![i].treeNum
                            if(treeNum < 1){
                                txt_main_exp1.setText(getString(R.string.treeNumText0))
                                txt_main_exp1.visibility = View.VISIBLE
                            }else if(treeNum < 11){
                                val text = treeNum.toString() + getString( R.string.treeNumText10)
                                txt_main_exp1.setText(text)
                                txt_main_exp1.visibility = View.VISIBLE
                            }else if(treeNum < 21){
                                val text = treeNum.toString() + getString(R.string.treeNumText20)
                                txt_main_exp1.setText(text)
                                txt_main_exp1.visibility = View.VISIBLE
                            }else{
                                val text = treeNum.toString() + getString(R.string.treeNumText21)
                                txt_main_exp1.setText(text)
                                txt_main_exp1.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        })
    }

    fun initializeTree(){
        val initTree = drawableToBitmap(R.drawable.tree_size)
        for(i in 0..31) locationList.get(i).setImageBitmap(initTree)
    }

    fun setLocation(){
        locationList = listOf(img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11,
                            img12, img13, img14, img15, img16, img17, img18, img19, img20, img21_weed, img22,
                            img23, img24, img25, img26, img27, img28, img29, img30_weed, img31, img32)

    }

    fun setTree(){
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

    private fun drawableToBitmap(icnName : Int) : Bitmap {
        val drawable = resources.getDrawable(icnName) as BitmapDrawable
        val bitmap = drawable.bitmap
        return bitmap
    }

}