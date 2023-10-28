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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jrod7938.textchangeapp.model.MBook
import com.jrod7938.textchangeapp.model.MUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Viewmodel for the Account Screen
 *
 * @property db FirebaseFirestore the firestore database
 * @property userDocID String? the user document id
 * @property dataFetched Boolean if the data has been fetched
 * @property _loading MutableLiveData<Boolean> the loading state of the screen
 * @property loading LiveData<Boolean> the loading state of the screen
 * @property _message MutableStateFlow<String?> the message to display
 * @property message StateFlow<String?> the message to display
 * @property _mUser MutableLiveData<MUser> the user
 * @property mUser LiveData<MUser> the user
 */
class AccountScreenViewModel : ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val userDocID = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)

    private var dataFetched = false

    private val _loading = MutableLiveData(false)
    var loading: LiveData<Boolean> = _loading

    private val _message = MutableStateFlow<String?>("")
    var message: StateFlow<String?> = _message

    private val _mUser = MutableLiveData<MUser>()
    val mUser: LiveData<MUser> = _mUser

    private val _bookListings = MutableLiveData<List<MBook>>()
    val bookListings: LiveData<List<MBook>> = _bookListings

    // Initializing the user
    init {
        if (!dataFetched) {
            viewModelScope.launch {
                _loading.value = true
                _mUser.value = getUserInfo()
                _bookListings.value = getBookListings()
                dataFetched = true
                _loading.value = false
            }
        }
    }

    /**
     * Gets the book listings from the database
     *
     * @return List<MBook>? the list of books
     *
     * @see MBook
     */
    private suspend fun getBookListings(): List<MBook>? = withContext(Dispatchers.IO) {
        val docRef = db.collection("users").document(userDocID ?: return@withContext null)

        try {
            val docSnapshot = docRef.get().await()
            if (docSnapshot.exists()) {
                val bookIds = docSnapshot.get("book_listings") as? List<String> ?: emptyList()

                val books = mutableListOf<MBook>()

                for (bookId in bookIds) {
                    val bookSnapshot = db.collection("books").document(bookId).get().await()

                    if (bookSnapshot.exists()) {
                        val book = MBook.fromDocument(bookSnapshot)
                        books.add(book)
                    }
                }

                return@withContext books
            }
        } catch (e: Exception) {
            Log.e("AccountScreenViewModel", "Error fetching book listings", e)
            _message.tryEmit(e.localizedMessage)
        }

        return@withContext null
    }

    /**
     * Gets the user info from the database
     *
     * @return MUser? the user
     *
     * @see MUser
     */
    suspend fun getUserInfo(): MUser? = withContext(Dispatchers.IO) {
        val docRef = db.collection("users").document(userDocID ?: return@withContext null)

        try {
            val docSnapshot = docRef.get().await()
            if (docSnapshot.exists()) {
                return@withContext MUser.fromDocument(docSnapshot)
            }
        } catch (e: Exception) {
            Log.e("AccountScreenViewModel", "Error fetching user info", e)
            _message.tryEmit(e.localizedMessage)
        }
        return@withContext null
    }

    /**
     * Deletes the book from the mCategory, books, and users collection
     * Also deletes the book from the saved_books array of all users that have saved the book
     *
     * @param book MBook the book to delete
     *
     * @see MBook
     */
    fun deleteBook(book: MBook) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val batch = db.batch()

                val bookRef = db.collection("books").document(book.bookID)
                batch.delete(bookRef)
                Log.d("AccountScreenViewModel", "Deleted book from books")

                val categoryRef = db.collection(book.mCategory).document(book.bookID)
                batch.delete(categoryRef)
                Log.d("AccountScreenViewModel", "Deleted book from ${book.mCategory}")

                val userRef = db.collection("users").document(userDocID!!)
                batch.update(userRef, "book_listings", FieldValue.arrayRemove(book.bookID))
                Log.d("AccountScreenViewModel", "Deleted book from user")

                val usersWithSavedBook = db.collection("users")
                    .whereArrayContains("saved_books", book.bookID)
                    .get()
                    .await()

                for (userDoc in usersWithSavedBook) {
                    val userWithSavedBookRef = db.collection("users").document(userDoc.id)
                    batch.update(
                        userWithSavedBookRef,
                        "saved_books",
                        FieldValue.arrayRemove(book.bookID)
                    )

                }
                Log.d("AccountScreenViewModel", "Deleted book from users with saved book")

                batch.commit().await()
                _bookListings.value = getBookListings()
                _message.tryEmit("Book deleted successfully")
            } catch (e: Exception) {
                Log.e("AccountScreenViewModel", "Error deleting book", e)
                _message.tryEmit(e.localizedMessage)
            }
            _loading.value = false
        }
    }

    /**
     * Updates the book in the database with the provided details.
     *
     * @param book MBook the edited book details.
     */
    fun updateBook(book: MBook) {
        Log.d("AccountScreenViewModel", "Updating book: $book")
        viewModelScope.launch {
            _loading.value = true
            try {
                db.collection("books").document(book.bookID)
                    .update(
                        mapOf(
                            "condition" to book.condition,
                            "price" to book.price
                        )
                    ).addOnSuccessListener {
                        Log.d("AccountScreenViewModel", "Book updated successfully")
                        db.collection(book.mCategory).document(book.bookID)
                            .update(
                                mapOf(
                                    "condition" to book.condition,
                                    "price" to book.price
                                )
                            ).addOnSuccessListener {
                                Log.d(
                                    "AccountScreenViewModel",
                                    "Book category updated successfully"
                                )
                                viewModelScope.launch {
                                    _bookListings.value = getBookListings()
                                }
                            }
                        _message.tryEmit("Book updated successfully")
                    }.addOnFailureListener { e ->
                        Log.e("AccountScreenViewModel", "Error updating book", e)
                        _message.tryEmit(e.localizedMessage)
                    }
            } catch (e: Exception) {
                Log.e("AccountScreenViewModel", "Error updating book", e)
                _message.tryEmit(e.localizedMessage)
            }
            _loading.value = false
        }
    }
}
