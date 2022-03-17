package com.sasarinomari.diary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_day_detail.*

class DayDetailActivity : DiaryActivity() {

    private val api = object : APICall() {
        override fun onError(message: String) {
            Log.e("Error", message)
        }

        override fun onMessage(message: String) {
            Log.i("Activity", message)
        }
    }

    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_detail)

        fetchDay()

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK) {
                fetchDay()
            }
        }
    }

    private fun fetchDay() {
        val date = intent.getStringExtra("date")!!
        api.getDay(date) { diary ->
            text_title.text = diary.date
            text_content.text = diary.text

            button_modify.setOnClickListener {
                val json = Gson().toJson(diary)
                val i = Intent(this@DayDetailActivity, DayModifyActivity::class.java)
                i.putExtra("diary", json)
                launcher.launch(i)
            }
        }
    }
}