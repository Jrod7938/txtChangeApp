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

import android.inputmethodservice.Keyboard
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jrod7938.textchangeapp.components.AccountInfo
import com.jrod7938.textchangeapp.components.AccountListings
import com.jrod7938.textchangeapp.components.EditBookDialog
import com.jrod7938.textchangeapp.components.PostListingMBS
import com.jrod7938.textchangeapp.model.MBook

/**
 * Account Screen
 *
 * @param navController NavController the navigation controller
 * @param viewModel AccountScreenViewModel the viewmodel for the screen
 *
 * @see NavController
 * @see AccountScreenViewModel
 */
@OptIn(ExperimentalFoundationApi::class)
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
    var showBottomSheet by remember {mutableStateOf(false)}
    var showLogoutDialog by remember { mutableStateOf(false)}

    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            if (loading) CircularProgressIndicator()
            if (message?.isNotEmpty() == true) {
                Text(text = message!!, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
        item {
            user?.let { AccountInfo(it, navController = navController) }
        }
        stickyHeader {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                Text(
                    text = "My Listings:",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                    .padding(14.dp),
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.inverseSurface,
                )
                IconButton(onClick = { showBottomSheet = true }){
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Listing",
                        tint = MaterialTheme.colorScheme.inverseSurface,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 15.dp, end = 15.dp)
                    )
                }
            }
            Divider(modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp))
        }
        item {
            if(!loading && user!= null && bookListings != null) {
                AccountListings(
                    bookListings = bookListings!!,
                    currentlyEditingBook = currentlyEditingBook,
                    navController = navController,
                )
            }
            if(!loading && user != null && bookListings?.isEmpty() == true) {
                Text("Whoops..Looks like you've reached the end.", color =  MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    }

    if(showBottomSheet) PostListingMBS(onSheetDismissed = { showBottomSheet = false } )
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
    }

}
