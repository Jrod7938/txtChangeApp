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

package com.jrod7938.textchangeapp.screens.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jrod7938.textchangeapp.components.AppLogo
import com.jrod7938.textchangeapp.components.UserForm
import com.jrod7938.textchangeapp.components.VerificationDialog
import com.jrod7938.textchangeapp.navigation.AppScreens

/**
 * LoginScreen composable
 *
 * @param navController NavHostController to navigate between screens
 * @param viewModel LoginScreenViewModel to handle the logic
 *
 * @constructor Creates a LoginScreen composable
 */
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val showLoginForm = rememberSaveable { mutableStateOf(true) }
    val loading by viewModel.loading.observeAsState(initial = false)
    val errorMessage by viewModel.errorMessage.collectAsState()
    val accountCreated by viewModel.accountCreatedSignal.collectAsState(initial = false)
    val isVerificationSent by viewModel.isVerificationSent.collectAsState(initial = false)
    val context = LocalContext.current


    if (accountCreated) {
        LaunchedEffect(key1 = true) {
            Toast.makeText(context, "Account Created Successfully!", Toast.LENGTH_SHORT).show()
        }
        viewModel.resetViewModel()
    }

    if(isVerificationSent){
        VerificationDialog(isVisible = true)
    }
    Column {


        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(top = 100.dp)
        ) {
            AppLogo()
            if (showLoginForm.value) {
                UserForm(loading = loading, isCreateAccount = false, errorMessage = errorMessage, viewModel = viewModel) { _, _, email, password ->
                    viewModel.signInWithEmailAndPassword(email, password) {
                        navController.navigate(AppScreens.HomeScreen.name)
                    }
                }
            } else {
                UserForm(
                    loading = loading,
                    isCreateAccount = true,
                    errorMessage = errorMessage,
                    viewModel = viewModel,
                ) { firstName, lastName, email, password ->
                    viewModel.createUserWithEmailAndPassword(
                        firstName,
                        lastName,
                        email,
                        password,
                        home = { navController.navigate(AppScreens.HomeScreen.name) },
                    )
                }
            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val textValue =
                if (showLoginForm.value) "Don't have an account?" else "Have an Account?"
            val text = if (showLoginForm.value) "Sign Up" else "Login"
            Text(
                text = textValue,
                color = MaterialTheme.colorScheme.background
            )
            Text(
                text = text,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .clickable {
                        showLoginForm.value = !showLoginForm.value
                    },
                fontWeight = FontWeight.ExtraBold,
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.background
            )
        }

    }


        // Spacer(modifier = Modifier.height(300.dp))

}
