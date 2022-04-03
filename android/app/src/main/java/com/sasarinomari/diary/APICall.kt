package com.sasarinomari.diary

import android.util.Log
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class APICall {
    abstract fun onError(message: String)
    abstract fun onMessage(message: String)

    private var token: String? = ""

    // region Task API

    fun getDays(option: String, callback: (Array<String>)->Unit) {
        val call = APIInterface.api.getDays(token!!, option)
        call.enqueue(object : Callback<Array<String>> {
            override fun onResponse(call: Call<Array<String>>, response: Response<Array<String>>) {
                if (response.isSuccessful) {
                    val tasks = response.body()!!
                    callback(tasks)
                } else {
                    onMessage("${response.code()} : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Array<String>>, t: Throwable) {
                onError(t.toString())
            }
        })
    }

    fun getDay(date: String, callback: (DiaryModel)->Unit) {
        val task = DiaryModel()
        task.date = date
        val call = APIInterface.api.getDay(token!!, task)
        call.enqueue(object : Callback<DiaryModel> {
            override fun onResponse(call: Call<DiaryModel>, response: Response<DiaryModel>) {
                if (response.isSuccessful) {
                    val result = response.body()!!
                    callback(result)
                } else {
                    onMessage("${response.code()} : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DiaryModel>, t: Throwable) {
                onError(t.toString())
            }
        })
    }

    fun createDay(task: DiaryModel, callback: (JsonObject)->Unit) {
        val call = APIInterface.api.createDay(token!!, task)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()!!
                    Log.d("API_NEW_DAY", result.toString())
                    callback(result)
                } else {
                    onMessage("${response.code()} : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onError(t.toString())
            }
        })
    }

    // 변경할 부분만 Task Object에 넣으면 된다. 나머지 요소는 null
    fun modifyDay(task: DiaryModel, callback: ()->Unit) {
        val call = APIInterface.api.modifyDay(token!!, task)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()!!
                    Log.d("API_MODIFY_DAY", result.toString())
                    callback()
                } else {
                    onMessage("${response.code()} : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onError(t.toString())
            }
        })
    }

    fun deleteDay(id: Int, callback: ()->Unit) {
        val task = DiaryModel()
        task.idx = id
        val call = APIInterface.api.deleteDay(token!!, task)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()!!
                    Log.d("API_REMOVE_DAY", result.toString())
                    callback()
                } else {
                    onMessage("${response.code()} : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onError(t.toString())
            }

        })
    }
}