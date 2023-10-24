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
 * This class represents a category of a book.
 *
 * @property categoryName the name of the category
 */
data class MCategory(
    private val categoryName: String
) {
    /**
     * Returns the name of the category.
     *
     * @return the name of the category
     */
    override fun toString(): String {
        return categoryName
    }

    companion object {
        val BusinessAndEconomics = MCategory("Business and Economics")
        val CommunicationAndMedia = MCategory("Communication and Media")
        val ComputingAndEngineering = MCategory("Computing and Engineering")
        val Education = MCategory("Education")
        val HealthSciences = MCategory("Health Sciences")
        val HumanitiesAndSocialSciences = MCategory("Humanities and Social Sciences")
        val LanguageAndLiterature = MCategory("Language and Literature")
        val MathematicsAndStatistics = MCategory("Mathematics and Statistics")
        val NaturalAndPhysicalSciences = MCategory("Natural and Physical Sciences")
        val PerformingAndVisualArts = MCategory("Performing and Visual Arts")
        val ReligionAndPhilosophy = MCategory("Religion and Philosophy")
        val Other = MCategory("Other")

        val categories = listOf(
            BusinessAndEconomics,
            CommunicationAndMedia,
            ComputingAndEngineering,
            Education,
            HealthSciences,
            HumanitiesAndSocialSciences,
            LanguageAndLiterature,
            MathematicsAndStatistics,
            NaturalAndPhysicalSciences,
            PerformingAndVisualArts,
            ReligionAndPhilosophy,
            Other
        )
    }
}

