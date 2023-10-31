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

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jrod7938.textchangeapp.model.MBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * ViewModel for the SavedBooksScreen
 *
 * @property _loading MutableLiveData<Boolean> that represents the loading state of the screen
 * @property loading LiveData<Boolean> that represents the loading state of the screen
 * @property _errorMessage MutableStateFlow<String?> that represents the error message of the screen
 * @property errorMessage StateFlow<String?> that represents the error message of the screen
 * @property userDoc String that represents the current user's email without @pride.hofstra.edu
 * @property _savedBooks MutableLiveData<List<MBook>> that represents the current user's saved books
 * @property savedBooks LiveData<List<MBook>> that represents the current user's saved books
 *
 * @see ViewModel
 * @see MBook
 */
class SavedBooksScreenViewModel : ViewModel() {
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val userDoc: String =
        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) ?: ""

    private val _savedBooks = MutableLiveData<List<MBook>>()
    val savedBooks: LiveData<List<MBook>> = _savedBooks

    private var fetchedBooks = false

    init {

        if (!fetchedBooks) {
            viewModelScope.launch {
                _savedBooks.value = loadSavedBooks()
                fetchedBooks = true
            }
        }
    }

    /**
     * Queries Firestore (Firebase database) to acquire the current user's list of saved books
     *
     * @return Unit
     *
     * @see MBook
     */
    private suspend fun loadSavedBooks(): List<MBook>? = withContext(Dispatchers.IO) {
        _loading.postValue(true)
        val db = FirebaseFirestore.getInstance()
        val books = mutableListOf<MBook>()

        try {
            val document = db.collection("users").document(userDoc).get().await()
            val savedBooks = document.get("saved_books") as? List<String> ?: listOf()

            for (book in savedBooks) {
                try {
                    val doc = db.collection("books").document(book).get().await()
                    val mBook = MBook.fromDocument(doc)
                    books.add(mBook)
                    Log.d("SavedBooksScreenViewModel", "loadSavedBooks: Added to list")
                } catch (e: Exception) {
                    Log.e("SavedBooksScreenViewModel", "Error fetching a saved book", e)
                    _errorMessage.emit(e.message)
                }
            }
            Log.d("SavedBooksScreenViewModel", "loadSavedBooks: Complete")
        } catch (e: Exception) {
            Log.e("SavedBooksScreenViewModel", "Error fetching user's saved books", e)
            _errorMessage.emit(e.message)
        }

        _loading.postValue(false)
        return@withContext books
    }

}