package com.example.carmanagement.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.carmanagement.data.db.MainDataBase
import com.example.carmanagement.data.model.Car
import com.example.carmanagement.ui.theme.MainBackGroundColor
import com.example.carmanagement.ui.theme.MainRedColor
import com.example.carmanagement.util.CarDbActions

@Composable
fun AddCarScreen(navHostController: NavHostController) {

    val userId = 1

    val context = LocalContext.current

    val carDao = MainDataBase.getDataBase(context).carDao()

    var carName by remember {
        mutableStateOf("")
    }
    var carModel by remember {
        mutableStateOf("")
    }
    var carColor by remember {
        mutableStateOf("")
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
        CustomButton("ثبت ماشین") {
            if (carName.isNotEmpty() && carModel.isNotEmpty() && carModel.isNotEmpty()) {
                CarDbActions.insertNewCar(
                    carDao,
                    Car(name = carName, model = carModel,color = carColor , userId = userId)
                )
                navHostController.popBackStack()
            } else {
                Toast.makeText(context, "لطفا همه فیلد ها را پر کنید .", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


@Composable
fun CustomTextFiled(value: String, hint: String ,textColor : Color = Color.White, onValueChange: (String) -> Unit) {

    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(text = hint, color = textColor) },
        textStyle = TextStyle(color = textColor, textDirection = TextDirection.Content),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MainRedColor
        )
    )

}

@Composable
fun CustomButton(title: String , color : Color = MainRedColor, onClicked: () -> Unit) {
    Button(
        onClick = { onClicked() },
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        shape = CircleShape
    ) {
        Text(text = title, color = Color.White, fontSize = 20.sp)
    }
}