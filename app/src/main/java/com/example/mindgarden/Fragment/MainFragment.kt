package com.example.mindgarden.Fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mindgarden.Activity.InventoryActivity
import com.example.mindgarden.Activity.MainCalendarActivity
import com.example.mindgarden.Activity.MypageActivity
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.toolbar_diary_list.*
import kotlinx.android.synthetic.main.toolbar_diary_list.btn_left
import kotlinx.android.synthetic.main.toolbar_diary_list.btn_right
import kotlinx.android.synthetic.main.toolbar_diary_list.txt_month
import kotlinx.android.synthetic.main.toolbar_diary_list.txt_year
import kotlinx.android.synthetic.main.toolbar_main.*
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
class MainFragment : Fragment() {

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

        val cal = Calendar.getInstance()
        var year = cal.get(Calendar.YEAR).toString()
        var month = (cal.get(Calendar.MONTH) + 1).toString()
        var toolbarDate = ll_date_toolbar_diary_list

        txt_main_year.setText(year)
        if (month.toInt() < 10) {
            month = "0$month"
        }
        txt_main_month.setText(month)

        if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == "0" + (cal.get(Calendar.MONTH) + 1).toString()) {
            btn_reward.isEnabled = true
        } else {
            btn_reward.isEnabled = false
        }

        toolbarDate.setOnClickListener {
            startActivity<MainCalendarActivity>()
        }

        btn_left.setOnClickListener {
            if (month.toInt() == 1) {
                month = (month.toInt() + 11).toString()
                year = (year.toInt() - 1).toString()
                if (month.toInt() < 10) {
                    month = "0$month"
                }
                txt_main_year.setText(year)
                txt_main_month.setText(month)

                if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == "0" + (cal.get(Calendar.MONTH) + 1).toString()) {
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

                if (txt_main_year.text == cal.get(Calendar.YEAR).toString() && txt_main_month.text == "0" + (cal.get(Calendar.MONTH) + 1).toString()) {
                    btn_reward.isEnabled = true
                } else {
                    btn_reward.isEnabled = false
                }
            }
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
    }
}