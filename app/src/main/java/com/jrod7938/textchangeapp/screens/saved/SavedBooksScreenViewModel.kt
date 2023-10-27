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

package com.jrod7938.textchangeapp.screens.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.jrod7938.textchangeapp.model.MBook
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.firebase.firestore.FirebaseFirestore

class SavedBooksScreenViewModel {
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val userID: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val _sbooks = MutableLiveData<List<MBook>>()
    val sbooks: LiveData<List<MBook>> = _sbooks

    /**
     * Queries Firestore (Firebase database) to acquire the current user's list of saved books
     *
     * @see MBook
     */
    fun LoadSavedBooks() {
        if (_loading.value == false) {
            _loading.value = true
            val db = FirebaseFirestore.getInstance()
            try {
                db.collection("users").document(userID).get()
                    .addOnSuccessListener { document ->
                        val sbooklist = document.get("saved_books") as List<String>
                        val books = mutableListOf<MBook>()
                        for (book in sbooklist) {
                            db.collection("books")
                                .document(book)
                                .get()
                                .addOnSuccessListener { document ->
                                    val mBook = MBook.fromDocument(document)
                                    books.add(mBook)
                                }.addOnFailureListener {exception->
                                    _errorMessage.value = exception.message
                                }
                        }
                        _sbooks.value = books
                    }.addOnFailureListener {exception->
                        _errorMessage.value = exception.message
                    }


            } catch(e: Exception){
                _errorMessage.value = e.message
            }
            _loading.value = false
        }
    }
}