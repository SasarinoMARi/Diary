package com.sasarinomari.diary

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_day_detail.*


class DayDetailActivity : AppCompatActivity() {
    private val api = object : APICall() {
        override fun onError(message: String) {
            Log.e("Error", message)
        }
    }

    private val conv = DateConverter()
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK) {
                val diary = Gson().fromJson(it.data?.getStringExtra("diary"), DiaryModel::class.java)
                if(diary!=null) {
                    initializeViewWithDiary(diary)
                }
                this@DayDetailActivity.setResult(RESULT_OK)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_detail)

        val diary = Gson().fromJson(intent.getStringExtra("diary"), DiaryModel::class.java)!!
        initializeViewWithDiary(diary)
    }

    private fun initializeViewWithDiary(diary: DiaryModel) {
        text_title.text = conv.toDisplayable(diary.date)
        text_content.text = diary.text

        button_modify.setOnClickListener {
            val i = Intent(this@DayDetailActivity, DayWriteActivity::class.java)
            i.putExtra("diary", Gson().toJson(diary))
            resultLauncher.launch(i)
        }

        button_delete.setOnClickListener {
            val adb= AlertDialog.Builder(this)
            adb.setTitle(getString(R.string.DeleteConfirmDialog))
            adb.setPositiveButton(getString(R.string.OK)) { dialog, which ->
                api.deleteDay(diary.idx) {
                    Toast.makeText(this@DayDetailActivity, "일기를 삭제했습니다.", Toast.LENGTH_LONG).show()
                    setResult(RESULT_OK)
                    finish()
                }
            }
            adb.setNegativeButton(getString(R.string.Cancel)) { dialog, which ->
                //finish()
            }
            adb.show()
        }
    }
}