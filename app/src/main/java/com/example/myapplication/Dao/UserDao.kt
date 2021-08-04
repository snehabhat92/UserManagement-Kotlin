
package com.example.myapplication.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.Model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userList: List<User?>?)

    @get:Query("SELECT * FROM User")
    val allUsers: LiveData<List<User?>?>?

    @Query("DELETE FROM User")
    fun deleteAll()

    @Query("SELECT * FROM User WHERE User.firstname LIKE :search || '%' OR User.lastname LIKE :search || '%' OR username LIKE :search || '%' ORDER BY User.firstname ASC")
    fun getSearchedUsersList(search: String?): List<User?>?
}
