package com.example.android.nixknewsapp.utils

import android.text.format.DateUtils
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Extensions {
    fun String.formatTimeAgo(): String {
        var ago: CharSequence = ""
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        try {
            val time: Long = sdf.parse(this).time
            val now: Long = System.currentTimeMillis()
            ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ago.toString()
    }
}