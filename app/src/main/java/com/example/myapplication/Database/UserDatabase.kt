package com.example.myapplication.Database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.Dao.UserDao
import com.example.myapplication.Model.Converters
import com.example.myapplication.Model.User

@Database(entities = [User::class], version = 1)
@TypeConverters(Converters::class)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao?

    companion object {
        private val DATABASE_NAME = "UserDatabase"

        private var INSTANCE: UserDatabase? = null

        @Synchronized
        fun getInstance(context: Context?): UserDatabase {
            if (INSTANCE == null) {
                synchronized(UserDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context!!, UserDatabase::class.java,
                            DATABASE_NAME
                        )
                            .addCallback(callback)
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }

        val callback: RoomDatabase.Callback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateAsynTask(INSTANCE)
            }
        }


    }
    internal class PopulateAsynTask(userDatabase: com.example.myapplication.Database.UserDatabase?) :
        AsyncTask<Void?, Void?, Void?>() {
        private val userDao: UserDao
        protected override fun doInBackground(vararg params: Void?): Void? {
            userDao.deleteAll()
            return null
        }

        init {
            userDao = userDatabase?.userDao()!!
        }
    }
}