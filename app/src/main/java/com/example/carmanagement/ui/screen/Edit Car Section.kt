package com.example.carmanagement.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.carmanagement.data.db.MainDataBase
import com.example.carmanagement.data.model.Car
import com.example.carmanagement.ui.theme.MainBackGroundColor
import com.example.carmanagement.util.CarDbActions
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@Composable
fun EditCarScreen(navHostController: NavHostController, id: Int) {

    val context = LocalContext.current

    val carDao = MainDataBase.getDataBase(context).carDao()

    var foundedCar by remember { mutableStateOf(Car("", "", "", -1)) }
    var loading by remember { mutableStateOf(true) }

    carDao.findCarById(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : SingleObserver<Car> {
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {}
            override fun onSuccess(t: Car) {
                foundedCar = t
                loading = false
            }
        })
    if (!loading) {
        var carName by remember {
            mutableStateOf(foundedCar.name)
        }
        var carModel by remember {
            mutableStateOf(foundedCar.model)
        }
        var carColor by remember {
            mutableStateOf(foundedCar.color)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MainBackGroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextFiled(value = carName, hint = "نام ماشین", onValueChange = { carName = it })
            Spacer(modifier = Modifier.height(30.dp))
            CustomTextFiled(value = carModel, hint = "مدل ماشین", onValueChange = { carModel = it })
            Spacer(modifier = Modifier.height(30.dp))
            CustomTextFiled(value = carColor, hint = "رنگ ماشین", onValueChange = { carColor = it })
            Spacer(modifier = Modifier.height(60.dp))
            CustomButton("ویرایش ماشین"){
                if (carName.isNotEmpty() && carModel.isNotEmpty() && carModel.isNotEmpty()){
                    CarDbActions.insertNewCar(
                        carDao,
                        Car(name = carName, model = carModel, color = carColor , userId = foundedCar.userId , id = foundedCar.id)
                    )
                    navHostController.popBackStack()
                }else {
                    Toast.makeText(context, "لطفا همه فیلد ها را پر کنید .", Toast.LENGTH_SHORT).show()
                }
            }
        }
    } else {
        LoadingScreen()
    }

}

