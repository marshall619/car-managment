package com.example.carmanagement.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.carmanagement.ui.theme.MainBackGroundColor
import com.example.carmanagement.ui.theme.WhiteColor
import com.example.carmanagement.R

@Composable
fun AboutScreen(navHostController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBackGroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(WhiteColor)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Text(
                    text = stringResource(id = R.string.about),
                    color = Color.Black,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Justify,

                    )
            }
        }

        Box(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(WhiteColor)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column (horizontalAlignment = Alignment.CenterHorizontally){
                Text(
                    text = "Another",
                    color = Color.Black,
                    fontSize = 18.sp,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(id = R.string.creator),
                    color = Color.Black,
                    fontSize = 18.sp,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold
                )
            }

        }


    }

}