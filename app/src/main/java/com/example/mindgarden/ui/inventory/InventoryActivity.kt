package com.example.mindgarden.ui.inventory

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.mindgarden.db.TokenController
import com.example.mindgarden.data.GridData
import com.example.mindgarden.data.InventoryData
import com.example.mindgarden.R
import com.example.mindgarden.data.MindgardenRepository
import com.example.mindgarden.db.RenewAcessTokenController
import com.example.mindgarden.ui.login.LoginActivity
import com.example.mindgarden.ui.main.MainActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_inventory.*
import kotlinx.android.synthetic.main.toolbar_inventory.*
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList


class InventoryActivity : AppCompatActivity() {
    private val repository : MindgardenRepository by inject()

    val cal = Calendar.getInstance()
    var month = (cal.get(Calendar.MONTH) + 1).toString()
    lateinit var inventoryRecyclerViewAdapter: InventoryRecyclerViewAdapter
    lateinit var gridRecyclerViewAdapter: GridRecyclerViewAdapter
    lateinit var inventoryList: List<Int>
    var gridList: ArrayList<GridData> = ArrayList()

    companion object {
        var isClickAvailable: Boolean = true
        var isGridClick: Boolean = true
        var inventoryIdx: Int = 0
        var gridIdx: Int = 0
        val fromServerToUs = arrayOf(100,0,6,1,12,7,2,18,13,8,3,24,19,9,4,30,25,10,5,31,26,16,11,32,27,22,17,33,28,23,34,29,35)
        var locationList = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        var rBal: Int = 0
        var rCheck: Int = 0
    }

    override fun onResume() {
        super.onResume()

        //getPlantResponse()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        //txtSetting.text = "나무 심기"

        //활성화 관련
        var rActivated = intent?.getIntExtra("activated", 0)
        Log.e("rActivated", rActivated.toString())

        btn_back_toolbar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            //백 스페이스 누르면 다시 메인 페이지로
            startActivity(intent)

            finish()
        }

        gridList.add(GridData(1, R.drawable.tree_size))
        gridList.add(GridData(3, R.drawable.tree_size))
        gridList.add(GridData(6, R.drawable.tree_size))
        gridList.add(GridData(10, R.drawable.tree_size))
        gridList.add(GridData(14, R.drawable.tree_size))
        gridList.add(GridData(18, R.drawable.tree_size))

        gridList.add(GridData(2, R.drawable.tree_size))
        gridList.add(GridData(5, R.drawable.tree_size))
        gridList.add(GridData(9, R.drawable.tree_size))
        gridList.add(GridData(13, R.drawable.tree_size))
        gridList.add(GridData(17, R.drawable.tree_size))
        gridList.add(GridData(22, R.drawable.tree_size))

        gridList.add(GridData(4, R.drawable.tree_size))
        gridList.add(GridData(8, R.drawable.tree_size))
        gridList.add(GridData(33, R.drawable.img_small_lake)) //호수
        gridList.add(GridData(33,  R.drawable.img_small_lake)) //호수
        gridList.add(GridData(21, R.drawable.tree_size))
        gridList.add(GridData(26, R.drawable.tree_size))

        gridList.add(GridData(7, R.drawable.tree_size))
        gridList.add(GridData(12, R.drawable.tree_size))
        gridList.add(GridData(33,  R.drawable.img_small_lake)) //호수
        gridList.add(GridData(33,  R.drawable.img_small_lake)) //호수
        gridList.add(GridData(25, R.drawable.tree_size))
        gridList.add(GridData(29, R.drawable.tree_size))

        gridList.add(GridData(11, R.drawable.tree_size))
        gridList.add(GridData(16, R.drawable.tree_size))
        gridList.add(GridData(20, R.drawable.tree_size))
        gridList.add(GridData(24, R.drawable.tree_size))
        gridList.add(GridData(28, R.drawable.tree_size))
        gridList.add(GridData(31, R.drawable.tree_size))

        gridList.add(GridData(15, R.drawable.tree_size))
        gridList.add(GridData(19, R.drawable.tree_size))
        gridList.add(GridData(23, R.drawable.tree_size))
        gridList.add(GridData(27, R.drawable.tree_size))
        gridList.add(GridData(30, R.drawable.tree_size))
        gridList.add(GridData(32, R.drawable.tree_size))

        gridRecyclerViewAdapter = GridRecyclerViewAdapter(this, gridList)
        rv_grid.adapter = gridRecyclerViewAdapter
        rv_grid.layoutManager =
            androidx.recyclerview.widget.GridLayoutManager(this, 6)

        configureRecyclerView()

        loadData()

        btn_save_inventory.setOnClickListener {
            val toast: Toast = Toast(this)
            val inflater: LayoutInflater = LayoutInflater.from(this)
            val toastView: View = inflater.inflate(R.layout.toast, null)
            val toastText: TextView = toastView.findViewById(R.id.toastText)

            if (isValid(TokenController.getAccessToken(this), gridList[gridIdx].product_id,
                    inventoryIdx
                )) {
                if (rActivated == 1) {
                    postPlant(
                        TokenController.getAccessToken(this),
                        gridList[gridIdx].product_id ,
                        inventoryIdx
                    )

                    finish()
                } else {
                    toastText.setText("나무 심을 수 없어요.")
                    //toastText.width = 248
                    toastText.gravity = Gravity.CENTER
                    toast.view = toastView
                    toast.show()
                    //toast("일기를 써야 나무를 심을 수 있어요")
                }
            }

            Log.e("start", gridList[gridIdx].product_id.toString())

            //finish()
        }
    }

    private fun configureRecyclerView() {
        val image1 = drawableToBitmap(R.drawable.android_tree1)
        val image2 = drawableToBitmap(R.drawable.android_tree2)
        val image3 = drawableToBitmap(R.drawable.android_tree3)
        val image4 = drawableToBitmap(R.drawable.android_tree4)
        val image5 = drawableToBitmap(R.drawable.android_tree5)
        val image6 = drawableToBitmap(R.drawable.android_tree6)
        val image7 = drawableToBitmap(R.drawable.android_tree7)
        val image8 = drawableToBitmap(R.drawable.android_tree8)
        val image9 = drawableToBitmap(R.drawable.android_tree9)
        val image10 = drawableToBitmap(R.drawable.android_tree10)
        val image11 = drawableToBitmap(R.drawable.android_tree11)
        val image12 = drawableToBitmap(R.drawable.android_tree12)
        val image13 = drawableToBitmap(R.drawable.android_tree13)
        val image14 = drawableToBitmap(R.drawable.android_tree14)
        val image15 = drawableToBitmap(R.drawable.android_tree15)
        val image16 = drawableToBitmap(R.drawable.android_tree16)

        var inventory_dataList: ArrayList<InventoryData> = ArrayList()
        inventory_dataList.add(InventoryData(image1, 0))
        inventory_dataList.add(InventoryData(image2, 1))
        inventory_dataList.add(InventoryData(image3, 2))
        inventory_dataList.add(InventoryData(image4, 3))
        inventory_dataList.add(InventoryData(image5, 4))
        inventory_dataList.add(InventoryData(image6, 5))
        inventory_dataList.add(InventoryData(image7, 6))
        inventory_dataList.add(InventoryData(image8, 7))
        inventory_dataList.add(InventoryData(image9, 8))
        inventory_dataList.add(InventoryData(image10, 9))
        inventory_dataList.add(InventoryData(image11, 10))
        inventory_dataList.add(InventoryData(image12, 11))
        inventory_dataList.add(InventoryData(image13, 12))
        inventory_dataList.add(InventoryData(image14, 13))
        inventory_dataList.add(InventoryData(image15, 14))
        inventory_dataList.add(InventoryData(image16, 15))

        inventoryRecyclerViewAdapter =
            InventoryRecyclerViewAdapter(this, inventory_dataList)
        rv_inventory.adapter = inventoryRecyclerViewAdapter
        rv_inventory.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(
                this,
                androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
                false
            )

        inventoryList = listOf<Int> (
            R.drawable.android_tree1, R.drawable.android_tree2, R.drawable.android_tree3, R.drawable.android_tree4,
            R.drawable.android_tree5, R.drawable.android_tree6, R.drawable.android_tree7, R.drawable.android_tree8,
            R.drawable.android_tree9, R.drawable.android_tree10, R.drawable.android_tree11, R.drawable.android_tree12,
            R.drawable.android_tree13, R.drawable.android_tree14, R.drawable.android_tree15, R.drawable.android_tree16
        )
    }

    private fun drawableToBitmap(icnName: Int): Bitmap {
        val drawable = resources.getDrawable(icnName) as BitmapDrawable
        val bitmap = drawable.bitmap
        return bitmap
    }

    fun isValid(accessToken: String, location: Int, treeIdx: Int): Boolean {
        val toast: Toast = Toast(this)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val toastView: View = inflater.inflate(R.layout.toast, null)
        val toastText: TextView = toastView.findViewById(R.id.toastText)

        if (accessToken.toString() == "") {
            toastText.setText("로그인하세요")
            toastText.gravity = Gravity.CENTER
            toast.view = toastView
            toast.show()
        }

        else if (location.toString() == "") {
            toastText.setText("위치를 고르세요")
            toastText.gravity = Gravity.CENTER
            toast.view = toastView
            toast.show()
        }

        else if (treeIdx.toString() == "") {
            toastText.setText("나무를 선택하세요")
            toastText.gravity = Gravity.CENTER
            toast.view = toastView
            toast.show()
        }

        else return true

        return false
    }

    private fun postPlant(accessToken: String, location: Int, treeIdx: Int){
        var jsonObject = JSONObject()
        //TODO 수정 필요 함수파라메터 useIdx에서 accessToken으로 바꿈
        //TODO 수정 필요 jsonObject.put("userIdx", userIdx)

        jsonObject.put("location", location)
        jsonObject.put("treeIdx", treeIdx)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        repository
            .postPlant(TokenController.getAccessToken(this), gsonObject,
                {
                    Log.e("plant success", "성공")
                },
                {
                    //에러처리
                    Log.e("plant failed", "실패")
                })
    }


    private fun loadData(){
        if(!TokenController.isValidToken(this)){
            RenewAcessTokenController.postRenewAccessToken(this,repository)
        }

        if (month.toInt() < 10) {
            month = "0$month"
        }

        repository
            .getGarden(TokenController.getAccessToken(this), cal.get(Calendar.YEAR).toString() + "-" + month.toString(),
                {
                    rBal = it.data!![0].balloon
                    //rCheck = it.data!![0].check

                    //나무 수만큼
                    for(i in 0..(it.data.size - 1)) {
                        Log.e("Adapter: rdate : ", it.data[i].date)

                        var treeIdx = 0
                        var location = 0
                        treeIdx = it.data[i].treeIdx
                        location = it.data[i].location
                        Log.e("Adapter:location ", location.toString())
                        Log.e("Adapter: treeIdx", treeIdx.toString())

                        locationList[i] = location

                        if(it.data[i].treeIdx == 16) {
                            gridList[fromServerToUs[location]].img = R.drawable.android_weeds
                            gridRecyclerViewAdapter.notifyDataSetChanged()
                        } else {
                            Log.e("h", location.toString())

                            gridList[fromServerToUs[location]].img = inventoryList.get(treeIdx)
                            gridRecyclerViewAdapter.notifyDataSetChanged()

                            Log.e("position ",
                                fromServerToUs[location].toString())
                        }
                    }
                },
                {
                    //에러처리
                })
    }

}
