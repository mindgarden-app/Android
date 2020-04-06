package com.mindgarden.mindgarden.ui.diary

import java.text.SimpleDateFormat
import java.util.*

interface DiaryDate {
    private fun stringToDate(str : String) = SimpleDateFormat("yyyy-MM-dd EEE HH:mm:ss", Locale.ENGLISH).parse(str)

    private fun getFormat(d: String, f : SimpleDateFormat) = f.format(stringToDate(d))

    fun getDay(d: String) = getFormat(d, SimpleDateFormat("dd", Locale.getDefault()))

    fun getDayOfWeek(d : String) = getFormat(d, SimpleDateFormat("EEE", Locale.ENGLISH))

    fun getDiaryDate(d: String) = getFormat(d, SimpleDateFormat("yy.MM.dd. (EEE)", Locale.ENGLISH))

    fun getTime(d: String) = getFormat(d, SimpleDateFormat("HH:mm:ss", Locale.getDefault()))

}