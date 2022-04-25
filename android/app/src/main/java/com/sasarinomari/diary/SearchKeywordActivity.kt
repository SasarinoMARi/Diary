package com.sasarinomari.diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class SearchKeywordActivity : AppCompatActivity() {
    private val api = object : APICall() {
        override fun onError(message: String) {
            Log.e("Error", message)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_keyword)

        api.getDayWithKeyword("다정") {
            Log.i("TEST", it.size.toString())
        }
    }
}