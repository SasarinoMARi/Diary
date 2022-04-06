package com.sasarinomari.diary

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_day_write.*
import java.time.LocalDate
import java.util.*

class DayWriteActivity : DiaryActivity(), DatePickerDialog.OnDateSetListener {
    private val conv = DateConverter()
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
        setContentView(R.layout.activity_day_write)

        text_title.text = conv.toDisplayable(Date())

        button_set_date_today.setOnClickListener {
            val f = DatePickerFragment(this@DayWriteActivity)
            f.show(supportFragmentManager, "DatePicker")
        }

        button_ok.setOnClickListener {
            val model = DiaryModel()
            model.date = conv.toSystematic(text_title.text.toString())
            model.text = text_content.text.toString()

            api.createDay(model) {
                Toast.makeText(this@DayWriteActivity, "일기 등록을 완료했습니다.", Toast.LENGTH_LONG).show()
                finish()
            }
        }

    }

    override fun onDateSet(obj: DatePicker?, year: Int, month: Int, date: Int) {
        val text = conv.toDisplayable(year, month+1, date)
        text_title.text = text
    }
}