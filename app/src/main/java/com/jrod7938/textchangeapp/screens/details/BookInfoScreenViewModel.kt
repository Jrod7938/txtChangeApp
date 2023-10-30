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

package com.jrod7938.textchangeapp.screens.details

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jrod7938.textchangeapp.model.MBook
import com.jrod7938.textchangeapp.model.MUser
import com.jrod7938.textchangeapp.screens.account.AccountScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel for the BookInfoScreen.
 *
 * @property email String? the user's email
 * @property userName String? the user's username
 * @property _userName String? the user's username
 * @property loading MutableLiveData<Boolean> the loading state
 * @property message MutableStateFlow<String?> the message state
 * @property user MutableLiveData<MUser> the user state
 * @property book MutableLiveData<MBook> the book state
 * @property accountVM AccountScreenViewModel the account view model
 *
 * @see ViewModel
 * @see BookInfoScreen
 * @see MBook
 */
class BookInfoScreenViewModel : ViewModel() {

    val email = FirebaseAuth.getInstance().currentUser?.email

    private val _userName: String? =
        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)
    val userName = _userName

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _user = MutableLiveData<MUser>(null)
    val user: LiveData<MUser> = _user

    private val _book = MutableLiveData<MBook>(null)
    val book: LiveData<MBook> = _book

    private val accountVM = AccountScreenViewModel()

    /**
     * Prepares to send an Email
     *
     * @param mBook MBook to be contacted about
     *
     * @return Intent
     *
     * @see Int
     */
    fun prepareInterestEmailIntent(mBook: MBook): Intent {
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT, "Hello, I am interested in purchasing your book " +
                        "titled '${mBook.title}' for $${mBook.price}. You can contact me at $email." +
                        "\n\nReminder from txtChange Team: Please check the confirmation boxes in the book " +
                        "details page once the sale has been completed."
            )
            putExtra(Intent.EXTRA_SUBJECT, "txtChange: Interest in Book ${mBook.title}")
            putExtra(Intent.EXTRA_BCC, arrayOf("txtChangeTeam@gmail.com"))
            putExtra(Intent.EXTRA_EMAIL, arrayOf(mBook.email))
        }

        return emailIntent
    }

    /**
     * Fetches the user details from the database
     *
     * @param userID String the user id
     *
     * @return Unit
     *
     * @see MUser
     * @see FirebaseFirestore
     * @see AccountScreenViewModel
     */
    suspend fun getUser(userID: String = userName!!) {
        _loading.postValue(true)
        _user.value = accountVM.getUserInfo()
        _loading.postValue(false)
    }

    /**
     * Fetches the book details from the database
     *
     * @param bookId String the book id
     *
     * @return Unit
     *
     * @see MBook
     * @see FirebaseFirestore
     */
    fun fetchBookDetails(bookId: String) {
        _loading.postValue(true)
        val db = FirebaseFirestore.getInstance()
        db.collection("books")
            .document(bookId)
            .get()
            .addOnSuccessListener { document ->
                val book = MBook.fromDocument(document)
                _book.postValue(book)
                _loading.postValue(false)
                Log.d("fetchBookDetails", "Successfully fetched book details")
            }.addOnFailureListener { exception ->
                _message.value = "Failed to fetch book details: ${exception.message}"
                _loading.postValue(false)
                Log.e("fetchBookDetails", "Failed to fetch book details: ${exception.message}")
            }
    }

    /**
     * Updates the book in the database with the provided details.
     *
     * @param mBook MBook the edited book details.
     *
     * @return Unit
     *
     * @see MBook
     */
    fun buyerVerifiedBook(mBook: MBook) {
        val db = FirebaseFirestore.getInstance()

        val bookReference = db.collection("books").document(mBook.bookID)
        bookReference.update("buyer_confirm", !mBook.buyerConfirm)
            .addOnSuccessListener {
                Log.d("buyerVerifiedBook", "Successfully updated buyerConfirm in books collection.")
            }.addOnFailureListener { e ->
                Log.e("buyerVerifiedBook", "Error updating buyerConfirm in books collection: $e")
                _message.value = e.message
            }

        val categoryReference = db.collection(mBook.mCategory).document(mBook.bookID)
        categoryReference.update("buyer_confirm", !mBook.buyerConfirm)
            .addOnSuccessListener {
                Log.d(
                    "buyerVerifiedBook",
                    "Successfully updated buyerConfirm in books mCategory collection."
                )
            }.addOnFailureListener { e ->
                Log.e(
                    "buyerVerifiedBook",
                    "Error updating buyerConfirm in books mCategory collection: $e"
                )
                _message.value = e.message
            }
    }

    /**
     * Updates the book in the database with the provided details.
     *
     * @param mBook MBook the edited book details.
     *
     * @return Unit
     *
     * @see MBook
     */
    fun sellerVerifiedBook(mBook: MBook) {
        val db = FirebaseFirestore.getInstance()

        val bookReference = db.collection("books").document(mBook.bookID)
        bookReference.update("seller_confirm", !mBook.sellerConfirm)
            .addOnSuccessListener {
                Log.d(
                    "sellerVerifiedBook",
                    "Successfully updated sellerConfirm in books collection."
                )
            }.addOnFailureListener { e ->
                Log.e("sellerVerifiedBook", "Error updating sellerConfirm in books collection: $e")
                _message.value = e.message
            }

        val categoryReference = db.collection(mBook.mCategory).document(mBook.bookID)
        categoryReference.update("seller_confirm", !mBook.sellerConfirm)
            .addOnSuccessListener {
                Log.d(
                    "sellerVerifiedBook",
                    "Successfully updated sellerConfirm in books mCategory collection."
                )
            }.addOnFailureListener { e ->
                Log.e(
                    "sellerVerifiedBook",
                    "Error updating sellerConfirm in books mCategory collection: $e"
                )
                _message.value = e.message
            }
    }

    /**
     * Removes the book from the database if both buyer and seller have confirmed.
     *
     * @param mBook MBook the book to be checked and possibly removed.
     *
     * @return Unit
     *
     * @see MBook
     */
    fun removeBookIfBothPartiesVerified(mBook: MBook) {
        val db = FirebaseFirestore.getInstance()

        if (mBook.buyerConfirm && mBook.sellerConfirm) {

            val userID = mBook.email.split("@")[0]
            val userReference = db.collection("users").document(userID)
            userReference.update("book_listings", FieldValue.arrayRemove(mBook.bookID))
                .addOnSuccessListener {
                    Log.d("removeBook", "Successfully removed book from user's book_listings.")
                }.addOnFailureListener { e ->
                    Log.e("removeBook", "Error removing book from user's book_listings: $e")
                }

            val usersCollection = db.collection("users")
            usersCollection.whereArrayContains("saved_books", mBook.bookID)
                .get().addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        document.reference.update(
                            "saved_books",
                            FieldValue.arrayRemove(mBook.bookID)
                        )
                    }
                    Log.d("removeBook", "Successfully removed book from saved books of all users.")
                }.addOnFailureListener { e ->
                    Log.e("removeBook", "Error removing book from saved books: $e")
                }

            val categoryReference = db.collection(mBook.mCategory).document(mBook.bookID)
            categoryReference.delete()
                .addOnSuccessListener {
                    Log.d("removeBook", "Successfully removed book from category collection.")
                }.addOnFailureListener { e ->
                    Log.e("removeBook", "Error removing book from category collection: $e")
                }

            val bookReference = db.collection("books").document(mBook.bookID)
            bookReference.delete()
                .addOnSuccessListener {
                    Log.d("removeBook", "Successfully removed book from books collection.")
                }.addOnFailureListener { e ->
                    Log.e("removeBook", "Error removing book from books collection: $e")
                }
        }
    }


    /**
     * Saves the book in the user's collection by updating the "saved_books" field.
     *
     * @param book MBook the book details.
     *
     * @return Unit
     *
     * @see MBook
     */
    fun saveBook(book: MBook) {
        val db = FirebaseFirestore.getInstance()

        val userReference = db.collection("users").document(userName!!)
        userReference.update("saved_books", FieldValue.arrayUnion(book.bookID))
            .addOnSuccessListener {
                Log.d("saveBook", "Book successfully saved.")
            }.addOnFailureListener { e ->
                Log.e("saveBook", "Error saving book: $e")
                _message.value = e.message
            }
    }

    /**
     * Removes the book from the user's saved books collection by updating the "saved_books" field.
     *
     * @param book MBook the book details.
     *
     * @return Unit
     *
     * @see MBook
     */
    fun unsaveBook(book: MBook) {
        val db = FirebaseFirestore.getInstance()

        val userReference = db.collection("users").document(userName!!)
        userReference.update("saved_books", FieldValue.arrayRemove(book.bookID))
            .addOnSuccessListener {
                Log.d("unsaveBook", "Book successfully removed from saved books.")
            }.addOnFailureListener { e ->
                Log.e("unsaveBook", "Error removing book from saved books: $e")
                _message.value = e.message
            }
    }

}