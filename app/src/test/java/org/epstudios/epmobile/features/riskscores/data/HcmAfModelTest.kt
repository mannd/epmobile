package org.epstudios.epmobile.features.riskscores.data

/**
Copyright (C) 2025 EP Studios, Inc.
www.epstudiossoftware.com

Created by mannd on 11/18/25.

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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HcmAfModelTest {

    //region getPoints() Tests

    @Test
    fun `getPoints() with valid inputs returns Success with correct score`() {
        // Let's manually calculate an expected score:
        // LA Diameter (40): (40-24)/6 = 2 -> 2*2+8 = 12 points
        // Age at Eval (50): (50-10)/10 = 4 -> 4*3+8 = 20 points
        // Age at Dx   (35): 35/10 = 3      -> 3*(-2) = -6 points
        // HF Sx       (true): 3 points
        // Total = 12 + 20 - 6 + 3 = 29 points

        val model = HcmAfModel(
            laDiameter = 40,
            ageAtEval = 50,
            ageAtDx = 35,
            hfSx = true
        )

        val result = model.getPoints()

        assertTrue("Result should be Success", result is HcmAfCalculationResult.Success)
        val points = (result as HcmAfCalculationResult.Success).points
        assertEquals(29, points)
    }

    @Test
    fun `getPoints() with heart failure symptoms false correctly adjusts score`() {
        // Using same values as above, but hfSx = false (0 points)
        // Total = 12 + 20 - 6 + 0 = 26 points
        val model = HcmAfModel(
            laDiameter = 40,
            ageAtEval = 50,
            ageAtDx = 35,
            hfSx = false
        )

        val result = model.getPoints()

        assertTrue("Result should be Success", result is HcmAfCalculationResult.Success)
        assertEquals(26, (result as HcmAfCalculationResult.Success).points)
    }

    @Test
    fun `getPoints() with null input returns ParsingError`() {
        val model = HcmAfModel(
            laDiameter = null, // Null input
            ageAtEval = 50,
            ageAtDx = 35,
            hfSx = false
        )

        val result = model.getPoints()

        assertTrue("Result should be Failure", result is HcmAfCalculationResult.Failure)
        val error = (result as HcmAfCalculationResult.Failure).error
        assertTrue("Error type should be ParsingError", error is HcmAfValidationError.ParsingError)
    }

    @Test
    fun `getPoints() with LA diameter below range returns LaDiameterOutOfRange error`() {
        val model = HcmAfModel(laDiameter = 23, ageAtEval = 50, ageAtDx = 35, hfSx = false)
        val result = model.getPoints()
        assertTrue(result is HcmAfCalculationResult.Failure)
        assertTrue((result as HcmAfCalculationResult.Failure).error is HcmAfValidationError.LaDiameterOutOfRange)
    }

    @Test
    fun `getPoints() with LA diameter above range returns LaDiameterOutOfRange error`() {
        val model = HcmAfModel(laDiameter = 66, ageAtEval = 50, ageAtDx = 35, hfSx = false)
        val result = model.getPoints()
        assertTrue(result is HcmAfCalculationResult.Failure)
        assertTrue((result as HcmAfCalculationResult.Failure).error is HcmAfValidationError.LaDiameterOutOfRange)
    }

    @Test
    fun `getPoints() with Age at Eval below range returns AgeAtEvalOutOfRange error`() {
        val model = HcmAfModel(laDiameter = 40, ageAtEval = 9, ageAtDx = 35, hfSx = false)
        val result = model.getPoints()
        assertTrue(result is HcmAfCalculationResult.Failure)
        assertTrue((result as HcmAfCalculationResult.Failure).error is HcmAfValidationError.AgeAtEvalOutOfRange)
    }

    @Test
    fun `getPoints() with Age at Eval above range returns AgeAtEvalOutOfRange error`() {
        val model = HcmAfModel(laDiameter = 40, ageAtEval = 80, ageAtDx = 35, hfSx = false)
        val result = model.getPoints()
        assertTrue(result is HcmAfCalculationResult.Failure)
        assertTrue((result as HcmAfCalculationResult.Failure).error is HcmAfValidationError.AgeAtEvalOutOfRange)
    }

    @Test
    fun `getPoints() with Age at Dx below range returns AgeAtDxOutOfRange error`() {
        val model = HcmAfModel(laDiameter = 40, ageAtEval = 50, ageAtDx = -1, hfSx = false)
        val result = model.getPoints()
        assertTrue(result is HcmAfCalculationResult.Failure)
        assertTrue((result as HcmAfCalculationResult.Failure).error is HcmAfValidationError.AgeAtDxOutOfRange)
    }

    @Test
    fun `getPoints() with Age at Dx above range returns AgeAtDxOutOfRange error`() {
        val model = HcmAfModel(laDiameter = 40, ageAtEval = 50, ageAtDx = 80, hfSx = false)
        val result = model.getPoints()
        assertTrue(result is HcmAfCalculationResult.Failure)
        assertTrue((result as HcmAfCalculationResult.Failure).error is HcmAfValidationError.AgeAtDxOutOfRange)
    }

    //endregion

    //region getRiskData() Tests

    @Test
    fun `getRiskData() for low risk score returns correct data`() {
        val model = HcmAfModel(
            null,
            null,
            null,
            null
        ) // Instance data doesn't matter for this static lookup
        val score = 10
        val riskData = model.getRiskData(score)

        assertNotNull(riskData)
        assertEquals(score, riskData!!.score)
        assertEquals(HcmAfModel.HcmAfRiskCategory.LOW, riskData.riskCategory)
        assertEquals(0.6, riskData.riskAt2YearsPercent, 0.001)
        assertEquals(1.4, riskData.riskAt5YearsPercent, 0.001)
    }

    @Test
    fun `getRiskData() for intermediate risk score returns correct data`() {
        val model = HcmAfModel(null, null, null, null)
        val score = 20
        val riskData = model.getRiskData(score)

        assertNotNull(riskData)
        assertEquals(score, riskData!!.score)
        assertEquals(HcmAfModel.HcmAfRiskCategory.INTERMEDIATE, riskData.riskCategory)
        assertEquals(3.8, riskData.riskAt2YearsPercent, 0.001)
        assertEquals(8.9, riskData.riskAt5YearsPercent, 0.001)
    }

    @Test
    fun `getRiskData() for high risk score returns correct data`() {
        val model = HcmAfModel(null, null, null, null)
        val score = 30
        val riskData = model.getRiskData(score)

        assertNotNull(riskData)
        assertEquals(score, riskData!!.score)
        assertEquals(HcmAfModel.HcmAfRiskCategory.HIGH, riskData.riskCategory)
        assertEquals(22.0, riskData.riskAt2YearsPercent, 0.001)
        assertEquals(45.2, riskData.riskAt5YearsPercent, 0.001)
    }

    @Test
    fun `getRiskData() for score below valid range returns null`() {
        val model = HcmAfModel(null, null, null, null)
        val riskData = model.getRiskData(7) // Below the minimum score of 8
        assertNull(riskData)
    }

    @Test
    fun `getRiskData() for score above valid range returns null`() {
        val model = HcmAfModel(null, null, null, null)
        val riskData = model.getRiskData(32) // Above the maximum score of 31
        assertNull(riskData)
    }

    //endregion
}