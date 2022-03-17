package com.sasarinomari.diary

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_day_write.*
import java.time.LocalDate
import java.util.*

class DayModifyActivity : DiaryActivity(), DatePickerDialog.OnDateSetListener {
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
        setContentView(R.layout.activity_day_modify)

        val json = intent.getStringExtra("diary")
        val diary = Gson().fromJson(json, DiaryModel::class.java)

        text_title.text = conv.toDisplayable(diary.date!!)
        text_content.setText(diary.text)

        button_set_date_today.setOnClickListener {
            val f = DatePickerFragment(this@DayModifyActivity)
            f.show(supportFragmentManager, "DatePicker")
        }

        button_ok.setOnClickListener {
            diary.date = conv.toSystematic(text_title.text.toString())
            diary.text = text_content.text.toString()

            api.modifyDay(diary) {
                Toast.makeText(this@DayModifyActivity, "일기 수정을 완료했습니다.", Toast.LENGTH_LONG).show()
                setResult(RESULT_OK)
                finish()
            }
        }

    }

    override fun onDateSet(obj: DatePicker?, year: Int, month: Int, date: Int) {
        val text = conv.toDisplayable(year, month+1, date)
        text_title.text = text
    }
}