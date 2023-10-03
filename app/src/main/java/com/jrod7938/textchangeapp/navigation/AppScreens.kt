package com.jrod7938.textchangeapp.navigation

enum class AppScreens {
    SplashScreen,
    LoginScreen,
    CreateAccountScreen,
    HomeScreen,
    SearchScreen,
    BookInfoScreen,
    SellBookScreen,
    SavedBooksScreen;

    companion object{
        fun fromRoute(route: String): AppScreens = when(route?.substringBefore("/")) {
            SplashScreen.name -> SplashScreen
            LoginScreen.name -> LoginScreen
            CreateAccountScreen.name -> CreateAccountScreen
            HomeScreen.name -> HomeScreen
            SearchScreen.name -> SearchScreen
            BookInfoScreen.name -> BookInfoScreen
            SellBookScreen.name -> SellBookScreen
            SavedBooksScreen.name -> SavedBooksScreen
            null -> HomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized.")
        }
    }
}