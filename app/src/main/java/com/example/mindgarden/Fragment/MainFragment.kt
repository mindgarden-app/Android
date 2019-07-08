package com.example.mindgarden.Fragment


import android.app.Activity
import android.content.Intent
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
import android.support.design.widget.TabLayout
import kotlinx.android.synthetic.main.activity_main.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MainFragment : Fragment() {

    val REQUEST_CODE_SET_TOOLBAR_DATE = 1005
    var toolbarYear : String = ""
    var toolbarMonth : String = ""
    var year : String =""
    var month : String = ""
    val cal = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        btn_reward.isEnabled = false

        year = cal.get(Calendar.YEAR).toString()
        month = (cal.get(Calendar.MONTH) + 1).toString()

        txt_main_year.setText(year)
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_main_month.setText(month)

        //현재 년,월 (숫자만) , 년도가 현재인지 월이 현재 달인지
        if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == "0" + (cal.get(Calendar.MONTH) + 1).toString()) {
            btn_reward.isEnabled = true
        } else {
            btn_reward.isEnabled = false
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
                REQUEST_CODE_SET_TOOLBAR_DATE,
                "year" to toolbarYear,
                "month" to toolbarMonth
            )
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

            }
        }
    }

    override fun onResume() {
        super.onResume()

        //툴바 년/월 설정(MainCalendar로 전달)
        toolbarYear = txt_main_year.text.toString()
        toolbarMonth = txt_main_month.text.toString()


        btn_left.setOnClickListener {
            //1월로 갔을때 년도 바뀜
            if (month.toInt() == 1) {
                month = (month.toInt() + 11).toString() //1->12월로 가도록
                year = (year.toInt() - 1).toString()    //12월로 가면 년도 바뀜
                if (month.toInt() < 10) {   //한자리수면 0붙여주기
                    month = "0$month"
                }
                txt_main_year.setText(year)
                txt_main_month.setText(month)
                //툴바 년/월 설정(MainCalendar로 전달)
                toolbarYear = txt_main_year.text.toString()
                toolbarMonth = txt_main_month.text.toString()

                //현재 년,월 (숫자만) , 년도가 현재인지 월이 현재 달인지
                if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == "0" + (cal.get(Calendar.MONTH) + 1).toString()) {
                    btn_reward.isEnabled = true

                } else {
                    btn_reward.isEnabled = false
                }
            }
            else {
                month = (month.toInt() - 1).toString()
                if (month.toInt() < 10) {
                    month = "0$month"
                }
                txt_main_month.setText(month)
                //툴바 월 설정(MainCalendar로 전달)
                toolbarMonth = txt_main_month.text.toString()
                if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == "0" + (cal.get(Calendar.MONTH) + 1).toString()) {
                    btn_reward.isEnabled = true
                } else {
                    btn_reward.isEnabled = false
                }
            }
        }

        btn_right.setOnClickListener {
            if (month.toInt() == 12) {
                month = (month.toInt() - 11).toString()
                year = (year.toInt() + 1).toString()
                if (month.toInt() < 10) {
                    month = "0$month"
                }
                txt_main_year.setText(year)
                txt_main_month.setText(month)

                //툴바 년/월 설정(MainCalendar로 전달)
                toolbarYear = txt_main_year.text.toString()
                toolbarMonth = txt_main_month.text.toString()

                if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == "0" + (cal.get(Calendar.MONTH) + 1).toString()) {
                    btn_reward.isEnabled = true
                } else {
                    btn_reward.isEnabled = false
                }
            } else {
                month = (month.toInt() + 1).toString()
                if (month.toInt() < 10) {
                    month = "0$month"
                }
                txt_main_month.setText(month)

                //툴바 월 설정(MainCalendar로 전달)
                toolbarMonth = txt_main_month.text.toString()
                if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == "0" + (cal.get(Calendar.MONTH) + 1).toString()) {
                    btn_reward.isEnabled = true
                }
                else {
                    btn_reward.isEnabled = false
                }
            }
        //툴바 날짜 클릭했을 때 -> 팝업 띄우기
        ll_date_toolbar_main.setOnClickListener {
            startActivityForResult<MainCalendarActivity>(
                REQUEST_CODE_SET_TOOLBAR_DATE,
                "year" to toolbarYear,
                "month" to toolbarMonth
            )
        }
    }

}
}