package com.sasarinomari.diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_write.*

class WriteActivity : DiaryActivity() {

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
        setContentView(R.layout.activity_write)

        button_ok.setOnClickListener {
            api.createDay(makeDiary()) {
                Toast.makeText(this@WriteActivity, "OK", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun makeDiary() : DiaryModel {
        val instance = DiaryModel()
        instance.date = text_title.text.toString()
        instance.text = text_content.text.toString()
        return instance
    }
}