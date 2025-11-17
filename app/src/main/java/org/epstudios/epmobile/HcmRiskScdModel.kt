package org.epstudios.epmobile

import kotlin.math.exp
import kotlin.math.pow

/**
 * Copyright (C) 2024 EP Studios, Inc.
 * www.epstudiossoftware.com
 *
 *
 * Created by mannd on 11/19/24.
 *
 *
 * This file is part of epmobile.
 *
 *
 * epmobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * epmobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License
 * along with epmobile.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
internal class AgeOutOfRangeException(val age: Int) : Exception()

internal class LvWallThicknessOutOfRangeException(val lvWallThickness: Int) : Exception()

internal class LvotGradientOutOfRangeException(val lvotGradient: Int) : Exception()

internal class ParsingException : Exception {
    constructor(message: String?) : super(message)
    constructor()
}

internal class LaSizeOutOfRangeException(val laSize: Int) : Exception()

// Define the restricted set of possible validation errors
sealed interface HcmValidationError {
    data class AgeOutOfRange(val age: Int) : HcmValidationError
    data class LvWallThicknessOutOfRange(val thickness: Int) : HcmValidationError
    data class LvotGradientOutOfRange(val gradient: Int) : HcmValidationError
    data class LaSizeOutOfRange(val size: Int) : HcmValidationError
    object ParsingError : HcmValidationError
}

sealed interface CalculationResult {
    data class Success(val value: Double) : CalculationResult
    data class Failure(val error: HcmValidationError) : CalculationResult
}

class HcmRiskScdModel {
    constructor(
        ageString: String, maxLvWallThicknessString: String, maxLvotGradientString: String,
        laSizeString: String, hasFamilyHxScd: Boolean, hasNsvt: Boolean, hasSyncope: Boolean
    ) {
        this.ageString = ageString
        this.maxLvWallThicknessString = maxLvWallThicknessString
        this.maxLvotGradientString = maxLvotGradientString
        this.laSizeString = laSizeString
        this.hasFamilyHxScd = hasFamilyHxScd
        this.hasNsvt = hasNsvt
        this.hasSyncope = hasSyncope
    }

    internal constructor()

    private var ageString: String? = null
    private var maxLvWallThicknessString: String? = null
    private var maxLvotGradientString: String? = null
    private var laSizeString: String? = null
    private var hasFamilyHxScd = false
    private var hasNsvt = false
    private var hasSyncope = false

    fun calculateResult(): CalculationResult {
        val age = ageString?.toIntOrNull()
        if (age == null) {
            return CalculationResult.Failure(HcmValidationError.ParsingError)
        }
        if (age > 115 || age < 16) {
            return CalculationResult.Failure(HcmValidationError.AgeOutOfRange(age))
        }
        val maxLvWallThickness = maxLvWallThicknessString?.toIntOrNull()
        if (maxLvWallThickness == null) {
            return CalculationResult.Failure(HcmValidationError.ParsingError)
        }
        if (maxLvWallThickness < 10 || maxLvWallThickness > 35) {
            return CalculationResult.Failure(
                HcmValidationError.LvWallThicknessOutOfRange(
                    maxLvWallThickness
                )
            )
        }
        val maxLvotGradient = maxLvotGradientString?.toIntOrNull()
        if (maxLvotGradient == null) {
            return CalculationResult.Failure(HcmValidationError.ParsingError)
        }
        if (maxLvotGradient < 2 || maxLvotGradient > 154) {
            return CalculationResult.Failure(
                HcmValidationError.LvotGradientOutOfRange(
                    maxLvotGradient
                )
            )
        }
        val laSize = laSizeString?.toIntOrNull()
        if (laSize == null) {
            return CalculationResult.Failure(HcmValidationError.ParsingError)
        }
        if (laSize < 28 || laSize > 67) {
            return CalculationResult.Failure(HcmValidationError.LaSizeOutOfRange(laSize))
        }
        return internalCalculateResult(
            maxLvWallThickness,
            laSize,
            maxLvotGradient,
            age,
            hasFamilyHxScd,
            hasNsvt,
            hasSyncope
        )
    }
}

private fun internalCalculateResult(
    maxLvWallThickness: Int,
    laDiameter: Int,
    maxLvotGradient: Int,
    age: Int,
    hasFamilyHxScd: Boolean,
    hasNsvt: Boolean,
    hasSyncope: Boolean
): CalculationResult {
    val coefficient = 0.998
    val prognosticIndex = (((0.15939858 * maxLvWallThickness
            - 0.00294271 * maxLvWallThickness * maxLvWallThickness
            ) + 0.0259082 * laDiameter + 0.00446131 * maxLvotGradient + (if (hasFamilyHxScd) 0.4583082 else 0.0)
            + (if (hasNsvt) 0.82639195 else 0.0)
            + (if (hasSyncope) 0.71650361 else 0.0))
            - 0.01799934 * age)
    val scdProb = 1 - coefficient.pow(exp(prognosticIndex))
    return CalculationResult.Success(scdProb)
}