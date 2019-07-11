package com.example.mindgarden.Data

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

data class MoodChoiceData(
    var weatherIdx : Int,
    var moodIcn : Bitmap,
    var moodTxt : String
)