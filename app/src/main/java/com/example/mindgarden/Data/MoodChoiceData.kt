package com.example.mindgarden.Data

import android.graphics.Bitmap

data class MoodChoiceData(
    var weatherIdx : Int,
    var moodIcn : Bitmap,
    var moodTxt : String
)