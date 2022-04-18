package com.sasarinomari.diary

import android.util.Log
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class APICall {
    abstract fun onError(message: String)

    fun getDays(param: GetDiaryParameter, callback: (Array<DiaryModel>)->Unit) {
        val call = APIInterface.api.getDays(APIInterface.token, param)
        call.enqueue(object : Callback<Array<DiaryModel>> {
            override fun onResponse(call: Call<Array<DiaryModel>>, response: Response<Array<DiaryModel>>) {
                if (response.isSuccessful) {
                    val tasks = response.body()!!
                    callback(tasks)
                } else {
                    onError("${response.code()} : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Array<DiaryModel>>, t: Throwable) {
                onError(t.toString())
            }
        })
    }

    fun getDaysWithDate(date: String, callback: (Array<DiaryModel>)->Unit) {
        val call = APIInterface.api.getDaysWithDate(APIInterface.token, date)
        call.enqueue(object : Callback<Array<DiaryModel>> {
            override fun onResponse(call: Call<Array<DiaryModel>>, response: Response<Array<DiaryModel>>) {
                if (response.isSuccessful) {
                    val result = response.body()!!
                    callback(result)
                } else {
                    onError("${response.code()} : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Array<DiaryModel>>, t: Throwable) {
                onError(t.toString())
            }
        })
    }

    fun getRandomDay(isFindingCorrectionTarget: Boolean, callback: (DiaryModel)->Unit) {
        val call = APIInterface.api.getRandomDay(APIInterface.token, isFindingCorrectionTarget)
        call.enqueue(object : Callback<DiaryModel> {
            override fun onResponse(call: Call<DiaryModel>, response: Response<DiaryModel>) {
                if (response.isSuccessful) {
                    val result = response.body()!!
                    callback(result)
                } else {
                    onError("${response.code()} : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DiaryModel>, t: Throwable) {
                onError(t.toString())
            }
        })
    }

    fun createDay(task: DiaryModel, callback: (JsonObject)->Unit) {
        val call = APIInterface.api.createDay(APIInterface.token, task)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()!!
                    Log.d("API_NEW_DAY", result.toString())
                    callback(result)
                } else {
                    onError("${response.code()} : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onError(t.toString())
            }
        })
    }

    fun modifyDay(task: DiaryModel, callback: ()->Unit) {
        val call = APIInterface.api.modifyDay(APIInterface.token, task)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()!!
                    Log.d("API_MODIFY_DAY", result.toString())
                    callback()
                } else {
                    onError("${response.code()} : ${response.message()}")
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
        val call = APIInterface.api.deleteDay(APIInterface.token, task)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()!!
                    Log.d("API_REMOVE_DAY", result.toString())
                    callback()
                } else {
                    onError("${response.code()} : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onError(t.toString())
            }

        })
    }
}