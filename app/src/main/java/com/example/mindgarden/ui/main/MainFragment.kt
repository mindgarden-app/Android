package com.example.mindgarden.ui.main


import android.app.Activity
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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.mindgarden.data.MindgardenRepository
import com.example.mindgarden.data.vo.GardenResponse
import com.example.mindgarden.db.RenewAcessTokenController
import com.example.mindgarden.db.TokenController
import com.example.mindgarden.ui.diary.DiaryDate
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

//수정중
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 *
 */
class MainFragment : Fragment(), DiaryDate, Tree {
    private val repository: MindgardenRepository by inject()

    val cal = Calendar.getInstance()

    private lateinit var treeList: List<Int>
    lateinit var locationList: List<ImageView>

    companion object{
        const val TOOLBAR_DATE_REQUEST_CODE = 100
        const val INVENTORY_REQUEST_CODE = 200
    }

    //수정중
    //인벤토리 토스트 작업
    var balloon: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.e("MainFragment","start")

        setLocation()

        init()
    }

    private fun init(){
        btnToolbarClick()
        mainFragmentClick()
        initToolbarTextCurrent()
        initTreeList()
        loadData()
    }

    private fun initToolbarTextCurrent(){
        txtDateToolbarMain.text = getToolbarDate(Calendar.getInstance())
    }

    private fun mainFragmentClick(){
        btnToolbarClick()
        btnSettingClick()
        btnRewardClick()
        txtToolbarDateClick()
    }
    private fun btnToolbarClick(){
        btn_left.setOnClickListener {
            txtDateToolbarMain.text = setDateMoveControl(1)
        }

        btn_right.setOnClickListener {
            txtDateToolbarMain.text = setDateMoveControl(0)
        }
    }

    private fun btnSettingClick(){
        btn_main_setting.setOnClickListener {
            startActivity(Intent(activity!!.applicationContext, MypageActivity::class.java))
        }
    }

    //수정중
    //인벤토리 토스트 작업
    private fun btnRewardClick(){
        btn_reward.setOnClickListener {
            startActivityForResult(Intent(activity!!.applicationContext, InventoryActivity::class.java).putExtra("balloon", balloon), INVENTORY_REQUEST_CODE)
        }
    }

    private fun txtToolbarDateClick(){
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
                if(data != null){
                    cal.set(Calendar.MONTH, data.getIntExtra("month",-1))
                    cal.set(Calendar.YEAR, data.getIntExtra("year",-1))
                    txtDateToolbarMain.text = getToolbarDate(cal)
                    loadData()
                }else{
                    Log.e("MainFragment", "intentFail")
                }
            }
        }

        if(requestCode == INVENTORY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                loadData()
                val ft = fragmentManager?.beginTransaction()
                ft?.let {
                    ft.detach(this).attach(this).commit()
                }
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


    private fun loadData() {
        if (!TokenController.isValidToken(activity!!.applicationContext)) {
            RenewAcessTokenController.postRenewAccessToken(activity!!.applicationContext, repository)
        }

        val date = getServerDate(Calendar.getInstance())
        repository
            .getGarden(TokenController.getAccessToken(activity!!.applicationContext), date,
                {
                    if (it.success) {
                        setMainDateText()   //날짜
                        it.data[0].let {d->
                            setMainComment(d.treeNum)  //코멘트
                            setTree(it.data.size,it.data)//namu
                            setGardenBalloon(d.balloon) //풍선
                        }

                    }else{
                        Log.e("mainFragment load", it.message)
                    }
                },
                {Log.e("MainFragment", it)})
    }


    private fun setMainDateText(){
        if(isCurrentToolbarDate()){
            txtDateMain.visibility = View.VISIBLE
            txtDateMain.text = getMainDateTextFormat(Calendar.getInstance())
        }else txtDateMain.visibility = View.INVISIBLE
    }

    private fun getMainDateTextFormat(calendar: Calendar):String{
        val f = SimpleDateFormat("dd'th.' EEE", Locale.ENGLISH)
        return f.format(calendar.time)

    }

    private fun setMainComment(treeNum: Int){
        txt_main_exp1.let {
            it.visibility = View.VISIBLE
            if(isCurrentToolbarDate()) {
                when (treeNum) {
                    in 1..10 -> it.text = getString(R.string.treeNumTextCurrent10)
                    in 11..20 -> it.text = getString(R.string.treeNumTextCurrent20)
                    in 21..31 -> it.text = getString(R.string.treeNumTextCurrent21)
                    else -> it.text = getString(R.string.treeNumTextCurrent0) }
            }else{
                when (treeNum) {
                    in 1..10 -> it.text = getString( R.string.treeNumText10)
                    in 11..20 -> it.text = getString(R.string.treeNumText20)
                    in 21..31 -> it.text = getString(R.string.treeNumText21)
                    else -> it.text = getString(R.string.treeNumText0) }
            }
        }
    }

    private fun setGardenBalloon(b: Int){
        when(b){
            1->{
                img_balloon.visibility = View.VISIBLE
                btn_reward.setImageResource(R.drawable.btn_plus_redbdg)
                //수정중
                //인벤토리 토스트 작업
                balloon = 1
            }
            0->{
                img_balloon.visibility = View.INVISIBLE
                btn_reward.setImageResource(R.drawable.btn_reward)
                //수정중
                //인벤토리 토스트 작업
                balloon = 0
            }
        }
    }

    private fun setTree(dataSize : Int, data: ArrayList<GardenResponse.GardenData>){
        for(i in 0 until dataSize){
            if(data[i].treeIdx == 16){
                locationList[data[i].location-1].setImageResource(treeList[16])
            }else{
                Log.e("mainF", data[i].location.toString())
                Log.e("mainF", data[i].treeIdx.toString())
                locationList[data[i].location-1].setImageResource(treeList[data[i].treeIdx])
            }
        }
    }


    private fun isCurrentToolbarDate(): Boolean{
        return txtDateToolbarMain.text == getToolbarDate(Calendar.getInstance(Locale.KOREA))
    }

    private fun getToolbarDate(calendar: Calendar):String{
        val f =SimpleDateFormat("yyyy년 MM월", Locale.getDefault())
        return f.format(calendar.time)
    }

    private fun getServerDate(calendar: Calendar):String{
        val f =SimpleDateFormat("yyyy-MM", Locale.getDefault())
        return f.format(calendar.time)
    }

    private fun setDateMoveControl(rl : Int): String{
        when(rl){
            0->{    //right +
                if(!isCurrentToolbarDate()) cal.add(Calendar.MONTH, 1)
            }
            1->{    //left -
                val mindgardenStartDate = Calendar.getInstance()
                mindgardenStartDate.set(2019, 6,31)
                if(txtDateToolbarMain.text != getToolbarDate(mindgardenStartDate))cal.add(Calendar.MONTH, -1)
            }
        }
        return getToolbarDate(cal)
    }


    private fun setLocation() {
        locationList = listOf(img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11,
            img12, img13, img14, img15, img16, img17, img18, img19, img20, img21_weed, img22,
            img23, img24, img25, img26, img27, img28, img29, img30_weed, img31, img32)

    }

    private fun initTreeList() {
        treeList = listOf (
            R.drawable.android_tree1, R.drawable.android_tree2, R.drawable.android_tree3, R.drawable.android_tree4,
            R.drawable.android_tree5, R.drawable.android_tree6, R.drawable.android_tree7, R.drawable.android_tree8,
            R.drawable.android_tree9, R.drawable.android_tree10, R.drawable.android_tree11, R.drawable.android_tree12,
            R.drawable.android_tree13, R.drawable.android_tree14, R.drawable.android_tree15, R.drawable.android_tree16,
            R.drawable.android_weeds
        )
    }
}