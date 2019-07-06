package com.example.mindgarden

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView

class InventoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        val GV=this.findViewById(R.id.gridView) as GridView
        val adapter=GridViewAdapter(this,R.layout.gridview_inventory,data)

        GV.adapter=adapter
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
}
