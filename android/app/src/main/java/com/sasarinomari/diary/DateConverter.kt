package com.sasarinomari.diary

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class DateConverter {
    private val dbFormat = SimpleDateFormat("yyyy-MM-dd")
    private val humanFormat = SimpleDateFormat("yyyy년 MM월 dd일 EE요일")

    fun toDisplayable(dbString: String) : String {
        val date = dbFormat.parse(dbString)
        return humanFormat.format(date)
    }

    fun toSystematic(readableString: String) : String {
//         val date = humanFormat.parse(readableString)
//         return dbFormat.format(date)
        return readableString.replace("년 ", "-")
            .replace("월 ", "-")
            .replace("일", "")
            .substring(0,10)
    }

    fun toDisplayable(year: Int, month: Int, day: Int) : String {
        return "${year}년 ${month}월 ${day}일"
    }

    fun toSystematic(year: Int, month: Int, day: Int) : String {
        return "${year}-${month}-${day}"
    }

    fun toDisplayable(date: Date) : String {
        return humanFormat.format(date)
    }

    fun toSystematic(date: Date) : String {
        return dbFormat.format(date)
    }
}