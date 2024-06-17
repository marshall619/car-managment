package com.example.carmanagement.ui.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.carmanagement.MainActivity
import com.example.carmanagement.ui.theme.MainRedColor
import com.example.carmanagement.ui.theme.WhiteColor
import kotlinx.coroutines.launch
import com.example.carmanagement.R
import com.example.carmanagement.data.db.CarDao
import com.example.carmanagement.data.db.MainDataBase
import com.example.carmanagement.data.model.Car
import com.example.carmanagement.ui.theme.MainBackGroundColor
import com.example.carmanagement.util.CarDbActions
import com.example.carmanagement.util.Constants
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeScreen(navHostController: NavHostController) {

    val context = LocalContext.current

    //get all cars
    val carDao = MainDataBase.getDataBase(context).carDao()
    var carList by remember { mutableStateOf<List<Car>>(emptyList()) }

    carDao.getAllCars()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Observer<List<Car>> {
            override fun onSubscribe(d: Disposable) {}

            override fun onError(e: Throwable) {}

            override fun onComplete() {}

            override fun onNext(t: List<Car>) {
                carList = t
            }
        })
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navHostController.navigate(Constants.ADD_CAR_SCREEN)
                },
                shape = CircleShape,
                containerColor = MainRedColor
            ) {
                Icon(Icons.Filled.Add, "", tint = Color.White)
            }
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MainBackGroundColor)
                .padding(top = 80.dp)
                .padding(paddingValues)
        ) {
            items(carList) {
                CarItem(it, carDao, navHostController) { car ->
                    navHostController.navigate(Constants.CAR_SCREEN + "/${car.id}")
                }
            }
        }
    }

}


@Composable
fun CarItem(
    car: Car,
    dao: CarDao,
    navHostController: NavHostController,
    onCarClicked: (Car) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onCarClicked(car)
            }
            .background(WhiteColor)
            .padding(8.dp), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = car.name, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = car.model, fontSize = 14.sp)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = null,
                Modifier
                    .size(30.dp)
                    .clickable {
                        navHostController.navigate(Constants.EDIT_CAR_SCREEN + "/${car.id}")
                    }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = null,
                Modifier
                    .size(30.dp)
                    .clickable {
                        CarDbActions.deleteCar(dao, car)
                    }
            )
        }
    }

}
