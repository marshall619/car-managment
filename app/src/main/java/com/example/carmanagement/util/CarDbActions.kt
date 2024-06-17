package com.example.carmanagement.util

import com.example.carmanagement.data.db.CarDao
import com.example.carmanagement.data.model.Car
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

object CarDbActions {


    fun insertNewCar(dao: CarDao, car: Car) {

        dao.insertOrUpdateCar(car)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}

                override fun onComplete() {}

                override fun onError(e: Throwable) {}
            })

    }

    fun deleteCar(dao: CarDao, car: Car) {

        dao.deleteCar(car)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}

                override fun onComplete() {}

                override fun onError(e: Throwable) {}
            })

    }


}