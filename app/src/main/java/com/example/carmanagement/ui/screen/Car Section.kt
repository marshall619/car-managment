package com.example.carmanagement.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.carmanagement.data.db.MainDataBase
import com.example.carmanagement.data.model.Car
import com.example.carmanagement.ui.theme.GrayColor
import com.example.carmanagement.ui.theme.MainBackGroundColor
import com.example.carmanagement.ui.theme.WhiteColor
import com.example.carmanagement.util.Constants
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@Composable
fun CarScreen(navHostController: NavHostController, id: Int) {

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

    if (loading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MainBackGroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Loading ........")
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MainBackGroundColor)
                .padding(top = 100.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(GrayColor)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = foundedCar.name, color = Color.White, fontSize = 16.sp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                        .padding(top = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = foundedCar.model, color = Color.White, fontSize = 16.sp)
                    Text(text = foundedCar.color, color = Color.White, fontSize = 16.sp)
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "مشخصات خودرو", fontSize = 18.sp, modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            navHostController.navigate(Constants.EDIT_CAR_SCREEN + "/${foundedCar.id}")
                        }
                        .background(
                            WhiteColor
                        )
                        .padding(vertical = 12.dp, horizontal = 60.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "سرویس ها", fontSize = 18.sp, modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            navHostController.navigate(Constants.SERVICE_SCREEN + "/${foundedCar.id}")
                        }
                        .background(
                            WhiteColor
                        )
                        .padding(vertical = 12.dp, horizontal = 60.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "گزارش", fontSize = 18.sp, modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            navHostController.navigate(Constants.REPORT_SCREEN + "/${foundedCar.id}")
                        }
                        .background(
                            WhiteColor
                        )
                        .padding(vertical = 12.dp, horizontal = 60.dp)
                )
            }

        }
    }


}