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
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.jrod7938.textchangeapp.model.InterestObject
import com.jrod7938.textchangeapp.model.MBook
import com.jrod7938.textchangeapp.model.MUser
import com.jrod7938.textchangeapp.screens.account.AccountScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * ViewModel for the BookInfoScreen.
 *
 * @property email String? the user's email
 * @property userName String? the user's username
 * @property _userName String? the user's username
 * @property userName String? the user's username
 * @property _loading MutableLiveData<Boolean> the loading state
 * @property loading MutableLiveData<Boolean> the loading state
 * @property _message MutableStateFlow<String?> the message state
 * @property message MutableStateFlow<String?> the message state
 * @property _user MutableLiveData<MUser> the user state
 * @property user MutableLiveData<MUser> the user state
 * @property _book MutableLiveData<MBook> the book state
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

    private val _sellerInterestList = MutableLiveData<List<MBook>>(listOf())
    val sellerInterestList: LiveData<List<MBook>> = _sellerInterestList

    private val accountVM = AccountScreenViewModel()



    /**
     * Prepares to send an Email
     *
     * @param mBook MBook to be contacted about
     *
     * @return Intent
     *
     * @see Intent
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
        withContext(Dispatchers.Main) {
            _loading.value = true
        }
        // fetch user data asynchronously
        withContext(Dispatchers.Main) {
            _user.value = accountVM.getUserInfo()
            _loading.value = false
        }
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
                viewModelScope.launch {
                    val book = MBook.fromDocument(document)
                    book.interestList = reloadInterestList(book)
                    _book.postValue(book)
                    _loading.postValue(false)
                    Log.d("fetchBookDetails", "Successfully fetched book details")
                }
            }.addOnFailureListener { exception ->
                _message.value = "Failed to fetch book details: ${exception.message}"
                _loading.postValue(false)
                Log.e("fetchBookDetails", "Failed to fetch book details: ${exception.message}")
            }
    }

    /**
     * Updates the book in the database with the provided details.
     *
     * @param book MBook the edited book details.
     *
     * @return Unit
     *
     * @see MBook
     */
    fun buyerVerifiedBook(book: MBook, currInterestObject: InterestObject) {
        viewModelScope.launch {
            val db = FirebaseFirestore.getInstance()

            val refByBook = db.collection("books")
            val refByCategory = db.collection(book.mCategory)


            // update by book

            refByBook.whereEqualTo("book_id", book.bookID)
                .get()
                .addOnSuccessListener { snapShot ->
                    for (document in snapShot.documents) {
                        val convertedInterestList = mutableListOf<InterestObject>()
                        val interestList = document.get("interest_list") as ArrayList<HashMap<String, Any>>

                        for(item in interestList){ convertedInterestList.add(InterestObject.fromMap(item)) }

                        val updatedInterestObject =
                            convertedInterestList.find { it.interestId == currInterestObject.interestId }
                        updatedInterestObject?.let {
                            it.buyerConfirm = !it.buyerConfirm

                            document.reference.update(
                                "interest_list", FieldValue.arrayRemove(currInterestObject.toMap()),
                                "interest_list", FieldValue.arrayUnion(it.toMap())
                            )
                                .addOnSuccessListener {
                                    Log.d(
                                        "buyerVerifiedBook: BY BOOK",
                                        "Buyer verified value updated successfully"
                                    )
                                }
                                .addOnFailureListener {
                                    Log.d(
                                        "buyerVerifiedBook: BY BOOK",
                                        "Error updating buyer verified value"
                                    )
                                }
                        }

                    }
                }
                .addOnFailureListener { e ->
                    Log.d("buyerVerifiedBook", "Error retrieving document: $e")
                }


            // update by category

            refByCategory.whereEqualTo("book_id", book.bookID)
                .get()
                .addOnSuccessListener { snapShot ->
                    for (document in snapShot.documents) {
                        val convertedInterestList = mutableListOf<InterestObject>()
                        val interestList = document.get("interest_list") as ArrayList<HashMap<String, Any>>

                        for(item in interestList){ convertedInterestList.add(InterestObject.fromMap(item)) }

                        val updatedInterestObject =
                            convertedInterestList.find { it.interestId == currInterestObject.interestId }
                        updatedInterestObject?.let {
                            it.buyerConfirm = !it.buyerConfirm

                            document.reference.update(
                                "interest_list", FieldValue.arrayRemove(currInterestObject.toMap()),
                                "interest_list", FieldValue.arrayUnion(it.toMap())
                            )
                                .addOnSuccessListener {
                                    Log.d(
                                        "buyerVerifiedBook: BY CAT",
                                        "Buyer verified value updated successfully"
                                    )
                                }
                                .addOnFailureListener {
                                    Log.d(
                                        "buyerVerifiedBook: BY CAT",
                                        "Error updating buyer verified value"
                                    )
                                }
                        }

                    }
                }
                .addOnFailureListener { e ->
                    Log.d("buyerVerifiedBook", "Error retrieving document: $e")
                }
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
    fun sellerVerifiedBook(book: MBook, currInterestObject: InterestObject) {
        viewModelScope.launch {
            _loading.postValue(true)
            val db = FirebaseFirestore.getInstance()

            val refByBook = db.collection("books")
            val refByCategory = db.collection(book.mCategory)


            // update by book

            refByBook.whereEqualTo("book_id", book.bookID)
                .get()
                .addOnSuccessListener { snapShot ->
                    for (document in snapShot.documents) {
                        val convertedInterestList = mutableListOf<InterestObject>()
                        db.runTransaction { transaction ->
                            val currBook = transaction.get(document.reference)
                            val interestList = currBook.get("interest_list") as ArrayList<HashMap<String, Any>>

                            for(item in interestList) { convertedInterestList.add(InterestObject.fromMap(item)) }

                            val updatedInterestObject =
                                convertedInterestList.find { it.interestId == currInterestObject.interestId }
                            updatedInterestObject?.let {
                                it.sellerConfirm = !it.sellerConfirm

                                transaction.update(
                                    document.reference,
                                    "interest_list",
                                    FieldValue.arrayRemove(currInterestObject.toMap()),
                                    "interest_list",
                                    FieldValue.arrayUnion(it.toMap())
                                )

                                null
                            }
                        }
                            .addOnSuccessListener {
                                Log.d(
                                    "sellerVerifiedBook: BY BOOK",
                                    "Seller verified value updated successfully"
                                )
                            }
                            .addOnFailureListener {
                                Log.d(
                                    "sellerVerifiedBook: BY BOOK",
                                    "Error updating seller verified value"
                                )
                            }

                    }
                }
                .addOnFailureListener { e ->
                    Log.d("sellerVerifiedBook", "Error retrieving document: $e")
                }.await()


            // update by category

            refByCategory.whereEqualTo("book_id", book.bookID)
                .get()
                .addOnSuccessListener { snapShot ->
                    for (document in snapShot.documents) {
                        val convertedInterestList = mutableListOf<InterestObject>()
                        db.runTransaction { transaction ->
                            val currBook = transaction.get(document.reference)
                            val interestList = currBook.get("interest_list") as ArrayList<HashMap<String, Any>>

                            for(item in interestList) { convertedInterestList.add(InterestObject.fromMap(item)) }

                            val updatedInterestObject =
                                convertedInterestList.find { it.interestId == currInterestObject.interestId }
                            updatedInterestObject?.let {
                                it.sellerConfirm = !it.sellerConfirm

                                transaction.update(
                                    document.reference,
                                    "interest_list",
                                    FieldValue.arrayRemove(currInterestObject.toMap()),
                                    "interest_list",
                                    FieldValue.arrayUnion(it.toMap())
                                )

                                null
                            }
                        }
                            .addOnSuccessListener {
                                Log.d(
                                    "sellerVerifiedBook: BY CAT",
                                    "Seller verified value updated successfully"
                                )
                            }
                            .addOnFailureListener {
                                Log.d(
                                    "sellerVerifiedBook: BY CAT",
                                    "Error updating seller verified value"
                                )
                            }

                    }
                }
                .addOnFailureListener { e ->
                    Log.d("sellerVerifiedBook", "Error retrieving document: $e")
                }.await()
            _loading.postValue(false)
        }
    }


    /**
     * Removes the book from the database if both buyer and seller have confirmed.
     *
     * @param book MBook the book to be checked and possibly removed.
     *
     * @return Unit
     *
     * @see MBook
     */
    fun removeBookIfBothPartiesVerified(book: MBook, currInterestObject: InterestObject) {
        val db = FirebaseFirestore.getInstance()

        if (currInterestObject.buyerConfirm && currInterestObject.sellerConfirm) {

            val userID = book.email.split("@")[0]
            val userReference = db.collection("users").document(userID)
            userReference.update("book_listings", FieldValue.arrayRemove(book.bookID))
                .addOnSuccessListener {
                    Log.d("removeBook", "Successfully removed book from user's book_listings.")
                }.addOnFailureListener { e ->
                    Log.e("removeBook", "Error removing book from user's book_listings: $e")
                }

            val usersCollection = db.collection("users")
            usersCollection.whereArrayContains("saved_books", book.bookID)
                .get().addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        document.reference.update(
                            "saved_books",
                            FieldValue.arrayRemove(book.bookID)
                        )
                    }
                    Log.d("removeBook", "Successfully removed book from saved books of all users.")
                }.addOnFailureListener { e ->
                    Log.e("removeBook", "Error removing book from saved books: $e")
                }

            val categoryReference = db.collection(book.mCategory).document(book.bookID)
            categoryReference.delete()
                .addOnSuccessListener {
                    Log.d("removeBook", "Successfully removed book from category collection.")
                }.addOnFailureListener { e ->
                    Log.e("removeBook", "Error removing book from category collection: $e")
                }

            val bookReference = db.collection("books").document(book.bookID)
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

    fun addInterestObject(book: MBook, buyer: MUser){
        _loading.value = true
        val db = FirebaseFirestore.getInstance()

        val newInterestObject =
            InterestObject(
                interestId = buyer.userId + book.userId,
                buyerConfirm = false,
                sellerConfirm = false,
                userDisplayName = buyer.displayName
            )
        viewModelScope.launch {
            // add by book
            val refByBook = db.collection("books").document(book.bookID)
            refByBook.update("interest_list", FieldValue.arrayUnion(newInterestObject.toMap()))
                .addOnSuccessListener {
                    Log.d("addInterestObject: BY BOOK", "Interest object added successfully")
                    viewModelScope.launch { _book.value?.interestList = reloadInterestList(book) }
                }
                .addOnFailureListener { e ->
                    Log.e("addInterestObject: BY BOOK", "Error adding interest object")
                    _message.value = e.message
                }

            // add by category
            val refByCategory = db.collection(book.mCategory).document(book.bookID)
            refByCategory.update("interest_list", FieldValue.arrayUnion(newInterestObject.toMap()))
                .addOnSuccessListener {
                    Log.d("addInterestObject: BY CAT", "Interest object added successfully")
                    viewModelScope.launch { _book.value?.interestList = reloadInterestList(book) }
                }
                .addOnFailureListener{ e ->
                    Log.e("addInterestObject: BY CAT", "Error adding interest object")
                    _message.value = e.message
                }
        }

        _loading.value = false

    }

    fun deleteInterestObject(book: MBook, currInterestObject: InterestObject){
        _loading.value = true
        val db = FirebaseFirestore.getInstance()

        viewModelScope.launch {
            // remove by book
            val refByBook = db.collection("books").document(book.bookID)
            refByBook.update("interest_list", FieldValue.arrayRemove(currInterestObject.toMap()))
                .addOnSuccessListener {
                    Log.d("deleteInterestObject: BY BOOK", "Interest object removed successfully")
                    viewModelScope.launch { _book.value?.interestList = reloadInterestList(book) }
                }
                .addOnFailureListener { e ->
                    Log.e("deleteInterestObject: BY BOOK", "Error removing interest object")
                    _message.value = e.message
                }

            // remove by category
            val refByCategory = db.collection(book.mCategory).document(book.bookID)
            refByCategory.update("interest_list", FieldValue.arrayRemove(currInterestObject.toMap()))
                .addOnSuccessListener {
                    Log.d("deleteInterestObject: BY CAT", "Interest object removed successfully")
                    viewModelScope.launch { _book.value?.interestList = reloadInterestList(book) }
                }
                .addOnFailureListener { e ->
                    Log.e("deleteInterestObject: BY CAT", "Error removing interest object")
                    _message.value = e.message
                }
        }

        _loading.value = false
    }

    private suspend fun reloadInterestList(book: MBook): List<InterestObject> = withContext(Dispatchers.IO){
        val db = FirebaseFirestore.getInstance()
        val interestList = mutableListOf<InterestObject>()

        Log.d("reloadInterestList", "THIS TYPE: ${interestList::class}")

        try {
            val bookReference = db.collection("books").document(book.bookID).get().await()
            val thisInterestList = bookReference.get("interest_list") as? ArrayList<HashMap<String, Any>> ?: listOf()

            for(item in thisInterestList) {
                try {
                    val interestItem = InterestObject.fromMap(item)
                    interestList.add(interestItem)
                    Log.d("reloadInterestList", "CURR TYPE ${interestList::class}")
                    Log.d("reloadInterestList", "InterestObject added to list")
                } catch (e: Exception) {
                    Log.e("reloadInterestList", "Error adding InterestObject to list", e)
                    _message.emit(e.message)
                }
            }
        } catch(e: Exception){
            Log.e("reloadInterestList", "Error reloading interest list", e)
            _message.emit(e.message)
        }

        for(i in interestList){
            Log.d("reloadInterestList", "TYPE TYPE ${i::class}")
        }
        return@withContext interestList
    }

    fun retrieveSellerInterestList(user: MUser){
        _loading.value = true
        val db = FirebaseFirestore.getInstance()
        val bookList = mutableListOf<MBook>()

        db.collection("books")
            .whereEqualTo("user_id", user.userId)
            .get()
            .addOnSuccessListener { snapShot ->
                for(document in snapShot.documents){
                    val convertedInterestList = mutableListOf<InterestObject>()
                    val interestList = document.get("interest_list") as ArrayList<HashMap<String, Any>>

                    for(item in interestList) {convertedInterestList.add(InterestObject.fromMap(item))}

                    val book = MBook.fromDocument(document)
                    book.interestList = convertedInterestList

                    bookList.add(book)
                }

                _sellerInterestList.value = bookList
                Log.d("retrieveInterestList", "Seller interest list successfully retrieved")
            }.addOnFailureListener { e ->
                Log.e("retrieveInterestList", "Error retrieving seller interest list", e)
                _message.value = e.message
            }

            _loading.value = false
    }

}