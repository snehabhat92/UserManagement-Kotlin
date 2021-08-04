package com.example.myapplication.Network

import com.example.myapplication.Model.User
import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("users")
    fun allUsers() : Call<List<User>>
}