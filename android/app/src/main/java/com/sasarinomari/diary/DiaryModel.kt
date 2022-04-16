package com.sasarinomari.diary

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.item_diary.view.*
import java.text.SimpleDateFormat
import java.util.*

class DiaryModel {
    @SerializedName("idx")
    var idx: Int = -1
    @SerializedName("date")
    var date: String = ""
    @SerializedName("text")
    var text: String = ""
    @SerializedName("last_modify")
    var lastModify: String = ""
    @SerializedName("feeling")
    var feeling: String? = null
}

class DiaryAdapter(private val context: Context) : BaseAdapter() {
    private val df = SimpleDateFormat("yyyy-MM-dd EEEE hh:mm:ss", Locale.getDefault())
    private val items = ArrayList<DiaryModel>()

    companion object {
        private val dateConvertor = DateConverter()
    }

    fun clear() {
        items.clear()
    }

    fun append(new: List<DiaryModel>) {
        items.addAll(new)
    }

    override fun getCount(): Int = items.size
    override fun getItem(position: Int): DiaryModel = items[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val convertView = view?: LayoutInflater.from(parent?.context).inflate(R.layout.item_diary, parent, false)!!

        val item: DiaryModel = getItem(position)
        convertView.text_date.text = dateConvertor.toDisplayable(item.date)

        convertView.setOnClickListener {
            val intent = Intent(context, DayDetailActivity::class.java)
            intent.putExtra("diary", Gson().toJson(item))
            context.startActivity(intent)
        }

        val previewMaxLength = 100
        val previewMaxLine = 3

        var previewText = if(item.text.length < previewMaxLength) item.text else "${item.text.substring(0, previewMaxLength)}..."
        val lines = previewText.split("\n")
        if(lines.count() > 3) {
            previewText = "${lines.dropLast(lines.count()-previewMaxLine).joinToString("\n")}..."
        }

        convertView.text_preview.text = previewText


        return convertView
    }
}