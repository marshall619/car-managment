package com.example.carmanagement.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import com.example.carmanagement.data.model.Services
import com.example.carmanagement.ui.theme.MainBackGroundColor
import com.example.carmanagement.ui.theme.MainRedColor
import com.example.carmanagement.ui.theme.WhiteColor
import com.example.carmanagement.util.Constants
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@Composable
fun ServiceScreen(navHostController: NavHostController, carId: Int) {

    val context = LocalContext.current
    val serviceDao = MainDataBase.getDataBase(context).serviceDao()
    var loading by remember { mutableStateOf(true) }
    var serviceList by remember { mutableStateOf<List<Services>>(emptyList()) }

    serviceDao.getAllServices(carId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Observer<List<Services>> {
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: List<Services>) {
                serviceList = t
                loading = false
            }

        })

    if (loading) {
        LoadingScreen()
    } else {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navHostController.navigate(Constants.ADD_SERVICE_SCREEN + "/$carId")
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
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(serviceList){
                    ServiceItem(it){
                        navHostController.navigate(Constants.EDIT_SERVICE_SCREEN + "/${it.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceItem(services: Services , onClicked : () -> Unit) {
    Text(
        text = services.name, fontSize = 18.sp, modifier = Modifier
            .clip(CircleShape)
            .clickable {
                onClicked()
            }
            .background(
                WhiteColor
            )
            .padding(vertical = 12.dp, horizontal = 60.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
}