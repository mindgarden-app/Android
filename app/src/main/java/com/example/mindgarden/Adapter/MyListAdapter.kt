package com.example.mindgarden.Adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.mindgarden.R


class MyListAdapter (val context: Activity,  val option : Array<String>): ArrayAdapter<String>(context, R.layout.list_item_choice_image, option){

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_item_choice_image, null, true)


        val imgChoice = rowView.findViewById(R.id.txt_img_choice) as TextView

        imgChoice.text = option[position]

        return rowView
    }
}