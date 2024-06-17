package com.example.carmanagement.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.carmanagement.data.model.Car
import com.example.carmanagement.data.model.UserModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface UserDao {

    @Insert
    fun insertUser(user : UserModel) : Completable

    @Query("SELECT password FROM usermodel WHERE userName = :userName LIMIT 1")
    fun authenticate(userName: String): Single<String?>

    @Query("select * from usermodel")
    fun getAllUser() : Single<List<UserModel>>

}