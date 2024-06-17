package com.example.carmanagement.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.carmanagement.data.model.Car
import com.example.carmanagement.data.model.Services
import com.example.carmanagement.data.model.UserModel

@Database(entities = [Car::class , Services::class , UserModel :: class], version = 2 , exportSchema = false)
abstract class MainDataBase : RoomDatabase() {

    abstract fun carDao() : CarDao
    abstract fun serviceDao() : ServiceDao
    abstract fun userDao() : UserDao

    companion object {

        private val database: MainDataBase? = null

        fun getDataBase(context: Context): MainDataBase {
            var instance = database
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDataBase::class.java,
                    "mainDataBase"
                )
                    .allowMainThreadQueries()
                    .build()

            }
            return instance
        }
    }


}