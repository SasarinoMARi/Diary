package com.sasarinomari.diary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class CorrectingRandomDayActivity : AppCompatActivity() {
    private val api = object: APICall() {
        override fun onError(message: String) {
            Log.e("CorrectingRandomDay", message)
        }
    }
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK) {
                setResult(RESULT_OK)
            }
            finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        api.getRandomDay(true) {
            val i = Intent(this, DayWriteActivity::class.java)
            i.putExtra("diary", Gson().toJson(it))
            resultLauncher.launch(i)
        }
    }
}