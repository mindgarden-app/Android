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
import kotlinx.android.synthetic.main.rv_item_inventory.*
import org.jetbrains.anko.startActivity


class InventoryActivity : AppCompatActivity() {
    lateinit var inventoryRecyclerViewAdapter: InventoryRecyclerViewAdapter

    companion object {
        var isClickAvailable: Boolean = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        val GV=this.findViewById(R.id.gridView) as GridView
        val adapter= GridViewAdapter(this, R.layout.gridview_inventory, data)

        GV.adapter=adapter

        configureRecyclerView()

        btn_choose.setOnClickListener {
            finish()
        }
    }

    val data:ArrayList<CustomGridViewLayout>
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
    }

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
    }

    private fun drawableToBitmap(icnName : Int): Bitmap {
        val drawable = resources.getDrawable(icnName) as BitmapDrawable
        val bitmap = drawable.bitmap
        return bitmap
    }
}
