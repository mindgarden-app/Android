package com.mindgarden.mindgarden.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

interface MyObserver : LifecycleObserver{
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded()
}