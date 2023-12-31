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

/**
 * This class represents the condition of a book.
 *
 * @property condition the condition of the book
 * @property description the description of the book condition
 */
data class MCondition(
    private val condition: String,
    private val description: String,
) {
    companion object {
        val conditions = listOf(
            MCondition(
                condition = "As New",
                description = "Book looks new and has no defects. May show remainder marks."),
            MCondition(
                condition = "Fine",
                description = "Book may show slight wear at edges of book or dust jackets."),
            MCondition(
                condition = "Very Good",
                description = "Book has clear signs of wear. May have minor defects including remainder marks, owner inscription, or clipped/chipped dust jacket."),
            MCondition(
                condition = "Good",
                description = "Book may have a greater degree of defects, including highlighting, library markings, or loose bindings."
            ),
            MCondition(
                condition = "Fair",
                description = "Book may be very worn, soiled, torn, or barely holding together."
            ),
            MCondition(
                condition = "Poor",
                description = "Book may have extensive damage. Parts may be missing."
            )
        )
    }

    /**
     * Returns the condition of the book.
     *
     * @return the condition of the book
     */
    fun returnCondition(): String {
        return condition
    }

    /**
     * Returns the description of the book condition.
     *
     * @return the description of the book condition
     */
    fun returnDescription(): String {
        return description
    }
}
