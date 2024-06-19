package com.example.carmanagement.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.carmanagement.util.Constants
import com.gmail.hamedvakhide.compose_jalali_datepicker.JalaliDatePickerDialog
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@Composable
fun ReportScreen(navHostController: NavHostController, carId: Int) {

    val context = LocalContext.current
    val serviceDao = MainDataBase.getDataBase(context).serviceDao()
    var searchText by remember { mutableStateOf("") }
    var serviceList by remember { mutableStateOf<List<Services>>(emptyList()) }
    var allServices by remember { mutableStateOf<List<Services>>(emptyList()) }
    val openStartDialog = remember { mutableStateOf(false) }
    val openEndDialog = remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf("از تاریخ") }
    var startDay by remember { mutableIntStateOf(0) }
    var startMonth by remember { mutableIntStateOf(0) }
    var startYear by remember { mutableIntStateOf(0) }
    var endDate by remember { mutableStateOf("تا تاریخ") }
    var endDay by remember { mutableIntStateOf(0) }
    var endMonth by remember { mutableIntStateOf(0) }
    var endYear by remember { mutableIntStateOf(0) }


    LaunchedEffect(searchText) {
        serviceDao.searchServiceName(searchText, carId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Services>> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {}

                override fun onSuccess(t: List<Services>) {
                    serviceList = t
                }

            })
    }

    serviceDao.getAllServices(carId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Observer<List<Services>> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: List<Services>) {
                allServices = t
            }

            override fun onError(e: Throwable) {}
            override fun onComplete() {}
        })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBackGroundColor)
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTextFiled(
            value = searchText,
            hint = "جستجو کنید",
            onValueChange = { searchText = it })
        Spacer(modifier = Modifier.height(16.dp))

        Row (modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.Center){
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, MainRedColor, RoundedCornerShape(18.dp))
                    .clickable {
                        openEndDialog.value = true
                    }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = endDate, color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, MainRedColor, RoundedCornerShape(18.dp))
                    .clickable {
                        openStartDialog.value = true
                    }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = startDate, color = Color.White, fontSize = 18.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        CustomButton(title = "جستجو با تاریخ") {
            serviceList = findServicesBetween(
                allServices,
                startDate,
                endDate
            )
        }

        if (serviceList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(serviceList) {
                    ServiceItem(it) {
                        navHostController.navigate(Constants.EDIT_SERVICE_SCREEN + "/${it.id}")
                    }
                }
            }
        }
    }

    JalaliDatePickerDialog(
        openDialog = openStartDialog,
        onSelectDay = {},
        onConfirm = {
            val month = if (it.month< 10) "0${it.month}" else it.month
            val day = if (it.day< 10) "0${it.day}" else it.day

            startDate = "${it.year}/${month}/${day}"
            startDay = it.day
            startYear = it.year
            startMonth = it.month
        }
    )

    JalaliDatePickerDialog(
        openDialog = openEndDialog,
        onSelectDay = {},
        onConfirm = {
            val month = if (it.month< 10) "0${it.month}" else it.month
            val day = if (it.day< 10) "0${it.day}" else it.day

            endDate = "${it.year}/${month}/${day}"
            endDay = it.day
            endYear = it.year
            endMonth = it.month
        }
    )

}

fun findServicesBetween(
    list: List<Services>,
    startDate: String,
    endDate: String
): List<Services> {

    val foundedList = arrayListOf<Services>()
    val startIntDate = startDate.replace("/","").toInt()
    val endIntDate = endDate.replace("/","").toInt()

    list.forEach {
        val realDate = it.date.replace("/","").toInt()
        if (realDate in startIntDate..endIntDate){
            foundedList.add(it)
        }
    }

    Log.v("6191" , foundedList.toString())
    return foundedList
}

data class SimpleDate(
    val day: Int,
    val month: Int,
    val year: Int
)