package com.example.carmanagement.ui.screen

import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.carmanagement.data.db.MainDataBase
import com.example.carmanagement.data.db.UserDao
import com.example.carmanagement.data.model.Car
import com.example.carmanagement.data.model.UserModel
import com.example.carmanagement.ui.theme.MainBackGroundColor
import com.example.carmanagement.ui.theme.WhiteColor
import com.example.carmanagement.util.Constants
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.util.Base64

@Composable
fun LoginScreen(navHostController: NavHostController) {

    var base64String by remember { mutableStateOf("") }
    val contentResolver = LocalContext.current.contentResolver
    val context = LocalContext.current
    val userDao = MainDataBase.getDataBase(context).userDao()
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var userList by remember { mutableStateOf<List<UserModel>>(emptyList()) }

    userDao.getAllUser()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : SingleObserver<List<UserModel>> {
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {}
            override fun onSuccess(t: List<UserModel>) {
                userList = t
            }
        })

    val getImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            imageBitmap = bitmap.asImageBitmap()

            // Save to database
            base64String = bitmapToBase64(bitmap)
        }
    }

    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var userImageText by remember { mutableStateOf("انتخاب عکس پروفایل") }
    if (imageBitmap != null) {
        userImageText = "تعویض عکس"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBackGroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(WhiteColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "ورود به حساب کاربری",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "توجه داشته باشید اگر با نام کاربری شما قبلا ثبت نام نشده باشد به منزله ثبت نام در نظر گرفته خواهد شد",
                color = Color.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            CustomTextFiled(
                value = userName,
                hint = "نام کاربری",
                textColor = Color.Black,
                onValueChange = { userName = it })
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextFiled(
                value = password,
                hint = "رمز عبور",
                textColor = Color.Black,
                onValueChange = { password = it })
            Spacer(modifier = Modifier.height(16.dp))
            imageBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { getImage.launch("image/*") }) {
                Text(text = userImageText)
            }
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    if (userName.isNotEmpty() && password.isNotEmpty()){

                        if (checkSignIn(userName , userList)){
                            if(checkAuth(userName , password , userList)){
                                navHostController.navigate(Constants.HOME_SCREEN){
                                    popUpTo(Constants.LOGIN_SCREEN){
                                        inclusive = true
                                    }
                                }
                            }else{
                                Toast.makeText(context, "نام کاربری با پسورد مطابقت ندارد", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            if (imageBitmap != null){
                                singInUser(userDao , UserModel(userName = userName , password = password , image = base64String))
                                navHostController.navigate(Constants.HOME_SCREEN){
                                    popUpTo(Constants.LOGIN_SCREEN){
                                        inclusive = true
                                    }
                                }
                            }else{
                                Toast.makeText(context, "لطفا یک تصویر انتخاب کنید", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }else{
                        Toast.makeText(context, "لطفا مقادیر را وارد کنید ", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFEB3B)
                )
            ) {
                Text(text = "ورود | ثبت نام", color = Color.Black, fontSize = 18.sp)
            }
        }
    }

}

fun singInUser(userDao: UserDao, userModel: UserModel) {

    userDao.insertUser(userModel).observeOn(Schedulers.io())
        .subscribeOn(AndroidSchedulers.mainThread()).subscribe(
            object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}

                override fun onComplete() {}

                override fun onError(e: Throwable) {}

            }
        )

}

fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.getEncoder().encodeToString(byteArray)
}

fun checkSignIn(userName: String , userList: List<UserModel>) : Boolean {
    userList.forEach {
        if (userName == it.userName){
            return true
        }
    }

    return false
}

fun checkAuth(userName : String  , password : String , userList : List<UserModel>) : Boolean{

    userList.forEach {
        if (userName == it.userName && password == it.password){
            return true
        }
    }

    return false
}

