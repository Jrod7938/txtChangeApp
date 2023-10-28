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
 * Model class for a book
 *
 * @property bookID String the id of the book
 * @property userId String the id of the user who created the book
 * @property title String the title of the book
 * @property author String the author of the book
 * @property price Double the price of the book
 * @property email String the email of the user who created the book
 * @property condition String the condition of the book
 * @property category String the category of the book from Google API
 * @property mCategory MCategory the category of the book from the user
 * @property imageURL String the url of the image of the book
 * @property description String the description of the book
 * @property isbn String the isbn of the book
 * @property sellerConfirm Boolean if the seller has confirmed the transaction
 * @property buyerConfirm Boolean if the buyer has confirmed the transaction
 */
data class MBook(
    var bookID: String = "",
    var userId: String = "",
    var title: String = "",
    var author: String = "",
    val price: Double,
    var email: String = "",
    val condition: String,
    var category: String = "",
    var mCategory: String = MCategory.Other.toString(),
    var imageURL: String = "",
    var description: String = "",
    val isbn: String,
    var sellerConfirm: Boolean = false,
    var buyerConfirm: Boolean = false,
) {
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf<String, Any>(
            "book_id" to this.bookID,
            "user_id" to this.userId,
            "title" to this.title,
            "author" to this.author,
            "price" to this.price,
            "email" to this.email,
            "condition" to this.condition,
            "category" to this.category,
            "mcategory" to this.mCategory,
            "imageURL" to this.imageURL,
            "description" to this.description,
            "isbn" to this.isbn,
            "seller_confirm" to this.sellerConfirm,
            "buyer_confirm" to this.buyerConfirm
        )
    }

    companion object {
        /**
         * Creates a MBook object from a DocumentSnapshot
         *
         * @param document DocumentSnapshot the document to create the MBook from
         *
         * @return MBook the MBook created from the DocumentSnapshot
         *
         * @see DocumentSnapshot
         * @see MBook
         */
        fun fromDocument(document: DocumentSnapshot): MBook {
            return MBook(
                bookID = document.getString("book_id") ?: "",
                userId = document.getString("user_id") ?: "",
                title = document.getString("title") ?: "",
                author = document.getString("author") ?: "",
                price = document.getDouble("price") ?: 0.0,
                email = document.getString("email") ?: "",
                condition = document.getString("condition") ?: "",
                category = document.getString("category") ?: "",
                mCategory = document.getString("mcategory") ?: MCategory.Other.toString(),
                imageURL = document.getString("imageURL") ?: "",
                description = document.getString("description") ?: "",
                isbn = document.getString("isbn") ?: "",
                sellerConfirm = document.getBoolean("seller_confirm") ?: false,
                buyerConfirm = document.getBoolean("buyer_confirm") ?: false
            )
        }
    }
}