package com.sasarinomari.diary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_day_detail.*

class DayDetailActivity : AppCompatActivity() {
    private val conv = DateConverter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_detail)

        val diary = Gson().fromJson(intent.getStringExtra("diary"), DiaryModel::class.java)!!

        text_title.text = conv.toDisplayable(diary.date)
        text_content.text = diary.text

        button_modify.setOnClickListener {
            val i = Intent(this@DayDetailActivity, DayWriteActivity::class.java)
            i.putExtra("diary", Gson().toJson(diary))
            startActivity(i)
        }
    }
}