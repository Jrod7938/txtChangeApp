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

package com.jrod7938.textchangeapp.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jrod7938.textchangeapp.components.BookInfoView

/**
 * Screen that displays the details of a book.
 *
 * @param navController the navigation controller
 * @param bookId the id of the book to display
 * @param viewModel the view model
 *
 * @see BookInfoScreenViewModel
 * @see BookInfoView
 */
@Composable
fun BookInfoScreen(
    navController: NavHostController,
    bookId: String? = "",
    viewModel: BookInfoScreenViewModel = viewModel()
) {
    val user by viewModel.user.observeAsState(initial = null)
    val book by viewModel.book.observeAsState(initial = null)
    val loading by viewModel.loading.observeAsState(initial = false)
    val message by viewModel.message.collectAsState(initial = null)

    val (showContactInfo, setContactInfo) = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        bookId?.let {
            viewModel.fetchBookDetails(it)
            viewModel.getUser()
        }
    }

    if (loading) {
        CircularProgressIndicator()
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Book Info Screen",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            if (book != null && user != null) {
                BookInfoView(
                    book = book!!,
                    user = user!!,
                    onContactClicked = { setContactInfo(true) })
            } else {
                Text(
                    text = "Failed to fetch book details: $message",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (showContactInfo && book != null) {
            val context = LocalContext.current

            AlertDialog(
                backgroundColor = MaterialTheme.colorScheme.background,
                onDismissRequest = { setContactInfo(false) },
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Contact Seller",
                        textAlign = TextAlign.Center
                    )
                },
                buttons = {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                        onClick = {
                            book!!.let { book ->
                                val emailIntent = viewModel.prepareInterestEmailIntent(book)
                                emailIntent?.let { intent ->
                                    context.startActivity(emailIntent)

                                }
                            }
                        }
                    ) {
                        Text(text = "Send Email")
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        onClick = { setContactInfo(false) }
                    ) {
                        Text("Close")
                    }
                }
            )
        }
    }
}


