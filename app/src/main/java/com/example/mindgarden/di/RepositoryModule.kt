package com.example.mindgarden.di

import com.example.mindgarden.data.MindgardenRepository
import com.example.mindgarden.data.MindgardenRepositoryImpl
import com.example.mindgarden.data.remote.MindgardenRemoteDataSource
import com.example.mindgarden.data.remote.MindgardenRemoteDataSourceImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<MindgardenRepository> {
        MindgardenRepositoryImpl(
            get()
        )
    }
    single<MindgardenRemoteDataSource> {
        MindgardenRemoteDataSourceImpl(get())
    }
}