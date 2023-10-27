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

import com.google.firebase.firestore.DocumentSnapshot

/**
 * A model for the User
 *
 * @param id String? the firebase ID of the user
 * @param userId String the ID of the user
 * @param displayName String the display name of the user
 * @param firstName String the first name of the user
 * @param lastName String the last name of the user
 * @param email String the email of the user
 * @param bookListings List<String> the list of book listings of the user
 * @param savedBooks List<String> the list of saved books of the user
 */
data class MUser(
    val id: String?,
    val userId: String,
    val displayName: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val bookListings: List<String>,
    val savedBooks: List<String>,
) {

    companion object {
        /**
         * Creates a MUser object from a DocumentSnapshot
         *
         * @param document DocumentSnapshot the document to create the MUser from
         *
         * @return MUser the MUser created from the DocumentSnapshot
         *
         * @see DocumentSnapshot
         * @see MUser
         */
        fun fromDocument(document: DocumentSnapshot): MUser {
            return MUser(
                id = document.id,
                userId = document.getString("user_id") ?: "",
                displayName = document.getString("display_name") ?: "",
                firstName = document.getString("first_name") ?: "",
                lastName = document.getString("last_name") ?: "",
                email = document.getString("email") ?: "",
                bookListings = document.get("book_listings") as List<String>,
                savedBooks = document.get("saved_books") as List<String>,
            )
        }
    }

    /**
     * Returns a map of the user
     *
     * @return MutableMap<String, Any> the map of the user
     *
     * @see MutableMap
     * @see MUser
     */
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "user_id" to this.userId,
            "display_name" to this.displayName,
            "first_name" to this.firstName,
            "last_name" to this.lastName,
            "email" to this.email,
            "book_listings" to this.bookListings,
            "saved_books" to this.savedBooks
        )
    }
}