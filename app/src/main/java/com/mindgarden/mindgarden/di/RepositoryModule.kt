package com.mindgarden.mindgarden.di

import com.mindgarden.mindgarden.data.MindgardenRepository
import com.mindgarden.mindgarden.data.MindgardenRepositoryImpl
import com.mindgarden.mindgarden.data.remote.MindgardenRemoteDataSource
import com.mindgarden.mindgarden.data.remote.MindgardenRemoteDataSourceImpl
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