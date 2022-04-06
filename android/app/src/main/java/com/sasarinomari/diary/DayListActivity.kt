package com.sasarinomari.diary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListAdapter
import android.widget.SimpleAdapter
import kotlinx.android.synthetic.main.activity_day_list.*
import kotlinx.android.synthetic.main.item_diary.view.*


class DayListActivity : DiaryActivity() {
    private val conv = DateConverter()

    private val api = object : APICall() {
        override fun onError(message: String) {
            Log.e("Error", message)
        }

        override fun onMessage(message: String) {
            Log.i("Activity", message)
        }
    }

    private var sortOption = "Default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_list)

        button_sort_default.setOnClickListener {
            sortOption = "Default"
            fetch()
        }
        button_sort_added.setOnClickListener {
            sortOption = "Recent"
            fetch()
        }
        button_sort_modified.setOnClickListener {
            sortOption = "LastModified"
            fetch()
        }

        fetch()

    }

    private fun fetch() {
        api.getDays(sortOption) {
            val adapter = buildAdapter(it)
            listview.adapter = adapter
        }
    }

    private fun buildAdapter(diaryList: Array<String>): ListAdapter? {
        val arrayList: ArrayList<HashMap<String, String>> = ArrayList()
        for (i in diaryList.indices) {
            val hashMap: HashMap<String, String> = HashMap()
            hashMap["name"] = conv.toDisplayable(diaryList[i])
            arrayList.add(hashMap)
        }
        val from = arrayOf("name")
        val to = intArrayOf(R.id.text_date)
        val adapter = SimpleAdapter(this, arrayList, R.layout.item_diary, from, to)
        listview.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this@DayListActivity, DayDetailActivity::class.java)
            intent.putExtra("date", diaryList[i])
            startActivity(intent)
        }
        return adapter
    }
}