package org.epstudios.epmobile.features.riskscores.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.epstudios.epmobile.features.riskscores.data.HcmAfCalculationResult
import org.epstudios.epmobile.features.riskscores.data.HcmAfModel
import org.epstudios.epmobile.features.riskscores.data.HcmAfValidationError

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

class HcmAfViewModel : ViewModel() {

    // 1. STATE HOLDING:
    // Private, mutable state that can only be changed within the ViewModel.
    // Exposed publicly as immutable StateFlow for the UI to observe.

    private val _laDiameterInput = MutableStateFlow("")
    val laDiameterInput: StateFlow<String> = _laDiameterInput.asStateFlow()

    private val _ageAtEvalInput = MutableStateFlow("")
    val ageAtEvalInput: StateFlow<String> = _ageAtEvalInput.asStateFlow()

    private val _ageAtDxInput = MutableStateFlow("")
    val ageAtDxInput: StateFlow<String> = _ageAtDxInput.asStateFlow()

    private val _hfSxChecked = MutableStateFlow(false)
    val hfSxChecked: StateFlow<Boolean> = _hfSxChecked.asStateFlow()

    private val _resultState = MutableStateFlow("Enter values to see result.")
    val resultState: StateFlow<String> = _resultState.asStateFlow()

    // 2. EVENT HANDLING:
    // Public functions that the UI calls to notify the ViewModel of user actions.

    fun onLaDiameterChanged(newText: String) {
        _laDiameterInput.value = newText
    }

    fun onAgeAtEvalChanged(newText: String) {
        _ageAtEvalInput.value = newText
    }

    fun onAgeAtDxChanged(newText: String) {
        _ageAtDxInput.value = newText
    }

    fun onHfSxChanged(isChecked: Boolean) {
        _hfSxChecked.value = isChecked
    }

    fun clear() {
        _laDiameterInput.value = ""
        _ageAtEvalInput.value = ""
        _ageAtDxInput.value = ""
        _hfSxChecked.value = false
        _resultState.value = "Enter values to see result."
    }


    // 3. PROCESSING & TRANSLATION:
    // calculate() is activated by the calculate button in the UI.
    public fun calculate() {
        // Translate Strings from UI state into Int?s for the Model
        val laDiameter = _laDiameterInput.value.toIntOrNull()
        val ageAtEval = _ageAtEvalInput.value.toIntOrNull()
        val ageAtDx = _ageAtDxInput.value.toIntOrNull()
        val hfSx = _hfSxChecked.value

        val model = HcmAfModel(laDiameter, ageAtEval, ageAtDx, hfSx)
        val pointsResult = model.getPoints()

        // Translate the complex Result object from the Model into a simple String for the View
        val message = when (pointsResult) {
            is HcmAfCalculationResult.Success -> {
                val points = pointsResult.points
                val riskData = model.getRiskData(points)
                if (riskData != null) {
                    // Successful calculation and lookup
                    "HCM-AF Score: $points\n" +
                            "${riskData.riskCategory.displayName}\n" +
                            "2-Year AF Risk: ${riskData.riskAt2YearsPercent}%\n" +
                            "5-Year AF Risk: ${riskData.riskAt5YearsPercent}%"
                } else {
                    // Valid calculation but score is out of lookup range
                    "Score ($points) is out of valid range (8-31)."
                }
            }

            is HcmAfCalculationResult.Failure -> {
                // Translate specific errors into user-friendly messages.
                // In a real app, these would come from string resources (R.string.*)
                when (pointsResult.error) {
                    is HcmAfValidationError.LaDiameterOutOfRange ->
                        "Error: LA Diameter must be between 24 and 65 mm."

                    is HcmAfValidationError.AgeAtEvalOutOfRange ->
                        "Error: Age at Evaluation must be between 10 and 79."

                    is HcmAfValidationError.AgeAtDxOutOfRange ->
                        "Error: Age at Diagnosis must be between 0 and 79."

                    is HcmAfValidationError.ParsingError ->
                        "Please enter all values."
                }
            }
        }
        _resultState.value = message
    }
}