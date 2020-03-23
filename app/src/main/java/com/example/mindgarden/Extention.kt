package com.example.mindgarden

import android.widget.ImageView

fun ImageView.setDefaultTreeImage(treeIdx: Int){
    val treeDrawable = when(treeIdx){
        0-> R.drawable.android_tree1
        1-> R.drawable.android_tree2
        2-> R.drawable.android_tree3
        3-> R.drawable.android_tree4
        4-> R.drawable.android_tree5
        5-> R.drawable.android_tree6
        6-> R.drawable.android_tree7
        7-> R.drawable.android_tree8
        8-> R.drawable.android_tree9
        9-> R.drawable.android_tree10
        10-> R.drawable.android_tree11
        11-> R.drawable.android_tree12
        12-> R.drawable.android_tree13
        13-> R.drawable.android_tree14
        14-> R.drawable.android_tree15
        15-> R.drawable.android_tree16
        16-> R.drawable.android_weeds
        else-> R.drawable.android_tree_empty
    }
    setImageResource(treeDrawable)
}

fun ImageView.setSpringTreeImage(treeIdx: Int){
    val springTreeDrawable = when(treeIdx){
        0-> R.drawable.android_spring_tree1
        1-> R.drawable.android_spring_tree2
        2-> R.drawable.android_spring_tree3
        3-> R.drawable.android_spring_tree4
        4-> R.drawable.android_spring_tree5
        5-> R.drawable.android_spring_tree6
        6-> R.drawable.android_spring_tree7
        7-> R.drawable.android_spring_tree8
        8-> R.drawable.android_spring_tree9
        9-> R.drawable.android_spring_tree10
        10-> R.drawable.android_spring_tree11
        11-> R.drawable.android_spring_tree12
        12-> R.drawable.android_spring_tree13
        13-> R.drawable.android_spring_tree14
        14-> R.drawable.android_spring_tree15
        15-> R.drawable.android_spring_tree16
        16-> R.drawable.android_spring_weeds
        else-> R.drawable.android_tree_empty
    }
    setImageResource(springTreeDrawable)
}