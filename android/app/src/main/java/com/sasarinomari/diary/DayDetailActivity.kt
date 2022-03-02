package com.sasarinomari.diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_day_detail.*

class DayDetailActivity : AppCompatActivity() {

    private val api = object : APICall() {
        override fun onError(message: String) {
            Log.e("Error", message)
        }

        override fun onMessage(message: String) {
            Log.i("Activity", message)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_detail)

        val date = intent.getStringExtra("date")!!
        api.getDay(date) {
            text_title.text = it.date
            text_content.text = it.text
        }
    }
}