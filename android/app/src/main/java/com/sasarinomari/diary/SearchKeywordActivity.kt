package com.sasarinomari.diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.activity_search_keyword.*

class SearchKeywordActivity : AppCompatActivity() {
    private val api = object : APICall() {
        override fun onError(message: String) {
            Log.e("Error", message)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_keyword)

        val adapter = DiaryAdapter(this@SearchKeywordActivity)
        listview.adapter = adapter

        button_search.setOnClickListener {
            // text_keyword.setText(text_keyword.text.trim())
            val keyword = text_keyword.text.trim().toString()
            if (keyword.isBlank()) return@setOnClickListener

            api.getDayWithKeyword(keyword) { days ->
                adapter.clear()
                adapter.append(days.toList())
                adapter.notifyDataSetChanged()
            }
        }

        text_keyword.setOnEditorActionListener { textView, actionId, keyEvent ->
            return@setOnEditorActionListener when(actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    button_search.callOnClick()
                    true
                }
                else -> false
            }
        }
    }
}