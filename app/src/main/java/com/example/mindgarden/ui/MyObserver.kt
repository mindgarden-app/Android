package com.example.mindgarden.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

interface MyObserver : LifecycleObserver{
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded()
}