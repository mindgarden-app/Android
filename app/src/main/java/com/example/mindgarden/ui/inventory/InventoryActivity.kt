package com.example.mindgarden.ui.inventory

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.mindgarden.DB.TokenController
import com.example.mindgarden.Data.GridData
import com.example.mindgarden.Data.InventoryData
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.GET.GetPlantResponse
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.Network.POST.PostPlantResponse
import com.example.mindgarden.R
import com.example.mindgarden.DB.RenewAcessTokenController
import com.example.mindgarden.ui.main.MainActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_inventory.*
import kotlinx.android.synthetic.main.toolbar_inventory.*
import org.jetbrains.anko.ctx
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class InventoryActivity : AppCompatActivity() {
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

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
        rv_grid.layoutManager = GridLayoutManager(this, 6)

        configureRecyclerView()

        getPlantResponse()

        btn_save_inventory.setOnClickListener {
            val toast: Toast = Toast(ctx)
            val inflater: LayoutInflater = LayoutInflater.from(ctx)
            val toastView: View = inflater.inflate(R.layout.toast, null)
            val toastText: TextView = toastView.findViewById(R.id.toastText)

            if (isValid(TokenController.getAccessToken(this), gridList[gridIdx].product_id,
                    inventoryIdx
                )) {
                if (rBal == 1 && rCheck == 0) {
                    postPlantResponse(
                        TokenController.getAccessToken(this),
                        gridList[gridIdx].product_id ,
                        inventoryIdx
                    )

                    finish()
                } else if (rBal == 0 && rCheck == 2) {
                    toastText.setText("일기를 써야 나무를 심을 수 있어요.")
                    //toastText.width = 248
                    toastText.gravity = Gravity.CENTER
                    toast.view = toastView
                    toast.show()
                    //toast("일기를 써야 나무를 심을 수 있어요")
                } else if (rBal == 0 && rCheck == 1) {
                    toastText.setText("나무는 하루에 하나만 심을 수 있어요!ㅠㅠ")
                    //toastText.width = 248
                    toastText.gravity = Gravity.CENTER
                    toast.view = toastView
                    toast.show()
                    //toast("나무는 하루에 하나만 심을 수 있어요ㅠㅠ!")
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
        rv_inventory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

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
        val toast: Toast = Toast(ctx)
        val inflater: LayoutInflater = LayoutInflater.from(ctx)
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

    fun postPlantResponse(accessToken: String, location: Int, treeIdx: Int) {
        var jsonObject = JSONObject()
       //TODO 수정 필요 함수파라메터 useIdx에서 accessToken으로 바꿈
        //TODO 수정 필요 jsonObject.put("userIdx", userIdx)

        jsonObject.put("location", location)
        jsonObject.put("treeIdx", treeIdx)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val postPlantResponse: Call<PostPlantResponse> =
            networkService.postPlantResponse(TokenController.getAccessToken(this), gsonObject)
        postPlantResponse.enqueue(object: Callback<PostPlantResponse> {
            override fun onFailure(call: Call<PostPlantResponse>, t: Throwable) {
                Log.e("fail", t.toString())
            }

            override fun onResponse(call: Call<PostPlantResponse>, response: Response<PostPlantResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                    }
                }
            }
        })
    }

    fun getPlantResponse() {

        if(!TokenController.isValidToken(ctx)){
            RenewAcessTokenController.postRenewAccessToken(ctx)
        }

        if (month.toInt() < 10) {
            month = "0$month"
        }

        val getPlantResponse = networkService.getPlantResponse(
            TokenController.getAccessToken(this), cal.get(Calendar.YEAR).toString() + "-" + month.toString())
        Log.e("why", cal.get(Calendar.YEAR).toString() + "-" + month.toString())
        getPlantResponse.enqueue(object: Callback<GetPlantResponse> {
            override fun onFailure(call: Call<GetPlantResponse>, t: Throwable) {
                Log.e("garden select fail", t.toString())
            }

            override fun onResponse(call: Call<GetPlantResponse>, response: Response<GetPlantResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        Log.e("Adapter: mainfragment : ", response.body()!!.message)

                        rBal = response.body()!!.data!![0].balloon
                        rCheck = response.body()!!.data!![0].check

                        //나무 수만큼
                        for(i in 0..(response.body()!!.data!!.size - 1)) {
                            Log.e("Adapter: rdate : ", response.body()!!.data!![i].date)

                            var treeIdx = 0
                            var location = 0
                            treeIdx = response.body()!!.data!![i].treeIdx
                            location = response.body()!!.data!![i].location
                            Log.e("Adapter:location ", location.toString())
                            Log.e("Adapter: treeIdx", treeIdx.toString())

                            locationList[i] = location

                            if(response.body()!!.data!![i].treeIdx == 16) {
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
                    }
                }
            }
        })
    }
}
