package com.example.mindgarden.Activity

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.GridView
import com.example.mindgarden.Adapter.GridRecyclerViewAdapter
import com.example.mindgarden.Adapter.GridViewAdapter
import com.example.mindgarden.Adapter.InventoryRecyclerViewAdapter
import com.example.mindgarden.DB.SharedPreferenceController
import com.example.mindgarden.Data.GridData
import com.example.mindgarden.Data.InventoryData
import com.example.mindgarden.Data.PlantData
import com.example.mindgarden.Fragment.MainFragment
import com.example.mindgarden.Layout.CustomGridViewLayout
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.Network.POST.PostPlantResponse
import com.example.mindgarden.R
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_inventory.*
import kotlinx.android.synthetic.main.rv_item_inventory.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InventoryActivity : AppCompatActivity() {
    lateinit var inventoryRecyclerViewAdapter: InventoryRecyclerViewAdapter
    lateinit var gridRecyclerViewAdapter: GridRecyclerViewAdapter
    lateinit var inventoryList : List<Bitmap>
    val networkService: NetworkService by lazy{
        ApplicationController.instance.networkService
    }

    companion object {
        var isClickAvailable: Boolean = true
        var isGridClick: Boolean = true
        var inventoryIdx: Int = 0
        var gridIdx: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        /*val GV=this.findViewById(R.id.gridView) as GridView
        val adapter= GridViewAdapter(this, R.layout.gridview_inventory, data)

        GV.adapter=adapter*/

        var gridList: ArrayList<GridData> = ArrayList()
        gridList.add(GridData(1, R.drawable.tree_size))
        gridList.add(GridData(3, R.drawable.tree_size))
        gridList.add(GridData(6, R.drawable.tree_size))
        gridList.add(GridData(10, R.drawable.tree_size))
        gridList.add(GridData(14, R.drawable.tree_size))
        gridList.add(GridData(8, R.drawable.tree_size))

        gridList.add(GridData(2, R.drawable.tree_size))
        gridList.add(GridData(5, R.drawable.tree_size))
        gridList.add(GridData(9, R.drawable.tree_size))
        gridList.add(GridData(13, R.drawable.tree_size))
        gridList.add(GridData(17, R.drawable.tree_size))
        gridList.add(GridData(22, R.drawable.tree_size))

        gridList.add(GridData(4, R.drawable.tree_size))
        gridList.add(GridData(8, R.drawable.tree_size))
        gridList.add(GridData(100, R.drawable.img_small_lake)) // 호수
        gridList.add(GridData(101, R.drawable.img_small_lake)) // 호수
        gridList.add(GridData(21, R.drawable.tree_size))
        gridList.add(GridData(26, R.drawable.tree_size))

        gridList.add(GridData(7, R.drawable.tree_size))
        gridList.add(GridData(12, R.drawable.tree_size))
        gridList.add(GridData(102, R.drawable.img_small_lake)) // 호수
        gridList.add(GridData(103, R.drawable.img_small_lake)) // 호수
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

        btn_choose.setOnClickListener {
            if(isValid(SharedPreferenceController.getUserID(this), gridList[gridIdx].product_id, inventoryIdx)) {
                postPlantResponse(SharedPreferenceController.getUserID(this), gridList[gridIdx].product_id, inventoryIdx)
            }
            Log.e("start", gridList[gridIdx].product_id.toString())
            finish()
        }
    }

    /*val data:ArrayList<CustomGridViewLayout>
    get(){
        val itemList :ArrayList<CustomGridViewLayout> = ArrayList<CustomGridViewLayout>()

        /*
        itemList.add(CustomGridViewLayout(R.drawable))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))

        //2
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))

        //3
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))

        //4
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))

        //4
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))

        //5
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
        itemList.add(CustomGridViewLayout(R.drawable.tree0))
*/
        return itemList
    }*/

    private fun configureRecyclerView(){
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
        inventory_dataList .add(InventoryData(image1, 0))
        inventory_dataList .add(InventoryData(image2, 1))
        inventory_dataList .add(InventoryData(image3, 2))
        inventory_dataList .add(InventoryData(image4, 3))
        inventory_dataList .add(InventoryData(image5, 4))
        inventory_dataList .add(InventoryData(image6, 5))
        inventory_dataList .add(InventoryData(image7, 6))
        inventory_dataList .add(InventoryData(image8, 7))
        inventory_dataList .add(InventoryData(image9, 8))
        inventory_dataList .add(InventoryData(image10, 9))
        inventory_dataList .add(InventoryData(image11, 10))
        inventory_dataList .add(InventoryData(image12, 11))
        inventory_dataList .add(InventoryData(image13, 12))
        inventory_dataList .add(InventoryData(image14, 13))
        inventory_dataList .add(InventoryData(image15, 14))
        inventory_dataList .add(InventoryData(image16, 15))

        inventoryRecyclerViewAdapter = InventoryRecyclerViewAdapter(this, inventory_dataList)
        rv_inventory.adapter = inventoryRecyclerViewAdapter
        rv_inventory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        inventoryList = listOf<Bitmap>(image1, image2, image3, image4,
            image5, image6, image7, image8,
            image9, image10, image11, image12,
            image13, image14, image15, image16)
    }

    private fun drawableToBitmap(icnName : Int): Bitmap {
        val drawable = resources.getDrawable(icnName) as BitmapDrawable
        val bitmap = drawable.bitmap
        return bitmap
    }

    fun isValid(userIdx: Int, location: Int, treeIdx: Int): Boolean {
        if(userIdx.toString() == "")
            toast("로그인하세요")

        else if(location.toString() == "")
            toast("위치를 고르세요")

        else if(treeIdx.toString() == "")
            toast("나무를 선택하세요")

        else return true

        return false
    }

    fun postPlantResponse(userIdx: Int, location: Int, treeIdx: Int) {
        var jsonObject = JSONObject()
        jsonObject.put("userIdx", userIdx)
        jsonObject.put("location", location)
        jsonObject.put("treeIdx", treeIdx)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject
        val postPlantResponse: Call<PostPlantResponse> =
                networkService.postPlantResponse("application/json", gsonObject)
        postPlantResponse.enqueue(object: Callback<PostPlantResponse>{
            override fun onFailure(call: Call<PostPlantResponse>, t: Throwable) {
                Log.e("fail", t.toString())
            }

            override fun onResponse(call: Call<PostPlantResponse>, response: Response<PostPlantResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        val tmp: ArrayList<PlantData> = response.body()!!.data!!
                        //diaryListRecyclerViewAdapter.dataList = tmp
                        //diaryListRecyclerViewAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}
