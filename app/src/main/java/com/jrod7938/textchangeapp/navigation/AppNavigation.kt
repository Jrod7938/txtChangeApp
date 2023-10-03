package com.jrod7938.textchangeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jrod7938.textchangeapp.screens.splash.SplashScreen
import com.jrod7938.textchangeapp.screens.createAccount.CreateAccountScreen
import com.jrod7938.textchangeapp.screens.details.BookInfoScreen
import com.jrod7938.textchangeapp.screens.home.HomeScreen
import com.jrod7938.textchangeapp.screens.login.LoginScreen
import com.jrod7938.textchangeapp.screens.search.SearchScreen
import com.jrod7938.textchangeapp.screens.saved.SavedBooksScreen
import com.jrod7938.textchangeapp.screens.sell.SellBooksScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.SplashScreen.name
    ){
        composable(route = AppScreens.SplashScreen.name){
            SplashScreen(navController = navController)
        }
        composable(route = AppScreens.HomeScreen.name){
            HomeScreen(navController = navController)
        }
        composable(route = AppScreens.LoginScreen.name){
            LoginScreen(navController = navController)
        }
        composable(route = AppScreens.SearchScreen.name){
            SearchScreen(navController = navController)
        }
        composable(route = AppScreens.SavedBooksScreen.name){
            SavedBooksScreen(navController = navController)
        }
        composable(route = AppScreens.BookInfoScreen.name){
            BookInfoScreen(navController = navController)
        }
        composable(route = AppScreens.SellBookScreen.name){
            SellBooksScreen(navController = navController)
        }
        composable(route = AppScreens.CreateAccountScreen.name){
            CreateAccountScreen(navController = navController)
        }
    }
}