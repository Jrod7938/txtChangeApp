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

package com.jrod7938.textchangeapp.screens.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jrod7938.textchangeapp.components.AccountInfo
import com.jrod7938.textchangeapp.components.AccountListings
import com.jrod7938.textchangeapp.components.EditBookDialog
import com.jrod7938.textchangeapp.model.MBook
import com.jrod7938.textchangeapp.navigation.AppScreens

/**
 * Account Screen
 *
 * @param navController NavController the navigation controller
 * @param viewModel AccountScreenViewModel the viewmodel for the screen
 *
 * @see NavController
 * @see AccountScreenViewModel
 */
@Composable
fun AccountScreen(
    navController: NavController,
    viewModel: AccountScreenViewModel = viewModel()
) {
    val user by viewModel.mUser.observeAsState(initial = null)
    val bookListings by viewModel.bookListings.observeAsState(initial = null)
    val message by viewModel.message.collectAsState(initial = "")
    val loading by viewModel.loading.observeAsState(initial = false)

    val currentlyEditingBook = remember { mutableStateOf<MBook?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (loading) {
            CircularProgressIndicator()
        }
        if (message?.isNotEmpty() == true) {
            Text(text = message!!, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
        if (!loading && user != null && bookListings != null) {
            if (currentlyEditingBook.value != null) {
                EditBookDialog(
                    book = currentlyEditingBook.value!!,
                    onConfirm = { book ->
                        viewModel.updateBook(book)
                        currentlyEditingBook.value = null
                    },
                    onDismiss = { currentlyEditingBook.value = null }
                )
            }
            AccountInfo(user!!, navController = navController)
            Text(
                text = "Selling",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Button(
                onClick = { navController.navigate(AppScreens.SellBookScreen.name) }
            ) {
                Text(
                    text = "Create a New Listing",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Start),
                text = "My Listings:",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            AccountListings(
                bookListings = bookListings!!,
                currentlyEditingBook = currentlyEditingBook,
                navController = navController
            )
        }
    }
}
