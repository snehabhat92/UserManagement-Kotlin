package com.example.myapplication.Repository

import android.app.Activity
import android.app.Application
import android.os.AsyncTask
import android.util.Log
import com.example.myapplication.Dao.UserDao
import com.example.myapplication.Database.UserDatabase
import com.example.myapplication.Model.User
import com.example.myapplication.Network.Api
import java.util.*
import java.util.concurrent.ExecutionException

open class UserRepository constructor(private val retrofitService: Api, application: Application?) {
    private var database : UserDatabase = UserDatabase.getInstance(application)

    fun getAllUsers() = database.userDao()?.allUsers


    open fun insert(userList: List<User>) {
        InsertAsyncTask(database).execute(userList)
    }

    open fun query(editSearch: String?): List<User?>? {
        try {
            return QueryAsyncTask(database)
                .execute(editSearch).get()
        } catch (exception: InterruptedException) {
            Log.e("QueryException", "Exception$exception")
        } catch (exception: ExecutionException) {
            Log.e("QueryException", "Exception$exception")
        }
        return null
    }

    internal class InsertAsyncTask(userDatabase: UserDatabase) :
        AsyncTask<List<User>, Void, Void>() {
        private val userDao: UserDao
        protected override fun doInBackground(vararg params: List<User>): Void? {
            val users = params[0]
            val updatedUserList: MutableList<User> = ArrayList()
            for (i in users.indices) {
                val user = users[i]
                if (user.name != null) {
                    val nameArray: Array<String> = user.name!!.split(" ").toTypedArray()
                    if (nameArray.size >= 2) {
                        user.lastname = nameArray[1]
                    }
                    user.firstname = nameArray[0]
                    updatedUserList.add(user)
                }
            }
            userDao.insert(updatedUserList)
            return null
        }

        init {
            userDao = userDatabase.userDao()!!
        }
    }

    internal class QueryAsyncTask(userDatabase: UserDatabase) :
        AsyncTask<String, Void, List<User?>?>() {
        private val userDao: UserDao
        protected override fun doInBackground(vararg strings: String): List<User?>? {
            return userDao.getSearchedUsersList(strings[0])
        }

        init {
            userDao = userDatabase.userDao()!!
        }
    }
}