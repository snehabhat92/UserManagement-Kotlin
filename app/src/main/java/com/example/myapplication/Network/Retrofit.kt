package com.example.myapplication.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {
    private var retrofitService: Api? = null
    fun getInstance(): Api {
        if (retrofitService == null) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Url.URL_DATA)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofitService = retrofit.create(Api::class.java)

        }
        return retrofitService!!
    }
}