package com.sasarinomari.diary

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_day_write.*
import java.util.*

class DayWriteActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private val conv = DateConverter()
    private val api = object : APICall() {
        override fun onError(message: String) {
            Log.e("Error", message)
        }
    }

    private val origin by lazy {
        val json = intent.getStringExtra("diary")?: return@lazy null
        Gson().fromJson(json, DiaryModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_write)

        button_set_date_today.setOnClickListener {
            val f = DatePickerFragment(this@DayWriteActivity)
            f.show(supportFragmentManager, "DatePicker")
        }

        if(origin == null) {
            text_title.text = conv.toDisplayable(Date())
            button_ok.setOnClickListener {
                val model = DiaryModel()
                model.date = conv.toSystematic(text_title.text.toString())
                model.text = text_content.text.toString()

                api.createDay(model) {
                    Toast.makeText(this@DayWriteActivity, "일기를 등록했습니다.", Toast.LENGTH_LONG).show()
                    val i = Intent()
                    i.putExtra("diary", Gson().toJson(model))
                    setResult(RESULT_OK, i)
                    finish()
                }
            }
        }
        else {
            origin?.let { diary ->
                text_title.text = conv.toDisplayable(diary.date)
                text_content.setText(diary.text)

                button_ok.setOnClickListener {
                    diary.date = conv.toSystematic(text_title.text.toString())
                    diary.text = text_content.text.toString()

                    api.modifyDay(diary) {
                        Toast.makeText(this@DayWriteActivity, "일기를 수정했습니다.", Toast.LENGTH_LONG).show()
                        val i = Intent()
                        i.putExtra("diary", Gson().toJson(diary))
                        setResult(RESULT_OK, i)
                        finish()
                    }
                }
            }
        }

    }

    override fun onDateSet(obj: DatePicker?, year: Int, month: Int, date: Int) {
        val text = conv.toDisplayable(year, month+1, date)
        text_title.text = text
    }
}