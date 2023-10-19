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

package com.jrod7938.textchangeapp.model

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

data class MUser(
    val id: String?,
    val userId: String,
    val displayName: String,
    val email: String,
    val bookListings: List<String>,
    val savedBooks: List<String>
) {
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "user_id" to this.userId,
            "display_name" to this.displayName,
            "email" to this.email,
            "book_listings" to this.bookListings,
            "saved_books" to this.savedBooks
        )
    }

    /**
     * Deletes a book listing from the current user's bookListings List
     * by using the book's bookID and userId to delete from
     * the books collections and to delete from the user's "book_listings" field
     * in the database.
     *
     * @param bookID A String that corresponds to a book in the database's books collection.
     * Assumes that each bookID in the database is unique.
     *
     * @return Void
     *
     * @see MBook for bookID
     */
    fun deleteListing(bookID: String) {
        // Initializes Firestore to access the books collection & the user document
        val db = FirebaseFirestore.getInstance()
        val booksRef = db.collection("books")
        val userDocRef = db.collection("users").document(this.userId)

        // Builds the query to search for the book in the books collection
        val bookQuery: Query =
            booksRef.whereEqualTo("user_id", this.userId)
            booksRef.whereEqualTo("book_id", bookID)

        // Executes query to delete the book from the database
        bookQuery.get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val bookDocRef = documents.documents[0].reference
                    bookDocRef.delete()
                        .addOnSuccessListener {
                            println("Successful deletion of book document from database. " +
                                    "\nbookId: $bookID")
                        }
                        .addOnFailureListener { exception ->
                            println("Error in deleting book document: $exception " +
                                    "\nbookId: $bookID")
                        }
                }
            }
            .addOnFailureListener { exception ->
                println("Error in searching for book document: $exception")
            }

        // Performs an update to delete the book from the user's book listings field
        userDocRef.update("book_listings", FieldValue.arrayRemove(bookID))
            .addOnSuccessListener {
                println("Successfully deleted ${this.displayName}'s book from the book_listings. " +
                        "\nuser_id: ${this.userId}; \nbook_id: $bookID")
            }
            .addOnFailureListener { exception ->
                println("Error in updating the user document: $exception " +
                        "\nuser_id: ${this.userId}")
            }
    }//end of deleteListing function

}