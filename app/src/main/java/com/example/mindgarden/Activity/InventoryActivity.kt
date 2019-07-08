package com.example.mindgarden.Activity

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.GridView
import com.example.mindgarden.Adapter.GridViewAdapter
import com.example.mindgarden.Adapter.InventoryRecyclerViewAdapter
import com.example.mindgarden.Adapter.MoodChoiceRecyclerViewAdapter
import com.example.mindgarden.Data.InventoryData
import com.example.mindgarden.Data.MoodChoiceData
import com.example.mindgarden.Fragment.MainFragment
import com.example.mindgarden.Layout.CustomGridViewLayout
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.activity_inventory.*
import kotlinx.android.synthetic.main.activity_mood_choice.*
import org.jetbrains.anko.startActivity


class InventoryActivity : AppCompatActivity() {
    lateinit var inventoryRecyclerViewAdapter: InventoryRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        val GV=this.findViewById(R.id.gridView) as GridView
        val adapter= GridViewAdapter(this, R.layout.gridview_inventory, data)

        GV.adapter=adapter

        configureRecyclerView()

        btn_check.setOnClickListener {
            startActivity<MainActivity>()
        }
    }

    val data:ArrayList<CustomGridViewLayout>
    get(){
        val itemList :ArrayList<CustomGridViewLayout> = ArrayList<CustomGridViewLayout>()

        //1
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))

        //2
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))

        //3
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))

        //4
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))

        //4
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))

        //5
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))
        itemList.add(CustomGridViewLayout(R.drawable.single_tree))

        return itemList
    }

    private fun configureRecyclerView(){
        val image1 = drawableToBitmap(R.drawable.android_tree1)

        var inventory_dataList: ArrayList<InventoryData> = ArrayList()
        inventory_dataList .add(InventoryData(image1, 0))
        inventory_dataList .add(InventoryData(image1, 1))
        inventory_dataList .add(InventoryData(image1, 2))
        inventory_dataList .add(InventoryData(image1, 3))
        inventory_dataList .add(InventoryData(image1, 4))
        inventory_dataList .add(InventoryData(image1, 5))
        inventory_dataList .add(InventoryData(image1, 6))
        inventory_dataList .add(InventoryData(image1, 7))
        inventory_dataList .add(InventoryData(image1, 8))
        inventory_dataList .add(InventoryData(image1, 9))
        inventory_dataList .add(InventoryData(image1, 10))
        inventory_dataList .add(InventoryData(image1, 11))
        inventory_dataList .add(InventoryData(image1, 12))
        inventory_dataList .add(InventoryData(image1, 13))
        inventory_dataList .add(InventoryData(image1, 14))
        inventory_dataList .add(InventoryData(image1, 15))

        inventoryRecyclerViewAdapter = InventoryRecyclerViewAdapter(this, inventory_dataList)
        rv_inventory.adapter = inventoryRecyclerViewAdapter
        rv_inventory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun drawableToBitmap(icnName : Int): Bitmap {
        val drawable = resources.getDrawable(icnName) as BitmapDrawable
        val bitmap = drawable.bitmap
        return bitmap
    }
}
