package com.example.myapplication.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.Model.User
import com.example.myapplication.Repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    var userList : LiveData<List<User?>?>? = null

    fun getAllUsers(): LiveData<List<User?>?>? {
        userList = repository.getAllUsers()
        return userList
    }

    fun insert(list: List<User>) {
        repository.insert(list)
    }

    fun query(searchText: String?): List<User?>? {
        return repository.query(searchText)
    }

}