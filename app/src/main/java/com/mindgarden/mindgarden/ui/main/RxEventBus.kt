package com.mindgarden.mindgarden.ui.main

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object RxEventBus {
    private val publisher = PublishSubject.create<Any>()

    fun publish(event: Any){
        publisher.onNext(event)
    }

    fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)

}