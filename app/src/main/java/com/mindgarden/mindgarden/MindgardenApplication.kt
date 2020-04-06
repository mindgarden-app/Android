package com.mindgarden.mindgarden

import android.app.Application
import com.mindgarden.mindgarden.di.networkModule
import com.mindgarden.mindgarden.di.repositoryModule
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