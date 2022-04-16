package com.sasarinomari.diary

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface APIInterface {
    @POST("getDays")
    fun getDays(@Header("key") token:String, @Body body: GetDiaryParameter): Call<Array<DiaryModel>>
    @POST("getDay")
    fun getDay(@Header("key") token:String, @Body body: DiaryModel): Call<DiaryModel>
    @POST("createDay")
    fun createDay(@Header("key") token:String, @Body body: DiaryModel): Call<JsonObject>
    @POST("modifyDay")
    fun modifyDay(@Header("key") token:String, @Body body: DiaryModel): Call<JsonObject>
    @POST("deleteDay")
    fun deleteDay(@Header("key") token:String, @Body body: DiaryModel): Call<JsonObject>

    companion object {
        private val BASE_URL = ""
        const val token: String = ""

        private val gson = GsonBuilder()
            .setLenient()
            .create()

        private val okHttpClient = OkHttpClient
            .Builder()
            .build()

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        val api: APIInterface by lazy {
            retrofit.create(APIInterface::class.java)
        }
    }
}