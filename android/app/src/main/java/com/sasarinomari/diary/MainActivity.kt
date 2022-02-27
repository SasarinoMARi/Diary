package com.sasarinomari.diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_main)

        button_getDays.setOnClickListener {
            api.getDays {
                for(date in it) {
                    Log.i("API_RESULT", date)
                }
            }
        }

        button_getDay.setOnClickListener {
            api.getDay("2000-01-01") {
                Log.i("API_RESULT", "${it.text}")
            }
        }

        button_createDay.setOnClickListener {
            val model = DiaryModel()
            model.text = "안드로이드에서 생성된 본문"
            model.date = "2000-01-01"

            api.createDay(model) {
                Log.i("API_RESULT", "OK")
            }
        }
        button_modifyDay.setOnClickListener {
            val model = DiaryModel()
            model.idx = 353
            model.text = "안드로이드에서 생성된 본문 2"
            model.date = "2000-01-02"

            api.modifyDay(model) {
                Log.i("API_RESULT", "OK")
            }
        }
        button_deleteDay.setOnClickListener {
            api.deleteDay(353) {
                Log.i("API_RESULT", "OK")
            }
        }
    }
}