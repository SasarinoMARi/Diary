package com.sasarinomari.diary

import android.content.Intent
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

        test.setOnClickListener {
            val i = Intent(this@MainActivity, DayListActivity::class.java)
            startActivity(i)
        }

        write.setOnClickListener {
            val i = Intent(this@MainActivity, DayWriteActivity::class.java)
            startActivity(i)
        }

    }
}