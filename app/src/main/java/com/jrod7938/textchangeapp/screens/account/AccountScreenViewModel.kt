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

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow



class AccountScreenViewModel {
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    /** Queries database to check if the current user matches the user associated with [...]
     *
     * @param userID a string corresponding to the user associated with a posted book
     *
     * @return a boolean value noting if the current user matches the associated user
     *
     */
    fun verifyCurrentUser(userID: String): Boolean{
        val db = FirebaseFirestore.getInstance()
        val currUser : String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        if(currUser.isEmpty()){
            Log.e("verify current user", "Error - current user's userID is null")
        }
        return userID == currUser
    }

    /** Queries Firestore(the Firebase database) using the bookID and updates the price of a book
     *  listing within the database books collection
     *
     *  @param bookID a string corresponding with a book in the database collection books
     *  @param price a double corresponding with the new price to set the book to
     *
     *  @return unit
     */
    fun updateBookPrice(bookID: String, price: Double){
        if(_loading.value == false){
            _loading.value = true
            val db = FirebaseFirestore.getInstance()
            db.collection("books")
                .document(bookID)
                .update("price",price)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Log.d("Update book price", "update book price was successful ${task.result}")
                    } else {
                        _errorMessage.value = "book price failed to update"
                    }
                }
        }
    }

    /** Queries Firestore(the Firebase database) using the bookID and updates the condition of a book
     *  listing within the database books collection
     *
     *  @param bookID a string corresponding with a book in the database collection books
     *  @param cond a string corresponding with the new condition to set the book to
     *
     *  @return unit
     */
    fun updateBookCondition(bookID: String, cond: Double){
        try {
            if (_loading.value == false) {
                _loading.value = true
                val db = FirebaseFirestore.getInstance()
                db.collection("books")
                    .document(bookID)
                    .update("cond", cond)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(
                                "Update book condition",
                                "update book condition was successful ${task.result}"
                            )
                        } else {
                            _errorMessage.value = "book condition failed to update"
                        }
                    }
            }
        } catch (e: Exception){
            _errorMessage.value = e.message
        }
    }
}