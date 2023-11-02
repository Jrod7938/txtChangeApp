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

package com.jrod7938.textchangeapp.screens.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.jrod7938.textchangeapp.model.MBook
import com.jrod7938.textchangeapp.model.MCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Viewmodel for the Home Screen
 *
 * @property db FirebaseFirestore the firestore database
 * @property _loading MutableLiveData<Boolean> the loading state of the screen
 * @property loading LiveData<Boolean> the loading state of the screen
 * @property _message MutableStateFlow<String?> the message to display
 * @property message StateFlow<String?> the message to display
 * @property _bookCategories MutableLiveData<List<MBook>> the list of books
 * @property bookCategories LiveData<List<MBook>> the list of books
 */
class HomeScreenViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var dataFetched = false

    private val _loading = MutableLiveData(false)
    var loading: LiveData<Boolean> = _loading

    private val _message = MutableStateFlow<String?>("")
    var message: StateFlow<String?> = _message

    private val _bookCategories = MutableLiveData<HashMap<String, MBook>>()
    val bookCategories: LiveData<HashMap<String, MBook>> = _bookCategories

    // Initializing the bookCategories hashmap
    init {
        if (!dataFetched) {
            viewModelScope.launch {
                _loading.value = true
                _bookCategories.value = getBookCategories()
                dataFetched = true
                _loading.value = false
            }
        }
    }

    /**
     * Gets all the books in the database based on categories
     *
     * @return HashMap<String, List<MBook>>
     */
    private suspend fun getBookCategories(): LinkedHashMap<String, MBook> =
        withContext(Dispatchers.IO) {
            val bookCategories = HashMap<String, MBook>()

            MCategory.categories.forEach { category ->
                try {
                    val querySnapshot = db.collection(category.toString())
                        .limit(1)
                        .get()
                        .await()

                    querySnapshot.documents.firstOrNull()?.let {
                        val book = MBook.fromDocument(it)
                        bookCategories[category.toString()] = book
                    }
                } catch (e: Exception) {
                    Log.e("GetBooksCategories", "Error: $e")
                    _message.value = "Error: ${e.message}"
                }
            }
            val sortedEntries = bookCategories.entries.sortedBy { (_, book) -> book.mCategory }
            return@withContext LinkedHashMap(sortedEntries.associateBy({ it.key }, { it.value }))
    }
}
