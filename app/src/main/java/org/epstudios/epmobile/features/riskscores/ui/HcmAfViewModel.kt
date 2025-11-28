package org.epstudios.epmobile.features.riskscores.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.epstudios.epmobile.features.riskscores.data.HcmAfCalculationResult
import org.epstudios.epmobile.features.riskscores.data.HcmAfModel

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

    private val _uiState = MutableStateFlow(HcmAfUiState())
    val uiState: StateFlow<HcmAfUiState> = _uiState.asStateFlow()

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
        _uiState.value = HcmAfUiState()
    }

    public fun calculate() {
        // Translate Strings from UI state into Int?s for the Model
        val laDiameter = _laDiameterInput.value.toIntOrNull()
        val ageAtEval = _ageAtEvalInput.value.toIntOrNull()
        val ageAtDx = _ageAtDxInput.value.toIntOrNull()
        val hfSx = _hfSxChecked.value

        val model = HcmAfModel(laDiameter, ageAtEval, ageAtDx, hfSx)
        val calculationResult = model.getCalculationResult()

        val newState = when (calculationResult) {
            is HcmAfCalculationResult.Success -> {
                val points = calculationResult.points
                val riskData = model.getRiskData(points)
                HcmAfUiState(
                    riskData = riskData,
                    error = null
                )
            }
            is HcmAfCalculationResult.Failure -> {
                HcmAfUiState(
                    riskData = null,
                    error = calculationResult.error
                )
            }
        }
        _uiState.value = newState
    }
}