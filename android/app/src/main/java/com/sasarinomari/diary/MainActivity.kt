package com.sasarinomari.diary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.ListAdapter
import android.widget.SimpleAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val api = object : APICall() {
        override fun onError(message: String) {
            Log.e("Error", message)
        }
    }
    private val fetchOption = GetDiaryParameter()
    private val adapter = DiaryAdapter(this)
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK) {
                adapter.clear()
                getDiary()
            }
        }

    private var lastItemVisibleFlag = false     // 스크롤이 맨 밑인지
    private var mLockListView = false           // 현재 데이터를 가져오는 중인지

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter.setActivityResultLauncher(resultLauncher)

        listview.adapter = adapter
        listview.setOnScrollListener(object: AbsListView.OnScrollListener {
            override fun onScrollStateChanged(absListView: AbsListView?, scrollState: Int) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && !mLockListView) {
                    fetchOption.page++
                    getDiary()
                }
            }

            override fun onScroll(absListView: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
        })

        button_write.setOnClickListener {
            val i = Intent(this@MainActivity, DayWriteActivity::class.java)
            resultLauncher.launch(i)
        }
        button_random_view.setOnClickListener {
            val i = Intent(this@MainActivity, SelectRandomDayActivity::class.java)
            resultLauncher.launch(i)
        }
        button_random_correct.setOnClickListener {
            val i = Intent(this@MainActivity, CorrectingRandomDayActivity::class.java)
            resultLauncher.launch(i)
        }

        getDiary()
    }

    private fun getDiary() {
        mLockListView = true
        api.getDays(fetchOption) { results ->
            adapter.append(results.toList())
            adapter.notifyDataSetChanged()
            mLockListView = false
            listview.scrollTo(0, -1)
        }
    }
}