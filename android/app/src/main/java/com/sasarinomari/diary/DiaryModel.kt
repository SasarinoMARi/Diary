package com.sasarinomari.diary

import com.google.gson.annotations.SerializedName


class DiaryModel {
    @SerializedName("idx")
    var idx: Int? = null
    @SerializedName("date")
    var date: String? = null
    @SerializedName("text")
    var text: String? = null
    @SerializedName("last_modify")
    var lastModify: String? = null
    @SerializedName("felling")
    var felling: String? = null
}