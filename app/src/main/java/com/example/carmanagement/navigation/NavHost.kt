package com.example.carmanagement.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.carmanagement.ui.screen.AboutScreen
import com.example.carmanagement.ui.screen.AddCarScreen
import com.example.carmanagement.ui.screen.AddServiceScreen
import com.example.carmanagement.ui.screen.CarScreen
import com.example.carmanagement.ui.screen.DetailScreen
import com.example.carmanagement.ui.screen.EditCarScreen
import com.example.carmanagement.ui.screen.EditServiceScreen
import com.example.carmanagement.ui.screen.HomeScreen
import com.example.carmanagement.ui.screen.LoginScreen
import com.example.carmanagement.ui.screen.ReportScreen
import com.example.carmanagement.ui.screen.ServiceScreen
import com.example.carmanagement.util.Constants


@Composable
fun SetupNavHost(navHostController: NavHostController) {

    NavHost(navController = navHostController, startDestination = Constants.HOME_SCREEN) {

        composable(Constants.LOGIN_SCREEN){
            LoginScreen(navHostController = navHostController)
        }

        composable(Constants.HOME_SCREEN ) {
            HomeScreen(navHostController = navHostController )
        }

        composable(Constants.ABOUT_SCREEN) {
            AboutScreen(navHostController = navHostController)
        }

        composable(Constants.DETAIL_SCREEN) {
            DetailScreen(navHostController = navHostController)
        }

        composable(Constants.REPORT_SCREEN+ "/{id}" , arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
                defaultValue = -1
                nullable = false
            },
        )) {
            ReportScreen(navHostController = navHostController , carId = it.arguments!!.getInt("id"))
        }

        composable(Constants.ADD_SERVICE_SCREEN  + "/{id}" , arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
                defaultValue = -1
                nullable = false
            }
        )) {
            AddServiceScreen(navHostController = navHostController , carId = it.arguments!!.getInt("id"))
        }

        composable(Constants.SERVICE_SCREEN + "/{id}" , arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
                defaultValue = -1
                nullable = false
            }
        )) {
            ServiceScreen(navHostController = navHostController , carId = it.arguments!!.getInt("id"))
        }

        composable(Constants.CAR_SCREEN + "/{id}" , arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
                defaultValue = -1
                nullable = false
            }
        )) {
            CarScreen(navHostController = navHostController , id = it.arguments!!.getInt("id"))
        }

        composable(Constants.ADD_CAR_SCREEN) {
            AddCarScreen(navHostController = navHostController)
        }

        composable(route = Constants.EDIT_CAR_SCREEN  + "/{id}" , arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
                defaultValue = -1
                nullable = false
            }
        )) {
            EditCarScreen(navHostController = navHostController , id = it.arguments!!.getInt("id"))
        }

        composable(route = Constants.EDIT_SERVICE_SCREEN  + "/{id}" , arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
                defaultValue = -1
                nullable = false
            }
        )) {
            EditServiceScreen(navHostController = navHostController , id = it.arguments!!.getInt("id"))
        }


    }

}
