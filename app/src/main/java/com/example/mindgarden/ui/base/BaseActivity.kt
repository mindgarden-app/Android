package com.example.mindgarden.ui.base

import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity(@LayoutRes private val layoutId:Int) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableScreenSizeChange()
        setContentView(layoutId)
    }

    private fun disableScreenSizeChange(){
        //screen size
        val configuration : Configuration = resources.configuration
        configuration.fontScale = 1f
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        configuration.densityDpi = resources.displayMetrics.xdpi.toInt()
        baseContext.resources.updateConfiguration(configuration, metrics)

    }
}