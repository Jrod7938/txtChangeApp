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

package com.jrod7938.textchangeapp.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * This composable is the App Logo. It displays the app logo as a circle with
 * the text "txt. CHANGE" inside of it.
 *
 * @param txtSize the size of the "txt." text
 * @param changeSize the size of the "CHANGE" text
 */
@Composable
fun AppLogo(txtSize: TextUnit = 42.sp, changeSize: TextUnit = 32.sp) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "txt.",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = txtSize,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "CHANGE",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = changeSize,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppLogoPreview() {
    AppLogo()
}

/**
 * This composable is the app logo. It displays the app logo as a circle with
 * the text "txt. CHANGE since 2023." inside of it.
 *
 * @param size the size of the logo
 * @param scale the scale of the logo
 * @param txtSize the size of the "txt." text
 * @param changeSize the size of the "CHANGE" text
 * @param legalSize the size of the "since 2023." text
 */
@Composable
fun AppSplashScreenLogo(
    size: Dp = 300.dp,
    scale: Animatable<Float, AnimationVector1D>,
    txtSize: TextUnit = 64.sp,
    changeSize: TextUnit = 24.sp,
    legalSize: TextUnit = 12.sp
){
    Surface(
        modifier = Modifier
            .size(size)
            .scale(scale = scale.value),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.onBackground,
        border = BorderStroke(width = 2.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "txt.",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = txtSize,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "CHANGE",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = changeSize,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "since",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = legalSize,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "2023.",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = legalSize,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// @Preview(showBackground = true)
@Composable
fun AppSplashScreenLogo(){
    AppSplashScreenLogo(scale = remember {
        Animatable(.9f)
    })
}

/**
 * This composable is the Email Input Field. It displays an input field for the
 * user to enter their email.
 *
 * @param modifier the modifier for the input field
 * @param emailState the state of the email
 * @param labelId the label for the input field
 * @param enabled whether the input field is enabled
 * @param imeAction the IME action for the input field
 * @param onAction the keyboard actions for the input field
 */
@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
){
    InputField(
        modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction
    )
}

/**
 * This composable is the Input Field. It displays an input field for the user
 * to enter text.
 *
 * @param modifier the modifier for the input field
 * @param valueState the state of the input field
 * @param labelId the label for the input field
 * @param enabled whether the input field is enabled
 * @param isSingleLine whether the input field is a single line
 * @param keyboardType the keyboard type for the input field
 * @param imeAction the IME action for the input field
 * @param onAction the keyboard actions for the input field
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth(),
        label = { Text(text = labelId) } ,
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction
    )
}