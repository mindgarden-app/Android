package com.example.mindgarden.ui.inventory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindgarden.db.TokenController
import com.example.mindgarden.data.GridData
import com.example.mindgarden.data.InventoryData
import com.example.mindgarden.R
import com.example.mindgarden.data.MindgardenRepository
import com.example.mindgarden.db.RenewAcessTokenController
import com.example.mindgarden.ui.main.MainActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_inventory.*
import kotlinx.android.synthetic.main.layout_data_load_fail.*
import kotlinx.android.synthetic.main.toolbar_inventory.*
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class InventoryActivity : AppCompatActivity() {
    private val repository : MindgardenRepository by inject()

    private lateinit var treeList: List<Int>
    private val  inventoryRecyclerViewAdapter: InventoryRecyclerViewAdapter by lazy {
        InventoryRecyclerViewAdapter{inventoryClickEventCallback(it)}
    }
    private val gridRecyclerViewAdapter : GridRecyclerViewAdapter by lazy {
        GridRecyclerViewAdapter{gridClickEventCallback(it)}
    }
    private val serverGardenLocation =
        arrayOf(100,0,6,1,12,7,2,18,13,8,3,24,19,9,4,30,25,10,5,31,26,16,11,32,27,22,17,33,28,23,34,29,35)
    private val gridList : ArrayList<GridData> by lazy {
        ArrayList<GridData>()
    }
    private val inventoryList : ArrayList<InventoryData> by lazy {
        ArrayList<InventoryData>()
    }
    private var treeIdx : Int = -1
    private var location : Int = -1
    private var balloon : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)
        init()
    }

    private fun init(){
        initRecyclerView()
        btnBackClick()
        btnSaveClick()
        loadData()
    }

    private fun showToast(msg : String){
        val toast = Toast(this)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val toastView: View = inflater.inflate(R.layout.toast, null)
        val toastText: TextView = toastView.findViewById(R.id.toastText)
        toastText.setText(msg)
        toastText.gravity = Gravity.CENTER
        toast.view = toastView
        toast.show()
    }

    private fun btnBackClick(){
        btn_back_toolbar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun gridClickEventCallback(position: Int){
        when(treeIdx){
            -1-> showToast("나무를 골라주세요")
            else-> {
                when(GridRecyclerViewAdapter.selectedStatus.get(position)) {
                    true -> {
                        GridRecyclerViewAdapter.selectedStatus.put(position, false)
                        gridRecyclerViewAdapter.notifyItemChanged(position)
                        location = -1
                    }
                    false -> {
                        GridRecyclerViewAdapter.selectedStatus.put(position, true)
                        gridList[position].img = treeList[treeIdx]
                        location = gridList[position].gridId
                        gridRecyclerViewAdapter.notifyItemChanged(position)
                    }
                }
            }
        }
    }

    private fun inventoryClickEventCallback(position: Int){
        val data = inventoryRecyclerViewAdapter.getDataAt(position)

        when(data.type){
            0-> {
                setInventoryType(2)
                inventoryList[position].type = 1
                inventoryRecyclerViewAdapter.setData(inventoryList)
                treeIdx = data.treeIdx
            }
            1-> {
                setInventoryType(0)
                inventoryRecyclerViewAdapter.setData(inventoryList)
                treeIdx = -1
            }
        }
        inventoryRecyclerViewAdapter.notifyItemChanged(position)
    }

    private fun setInventoryType(t: Int){
        for(i in 0 until treeList.size) inventoryList[i].type = t
    }

    //post garden
    private fun btnSaveClick(){
        btn_save_inventory.setOnClickListener {
            isValid()
        }
    }

    private fun isValid() {
        if(!TokenController.isValidToken(this)){
            RenewAcessTokenController.postRenewAccessToken(this,repository)
        }
        when{
            TokenController.getAccessToken(this).isNullOrBlank() -> showToast("로그인하세요")
            treeIdx == -1  -> showToast("나무를 선택하세요")
            location == -1 ->  showToast("위치를 고르세요")
            balloon == 0 -> showToast("나무는 하루에 하나, 일기를 쓴 후 심을 수 있어요!")
            else -> postPlant(location, treeIdx)
        }
    }

    private fun postPlant(location: Int, treeIdx: Int){
        val jsonObject = JSONObject()
        jsonObject.put("location", location)
        jsonObject.put("treeIdx", treeIdx)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        repository
            .postPlant(TokenController.getAccessToken(this), gsonObject,
                {
                    hideErrorView()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                {
                    showErrorView()
                    btnRetryDataLoad()
                })
    }

    //get garden
    private fun getCurrentDate():String{
        val f = SimpleDateFormat("yyyy-MM", Locale.KOREA)
        return f.format(Calendar.getInstance(Locale.KOREA).time)
    }

    private fun loadData(){
        if(!TokenController.isValidToken(this)){
            RenewAcessTokenController.postRenewAccessToken(this,repository)
        }

        repository
            .getGarden(TokenController.getAccessToken(this), getCurrentDate(),
                {
                    hideErrorView()
                    if(it.success){
                        for(i in 0 until it.data.size) {
                            it.data[i].let {data->
                                val location = serverGardenLocation[data.location]
                                balloon = data.balloon
                                if(data.treeIdx == 16){   //weed
                                    gridList[location].img = R.drawable.android_weeds
                                    gridList[location].type = 2
                                }else{  //not weed
                                    gridList[location].img = treeList[data.treeIdx]
                                    gridList[location].type = 2
                                }
                            }
                            gridRecyclerViewAdapter.notifyDataSetChanged()
                        }
                    }else{
                        Log.e("InventoryActivity: ", it.message)
                    }
                },
                {
                    showErrorView()
                    btnRetryDataLoad()
                })
    }

    //init
    private fun initRecyclerView(){
        //grid
        rv_grid.adapter = gridRecyclerViewAdapter
        rv_grid.layoutManager = GridLayoutManager(this, 6)
        GridRecyclerViewAdapter.selectedStatus.clear()
        initGridList()

        //inventory
        rv_inventory.adapter = inventoryRecyclerViewAdapter
        rv_inventory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        initInventoryList()
    }

    private fun initGridList(){
        gridList.run {
            add(GridData(0,1, null))
            add(GridData(0,3, null))
            add(GridData(0,6, null))
            add(GridData(0,10, null))
            add(GridData(0,14, null))
            add(GridData(0,18, null))

            add(GridData(0,2, null))
            add(GridData(0,5, null))
            add(GridData(0,9, null))
            add(GridData(0,13, null))
            add(GridData(0,17, null))
            add(GridData(0,22, null))

            add(GridData(0,4, null))
            add(GridData(0,8, null))
            add(GridData(1,33, null)) //호수
            add(GridData(1,33,  null)) //호수
            add(GridData(0,21, null))
            add(GridData(0,26, null))

            add(GridData(0,7, null))
            add(GridData(0,12, null))
            add(GridData(1,33,  null)) //호수
            add(GridData(1,33,  null)) //호수
            add(GridData(0,25, null))
            add(GridData(0,29, null))

            add(GridData(0,11, null))
            add(GridData(0,16, null))
            add(GridData(0,20, null))
            add(GridData(0,24, null))
            add(GridData(0,28, null))
            add(GridData(0,31, null))

            add(GridData(0,15, null))
            add(GridData(0,19,null))
            add(GridData(0,23, null))
            add(GridData(0,27, null))
            add(GridData(0,30, null))
            add(GridData(0,32, null))
        }

        gridRecyclerViewAdapter.setData(gridList)
    }

    private fun initInventoryList() {
        treeList = listOf (
            R.drawable.android_tree1, R.drawable.android_tree2, R.drawable.android_tree3, R.drawable.android_tree4,
            R.drawable.android_tree5, R.drawable.android_tree6, R.drawable.android_tree7, R.drawable.android_tree8,
            R.drawable.android_tree9, R.drawable.android_tree10, R.drawable.android_tree11, R.drawable.android_tree12,
            R.drawable.android_tree13, R.drawable.android_tree14, R.drawable.android_tree15, R.drawable.android_tree16
        )

        for (i in 0 until treeList.size){
            inventoryList.add(InventoryData(treeList[i], i, 0))
        }
        inventoryRecyclerViewAdapter.setData(inventoryList)
    }

    //errorview
    private fun showErrorView(){
        clInventory.visibility = View.GONE
        dataLoadFailInventory.visibility = View.VISIBLE
    }

    private fun hideErrorView(){
        clInventory.visibility = View.VISIBLE
        dataLoadFailInventory.visibility = View.GONE
    }

    private fun btnRetryDataLoad(){
        btnRetryDataLoadFail.setOnClickListener {
            loadData()
        }
    }

}
