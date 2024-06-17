package com.example.carmanagement.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.carmanagement.data.model.Services
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface ServiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateService(services: Services) : Completable

    @Delete
    fun deleteService(services: Services) : Completable

    @Query("select * from services where carId =:carId")
    fun getAllServices( carId :Int ) : Observable<List<Services>>

    @Query("delete from services where id =:id")
    fun deleteServiceById(id : Int) : Completable

    @Query("select * from services where id =:id")
    fun findServiceById(id: Int) : Single<Services>

    @Query("select * from services where name like ('%' || :searchQuery || '%') and carId =:carId")
    fun searchServiceName(searchQuery: String , carId: Int): Single<List<Services>>

}