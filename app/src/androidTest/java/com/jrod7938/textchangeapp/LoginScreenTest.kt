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

package com.jrod7938.textchangeapp

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jrod7938.textchangeapp.navigation.AppScreens
import com.jrod7938.textchangeapp.screens.home.HomeScreen
import com.jrod7938.textchangeapp.screens.login.LoginScreen
import org.junit.Rule
import org.junit.Test

/**
 *  This class is used to test the login screen
 *
 *  @property rule is used to create a compose rule
 */
class LoginScreenTest {

    @get:Rule
    val rule = createComposeRule()

    /**
     * This test is to check if user can login with valid credentials
     */
    @Test
    fun accountLoginTest(){
        rule.setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = AppScreens.LoginScreen.name) {
                composable(AppScreens.HomeScreen.name) {
                    HomeScreen(navController = navController)
                }
                composable(AppScreens.LoginScreen.name) {
                    LoginScreen(navController = navController)
                }
            }
        }

        rule.onNodeWithText("Email")
            .assertExists()
            .performTextInput("testemail@pride.hofstra.edu")

        rule.onNodeWithText("Password")
            .assertExists()
            .performTextInput("tester123")

        rule.onNodeWithText("Login")
            .assertExists()
            .performClick()
    }

    /**
     * This test is to check if the user can create an account with valid credentials
     */
    @Test
    fun accountCreationTest() {
        rule.setContent {
            LoginScreen(navController = rememberNavController())
        }

        rule.onNodeWithText("Sign Up")
            .assertExists()
            .performClick()

        rule.onNodeWithText("First Name")
            .assertExists()
            .performTextInput("Test")

        rule.onNodeWithText("Last Name")
            .assertExists()
            .performTextInput("Tester")

        rule.onNodeWithText("Email")
            .assertExists()
            .performTextInput("testemail@pride.hofstra.edu")

        rule.onNodeWithText("Password")
            .assertExists()
            .performTextInput("tester123")

        rule.onNodeWithText("Create Account")
            .assertExists()
            .assertIsEnabled()
    }

    /**
     * This test is to check if the login button is disabled when the Email is wrong and
     * the password is less than 6 characters
     */
    @Test
    fun accountLoginTest2(){
        rule.setContent {
            LoginScreen(navController = rememberNavController())
        }

        // enter invalid email
        rule.onNodeWithText("Email")
            .assertExists()
            .performTextInput("test@pride.com")

        rule.onNodeWithText("Password")
            .assertExists()
            .performTextInput("test")

        rule.onNodeWithText("Login")
            .assertExists()
            .assertIsNotEnabled()
    }

    /**
     * This test is to check if the create account button is disabled when the email is
     * wrong and the password is less than 6 characters
     */
    @Test
    fun accountCreationTest2(){
        rule.setContent {
            LoginScreen(navController = rememberNavController())
        }

        rule.onNodeWithText("Sign Up")
            .assertExists()
            .performClick()

        rule.onNodeWithText("Email")
            .assertExists()
            .performTextInput("test@pride.com")

        rule.onNodeWithText("Password")
            .assertExists()
            .performTextInput("test")

        rule.onNodeWithText("Create Account")
            .assertExists()
            .assertIsNotEnabled()

    }

    /**
     * This test is to check if the user can login with invalid credentials
     * and get an error message
     */
    @Test
    fun accountLoginTest3() {
        rule.setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = AppScreens.LoginScreen.name) {
                composable(AppScreens.LoginScreen.name) {
                    LoginScreen(navController = navController)
                }
            }
        }

        rule.onNodeWithText("Email")
            .assertExists()
            .performTextInput("test@pride.hofstra.edu")

        rule.onNodeWithText("Password")
            .assertExists()
            .performTextInput("tester123")

        rule.onNodeWithText("Login")
            .assertExists()
            .performClick()

        rule.onNodeWithText("Welcome, please login to continue!")
            .assertExists()
    }
}