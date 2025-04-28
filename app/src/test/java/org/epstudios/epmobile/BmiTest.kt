package org.epstudios.epmobile

import org.junit.Test
import kotlin.test.assertEquals

/**
Copyright (C) 2025 EP Studios, Inc.
www.epstudiossoftware.com

Created by mannd on 4/27/25.

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

class BmiTest {
    @Test
    fun testBmi() {
        assertEquals(20.1, BMI.calculate(65.0, 1.8), 0.05)
        assertEquals(20.1, BMI.calculateCm(65.0, 180.0), 0.05)
        assertEquals(20.1, BMI.calculateRounded(65.0, 1.8))
        assertEquals(20.1, BMI.calculateCmRounded(65.0, 180.0))
        assertEquals(20.3, BMI.calculateUSUnits(150.0, 72.0), 0.05)
        assertEquals(20.3, BMI.calculateUSUnitsRounded(150.0, 72.0))
    }

    @Test
    fun testClassification() {
        assertEquals(BMI.Classification.NORMAL, BMI.getClassification(20.1))
        assertEquals(BMI.Classification.OVERWEIGHT_PREOBESE, BMI.getClassification(25.0))
        assertEquals(BMI.Classification.OVERWEIGHT_CLASS_1, BMI.getClassification(30.0))
        assertEquals(BMI.Classification.OVERWEIGHT_CLASS_2, BMI.getClassification(35.0))
        assertEquals(BMI.Classification.OVERWEIGHT_CLASS_3, BMI.getClassification(40.0))
        assertEquals(BMI.Classification.UNDERWEIGHT_SEVERE, BMI.getClassification(15.9))
        assertEquals(BMI.Classification.UNDERWEIGHT_MODERATE, BMI.getClassification(16.9))
        assertEquals(BMI.Classification.UNDERWEIGHT_MILD, BMI.getClassification(18.4))
    }

    @Test
    fun testIsNormal() {
        assertEquals(true, BMI.isNormalBmi(20.1))
        assertEquals(true, BMI.isNormalBmi(18.5))
        assertEquals(true, BMI.isNormalBmi(24.9))
        assertEquals(false, BMI.isNormalBmi(25.0))
        assertEquals(false, BMI.isNormalBmi(30.0))
    }
}