package com.example.mindgarden

import android.app.Application
import com.example.mindgarden.di.networkModule
import com.example.mindgarden.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MindgardenApplication : Application(){
    private val moduleList = listOf(repositoryModule, networkModule)

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin(){
        startKoin {
            androidContext(this@MindgardenApplication)
            modules(moduleList)
        }
    }

}