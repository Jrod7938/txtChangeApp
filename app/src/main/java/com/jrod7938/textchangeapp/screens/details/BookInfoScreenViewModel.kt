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

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jrod7938.textchangeapp.model.MBook

class BookInfoScreenViewModel : ViewModel() {

    private val userName: String? =
        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)

    /**
     * Queries Firestore (the Firebase database) using the input userID
     * to delete the input bookID from the user's book_listings field.
     *
     * @param bookID A String corresponding to a book in the database's books collection.
     * Assumes that each book_id field is unique for each document in the collection.
     * @param user A String corresponding to a user in the database's users collection.
     *
     * @return Unit
     *
     * @see deleteListing
     * @see deleteBook
     */
    private fun updateUserBookListings(bookID: String, user: String = userName!!) {
        // Performs an update to the user's document by deleting the bookID from book_listings field
        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection("users").document(user)
        userDocRef.update("book_listings", FieldValue.arrayRemove(bookID))
            .addOnSuccessListener {
                Log.d(
                    "updateUserBookListings",
                    "Successful deletion from the current user's book_listings field." +
                            "\nuser_id: $user \nDeleted book_id: $bookID"
                )
            }
            .addOnFailureListener { exception ->
                Log.e(
                    "updateUserBookListings",
                    "Error in updating the book_listings field in the user document: $exception" +
                            "\nuser_id: $user " +
                            "\nbook_id to delete: $bookID"
                )
            }
    }

    /**
     * Queries Firestore (the Firebase database) using the input bookID & userID
     * to delete the corresponding book from the books collection.
     * After a successful deletion, calls the updateUserBookListings
     * with the same bookID & userID to delete from the corresponding user.
     *
     * @param bookID A String corresponding to a book in the database's books collection.
     * Assumes that each book_id field is unique for each document in the collection.
     * @param userID A String corresponding to a user in the database's users collection.
     * Assumes that each user_id field is unique for each document in the collection.
     *
     * @return Unit
     *
     * @see deleteListing
     * @see updateUserBookListings
     */
    private fun deleteBook(bookID: String, userID: String) {
        // Builds a query to search for the book in the books collection
        val booksRef = FirebaseFirestore.getInstance().collection("books")
        val bookQuery: Query =
            booksRef.whereEqualTo("user_id", userID)
                .whereEqualTo("book_id", bookID)

        // Executes the book query to delete the book from the database
        // then if successful, subsequently executes updateUserBookListings function
        // (no parallel queries to prevent further errors in updateUserBookListings if this function fails)
        bookQuery.get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val bookDocRef = documents.documents[0].reference
                    bookDocRef.delete()
                        .addOnSuccessListener {
                            Log.d(
                                "deleteBook",
                                "Successful deletion of book document from database. " +
                                        "\nDeleted book's book_id: $bookID \nDeleted book's user_id: $userID "
                            )
                            updateUserBookListings(bookID)
                        }
                        .addOnFailureListener { exception ->
                            Log.e(
                                "deleteBook", "Error in deleting book document: $exception " +
                                        "\nbook_id: $bookID \nuser_id: $userID "
                            )
                        }
                } else {
                    Log.e("deleteBook", "Error - documents: QuerySnapshot! is empty: $documents")
                }
            }
            .addOnFailureListener { exception ->
                Log.e(
                    "deleteBook", "Error in searching for the book document: $exception" +
                            "\nuser_id: $userID \nbook_id: $bookID"
                )
            }
    }

    /**
     * Deletes a book listing from the current user's bookListings List
     * by using their user ID and using the book ID inputted to delete the book
     * from the books collections then the user's book_listings field in the database.
     *
     * @param bookID A String corresponding to a book in the database's books collection.
     * Assumes that each book_id field is unique for each document in the collection.
     *
     * @return Unit
     *
     * @see MBook for bookID
     * @see deleteBook for book collections delete query
     * @see updateUserBookListings for user's book_listings update
     */
    fun deleteListing(bookID: String) {
        // Performs a check on the current user's id then, if valid, executes deleteBook
        val userID: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        if (userID.isEmpty()) {
            Log.e("deleteListing", "Error - current user's userID is null")
            return // Stops function here
        } else {
            deleteBook(bookID, userID)
        }
    }
}