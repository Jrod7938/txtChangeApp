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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.jrod7938.textchangeapp.model.MUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the LoginScreen
 *
 * @property auth Firebase Authentication instance
 * @property _loading MutableLiveData<Boolean> to indicate if the user is being created
 * @property loading LiveData<Boolean> to observe if the user is being created
 * @property _accountCreatedSignal MutableStateFlow<Boolean> to indicate if the user is created
 * @property accountCreatedSignal StateFlow<Boolean> to observe if the user is created
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
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful){
                        Log.d("Firebase", "signInWithEmailAndPassword: ${task.result}")
                        home()
                    } else {
                        _errorMessage.value = "Incorrect email or password"
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
        home: () -> Unit
    ){
        if(_loading.value == false){
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val displayName = task.result?.user?.email?.split('@')?.get(0)
                        createUser(displayName, firstName, lastName)
                        Log.d("Firebase", "createUserWithEmailAndPassword: Success ${task.result}")
                        _accountCreatedSignal.value = true
                        home()
                    } else {
                        _errorMessage.value = "Failed to create user"
                    }
                    _loading.value = false
                }
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