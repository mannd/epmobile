package org.epstudios.epmobile.features.riskscores.data

/**
Copyright (C) 2025 EP Studios, Inc.
www.epstudiossoftware.com

Created by mannd on 11/17/25.

This file is part of epmobile.

epmobile is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

epmobile is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with epmobile.  If not, see <http://www.gnu.org/licenses/>.
 */

// Define the restricted set of possible validation errors
sealed interface HcmAfValidationError {
    data class LaDiameterOutOfRange(val diameter: Int) : HcmAfValidationError
    data class AgeAtEvalOutOfRange(val age: Int) : HcmAfValidationError
    data class AgeAtDxOutOfRange(val age: Int) : HcmAfValidationError
    object ParsingError : HcmAfValidationError
}

sealed interface HcmAfCalculationResult {
    data class Success(val points: Int) : HcmAfCalculationResult
    data class Failure(val error: HcmAfValidationError) : HcmAfCalculationResult
}

class HcmAfModel(
    private val laDiameter: Int?,
    private val ageAtEval: Int?,
    private val ageAtDx: Int?,
    private val hfSx: Boolean?
) {
    // Enum to represent the risk categories in a type-safe way
    enum class HcmAfRiskCategory(val displayName: String) {
        LOW("Low risk (<1.0%/y)"),
        INTERMEDIATE("Intermediate risk (1.0%/y-2.0%/y)"),
        HIGH("High risk (>2.0%/y)")
    }

    // Data class to hold the lookup results for a given score
    data class HcmAfRiskData(
        val score: Int,
        val riskCategory: HcmAfRiskCategory,
        val riskAt2YearsPercent: Double,
        val riskAt5YearsPercent: Double
    )

    // Public function to get the risk data based on the score
    fun getRiskData(score: Int): HcmAfRiskData? {
        return riskDataLookupMap[score]
    }

    companion object {
        // This map is our lookup table. It's private and initialized only once.
        private val riskDataLookupMap: Map<Int, HcmAfRiskData> = mapOf(
            // Low Risk
            8 to HcmAfRiskData(8, HcmAfRiskCategory.LOW, 0.4, 1.0),
            9 to HcmAfRiskData(9, HcmAfRiskCategory.LOW, 0.5, 1.2),
            10 to HcmAfRiskData(10, HcmAfRiskCategory.LOW, 0.6, 1.4),
            11 to HcmAfRiskData(11, HcmAfRiskCategory.LOW, 0.7, 1.7),
            12 to HcmAfRiskData(12, HcmAfRiskCategory.LOW, 0.9, 2.1),
            13 to HcmAfRiskData(13, HcmAfRiskCategory.LOW, 1.0, 2.5),
            14 to HcmAfRiskData(14, HcmAfRiskCategory.LOW, 1.3, 3.0),
            15 to HcmAfRiskData(15, HcmAfRiskCategory.LOW, 1.5, 3.6),
            16 to HcmAfRiskData(16, HcmAfRiskCategory.LOW, 1.8, 4.3),
            17 to HcmAfRiskData(17, HcmAfRiskCategory.LOW, 2.2, 5.2),

            // Intermediate Risk
            18 to HcmAfRiskData(18, HcmAfRiskCategory.INTERMEDIATE, 2.6, 6.2),
            19 to HcmAfRiskData(19, HcmAfRiskCategory.INTERMEDIATE, 3.1, 7.4),
            20 to HcmAfRiskData(20, HcmAfRiskCategory.INTERMEDIATE, 3.8, 8.9),
            21 to HcmAfRiskData(21, HcmAfRiskCategory.INTERMEDIATE, 4.5, 10.6),

            // High Risk
            22 to HcmAfRiskData(22, HcmAfRiskCategory.HIGH, 5.4, 12.6),
            23 to HcmAfRiskData(23, HcmAfRiskCategory.HIGH, 6.5, 15.0),
            24 to HcmAfRiskData(24, HcmAfRiskCategory.HIGH, 7.8, 17.8),
            25 to HcmAfRiskData(25, HcmAfRiskCategory.HIGH, 9.3, 21.1),
            26 to HcmAfRiskData(26, HcmAfRiskCategory.HIGH, 11.1, 24.8),
            27 to HcmAfRiskData(27, HcmAfRiskCategory.HIGH, 13.3, 29.1),
            28 to HcmAfRiskData(28, HcmAfRiskCategory.HIGH, 15.7, 33.9),
            29 to HcmAfRiskData(29, HcmAfRiskCategory.HIGH, 18.7, 39.3),
            30 to HcmAfRiskData(30, HcmAfRiskCategory.HIGH, 22.0, 45.2),
            31 to HcmAfRiskData(31, HcmAfRiskCategory.HIGH, 25.9, 51.5)
        )
    }

    private fun getPointsLaDiameter(diameter: Int): Int {
        val diff = diameter - 24
        val multiplier = diff / 6
        return multiplier * 2 + 8
    }

    private fun getPointsAgeAtEval(age: Int): Int {
        val diff = age - 10
        val multiplier = diff / 10
        return multiplier * 3 + 8
    }

    private fun getPointsAgeAtDx(age: Int): Int {
        val multiplier = age / 10
        return multiplier * -2
    }

    private fun getPointsHfSx(hasSx: Boolean): Int {
        return if (hasSx) 3 else 0
    }

    fun getPoints(): HcmAfCalculationResult {
        if (laDiameter == null || ageAtEval == null || ageAtDx == null || hfSx == null) {
            return HcmAfCalculationResult.Failure(HcmAfValidationError.ParsingError)
        }
        if (laDiameter !in 24..65) {
            return HcmAfCalculationResult.Failure(HcmAfValidationError.LaDiameterOutOfRange(laDiameter))
        }
        if (ageAtEval !in 10..79) {
            return HcmAfCalculationResult.Failure(HcmAfValidationError.AgeAtEvalOutOfRange(ageAtEval))
        }
        if (ageAtDx !in 0..79) {
            return HcmAfCalculationResult.Failure(HcmAfValidationError.AgeAtDxOutOfRange(ageAtDx))
        }

        val laDiameterPoints = getPointsLaDiameter(laDiameter)
        val ageAtEvalPoints = getPointsAgeAtEval(ageAtEval)
        val ageAtDxPoints = getPointsAgeAtDx(ageAtDx)
        val hfSxPoints = getPointsHfSx(hfSx)
        val points = laDiameterPoints + ageAtEvalPoints + ageAtDxPoints + hfSxPoints

        return HcmAfCalculationResult.Success(points)
    }
}