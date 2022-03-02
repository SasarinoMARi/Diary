package com.sasarinomari.diary

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import kotlinx.android.synthetic.main.item_diary.view.*

class DiaryAdapter(
    private val context: Context,
    private val items: Array<String>
) : BaseAdapter() {
    override fun getCount(): Int = items.size
    override fun getItem(position: Int): String = items[position]
    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var convertView = view
        if (convertView == null) convertView = LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_diary, parent, false)
        val item = items[position]

        convertView!!.text_date.text = item

        convertView.setOnClickListener {
            val i = Intent(context, DayDetailActivity::class.java)
            i.putExtra("date", item)
            context.startActivity(i)
        }
        return convertView
    }
}
