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

package com.jrod7938.textchangeapp.screens.sell

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonParser
import com.jrod7938.textchangeapp.model.MBook
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * ViewModel for the SellScreen
 *
 * @property auth FirebaseAuth the Firebase authentication instance
 * @property db FirebaseFirestore the Firebase Firestore instance
 * @property client OkHttpClient the OkHttpClient instance
 * @property _loading MutableLiveData<Boolean> the loading state of the screen
 * @property loading LiveData<Boolean> the loading state of the screen
 * @property _message MutableStateFlow<String?> the message to be displayed on the screen
 * @property message StateFlow<String?> the message to be displayed on the screen
 *
 * @see FirebaseAuth
 * @see FirebaseFirestore
 * @see OkHttpClient
 */
class SellScreenViewModel : ViewModel() {

    val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val client = OkHttpClient()

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    /**
     * Creates a book listing in Firestore and updates the user's book listings
     *
     * @param book MBook the book to be listed
     *
     * @return Unit
     *
     * @see MBook
     * @see FirebaseFirestore
     * @see FirebaseAuth
     */
    fun createBookListing(book: MBook) {
        _loading.value = true

        // Create a document in the books collection
        val bookRef = db.collection("books").document()
        val bookId = bookRef.id
        book.bookID = bookId

        val userId = auth.currentUser?.uid
        if (userId == null) {
            _message.value = "Error: User not logged in"
            _loading.value = false
            return
        }

        // Use the authenticated user's email directly
        val userEmail = auth.currentUser?.email
        if (userEmail == null) {
            _message.value = "Error: Email not found for logged in user"
            _loading.value = false
            return
        }
        book.email = userEmail
        book.userId = userId

        // Make a query request to Google Books API
        val isbn = book.isbn
        val userName = userEmail.split('@')[0]
        fetchFromGoogleBooksAPI(isbn) { imageUrl, description, category, title, author ->
            book.imageURL = imageUrl
            book.description = description
            book.category = category
            book.title = title
            book.author = author

            // Add the book to Firestore
            bookRef.set(book.toMap())
                .addOnSuccessListener {
                    // Update user's book listings
                    val userRef = db.collection("users").document(userName)
                    userRef.update("book_listings", FieldValue.arrayUnion(bookId))
                        .addOnSuccessListener {
                            // update the book category collection
                            db.collection(book.mCategory).document(bookId).set(book.toMap())
                            _message.value = "Book added successfully"
                            _loading.value = false
                        }
                        .addOnFailureListener { e ->
                            _message.value = "Error updating user's book listings: ${e.message}"
                            _loading.value = false
                        }
                }.addOnFailureListener { e ->
                    _message.value = "Error adding book: ${e.message}"
                    _loading.value = false
                }
        }
    }

    /**
     * Makes a request to Google Books API to get the image url and description of a book
     *
     * @param isbn String the isbn of the book
     * @param callback Function2<String, String, String, Unit> a callback function that takes in the image
     * url, description of the book, and a book category
     *
     * @return Unit
     */
    private fun fetchFromGoogleBooksAPI(
        isbn: String,
        callback: (String, String, String, String, String) -> Unit
    ) =
        GlobalScope.launch {
            val url = "https://www.googleapis.com/books/v1/volumes?q=isbn:$isbn"

            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).execute().use { response ->
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    val jsonElement = JsonParser.parseString(responseBody)
                    val items = jsonElement.asJsonObject.get("items")
                    if (items != null && items.asJsonArray.size() > 0) {
                        val volumeInfo = items.asJsonArray[0].asJsonObject.get("volumeInfo")
                        val imageUrl =
                            volumeInfo.asJsonObject.get("imageLinks")?.asJsonObject?.get("thumbnail")?.asString
                        val description = volumeInfo.asJsonObject.get("description")?.asString
                        val categoriesArray = volumeInfo.asJsonObject.get("categories")?.asJsonArray
                        val mainCategory = categoriesArray?.get(0)?.asString ?: "Unknown"
                        val title =
                            volumeInfo.asJsonObject.get("title")?.asString ?: "Unknown Title"
                        val authorsArray = volumeInfo.asJsonObject.get("authors")?.asJsonArray
                        val author = authorsArray?.get(0)?.asString ?: "Unknown Author"
                        if (imageUrl != null && description != null) {
                            callback(imageUrl, description, mainCategory, title, author)
                        } else {
                            _message.value = "Error fetching book details from Google Books API"
                        }
                    } else {
                        _message.value = "No results found on Google Books API for the given ISBN"
                    }
                } else {
                    _message.value = "Error fetching book details from Google Books API"
                }
            }
        }
}

