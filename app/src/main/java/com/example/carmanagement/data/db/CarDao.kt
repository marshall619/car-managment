package com.example.carmanagement.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.carmanagement.data.model.Car
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface CarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateCar(car : Car) : Completable

    @Delete
    fun deleteCar(car: Car) : Completable

    @Query("select * from car")
    fun getAllCars() : Observable<List<Car>>

    @Query("delete from car where id =:id")
    fun deleteCarById(id : Int) : Completable

    @Query("select * from car where id =:id")
    fun findCarById(id: Int) : Single<Car>

}