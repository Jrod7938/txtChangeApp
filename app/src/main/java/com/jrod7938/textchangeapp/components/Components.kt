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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.jrod7938.textchangeapp.R
import com.jrod7938.textchangeapp.model.MBook
import com.jrod7938.textchangeapp.model.MUser
import com.jrod7938.textchangeapp.navigation.AppScreens
import com.jrod7938.textchangeapp.navigation.BottomNavItem
import com.jrod7938.textchangeapp.screens.account.AccountScreenViewModel
import com.jrod7938.textchangeapp.screens.details.BookInfoScreenViewModel
import com.jrod7938.textchangeapp.screens.home.HomeScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * This composable is the App Logo. It displays the app logo as a circle with
 * the text "txt. CHANGE" inside of it.
 *
 * @param txtSize the size of the "txt." text
 * @param changeSize the size of the "CHANGE" text
 */
@Composable
fun AppLogo(txtSize: TextUnit = 42.sp, changeSize: TextUnit = 42.sp, appLogoSize: Dp = 50.dp) {
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

    val modifier = Modifier
        .fillMaxHeight(.6f)
        .background(color = MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())

    Column(
        modifier = modifier,
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
fun BottomNavigationBar(
    navController: NavHostController,
    items: List<BottomNavItem>
) {
    BottomNavigation(
        elevation = 10.dp,
        modifier = Modifier.height(70.dp),
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        imageVector = if (currentRoute?.contains(item.route) == true) item.selectedIcon else item.unselectedIcon,
                        contentDescription = null,
                        tint = if (currentRoute?.contains(item.route) == true) MaterialTheme.colorScheme.primary else Color.DarkGray
                    )
                },
                label = {
                    Text(
                        item.title,
                        color = if (currentRoute == item.route) MaterialTheme.colorScheme.primary else Color.DarkGray
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route)
                    }
                },
                selectedContentColor = MaterialTheme.colorScheme.onBackground,
                unselectedContentColor = Color.DarkGray
            )
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
                AppLogo(txtSize = 30.sp, changeSize = 30.sp, appLogoSize = 54.dp)
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
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 20.sp),
                textAlign = TextAlign.Center,
                maxLines = 2
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
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier
                .padding(top = 16.dp)
                .size(width = 200.dp, height = 40.dp),
            onClick = { navController.navigate(AppScreens.SearchScreen.name) }
        ) {
            Text(text = "Find A Book")
        }
        Button(
            modifier = Modifier
                .padding(16.dp)
                .size(width = 200.dp, height = 40.dp),
            onClick = { navController.navigate(AppScreens.SellBookScreen.name) }
        ) {
            Text(text = "Sell A Book")
        }
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
                        text = book.title,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            shape = RectangleShape,
                            onClick = { currentlyEditingBook.value = book }
                        ) {
                            Text(text = "Edit", fontSize = 12.sp)
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            shape = RectangleShape,
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
        )
        IconButton(
            modifier = Modifier.size(30.dp),
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate(AppScreens.LoginScreen.name)
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
                listOf("New", "Used", "Worn").forEach { condition ->
                    DropdownMenuItem(
                        onClick = {
                            onConditionSelected(condition)
                            isDropdownExpanded = false
                        }
                    ) {
                        Text(condition, color = Color.Black)
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
            .padding(8.dp)
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
            Text(text = "Category: ${book.category}", fontSize = 16.sp)
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
