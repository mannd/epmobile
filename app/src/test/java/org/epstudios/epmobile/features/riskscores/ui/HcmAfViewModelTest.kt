// In: app/src/test/java/org/epstudios/epmobile/features/riskscores/ui/HcmAfViewModelTest.kt
package org.epstudios.epmobile.features.riskscores.ui

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

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.epstudios.epmobile.features.riskscores.data.HcmAfModel
import org.epstudios.epmobile.features.riskscores.data.HcmAfValidationError
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class HcmAfViewModelTest {

    private lateinit var viewModel: HcmAfViewModel
    private val testDispatcher = StandardTestDispatcher()

    // This setup is crucial for testing coroutines and ViewModels
    @Before
    fun setUp() {
        // Replaces the main dispatcher with a test dispatcher for predictable execution
        Dispatchers.setMain(testDispatcher)
        viewModel = HcmAfViewModel()
    }

    @After
    fun tearDown() {
        // Cleans up and resets the main dispatcher
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        assertEquals("Enter values to see result.", viewModel.resultState.value)
    }

    @Test
    fun `initial uiState is correct`() = runTest {
        assertEquals(HcmAfUiState(), viewModel.uiState.value)
    }

    @Test
    fun `onLaDiameterChanged2() with valid input triggers correct success state`() = runTest {
        // Use Turbine's `test` extension on the StateFlow to observe emissions
        viewModel.uiState.test {
            // The initial state is the first item emitted
            assertEquals(HcmAfUiState(), awaitItem())

            // Act: Change the inputs to valid values that result in a known score (29)
            viewModel.onLaDiameterChanged("40")
            viewModel.onAgeAtEvalChanged("50")
            viewModel.onAgeAtDxChanged("35")
            viewModel.onHfSxChanged(true)

            viewModel.calculate2()

            // Assert: Check the final, formatted string that the user would see
            val successState = awaitItem()
            val riskData = successState.riskData
            assertNotNull(riskData, "Risk data should not be null")
            assertEquals("The score should be correct", 29, riskData.score)
            assertEquals("The risk category should be HIGH", HcmAfModel.HcmAfRiskCategory.HIGH, riskData.riskCategory)
            assertEquals("The 2-year risk should be correct", 18.7, riskData.riskAt2YearsPercent, 0.001)
            assertEquals("The 5-year risk should be correct", 39.3, riskData.riskAt5YearsPercent, 0.001)
        }
    }

    @Test
    fun `onLaDiameterChanged2() with out-of-range input triggers correct error state`() = runTest {
        viewModel.uiState.test {
            awaitItem() // Skip initial state

            viewModel.onAgeAtEvalChanged("50")
            viewModel.onAgeAtDxChanged("35")
            viewModel.onHfSxChanged(true)
            // Act: Change one input to be out of range
            viewModel.onLaDiameterChanged("99") // This is out of range
            viewModel.calculate2()

            // Assert: Check for the specific, user-friendly error message
            val errorState = awaitItem()
            val error = errorState.error
            assertNotNull(error, "Error should not be null")
            assertTrue("Error: LA diameter should be out of range", error is HcmAfValidationError.LaDiameterOutOfRange)
        }
    }

    @Test
    fun `onAgeAtEvalChanged2() with non-numeric input triggers parsing error state`() = runTest {
        viewModel.uiState.test {
            awaitItem() // Skip initial state

            // Act: Set one input to something that isn't a number
            viewModel.onAgeAtEvalChanged("abc")
            viewModel.calculate2()

            // Assert: Check for the parsing error message
            val errorState = awaitItem()
            val error = errorState.error
            assertNotNull(error, "Error should not be null")
            assertTrue("Error: Parsing error", error is HcmAfValidationError.ParsingError)
//            assertEquals("Please enter all values.", errorState)
        }
    }

    @Test
    fun `onLaDiameterChanged() with valid input triggers correct success state`() = runTest {
        // Use Turbine's `test` extension on the StateFlow to observe emissions
        viewModel.resultState.test {
            // The initial state is the first item emitted
            assertEquals("Enter values to see result.", awaitItem())

            // Act: Change the inputs to valid values that result in a known score (29)
            viewModel.onLaDiameterChanged("40")
            viewModel.onAgeAtEvalChanged("50")
            viewModel.onAgeAtDxChanged("35")
            viewModel.onHfSxChanged(true)

            viewModel.calculate()

            // Assert: Check the final, formatted string that the user would see
            val successState = awaitItem()
            assertTrue("Should contain the score", successState.contains("HCM-AF Score: 29"))
            assertTrue("Should contain the risk category", successState.contains("High risk (>2.0%/y)"))
            assertTrue("Should contain the 5-year risk", successState.contains("5-Year AF Risk: 39.3%"))
        }
    }

    @Test
    fun `onLaDiameterChanged() with out-of-range input triggers correct error state`() = runTest {
        viewModel.resultState.test {
            awaitItem() // Skip initial state

            viewModel.onAgeAtEvalChanged("50")
            viewModel.onAgeAtDxChanged("35")
            viewModel.onHfSxChanged(true)
            // Act: Change one input to be out of range
            viewModel.onLaDiameterChanged("99") // This is out of range
            viewModel.calculate()

            // Assert: Check for the specific, user-friendly error message
            val errorState = awaitItem()
            assertEquals("Error: LA Diameter must be between 24 and 65 mm.", errorState)
        }
    }

    @Test
    fun `onAgeAtEvalChanged() with non-numeric input triggers parsing error state`() = runTest {
        viewModel.resultState.test {
            awaitItem() // Skip initial state

            // Act: Set one input to something that isn't a number
            viewModel.onAgeAtEvalChanged("abc")
            viewModel.calculate()

            // Assert: Check for the parsing error message
            val errorState = awaitItem()
            assertEquals("Please enter all values.", errorState)
        }
    }
}
