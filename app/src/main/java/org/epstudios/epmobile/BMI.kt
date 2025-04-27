package org.epstudios.epmobile

import kotlin.math.round

/**
Copyright (C) 2025 EP Studios, Inc.
www.epstudiossoftware.com

Created by mannd on 4/24/25.

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

class BMI {
    /**
     * Calculates BMI using metric units.  No rounding.
     * @param weight Weight in kilograms
     * @param height Height in meters
     */
    fun calculate(weight: Double, height: Double): Double {
        return weight / (height * height)
    }

    /**
     * Calculates BMI using metric units, using centimeters.
     * No rounding.
     * @param weight Weight in kilograms
     * @param height Height in centimeters
     */
    fun calculateCm(weight: Double, height: Double): Double {
        val heightInMeters = height / 100
        return calculate(weight, heightInMeters)
    }

    /**
     * Calculates BMI using US units.  No rounding.
     * @param weight Weight in pounds
     * @param height Height in inches
     */
    fun calculateUSUnits(weight: Double, height: Double): Double {
        return weight / (height * height) * 703
    }

    /**
     * Calculates BMI using metric units.  Rounded to nearest tenth.
     * @param weight Weight in kilograms
     * @param height Height in meters
     */
    fun calculateRounded(weight: Double, height: Double): Double {
        return roundToNearestTenth(calculate(weight, height))
    }

    /**
     * Calculates BMI using metric units, using centimeters.
     * Rounded to nearest tenth.
     * @param weight Weight in kilograms
     * @param height Height in centimeters
     */
    fun calculateCmRounded(weight: Double, height: Double): Double {
        return roundToNearestTenth(calculateCm(weight, height))
    }

    /**
     * Calculates BMI using US units.  Rounded to nearest tenth.
     * @param weight Weight in pounds
     * @param height Height in inches
     */
    fun calculateUSUnitsRounded(weight: Double, height: Double): Double {
        return roundToNearestTenth(calculateUSUnits(weight, height))
    }

    private fun roundToNearestTenth(value: Double): Double {
        val shifted = value * 10
        val roundedShifted = round(shifted)
        return roundedShifted / 10
    }
}