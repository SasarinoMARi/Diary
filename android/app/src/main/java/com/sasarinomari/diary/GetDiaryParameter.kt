package com.sasarinomari.diary

import com.google.gson.annotations.SerializedName

class GetDiaryParameter{
    @SerializedName("order_type")
    var orderType: Int = ORDER_DEFAULT
    @SerializedName("page")
    var page: Int = 0

    companion object {
        const val ORDER_DEFAULT = 0         // 일기 날짜순 정렬
        const val ORDER_LAST_MODIFY = 1     // 최종 수정일순 정렬
        const val ORDER_CREATED_AT = 2      // 등록일자순 정렬
    }
}