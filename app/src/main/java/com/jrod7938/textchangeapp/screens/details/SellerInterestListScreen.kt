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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jrod7938.textchangeapp.components.SellerInterestView
import com.jrod7938.textchangeapp.model.MBook
import com.jrod7938.textchangeapp.navigation.AppScreens

/**
 * Seller Interest Screen
 *
 * @param navController NavController the navigation controller
 * @param viewModel BookInfoScreenViewModel the viewModel for this screen
 *
 * @see NavController
 * @see BookInfoScreenViewModel
 */

@Composable
fun SellerInterestListScreen(
    navController: NavController,
    viewModel: BookInfoScreenViewModel = viewModel()
) {
    val loading by viewModel.loading.observeAsState(initial = false)
    val user by viewModel.user.observeAsState(initial = null)
    val sellerInterestList by viewModel.sellerInterestList.observeAsState(initial = listOf())

    LaunchedEffect(key1 = true) {
        viewModel.getUser()
    }

    LaunchedEffect(user != null){
        user?.let { viewModel.retrieveSellerInterestList(it) }
    }


    if(loading) {
        CircularProgressIndicator()
    }
    else if(sellerInterestList.isEmpty()){
        Text(
            text = "This page is empty. Post some listings for books to get started.",
            fontSize = 14.sp,
            modifier = Modifier.padding(15.dp),
            fontWeight = FontWeight.Bold,

            )
    }
    else {
        SellerInterestView(
            sellerInterestList = sellerInterestList,
            viewModel = viewModel,
            navController = navController
        )

    }

}