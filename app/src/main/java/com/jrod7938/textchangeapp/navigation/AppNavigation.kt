/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2023, Jrod7938, Khang-ALe, jesma14, Holesum
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jrod7938.textchangeapp.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jrod7938.textchangeapp.components.BottomNavigationBar
import com.jrod7938.textchangeapp.components.TxTchangeAppBar
import com.jrod7938.textchangeapp.screens.account.AccountScreen
import com.jrod7938.textchangeapp.screens.details.BookInfoScreen
import com.jrod7938.textchangeapp.screens.home.HomeScreen
import com.jrod7938.textchangeapp.screens.login.LoginScreen
import com.jrod7938.textchangeapp.screens.saved.SavedBooksScreen
import com.jrod7938.textchangeapp.screens.search.SearchScreen
import com.jrod7938.textchangeapp.screens.sell.SellBooksScreen
import com.jrod7938.textchangeapp.screens.splash.SplashScreen

/**
 * Navigation for the app
 *
 * @constructor Creates a Navigation for the app
 *
 * @see NavHost
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Sell,
        BottomNavItem.Search
    )

    val showAppBars = remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            if (showAppBars.value) {
                TxTchangeAppBar(navController = navController)
            }
        },
        bottomBar = {
            if (showAppBars.value) {  // Conditionally display BottomNavigationBar
                BottomNavigationBar(navController = navController, items = bottomNavItems)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = AppScreens.SplashScreen.name
            ) {
                composable(route = AppScreens.SplashScreen.name) {
                    DisposableEffect(Unit) {
                        showAppBars.value = false
                        onDispose { }
                    }
                    SplashScreen(navController = navController)
                }
                composable(route = AppScreens.HomeScreen.name) {
                    DisposableEffect(Unit) {
                        showAppBars.value = true
                        onDispose { }
                    }
                    HomeScreen(navController = navController)
                }
                composable(route = AppScreens.LoginScreen.name) {
                    DisposableEffect(Unit) {
                        showAppBars.value = false
                        onDispose { }
                    }
                    LoginScreen(navController = navController)
                }
                composable(
                    route = "${AppScreens.SearchScreen.name}/{category}",
                    arguments = listOf(navArgument("category") { type = NavType.StringType })
                ) { backStackEntry ->
                    val category = backStackEntry.arguments?.getString("category")
                    DisposableEffect(Unit) {
                        showAppBars.value = true
                        onDispose { }
                    }
                    SearchScreen(navController = navController, category = category)
                }
                composable(route = AppScreens.SearchScreen.name) {
                    DisposableEffect(Unit) {
                        showAppBars.value = true
                        onDispose { }
                    }
                    SearchScreen(navController = navController)
                }
                composable(route = AppScreens.SavedBooksScreen.name) {
                    DisposableEffect(Unit) {
                        showAppBars.value = true
                        onDispose { }
                    }
                    SavedBooksScreen(navController = navController)
                }
                composable(route = AppScreens.BookInfoScreen.name) {
                    DisposableEffect(Unit) {
                        showAppBars.value = true
                        onDispose { }
                    }
                    BookInfoScreen(navController = navController)
                }
                composable(route = "${AppScreens.BookInfoScreen.name}/{bookId}") {
                    DisposableEffect(Unit) {
                        showAppBars.value = true
                        onDispose { }
                    }
                    val bookId = it.arguments?.getString("bookId")
                    BookInfoScreen(navController = navController, bookId = bookId)
                }
                composable(route = AppScreens.SellBookScreen.name) {
                    DisposableEffect(Unit) {
                        showAppBars.value = true
                        onDispose { }
                    }
                    SellBooksScreen(navController = navController)
                }
                composable(route = AppScreens.AccountScreen.name) {
                    DisposableEffect(Unit) {
                        showAppBars.value = true
                        onDispose { }
                    }
                    AccountScreen(navController = navController)
                }
            }
        }
    }
}


