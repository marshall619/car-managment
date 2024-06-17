package com.example.carmanagement

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import java.util.Base64
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.carmanagement.navigation.SetupNavHost
import com.example.carmanagement.ui.screen.CustomTextFiled
import com.example.carmanagement.ui.screen.bitmapToBase64
import com.example.carmanagement.ui.theme.CarManagementTheme
import com.example.carmanagement.ui.theme.MainRedColor
import com.example.carmanagement.ui.theme.WhiteColor
import com.example.carmanagement.util.Constants
import com.example.carmanagement.util.PreferencesManager
import kotlinx.coroutines.launch
import kotlin.io.encoding.ExperimentalEncodingApi

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarManagementTheme(darkTheme = false) {

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val activity = LocalContext.current as Activity
                val navHostController = rememberNavController()

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            NavigationDrawerContent(navHostController, activity) {
                                scope.launch {
                                    drawerState.close()
                                }
                            }
                        }
                    },
                    gesturesEnabled = true
                ) {
                    Scaffold(topBar = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MainRedColor)
                                .padding(16.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.menu),
                                contentDescription = null,
                                Modifier
                                    .size(30.dp)
                                    .clickable {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    },
                                tint = Color.White
                            )
                        }
                    }) { padding ->
                        SetupNavHost(navHostController = navHostController)
                        //خط الکی
                        Box(modifier = Modifier.padding(padding))
                    }
                }
            }
        }
    }

    @Composable
    fun NavigationDrawerContent(
        navHostController: NavHostController,
        activity: Activity,
        closeDrawer: () -> Unit


    ) {
        val context = LocalContext.current
        var userName by remember { mutableStateOf("") }
        var userNameText by remember { mutableStateOf("نام و نام خانوادگی") }
        userNameText = PreferencesManager.getUserNameValue(context).toString()
        var showDialog by remember {
            mutableStateOf(false)
        }

        if (showDialog) {
            CustomDialog(showDialog = showDialog, onDismissRequest = { showDialog = it }) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomTextFiled(
                        value = userName,
                        hint = "نام کاربری",
                        textColor = Color.Black,
                        onValueChange = { userName = it })
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        if (userName.isNotEmpty()){
                            PreferencesManager.setUserNameValue(context , userName)
                            userNameText = userName
                            showDialog = false
                        }else{
                            Toast.makeText(context, "لطفا نام کاربری را انتخاب کنید", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text(text = "ذخیره")
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MainRedColor)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
            ) {

                Text(
                    text = userNameText, fontSize = 18.sp, modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            WhiteColor
                        )
                        .padding(vertical = 8.dp, horizontal = 4.dp)
                )

                Spacer(modifier = Modifier.width(30.dp))


                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = null,
                    Modifier
                        .size(30.dp)
                        .clickable {
                            showDialog = true
                        })
            }


            Column(
                Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "درباره", fontSize = 18.sp, modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            navHostController.navigate(Constants.ABOUT_SCREEN)
                            closeDrawer()
                        }
                        .background(
                            WhiteColor
                        )
                        .padding(vertical = 12.dp, horizontal = 60.dp)
                )
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = "خروج", fontSize = 18.sp, modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            activity.finish()
                            closeDrawer()
                        }
                        .background(
                            WhiteColor
                        )
                        .padding(vertical = 12.dp, horizontal = 60.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "v 1.0", fontSize = 18.sp, modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            WhiteColor
                        )
                        .padding(vertical = 8.dp, horizontal = 30.dp)
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

    }
}


@Composable
fun CustomDialog(
    showDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    content: @Composable () -> Unit,
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = {
                onDismissRequest(false)
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .pointerInput(Unit) { detectTapGestures { } }
                        .shadow(8.dp, shape = RoundedCornerShape(16.dp))
                        .width(300.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            MaterialTheme.colorScheme.surface,
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    content()
                }

            }
        }
    }
}
