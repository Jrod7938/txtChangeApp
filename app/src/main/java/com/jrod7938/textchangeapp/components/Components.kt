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

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.RichTooltipBox
import androidx.compose.material3.RichTooltipState
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable
import com.google.firebase.auth.FirebaseAuth
import com.jrod7938.textchangeapp.R
import com.jrod7938.textchangeapp.model.MBook
import com.jrod7938.textchangeapp.model.MCategory
import com.jrod7938.textchangeapp.model.MCondition
import com.jrod7938.textchangeapp.model.MUser
import com.jrod7938.textchangeapp.navigation.AppScreens
import com.jrod7938.textchangeapp.navigation.BottomNavItem
import com.jrod7938.textchangeapp.screens.account.AccountScreenViewModel
import com.jrod7938.textchangeapp.screens.details.BookInfoScreenViewModel
import com.jrod7938.textchangeapp.screens.home.HomeScreen
import com.jrod7938.textchangeapp.screens.search.SearchType
import com.jrod7938.textchangeapp.screens.sell.ListingSubmissionData
import com.jrod7938.textchangeapp.screens.sell.SellScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun NamePlate(
    size: Dp = 200.dp,
    overrideTopPadding: Dp = 50.dp,
    isRegistered: Boolean = true,
){
    val getResourceId = if(isRegistered){
        if(isSystemInDarkTheme()) R.drawable.suppreg_dark else R.drawable.suppreg_light
    } else if(isSystemInDarkTheme()) R.drawable.supp_unreg_dark else R.drawable.supp_unreg_light

    Surface(
        modifier = Modifier
            .width(size)
            .padding(top = overrideTopPadding)
    ){
        Image(
            modifier = Modifier.size(60.dp),
            painter = painterResource(id = getResourceId),
            contentDescription = "Supplementary Name Plate"
        )

    }
}

@Composable
fun AppLogo(
    appLogoSize: Dp = 50.dp,
    namePlateSize: Dp = 175.dp,
    namePlateTopPadding: Dp = 0.dp,
    namePlateRegistered: Boolean = false,
) {
    Row(
        modifier = Modifier
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(appLogoSize),
            painter = painterResource(id = R.drawable.applogo),
            contentDescription = "App Logo"
        )
        NamePlate(size = namePlateSize,
            overrideTopPadding = namePlateTopPadding,
            isRegistered = namePlateRegistered )
    }
}

// @Preview(showBackground = true)
@Composable
private fun AppLogoPreview() {
    AppLogo()
}

/**
 * This composable is the app logo. It displays the app logo.
 *
 * @param size the size of the logo
 * @param scale the scale of the logo
 */
@Composable
fun AppSplashScreenLogo(
    size: Dp = 500.dp,
    scale: Animatable<Float, AnimationVector1D>,
) {
    Surface(
        modifier = Modifier
            .size(size)
            .scale(scale = scale.value),
    ) {
        Image(
            modifier = Modifier.size(75.dp),
            painter = painterResource(id = R.drawable.applogo),
            contentDescription = "App Logo"
        )
    }
}

/**
 * This composable is to view the AppSplashScreenLogo.
 *
 * @see AppSplashScreenLogo
 */
// @Preview(showBackground = true)
@Composable
fun AppSplashScreenLogoPreview() {
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
) {
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
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction
    )
}

/**
 * This composable is the User Form. It displays a form for the user to enter
 * their email and password.
 *
 * @param loading whether the form is loading
 * @param isCreateAccount whether the form is for creating an account
 * @param onDone the function to call when the form is done
 *
 * @see EmailInput
 * @see PasswordInput
 * @see SubmitButton
 */
@OptIn(ExperimentalComposeUiApi::class)
//@Preview(showBackground = true)
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String, String, String) -> Unit = { firstName, lastName, email, pwd -> }
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val firstName = rememberSaveable { mutableStateOf("") }
    val lastName = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val passwordFocusRequest = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value, firstName.value, lastName.value) {
        email.value.trim().isNotEmpty()
                && email.value.contains("@pride.hofstra.edu")
                && password.value.trim().isNotEmpty()
                && password.value.length >= 6
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxHeight(.6f)
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isCreateAccount) {
            Text(
                text = stringResource(id = R.string.create_acct),
                modifier = Modifier.padding(4.dp),
                textAlign = TextAlign.Center
            )
            FirstNameInput(firstNameState = firstName)
            LastNameInput(lastNameState = lastName)
        } else {
            Text(
                text = "Welcome, please login to continue!",
                modifier = Modifier.padding(4.dp)
            )
        }
        EmailInput(
            emailState = email,
            enabled = !loading,
            onAction = KeyboardActions { passwordFocusRequest.requestFocus() }
        )
        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            labelId = "Password",
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onDone(
                    firstName.value.trim(),
                    lastName.value.trim(),
                    email.value.trim(),
                    password.value.trim()
                )
            }
        )
        SubmitButton(
            textId = if (isCreateAccount) "Create Account" else "Login",
            loading = loading,
            validInputs = valid,
        ) {
            onDone(
                firstName.value.trim(),
                lastName.value.trim(),
                email.value.trim(),
                password.value.trim()
            )
            keyboardController?.hide()
        }

    }
}

/**
 * This composable is the Submit Button. It displays a button for the user to
 * submit their email and password.
 *
 * @param textId the text for the button
 * @param loading whether the button is loading
 * @param validInputs whether the inputs are valid
 * @param onClick the function to call when the button is clicked
 */
@Composable
fun SubmitButton(
    textId: String,
    loading: Boolean,
    validInputs: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        enabled = !loading && validInputs,
        shape = CircleShape,
        onClick = onClick
    ) {
        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text = textId, modifier = Modifier.padding(5.dp))
    }
}

/**
 * This composable is the Password Input Field. It displays an input field for
 * the user to enter their password.
 *
 * @param modifier the modifier for the input field
 * @param passwordState the state of the password
 * @param labelId the label for the input field
 * @param enabled whether the input field is enabled
 * @param passwordVisibility whether the password is visible
 * @param imeAction the IME action for the input field
 * @param onAction the keyboard actions for the input field
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    val visualTransformation =
        if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()
    OutlinedTextField(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth(),
        value = passwordState.value,
        label = { Text(text = labelId) },
        singleLine = true,
        onValueChange = { passwordState.value = it },
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        ),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        visualTransformation = visualTransformation,
        trailingIcon = { PasswordVisibility(passwordVisibility = passwordVisibility) },
        keyboardActions = onAction
    )
}

/**
 * This composable is the Password Visibility Icon. It displays an icon for the
 * user to toggle the visibility of their password.
 *
 * @param passwordVisibility whether the password is visible
 */
@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visible }) {
        Icon(
            imageVector = if (visible) Icons.Outlined.Lock else Icons.Default.Lock,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}


/**
 * This composable is the Bottom Navigation Bar. It displays a bottom
 * navigation bar for the user to navigate between screens.
 *
 * @param navController the navigation controller
 * @param items the items for the bottom navigation bar
 *
 * @see BottomNavItem
 */

@Composable
fun BottomNavBar(
    navController: NavHostController,
    items: List<BottomNavItem>
){
    var selectedIndex by remember { mutableIntStateOf(0) }

    AnimatedNavigationBar(
        modifier = Modifier.height(70.dp),
        selectedIndex = selectedIndex,
        ballColor = MaterialTheme.colorScheme.primary,
        cornerRadius = shapeCornerRadius(cornerRadius = 0.dp),
        ballAnimation = Parabolic(tween(300)),
        indentAnimation = Height(tween(300)),
        barColor = MaterialTheme.colorScheme.primary
        ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            if (currentRoute?.contains(item.route) == true) selectedIndex = items.indexOf(item)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .noRippleClickable {
                        if (currentRoute != item.route) {
                            selectedIndex = items.indexOf(item)
                            navController.navigate(item.route)
                            // selectedIndex = item.ordinal
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = if (currentRoute?.contains(item.route) == true) item.selectedIcon else item.unselectedIcon,
                    contentDescription = null,
                    tint = if (currentRoute?.contains(item.route) == true) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.background
                )
            }
        }

    }

}

/**
 * This composable is the App Bar. It displays an app bar for the user to
 * navigate between account and saved books screen.
 *
 * @param navController the navigation controller
 *
 * @see AppLogo
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(navController: NavHostController, items: List<BottomNavItem>){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val (title, setTitle) = remember { mutableStateOf("")}

    val (expanded, setExpanded) = remember { mutableStateOf(false)}

    val context = LocalContext.current

    val sendInvite =  inviteFriends()
    val sendFeedback = sendFeedback()


    items.forEach{item ->
        if(currentRoute == item.route) setTitle(item.title)
    }
        TopAppBar(
            modifier = Modifier
                .fillMaxHeight(0.1f)
                .padding(top = 15.dp, bottom = 5.dp, start = 10.dp, end = 10.dp),
            title = {
                Text(
                    text = title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 10.dp)

                )
            },
            actions = {
                Box(modifier = Modifier
                   .wrapContentSize(Alignment.TopEnd)) {
                    IconButton(
                        onClick = { setExpanded(true)},

                        ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "More Vertical"
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { setExpanded(false) }
                    ){
                        DropdownMenuItem(
                            content = { Text("Invite Friends") },
                            onClick = {  sendInvite.let { context.startActivity(sendInvite) } }
                        )
                        DropdownMenuItem(
                            content = { Text("Send Feedback") },
                            onClick = { sendFeedback.let { context.startActivity(sendFeedback)}}
                        )
                    }

                }
            },
        )

}
@Composable
fun inviteFriends() : Intent {
    val invitation = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT, "Hello there fellow Hofstra Student!"+
            "\n\nI invite you to check out txtChange."+
                    "\n\nI am using it to buy and sell from other students"
        )
        putExtra(Intent.EXTRA_SUBJECT, "Join txtChange Today!")
    }
    return invitation
}

@Composable
fun sendFeedback() : Intent {
    val feedback = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_EMAIL, arrayOf("txtChangeTeam@gmail.com")
        )
        putExtra(
            Intent.EXTRA_SUBJECT, "txtChange Feedback"
        )
    }

    return feedback
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TxTchangeAppBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    TopAppBar(
        modifier = Modifier
            .fillMaxHeight(.1f)
            .padding(10.dp),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                AppLogo(appLogoSize = 54.dp,
                    namePlateTopPadding = 0.dp,
                    namePlateSize = 120.dp,
                    namePlateRegistered = false)
                Spacer(modifier = Modifier.fillMaxWidth(0.4f))
                Icon(
                    modifier = Modifier
                        .size(34.dp)
                        .clickable { navController.navigate(AppScreens.SavedBooksScreen.name) },
                    imageVector = if (currentRoute == AppScreens.SavedBooksScreen.name) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    tint = if (currentRoute == AppScreens.SavedBooksScreen.name) MaterialTheme.colorScheme.primary else Color.DarkGray,
                    contentDescription = "Favorite"
                )
                Spacer(modifier = Modifier.fillMaxWidth(0.1f))
                Icon(
                    modifier = Modifier
                        .size(34.dp)
                        .clickable { navController.navigate(AppScreens.AccountScreen.name) },
                    imageVector = if (currentRoute == AppScreens.AccountScreen.name) Icons.Filled.Person else Icons.Outlined.Person,
                    tint = if (currentRoute == AppScreens.AccountScreen.name) MaterialTheme.colorScheme.primary else Color.DarkGray,
                    contentDescription = "Account"
                )
            }
        }
    )
}


/**
 * A card that displays a book category
 *
 * @param category the category of the book
 * @param bookImageUrl the url of the book image
 * @param navController the nav controller
 *
 * @return a card that displays a book category
 *
 * @see HomeScreen
 */
@Composable
fun CategoryCard(
    category: String,
    bookImageUrl: String,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(200.dp)
            .clickable(onClick = { navController.navigate("${AppScreens.SearchScreen.name}/$category") }),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .padding(10.dp),
                painter = rememberAsyncImagePainter(model = bookImageUrl),
                contentDescription = "$category image"
            )
            Text(
                text = category,
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 18.sp),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Displays the categories of books.
 *
 * @param bookCategories the categories of books
 * @param navController the navigation controller
 *
 * @see CategoryCard
 * @see HomeScreen
 */
@Composable
fun DisplayCategories(bookCategories: HashMap<String, MBook>, navController: NavHostController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(bookCategories.entries.toList()) { entry ->
            val category = entry.key
            val book = entry.value

            CategoryCard(
                category = category,
                bookImageUrl = book.imageURL,
                navController = navController
            )
        }
    }
}

/**
 * This function displays the buttons on the home screen.
 *
 * @param navController the navigation controller
 *
 * @see HomeScreen
 */
@Composable
fun HomeScreenButtons(navController: NavHostController) {
    var show by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier
                .padding(top = 16.dp)
                .size(width = 200.dp, height = 40.dp),
            onClick = { navController.navigate(AppScreens.SearchScreen.name)}
        ) {
            Text(text = "Find A Book")
        }
        Button(
            modifier = Modifier
                .padding(16.dp)
                .size(width = 200.dp, height = 40.dp),
            onClick = { show = true }
        ) {
            Text(text = "Sell A Book")
        }

        if(show) PostListingMBS(onSheetDismissed = { show = false })
    }
}

/**
 * This composable is the FirstName Input Field. It displays an input field for the
 * user to enter their first name.
 *
 * @param modifier the modifier for the input field
 * @param firstNameState the state of the email
 * @param labelId the label for the input field
 * @param enabled whether the input field is enabled
 * @param imeAction the IME action for the input field
 * @param onAction the keyboard actions for the input field
 */
@Composable
fun FirstNameInput(
    modifier: Modifier = Modifier,
    firstNameState: MutableState<String>,
    labelId: String = "First Name",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField(
        modifier = modifier,
        valueState = firstNameState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Text,
        imeAction = imeAction,
        onAction = onAction
    )
}

/**
 * This composable is the LastName Input Field. It displays an input field for the
 * user to enter their last name.
 *
 * @param modifier the modifier for the input field
 * @param lastNameState the state of the email
 * @param labelId the label for the input field
 * @param enabled whether the input field is enabled
 * @param imeAction the IME action for the input field
 * @param onAction the keyboard actions for the input field
 */
@Composable
fun LastNameInput(
    modifier: Modifier = Modifier,
    lastNameState: MutableState<String>,
    labelId: String = "Last Name",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField(
        modifier = modifier,
        valueState = lastNameState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Text,
        imeAction = imeAction,
        onAction = onAction
    )
}

/**
 * This composable is the Book Edit Alert. It displays a card for a book in the account screen to edit.
 *
 * @param book the book to display
 * @param onConfirm the function to call when the user confirms the book
 * @param onDismiss the function to call when the user dismisses the book
 *
 * @see MBook
 */
@Composable
fun EditBookDialog(book: MBook, onConfirm: (MBook) -> Unit, onDismiss: () -> Unit) {
    var editedCondition by remember { mutableStateOf(book.condition) }
    var editedPrice by remember { mutableStateOf(book.price.toString()) }

    val valid by remember(editedCondition, editedPrice) {
        mutableStateOf(
            editedCondition.matches("^[a-zA-Z\\s]+$".toRegex())
                    && editedPrice.matches("^\\d*\\.?\\d+$".toRegex())
                    && editedPrice.toDouble() > 0
        )
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Edit ${book.title}") },
        text = {
            Column {
                BookConditionDropdown(
                    selectedCondition = editedCondition,
                    onConditionSelected = { editedCondition = it }
                )
                OutlinedTextField(
                    value = editedPrice,
                    enabled = true,
                    onValueChange = {
                        if (it.matches("^\\d*\\.?\\d*$".toRegex())) {
                            editedPrice = it
                        }
                    },
                    label = { Text("Book Price") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions.Default
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (valid) {
                    onConfirm(
                        book.copy(
                            condition = editedCondition,
                            price = editedPrice.toDouble()
                        )
                    )
                    onDismiss()
                }
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Account Book Listings
 *
 * @param bookListings List<MBook> the list of book listings
 * @param currentlyEditingBook MutableState<MBook?> the book that is currently being edited
 * @param viewModel AccountScreenViewModel the viewmodel for the screen
 *
 * @see MBook
 * @see AccountScreenViewModel
 */
@Composable
fun AccountListings(
    bookListings: List<MBook>,
    currentlyEditingBook: MutableState<MBook?>,
    viewModel: AccountScreenViewModel = viewModel(),
    navController: NavController
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(bookListings.size) { index ->
            val book = bookListings[index]
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .height(250.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(book.imageURL),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .padding(8.dp)
                            .clickable {
                                navController.navigate("${AppScreens.BookInfoScreen.name}/${book.bookID}")
                            }
                    )
                    Text(
                        modifier = Modifier.height(40.dp),
                        text = book.title,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            onClick = { currentlyEditingBook.value = book }
                        ) {
                            Text(text = "Edit", fontSize = 12.sp)
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            onClick = { viewModel.deleteBook(book) }
                        ) {
                            Text(text = "Delete", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Account Info
 *
 * @param user MUser the user
 *
 * @see MUser
 */
@Composable
fun AccountInfo(user: MUser, navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(0.7f),
            text = "Hello, ${user.firstName} ${user.lastName}.",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(
            modifier = Modifier.size(30.dp),
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate(AppScreens.LoginScreen.name) {
                    popUpTo(navController.graph.startDestinationRoute!!) { inclusive = true }
                    launchSingleTop = true
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Logout",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Name: ${user.firstName} ${user?.lastName}")
            Text(text = "Display Name: ${user.displayName}")
            Text(text = "Email: ${user.email}")
        }
    }
}

/**
 * This composable is the Book Condition Dropdown. It displays a dropdown for
 * the user to select the condition of their book.
 *
 * @param selectedCondition the selected condition
 * @param onConditionSelected the function to call when the condition is selected
 *
 */
@Composable
fun BookConditionDropdown(
    selectedCondition: String,
    onConditionSelected: (String) -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = "Condition: ")
            TextButton(onClick = { isDropdownExpanded = true }
            ) {
                Text(selectedCondition)
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Drop down arrow"
                )
            }

            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                MCondition.conditions.forEach { condition ->
                    DropdownMenuItem(
                        onClick = {
                            onConditionSelected(condition.toString())
                            isDropdownExpanded = false
                        }
                    ) {
                        Text(condition.toString(), color = Color.Black)
                    }
                }
            }
        }
    }
}

/**
 * Displays the book info
 *
 * @param book the book to display
 * @param user the user that is logged in
 * @param onContactClicked callback to display the seller's email
 * @param viewModel BookInfoScreenViewModel the viewmodel for the screen
 *
 * @see MBook
 * @see MUser
 * @see BookInfoScreenViewModel
 */
@Composable
fun BookInfoView(
    book: MBook,
    user: MUser,
    onContactClicked: () -> Unit,
    viewModel: BookInfoScreenViewModel = viewModel()
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = book.imageURL),
                contentDescription = "${book.title} Image",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = book.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (user.email != book.email) {
                    Button(
                        colors = ButtonDefaults
                            .buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        onClick = onContactClicked
                    ) {
                        Text(text = "Contact Seller", fontSize = 16.sp)
                    }
                    Button(onClick = {
                        if (user.savedBooks.contains(book.bookID)) {
                            viewModel.unsaveBook(book)
                            viewModel.fetchBookDetails(book.bookID)
                            GlobalScope.launch {
                                withContext(Dispatchers.Main) {
                                    viewModel.getUser()
                                }
                            }
                        } else {
                            viewModel.saveBook(book)
                            viewModel.fetchBookDetails(book.bookID)
                            GlobalScope.launch {
                                withContext(Dispatchers.Main) {
                                    viewModel.getUser()
                                }
                            }
                        }
                    }) {
                        Text(
                            text = if (user.savedBooks.contains(book.bookID)) "Unsave" else "Save",
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Author: ${book.author}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "ISBN: ${book.isbn}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Category: ${book.mCategory}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Condition: ${book.condition}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Price: $${book.price}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Status:", fontWeight = FontWeight.Bold)
                Box(modifier = Modifier
                    .clickable(
                        enabled = book.email != viewModel.email
                    ) {
                        viewModel.buyerVerifiedBook(book)
                        book.buyerConfirm = true
                        viewModel.fetchBookDetails(book.bookID)
                        viewModel.removeBookIfBothPartiesVerified(book)
                    }
                ) {
                    Row {
                        Text(text = "Buyer Verification: ")
                        androidx.compose.material3.Icon(
                            modifier = Modifier.border(
                                width = 3.dp,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            imageVector = if (book.buyerConfirm) Icons.Default.Check else Icons.Default.Clear,
                            tint = if (book.buyerConfirm) Color.Green else Color.Red,
                            contentDescription = "Buyer Verification"
                        )
                    }
                }

                Box(modifier = Modifier
                    .clickable(enabled = book.email == viewModel.email) {
                        viewModel.sellerVerifiedBook(book)
                        book.sellerConfirm = true
                        viewModel.fetchBookDetails(book.bookID)
                        viewModel.removeBookIfBothPartiesVerified(book)
                    }
                ) {
                    Row {
                        Text(text = "Seller Verification: ")
                        androidx.compose.material3.Icon(
                            modifier = Modifier.border(
                                width = 3.dp,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            imageVector = if (book.sellerConfirm) Icons.Default.Check else Icons.Default.Clear,
                            tint = if (book.sellerConfirm) Color.Green else Color.Red,
                            contentDescription = "Buyer Verification"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Description: ${book.description}", fontSize = 16.sp)
        }
    }
}

@Composable
fun SelectionPill(
    option: ToggleButtonOption,
    selected: Boolean,
    onClick: (option: ToggleButtonOption) -> Unit = {}
) {

    Button(
        onClick = { onClick(option)},
        colors = ButtonDefaults.buttonColors(
            containerColor = if(selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.background,
        ),
        shape = MaterialTheme.shapes.extraLarge,
        elevation  = ButtonDefaults.elevatedButtonElevation(0.dp),
        contentPadding = ButtonDefaults.ContentPadding,
        modifier = Modifier.padding(14.dp, 0.dp),
    ) {
        Row(
            modifier = Modifier.padding(0.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = option.text,
                color = if (selected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(0.dp),
                fontWeight = FontWeight.Bold
            )
            if (option.iconRes != null) {
                Icon(
                    painterResource(id = option.iconRes),
                    contentDescription = null,
                    tint = if (selected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(4.dp, 2.dp, 2.dp, 2.dp),
                )
            }
        }
    }
}

enum class SelectionType {
    NONE,
    SINGLE,
    MULTIPLE,
}

data class ToggleButtonOption(
    val text: String,
    val iconRes: Int?,
)

@Composable
fun ToggleButton(
    options: Array<ToggleButtonOption>,
    modifier: Modifier = Modifier,
    type: SelectionType = SelectionType.SINGLE,
    onClick: (selectedOptions: Array<ToggleButtonOption>) -> Unit = {},
) {
    val state = remember  { mutableStateMapOf<String, ToggleButtonOption>() }

    OutlinedButton(
        onClick = { },
        border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
        shape = MaterialTheme.shapes.extraLarge,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
        contentPadding = PaddingValues(0.dp, 0.dp),
        modifier = modifier
            .padding(0.dp)
            .height(52.dp),
    ) {
        if (options.isEmpty()) {
            return@OutlinedButton
        }
        val onItemClick: (option: ToggleButtonOption) -> Unit = { option ->
            if (type == SelectionType.SINGLE) {
                options.forEach {
                    val key = it.text
                    if (key == option.text) {
                        state[key] = option
                    } else {
                        state.remove(key)
                    }
                }
            } else {
                val key = option.text
                if (!state.contains(key)) {
                    state[key] = option
                } else {
                    state.remove(key)
                }
            }
            onClick(state.values.toTypedArray())
        }
        if (options.size == 1) {
            val option = options.first()
            SelectionPill(
                option = option,
                selected = state.contains(option.text),
                onClick = onItemClick,
            )
            return@OutlinedButton
        }
        val first = options.first()
        val last = options.last()
        val middle = options.slice(1..options.size - 2)
        SelectionPill(
            option = first,
            selected = state.contains(first.text) || state.isEmpty(),
            onClick = onItemClick,
        )
        // VerticalDivider()
        middle.map { option ->
            SelectionPill(
                option = option,
                selected = state.contains(option.text),
                onClick = onItemClick,
            )
            // VerticalDivider()
        }
        SelectionPill(
            option = last,
            selected = state.contains(last.text),
            onClick = onItemClick,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookThumbnail(
    book: MBook,
    viewModel: BookInfoScreenViewModel = viewModel(),
    navController: NavHostController,
    ) {

    val user by viewModel.user.observeAsState(initial = null)
    val isBookSaved = user?.savedBooks?.contains(book.bookID) == true
    val (isChecked, setChecked) = remember(isBookSaved) { mutableStateOf(isBookSaved) }
    val (view, setView) = remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(user) {
        setChecked(user?.savedBooks?.contains(book.bookID) == true)
    }
    LaunchedEffect(true) {
        viewModel.getUser()
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = book.imageURL),
            contentDescription = "Image of ${book.title}",
            modifier = Modifier.size(175.dp)
        )

        Text(
            text = book.title,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            modifier = Modifier.padding(top = 20.dp)
        )

        Text(text = "by ${book.author}")


        Text(
            text = "Price: $${book.price}",
            color = MaterialTheme.colorScheme.secondary,
        )
        Text(text = "Condition: ${book.condition}")
        Column() {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()) {
                SavedToFavoritesButton(
                    isChecked = isChecked,
                    onClick = {
                        if(user?.savedBooks?.contains(book.bookID)!!){
                            viewModel.unsaveBook(book)
                            viewModel.viewModelScope.launch { viewModel.getUser() }
                            Toast.makeText(
                                context,
                                "Removed from Saved",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            viewModel.saveBook(book)
                            viewModel.viewModelScope.launch { viewModel.getUser() }
                            Toast.makeText(
                                context,
                                "Added to Saved",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                            setChecked(!isChecked)
                    })
                Button(
                    onClick = { setView(true) }
                ) {
                    Text(text = "Purchase")
                }
                Icon(
                    Icons.Default.MoreHoriz,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "View More",
                    modifier = Modifier
                        .clickable { navController.navigate("${AppScreens.BookInfoScreen.name}/${book.bookID}") }
                        .padding(top = 15.dp, start = 15.dp)
                )
            }
        }
    }
    if(view) {

        AlertDialog(
            shape = MaterialTheme.shapes.medium,
            onDismissRequest = { setView(false) },
            dismissButton = {
                TextButton(onClick = { setView(false) }) {
                    Text("Cancel", fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    book.let { book ->
                        val emailIntent = viewModel.prepareInterestEmailIntent(book)
                        emailIntent.let {
                            context.startActivity(emailIntent)
                        }
                    }
                }) { Text("Continue", fontWeight = FontWeight.Bold) }

            },
            title = {
                Text("Contact Seller?",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary) },
            text = {
                Text("Email the seller of this listing to the begin transaction.",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.primary) }
        )
    }
}

@Composable
fun DisplaySearchResults(
    bookList: List<MBook>,
    text: String,
    filter: SearchType,
    navController: NavHostController,
    viewModel: BookInfoScreenViewModel = viewModel()
) {

    val (searchText, setSearchText ) = remember { mutableStateOf("")}
    val (searchType, setSearchType) = remember { mutableStateOf(filter)}

    setSearchType(filter)

    LaunchedEffect(true) { viewModel.getUser() }

    Column() {
        if(bookList.isEmpty()) {
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Sorry, we couldn't find anything for your query",
                    modifier = Modifier
                        .padding(top = 15.dp, start = 30.dp)
                        .fillMaxWidth(0.70f),
                    fontSize = 15.sp,
                    softWrap = true,)
            }
        } else  {

            // check if this display needs to be changed

            if(searchText != text && text.isNotEmpty()){
                if(((searchType == SearchType.ISBN) || (searchType == SearchType.None)) && searchText != bookList[0].isbn){
                    setSearchType(SearchType.ISBN)
                    if(searchType == filter && text == bookList[0].isbn) setSearchText(text)

                } else if ((searchType == SearchType.Title) && searchText != bookList[0].title) {
                    setSearchType(SearchType.Title)
                    if (searchType == filter && text == bookList[0].title) setSearchText(text)

                } else if ((searchType == SearchType.Author) && searchText != bookList[0].author) {
                    setSearchType(SearchType.Author)
                    if (searchType == filter && text == bookList[0].author) setSearchText(text)
                }
            }

            Column() {
                val annotatedString = buildAnnotatedString {
                    append("Here's what we found for: ")
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("'${if (searchText.isNotEmpty()) searchText else text}'")
                    }
                }
                Text(text = annotatedString,
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 15.dp, bottom = 15.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    softWrap = true,)

                LazyColumn {

                    bookList.forEach { book ->
                        item {
                            BookThumbnail(book, navController = navController)
                        }
                    }
                    }
                }
            }
        }
    }


@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun SavedToFavoritesButton(
    isChecked: Boolean,
    onClick: () -> Unit
) {
    IconToggleButton(
        checked = isChecked,
        onCheckedChange = { onClick() }
    ) {
        val transition = updateTransition(isChecked, label = "Checked indicator")

        val tint by transition.animateColor(
            label = "Tint"
        ) { isChecked ->
            if (isChecked) Color.Red else MaterialTheme.colorScheme.primary
        }

        val size by transition.animateDp(
            transitionSpec = {
                if (false isTransitioningTo true) {
                    keyframes {
                        durationMillis = 250
                        30.dp at 0 with LinearOutSlowInEasing // for 0-15 ms
                        35.dp at 15 with FastOutLinearInEasing // for 15-75 ms
                        40.dp at 75 // ms
                        35.dp at 150 // ms
                    }
                } else {
                    spring(stiffness = Spring.StiffnessVeryLow)
                }
            },
            label = "Size"
        ) { 30.dp }

        Icon(
            imageVector = if (isChecked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(size)
        )
    }
}
@Composable
fun SellFAB(){

    var show by remember {mutableStateOf(false)}
    SmallFloatingActionButton(
        content = { Icon(Icons.Filled.Add, contentDescription = "", modifier = Modifier.size(30.dp), tint = MaterialTheme.colorScheme.background) },
        shape = CircleShape,
        contentColor = MaterialTheme.colorScheme.background,
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(65.dp),
        onClick = { show = true }
    )

        if(show){
            PostListingMBS(onSheetDismissed = { show = false })
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PostListingMBS(onSheetDismissed: () -> Unit, viewModel: SellScreenViewModel = viewModel() ){
    val message by viewModel.message.collectAsState()
    val loading by viewModel.loading.observeAsState(initial = false)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onSheetDismissed,
        sheetState = sheetState,
        ) {

        PostListingForm(viewModel, loading, message )
    }

    DisposableEffect(Unit) {
        onDispose{
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListingForm(
    viewModel: SellScreenViewModel,
    loading: Boolean,
    message: String?
) {

    val textStateISBN = remember { mutableStateOf(TextFieldValue()) } // ISBN Text-field value
    var isValidISBN by remember { mutableStateOf(true)}
    fun checkISBN(isbn: String) : Boolean {
        return isbn.matches(Regex("^[0-9]*\$"))
    }

    val textStatePrice = remember { mutableStateOf(TextFieldValue()) } // Price Text-field value
    var isValidPrice by remember { mutableStateOf(true)}
    fun checkPrice(price: String) : Boolean {
        return price.matches(Regex("([0-9]*[.])?[0-9]+")) // field validation rege
    }

    var isConditionExpanded by remember { mutableStateOf(false)} // condition drop down state
    var selectedCondition by remember { mutableStateOf("")} // selection
    var isValidCondition by remember { mutableStateOf(true)}

    var isCategoryExpanded by remember { mutableStateOf(false)} // category drop down state
    var selectedCategory by remember { mutableStateOf("")} // selection
    var isValidCategory by remember { mutableStateOf(true)}

    var onFormConfirm by remember { mutableStateOf(false)}

    if(onFormConfirm){ // when form is submitted and confirm, reset values
        textStateISBN.value = TextFieldValue("")
        textStatePrice.value = TextFieldValue("")
        selectedCategory = ""
        selectedCondition = ""
    }

    Column {

            Text(
                text = "Create Textbook Listing",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 20.dp, top = 10.dp)
                    .fillMaxWidth(),
                maxLines = 1,
            )
            OutlinedTextField(
                label = { ISBNTooltip() },
                enabled = true,
                value =  textStateISBN.value,
                onValueChange = { input ->
                    onFormConfirm = false
                    textStateISBN.value = input
                    isValidISBN = input.text.isNotEmpty() && checkISBN(input.text)
                },
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 15.dp
                    )
                    .fillMaxWidth(),
                isError = !isValidISBN,
                maxLines = 1,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close ISBN",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { textStateISBN.value = TextFieldValue("") }
                            .size(20.dp)
                    )
                },

                supportingText = {
                    if(!isValidISBN){
                        ErrToolTip(
                            message = "ISBN must not be empty and must only contain numbers",
                            contentDescription = "ISBN Error Tooltip")
                    }
                },
                
            )

            // CATEGORY
            ExposedDropdownMenuBox(
                expanded = isCategoryExpanded,
                onExpandedChange = { newValue -> isCategoryExpanded = newValue },
                modifier = Modifier.padding(
                    start = 20.dp,
                    bottom = 15.dp,
                    end = 20.dp,
                    top = 15.dp),
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    label = { Text("Category", fontSize = 15.sp) },
                    value = selectedCategory,
                    onValueChange = { input ->
                        onFormConfirm = false
                        isValidCategory = input.isNotEmpty()
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryExpanded) },
                    isError = !isValidCategory,
                    maxLines = 1,
                )
                ExposedDropdownMenu(
                    expanded = isCategoryExpanded,
                    onDismissRequest = { isCategoryExpanded = false }
                ) {
                    MCategory.categories.forEach { category ->
                        DropdownMenuItem(
                            content = { Text(category.toString(), color = MaterialTheme.colorScheme.inverseSurface ) },
                            onClick = {
                                selectedCategory = category.toString()
                                isCategoryExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
            Row{
                OutlinedTextField(
                    label = { Text("Price", fontSize = 15.sp) },
                    value = textStatePrice.value,
                    onValueChange = { input ->
                        onFormConfirm = false
                        textStatePrice.value = input
                        isValidPrice = input.text.isNotEmpty() && checkPrice(input.text)
                    },
                    modifier = Modifier
                        .padding(
                            start = 20.dp,
                            bottom = 15.dp,
                            top = 15.dp
                        )
                        .fillMaxWidth(0.4f),
                    isError = !isValidPrice,
                    maxLines = 1,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear Price",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable { textStatePrice.value = TextFieldValue("") }
                                .size(20.dp)
                        )

                    },
                    supportingText = {
                        if(!isValidPrice){
                            ErrToolTip(
                                message = "Price must not be empty and only contain whole numbers or decimals",
                                contentDescription = "Price Error ToolTip" )
                        }
                    }
                )

                // TEXTBOOK CONDITION
                ExposedDropdownMenuBox(
                    expanded = isConditionExpanded,
                    onExpandedChange = { newValue -> isConditionExpanded = newValue },
                    modifier = Modifier.padding(
                        top = 15.dp,
                        bottom = 15.dp,
                        start = 20.dp,
                        end = 20.dp
                    ),
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        readOnly = true,
                        label = { ConditionTooltip() },
                        value = selectedCondition,
                        onValueChange = { input ->
                            onFormConfirm = false
                            isValidCondition = input.isNotEmpty()
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isConditionExpanded) },
                        isError = !isValidCondition,
                        maxLines = 1,
                    )
                    ExposedDropdownMenu(
                        expanded = isConditionExpanded,
                        onDismissRequest = { isConditionExpanded = false }
                    ) {
                        MCondition.conditions.forEach { condition ->
                            DropdownMenuItem(
                                content = {
                                    Text(
                                        condition.returnCondition(),
                                        color = MaterialTheme.colorScheme.inverseSurface
                                    )
                                },
                                onClick = {
                                    selectedCondition =
                                        condition.returnCondition(); isConditionExpanded =
                                    false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )

                        }
                    }
                }
            }
        SellSubmitButton(
            loading,
            viewModel,
            ListingSubmissionData(
                isbn = textStateISBN.value.text,
                price = textStatePrice.value.text,
                condition = selectedCondition,
                category = selectedCategory,

                isbnValid = isValidISBN,
                priceValid = isValidPrice,
                conditionValid = isValidCondition,
                categoryValid = isValidCondition
            )
        )

    }

    if(!message.isNullOrEmpty()){
        if(message.contains("Error") || message.contains("No results")) {
            ConfirmDialog(
                title = "Oops..",
                content = "$message\n\nPlease try again.",
                isVisible = true,
                confirmButtonText = "Okay",
                onClick = { viewModel.reset() }
            )
        }
        else {
            ConfirmDialog(
                title = "Congratulations!",
                content = "$message\n\nClick anywhere outside the form to exit the editor or keep working.",
                isVisible = true,
                confirmButtonText = "Okay",
                onClick = { viewModel.reset() ; onFormConfirm = true }
            )
        }
    }
}


@Composable
fun SellSubmitButton(
    loading: Boolean,
    viewModel: SellScreenViewModel,
    submissionData : ListingSubmissionData
){
    Button(
        content = {
            // get loading from view model, if loading set content to loading indicator
            if (loading) CircularProgressIndicator(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.size(10.dp),
                strokeWidth = 1.dp,
            )
            else {
                Text(
                    text = "Create",
                    color = MaterialTheme.colorScheme.background,
                    fontSize = 16.sp
                )
            }
        },
        onClick = {
            viewModel.viewModelScope.launch {
                viewModel.createBookListing(
                    MBook(
                        isbn = submissionData.isbn,
                        condition = submissionData.condition,
                        price = submissionData.price.toDouble(),
                        mCategory = submissionData.category,
                    )
                )
            }
            // when loading is done, close view, and send message
        },
        shape = MaterialTheme.shapes.large,
        enabled = (
                (submissionData.isbnValid && submissionData.isbn.isNotEmpty()) &&
                (submissionData.priceValid && submissionData.price.isNotEmpty()) &&
                (submissionData.categoryValid && submissionData.category.isNotEmpty()) &&
                (submissionData.conditionValid && submissionData.condition.isNotEmpty())
        ),
        modifier = Modifier.padding(start = 20.dp, bottom = 20.dp)

    )
}

@Composable
fun ConfirmDialog(
    title: String,
    content: String,
    isVisible: Boolean,
    confirmButtonText: String,
    onClick: () -> Unit,
){

    val (view, setView) = remember { mutableStateOf(isVisible)}

    if(view){
        AlertDialog(
            onDismissRequest = { setView(false) },
            confirmButton = {
                TextButton(onClick = onClick ,
                    content = {Text(confirmButtonText, fontWeight = FontWeight.Bold)
                    }
                )
            },
            title = {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                Text(
                    text = content,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            shape = MaterialTheme.shapes.medium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ISBNTooltip() {
    Row{
        Text("ISBN", fontSize = 15.sp)
        PlainTooltipBox(
            tooltip = {Text("The rest of the book information will be populated using the ISBN.") },
            modifier = Modifier.padding(start = 20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.HelpOutline,
                contentDescription = "ISBN Help Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 5.dp, top = 5.dp)
                    .tooltipAnchor()
                    .size(15.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConditionTooltip(){
    val scope = rememberCoroutineScope()
    val tooltipState by remember {mutableStateOf(RichTooltipState())}
    Row{
        Text("Condition", fontSize = 15.sp)
        RichTooltipBox(
            title = { Text("Guide To Used Book Conditions")},
            text = { ConditionsDescriptions() },
            tooltipState = tooltipState,
            modifier = Modifier.padding(end = 30.dp),
            action =
            {
                 TextButton(
                     onClick = {
                         scope.launch {
                             tooltipState.dismiss()
                         }
                     },
                     content = {Text("Close", fontWeight = FontWeight.Bold, fontSize = 15.sp)})
            },
            ) {
                Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = "Condition ToolTip Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(start = 5.dp, top = 5.dp)
                        .tooltipAnchor()
                        .size(15.dp)
                )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrToolTip( message: String, contentDescription: String){
    Row {
        PlainTooltipBox(
            tooltip = { Text(message) },
            modifier = Modifier.padding(start = 20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(end = 5.dp, top = 2.dp)
                    .tooltipAnchor()
                    .size(15.dp)
            )
        }
        Text("View Errors")
    }
}
@Composable
fun ConditionsDescriptions(){
    LazyColumn {
        MCondition.conditions.forEach { item ->
            item {
                Column {
                    Text(item.returnCondition(), fontWeight = FontWeight.Bold)
                    Text(item.returnDescription())
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }

}

