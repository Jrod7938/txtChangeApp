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

import android.util.Log
import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.jrod7938.textchangeapp.model.MUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * ViewModel for the LoginScreen
 *
 * @property auth Firebase Authentication instance
 * @property _loading MutableLiveData<Boolean> to indicate if the user is being created
 * @property loading LiveData<Boolean> to observe if the user is being created
 * @property _errorMessage MutableStateFlow<String?> to indicate if there is an error
 * @property errorMessage StateFlow<String?> to observe if there is an error
 * @property _accountCreatedSignal MutableStateFlow<Boolean> to indicate if the user is created
 * @property accountCreatedSignal StateFlow<Boolean> to observe if the user is created
 * @property _isVerificationSent MutableStateFlow<Boolean> to indicate if the verification email is sent
 * @property isVerificationSent StateFlow<Boolean> to observe if the verification email is sent
 *
 * @constructor Creates a ViewModel for the LoginScreen
 */
class LoginScreenViewModel: ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _accountCreatedSignal = MutableStateFlow(false)
    val accountCreatedSignal: StateFlow<Boolean> = _accountCreatedSignal

    private val _isVerificationSent = MutableStateFlow(false)
    val isVerificationSent: StateFlow<Boolean> = _isVerificationSent


    /**
     * Sign in with email and password
     *
     * @param email email of the user
     * @param password password of the user
     * @param home function to call when the user is signed in
     *
     * @return Unit
     *
     * @see FirebaseAuth.signInWithEmailAndPassword
     */
    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit) = viewModelScope.launch{
        try {
            _loading.value = true
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        Log.d("Firebase", "signInWithEmailAndPassword: ${task.result}")
                        FirebaseFirestore.getInstance().collection("users")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnSuccessListener {
                                try {
                                    val user = MUser.fromDocument(it.documents[0])
                                    if (user.displayName.isNotEmpty()) home()
                                } catch (e: Exception) {
                                    _errorMessage.value =
                                        "There seems to be an error logging you in"
                                    _loading.value = false
                                }
                                // check if user has been instantiated properly
                                // one way to do so is if they have a displayName
                            }.addOnFailureListener {
                                _errorMessage.value = "There seems to be an error logging you in"
                                _loading.value = false
                            }
                    } else {
                        _errorMessage.value = "Incorrect email or password"
                        _loading.value = false
                    }
                }

        } catch (e: Exception){
            _errorMessage.value = e.message
        }
    }

    /**
     * Create a user with email and password within Firebase Authentication and Database
     *
     * @param email email of the user
     * @param firstName first name of the user
     * @param lastName last name of the user
     * @param password password of the user
     * @param home function to call when the user is created
     *
     * @return Unit
     *
     * @see FirebaseAuth.createUserWithEmailAndPassword
     * @see createUser
     */
    fun createUserWithEmailAndPassword(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        home: () -> Unit,
    ){
        if(_loading.value == false){
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        emailVerificationAction(
                            onVerificationComplete = {
                                val displayName = task.result?.user?.email?.split('@')?.get(0)
                                createUser(displayName, firstName, lastName)
                                Log.d(
                                    "Firebase",
                                    "createUserWithEmailAndPassword: Success ${task.result}"
                                )
                                _accountCreatedSignal.value = true
                                home()
                            },
                            onVerificationFailed = {
                                _errorMessage.value = "Your verification link has expired. Please try again."
                                // delete user info from data base if not verified
                                auth.currentUser?.delete()
                            }
                        )
                    } else {
                        _errorMessage.value = "Failed to create user"
                    }
                    _loading.value = false
                    _isVerificationSent.value = false
                    // in case of verification failure, we want to reset back to default value
                }
        }
    }

    /**
     * Calls the sendEmailVerification method on user.
     * This function provided by firebase does not inherently check if the mailbox of the specified user exists.
     *
     * @return Unit
     * @param onVerificationComplete : () -> Unit
     * @param onVerificationFailed : () -> Unit
     *
     * @see waitForEmailVerification
     */
    private fun emailVerificationAction(
        onVerificationComplete: () -> Unit,
        onVerificationFailed: () -> Unit
    ){
        Log.d("loadingStatus", "${loading.value}")
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if(task.isSuccessful){
                    _isVerificationSent.value = true
                    viewModelScope.launch {
                        waitForEmailVerification(
                            onVerificationComplete = onVerificationComplete,
                            onVerificationFailed = onVerificationFailed
                        )
                    }
                }
                else {
                    _isVerificationSent.value = false
                    _errorMessage.value = "We encountered an error trying to validate your email."
                    user.delete()
                }
            }
            ?.addOnCanceledListener {
                user.delete()
                _isVerificationSent.value = false
            }
    }

    /**
     * Defines expiration for verification token sent to user inbox by waiting for user interaction.
     * Makes this check a certain number of times before it fails, and the token is expired.
     * Defines events in the case of success and failure
     *
     * @return Unit
     * @param onVerificationComplete : () -> Unit
     * @param onVerificationFailed: () -> Unit
     *
     * @see emailVerificationAction
     */
    private suspend fun waitForEmailVerification(
        onVerificationComplete: () -> Unit,
        onVerificationFailed: () -> Unit
    ){
        val user = auth.currentUser

        var retries = 0
        val maxRetries = 900 // 15 minutes

        while(user != null && !user.isEmailVerified && retries < maxRetries){
            delay(1000)
            retries++
            try {
                user.reload().await()
            } catch (e: Exception){
                _errorMessage.value = "Error occured while trying to validate user email."
            }
        }

        if(user != null && user.isEmailVerified) {
            onVerificationComplete.invoke()
        }
        else {
            onVerificationFailed.invoke()
        }
    }

    /**
     * Reset error message as the focus on the user form changes
     *
     * @return Unit
     * @param focusState : FocusState
     *
     */
    fun resetErrorMessage(focusState: FocusState){
        if(focusState.isFocused){
            if(!_errorMessage.value.isNullOrEmpty()) _errorMessage.value = null
        }
    }


    /**
     * Reset the ViewModel
     *
     * @return Unit
     */
    fun resetViewModel() {
        _accountCreatedSignal.value = false
    }

    /**
     * Create a user within the Firebase Database
     *
     * @param displayName display name of the user
     * @param firstName first name of the user
     * @param lastName last name of the user
     *
     * @return Unit
     *
     * @see FirebaseFirestore.collection
     */
    private fun createUser(displayName: String?, firstName: String, lastName: String) {
        val userId = auth.currentUser?.uid
        val user = MUser(
            id = displayName.toString(),
            userId = userId.toString(),
            displayName = displayName.toString(),
            firstName = firstName,
            lastName = lastName,
            email = displayName.plus("@pride.hofstra.edu"),
            bookListings = mutableListOf<String>(),
            savedBooks = mutableListOf<String>()
        ).toMap()

        Log.d("Firebase", "$user")

        FirebaseFirestore.getInstance().collection("users")
            .document(displayName.toString())
            .set(user.toMap())
            .addOnSuccessListener {
                Log.d(
                    "Firebase",
                    "createUser: Successful with document ID: ${displayName.toString()}"
                )
            }.addOnFailureListener {
                Log.d("Firebase", "createUser: Failed")
            }
    }
}