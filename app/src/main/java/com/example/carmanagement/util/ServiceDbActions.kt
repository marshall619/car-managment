package com.example.carmanagement.util

import com.example.carmanagement.data.db.ServiceDao
import com.example.carmanagement.data.model.Services
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

object ServiceDbActions {


    fun insertNewService(dao: ServiceDao, services: Services) {

        dao.insertOrUpdateService(services)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}

                override fun onComplete() {}

                override fun onError(e: Throwable) {}
            })

    }

    fun deleteService(dao: ServiceDao, services: Services) {

        dao.deleteService(services)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}

                override fun onComplete() {}

                override fun onError(e: Throwable) {}
            })

    }


}