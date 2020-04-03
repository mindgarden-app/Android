package com.example.mindgarden.ui.main


import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
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
import androidx.lifecycle.*
import com.example.mindgarden.data.MindgardenRepository
import com.example.mindgarden.data.vo.GardenResponse
import com.example.mindgarden.db.TokenController
import com.example.mindgarden.setDefaultTreeImage
import com.example.mindgarden.setSpringTreeImage
import com.example.mindgarden.ui.MyObserver
import com.example.mindgarden.ui.diary.DiaryDate
import com.example.mindgarden.ui.diary.WriteDiaryActivity
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

//수정중
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 *
 */
class MainFragment : Fragment(), DiaryDate, MyObserver {
    private val repository: MindgardenRepository by inject()
    private val cal = Calendar.getInstance()

    lateinit var locationList: List<ImageView>

    companion object {
        const val TOOLBAR_DATE_REQUEST_CODE = 100
        const val INVENTORY_REQUEST_CODE = 200
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        disableScreenSizeChange()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        init()
    }

    override fun onAppForegrounded() {
        init()
    }

    override fun onResume() {
        super.onResume()
        if (WriteDiaryActivity.CHECK) {
            init()
        }
    }

    private fun init() {
        setLocation()
        btnToolbarClick()
        mainFragmentClick()
        initToolbarTextCurrent()
        loadData()
    }

    private fun initToolbarTextCurrent() {
        txtDateToolbarMain.text = getToolbarDate(Calendar.getInstance())
    }

    private fun mainFragmentClick() {
        btnToolbarClick()
        btnSettingClick()
        btnRewardClick()
        txtToolbarDateClick()
    }

    private fun btnToolbarClick() {
        btn_left.setOnClickListener {
            txtDateToolbarMain.text = setDateMoveControl(1)
        }

        btn_right.setOnClickListener {
            txtDateToolbarMain.text = setDateMoveControl(0)
        }
    }

    private fun btnSettingClick() {
        btn_main_setting.setOnClickListener {
            startActivity(Intent(activity!!.applicationContext, MypageActivity::class.java))
        }
    }

    private fun btnRewardClick() {
        btn_reward.setOnClickListener {
            startActivityForResult(
                Intent(
                    activity!!.applicationContext,
                    InventoryActivity::class.java
                ).putExtra("season", getSeason()), INVENTORY_REQUEST_CODE
            )
        }
    }

    private fun txtToolbarDateClick() {
        llDateToolbarMain.setOnClickListener {
            Intent(activity!!.applicationContext, MainCalendarActivity::class.java).apply {
                putExtra("toolbarDate", txtDateToolbarMain.text)
                startActivityForResult(this, TOOLBAR_DATE_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TOOLBAR_DATE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    cal.set(Calendar.MONTH, data.getIntExtra("month", -1))
                    cal.set(Calendar.YEAR, data.getIntExtra("year", -1))
                    txtDateToolbarMain.text = getToolbarDate(cal)
                    loadData()
                } else {
                    Log.e("MainFragment", "intentFail")
                }
            }
        }

        if (requestCode == INVENTORY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                init()
            }
        }
    }

    private fun loadData() {
        setLand()
        initGarden()

        val date = getServerDate(cal)
        Log.e("mainF date", date)
        if (TokenController.isValidToken(activity!!.applicationContext, repository)) {
            repository
                .getGarden(TokenController.getAccessToken(activity!!.applicationContext), date,
                    {
                        if (it.success) {
                            setMainDateText()   //날짜
                            it.data[0].let { d ->
                                setMainComment(d.treeNum)
                                setTree(it.data.size, it.data)
                                setGardenBalloon(d.balloon)
                            }
                        } else {
                            Log.e("mainFragment load", it.message)
                        }
                    },
                    {
                        Log.e("MainFragment", it)
                    })
        } else {
            thread(start = true) {
                while (!TokenController.isValidToken(activity!!.applicationContext, repository)) {
                    loadData()
                    Thread.sleep(3000)
                }
            }
        }
    }


    private fun setMainDateText() {
        if (isCurrentToolbarDate()) {
            txtDateMain.visibility = View.VISIBLE
            txtDateMain.text = getMainDateTextFormat(Calendar.getInstance())
        } else txtDateMain.visibility = View.INVISIBLE
    }

    private fun getMainDateTextFormat(calendar: Calendar): String {
        val f = SimpleDateFormat("dd'th.' EEE", Locale.ENGLISH)
        return f.format(calendar.time)

    }

    private fun setMainComment(treeNum: Int) {
        txt_main_exp1.let {
            it.visibility = View.VISIBLE
            if (isCurrentToolbarDate()) {
                when (treeNum) {
                    in 1..10 -> it.text = getString(R.string.treeNumTextCurrent10)
                    in 11..20 -> it.text = getString(R.string.treeNumTextCurrent20)
                    in 21..32 -> it.text = getString(R.string.treeNumTextCurrent21)
                    else -> it.text = getString(R.string.treeNumTextCurrent0)
                }
            } else {
                when (treeNum) {
                    in 1..10 -> it.text = getString(R.string.treeNumText10, treeNum)
                    in 11..20 -> it.text = getString(R.string.treeNumText20, treeNum)
                    in 21..32 -> it.text = getString(R.string.treeNumText21, treeNum)
                    else -> it.text = getString(R.string.treeNumText0)
                }
            }
        }
    }

    private fun setGardenBalloon(b: Int) {
        when (b) {
            1 -> {
                img_balloon.visibility = View.VISIBLE
                btn_reward.setImageResource(R.drawable.btn_plus_redbdg)
            }
            0 -> {
                img_balloon.visibility = View.INVISIBLE
                btn_reward.setImageResource(R.drawable.btn_reward)
            }
        }
    }

    private fun setTree(dataSize: Int, data: ArrayList<GardenResponse.GardenData>) {
        when (getSeason()) {
            0 -> {
                for (i in 0 until dataSize) {
                    if (data[i].treeIdx == 16) locationList[data[i].location - 1].setSpringTreeImage(
                        data[i].treeIdx
                    )
                    else locationList[data[i].location - 1].setSpringTreeImage(data[i].treeIdx)
                }
            }
            else -> {
                for (i in 0 until dataSize) {
                    if (data[i].treeIdx == 16) locationList[data[i].location - 1].setDefaultTreeImage(
                        data[i].treeIdx
                    )
                    else locationList[data[i].location - 1].setDefaultTreeImage(data[i].treeIdx)
                }
            }
        }
    }

    private fun setLand() {
        when (getSeason()) {
            0 -> img_land.setImageResource(R.drawable.android_spring_land)
            else -> img_land.setImageResource(R.drawable.android_land)
        }
    }

    private fun initGarden() {
        for (i in 0..31) {
            locationList[i].setDefaultTreeImage(-1)
        }
    }

    private fun getSeason(): Int {
        return when (cal.get(Calendar.MONTH)) {
            2, 3, 4 -> 0
            else -> 1
        }
    }

    private fun isCurrentToolbarDate(): Boolean {
        return txtDateToolbarMain.text == getToolbarDate(Calendar.getInstance(Locale.KOREA))
    }

    private fun getToolbarDate(calendar: Calendar): String {
        val f = SimpleDateFormat("yyyy년 MM월", Locale.getDefault())
        return f.format(calendar.time)
    }

    private fun getServerDate(calendar: Calendar): String {
        val f = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        return f.format(calendar.time)
    }

    private fun setDateMoveControl(rl: Int): String {
        when (rl) {
            0 -> {    //right +
                if (!isCurrentToolbarDate()) {
                    cal.add(Calendar.MONTH, 1)
                    loadData()
                }
            }
            1 -> {    //left -
                val mindgardenStartDate = Calendar.getInstance()
                mindgardenStartDate.set(2019, 6, 31)
                if (txtDateToolbarMain.text != getToolbarDate(mindgardenStartDate)) {
                    cal.add(Calendar.MONTH, -1)
                    loadData()
                }
            }
        }
        return getToolbarDate(cal)
    }


    private fun setLocation() {
        locationList = listOf(
            img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11,
            img12, img13, img14, img15, img16, img17, img18, img19, img20, img21_weed, img22,
            img23, img24, img25, img26, img27, img28, img29, img30_weed, img31, img32
        )

    }

    private fun disableScreenSizeChange() {
        //screen size
        val configuration: Configuration = resources.configuration
        configuration.fontScale = 1f
        val metrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        configuration.densityDpi = resources.displayMetrics.xdpi.toInt()
        activity!!.baseContext.resources.updateConfiguration(configuration, metrics)

    }
}