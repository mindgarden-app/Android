package com.example.mindgarden.ui.diary

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

interface DiaryDate {
    companion object {
        var timeZone : TimeZone = TimeZone.getTimeZone("Asia/Seoul")
        //var simpleDateFormat : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd EEE HH:mm:ss", Locale.ENGLISH)
    }

    private fun stringToDate(str : String) = SimpleDateFormat("yyyy-MM-dd EEE HH:mm:ss", Locale.ENGLISH).parse(str)

    private fun getFormat(d: String, f : SimpleDateFormat) = f.format(stringToDate(d))

    fun getMainDate(d: String) = getDay(d) + "th. " + getDayOfWeek(d)

    fun getDiaryListDate(d: String) = getFormat(d, SimpleDateFormat("yyyy년 MM월", Locale.getDefault()))

    fun getDay(d: String) = getFormat(d, SimpleDateFormat("dd", Locale.getDefault()))

    fun getDayOfWeek(d : String) = getFormat(d, SimpleDateFormat("EEE", Locale.getDefault()))

    fun getDiaryDate(d: String) = getFormat(d, SimpleDateFormat("yy.MM.dd.(EEE)", Locale.getDefault()))

    fun getTime(d: String) = getFormat(d, SimpleDateFormat("HH:mm:ss", Locale.getDefault()))
}