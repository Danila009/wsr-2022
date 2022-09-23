package com.example.fastest_delivey_app

import android.annotation.SuppressLint
import android.icu.util.Calendar
import java.text.SimpleDateFormat

@SuppressLint("NewApi")
fun getUserLocalDate(
    simpleDateFormat: SimpleDateFormat
):String {
    val calendar = Calendar.getInstance()
    return simpleDateFormat.format(calendar.time)
}