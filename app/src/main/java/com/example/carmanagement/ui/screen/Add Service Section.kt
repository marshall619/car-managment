package com.example.carmanagement.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import com.example.carmanagement.util.ServiceDbActions
import com.gmail.hamedvakhide.compose_jalali_datepicker.JalaliDatePickerDialog

@Composable
fun AddServiceScreen(navHostController: NavHostController, carId: Int) {

    val context = LocalContext.current

    val serviceDao = MainDataBase.getDataBase(context).serviceDao()

    var serviceName by remember {
        mutableStateOf("")
    }
    var serviceDes by remember {
        mutableStateOf("")
    }
    var servicePrice by remember {
        mutableStateOf("")
    }
    var serviceDate by remember {
        mutableStateOf("تاریخی انتخاب نشده")
    }

    val openDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBackGroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTextFiled(
            value = serviceName,
            hint = "نام سرویس",
            onValueChange = { serviceName = it })
        Spacer(modifier = Modifier.height(30.dp))
        CustomTextFiled(
            value = serviceDes,
            hint = "کارکرد ماشین(قبل از سرویس)",
            onValueChange = { serviceDes = it })
        Spacer(modifier = Modifier.height(30.dp))
        CustomTextFiled(
            value = servicePrice,
            hint = "هزینه سرویس",
            onValueChange = { servicePrice = it })
        Spacer(modifier = Modifier.height(30.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, MainRedColor, RoundedCornerShape(18.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = serviceDate, color = Color.White, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        CustomButton(title = "انتخاب تاریخ", color = Color(0xFF009688)) {
            openDialog.value = true
        }
        Spacer(modifier = Modifier.height(30.dp))
        CustomButton("ثبت سرویس") {
            if (serviceName.isNotEmpty() && serviceDes.isNotEmpty() && servicePrice.isNotEmpty() && serviceDate != "تاریخی انتخاب نشده") {
                ServiceDbActions.insertNewService(
                    serviceDao,
                    Services(
                        name = serviceName,
                        carDestination = serviceDes,
                        price = servicePrice,
                        date = serviceDate,
                        carId = carId
                    )
                )
                navHostController.popBackStack()
            } else {
                Toast.makeText(context, "لطفا همه فیلد ها را پر کنید .", Toast.LENGTH_SHORT).show()
            }
        }
    }

    JalaliDatePickerDialog(
        openDialog = openDialog,
        onSelectDay = {},
        onConfirm = {
            val month = if (it.month< 10) "0${it.month}" else it.month
            val day = if (it.day< 10) "0${it.day}" else it.day

            serviceDate = "${it.year}/${month}/${day}"
        }
    )

}
