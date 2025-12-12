package org.epstudios.epmobile.features.calculators.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.preference.PreferenceManager
import org.epstudios.epmobile.R
import org.epstudios.epmobile.core.data.UnitConverter
import org.epstudios.epmobile.core.ui.base.EpActivity
import org.epstudios.epmobile.features.calculators.data.BMI

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

class BmiCalculator : EpActivity() {

    private var weightEditText: EditText? = null
    private var heightEditText: EditText? = null
    private var messageTextView: TextView? = null
    private var calculatedResult: TextView? = null
    private var weightUnitSpinner: AutoCompleteTextView? = null
    private var heightUnitSpinner: AutoCompleteTextView? = null

    private enum class WeightUnit(val arrayIndex: Int) {
        KG(0),
        LB(1)
    }

    private enum class HeightUnit(val arrayIndex: Int) {
        CM(0),
        IN(1)
    }

    private var defaultWeightUnitSelection = WeightUnit.KG
    private var defaultHeightUnitSelection = HeightUnit.CM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bmi)
        setupInsets(R.id.bmi_root_view)
        initToolbar()

        val calculateButton = findViewById<View?>(R.id.calculate_button)
        calculateButton?.setOnClickListener {
            calculate()
        }
        val clearButton = findViewById<View?>(R.id.clear_button)
        clearButton?.setOnClickListener {
            clearEntries()
        }

        weightEditText = findViewById(R.id.weightEditText)
        heightEditText = findViewById(R.id.heightEditText)
        messageTextView = findViewById(R.id.messageTextView)
        calculatedResult = findViewById(R.id.calculated_result)

        weightUnitSpinner = findViewById(R.id.weightUnitSpinner)
        heightUnitSpinner = findViewById(R.id.heightUnitSpinner)

        getPrefs()
        setAdapters()
        clearEntries()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val parentActivityIntent = Intent(this, CalculatorList::class.java)
            parentActivityIntent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        or Intent.FLAG_ACTIVITY_NEW_TASK
            )
            startActivity(parentActivityIntent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setAdapters() {
        // Weight Spinner
        val weightUnits = getResources().getStringArray(R.array.weight_unit_labels)
        val weightUnitAdapter = ArrayAdapter<String?>(
            this, R.layout.dropdown_menu_item, weightUnits
        )
        weightUnitSpinner?.setAdapter(weightUnitAdapter)

        if (defaultWeightUnitSelection == WeightUnit.KG) {
            weightUnitSpinner?.setText(weightUnits[WeightUnit.KG.arrayIndex], false)
        } else {
            weightUnitSpinner?.setText(weightUnits[WeightUnit.LB.arrayIndex], false)
        }

        // Height spinner
        val heightUnits = getResources().getStringArray(R.array.height_unit_labels)
        val heightUnitAdapter = ArrayAdapter<String?>(
            this, R.layout.dropdown_menu_item, heightUnits
        )
        heightUnitSpinner?.setAdapter(heightUnitAdapter)

        if (defaultHeightUnitSelection == HeightUnit.CM) {
            heightUnitSpinner?.setText(heightUnits[HeightUnit.CM.arrayIndex], false)
        } else {
            heightUnitSpinner?.setText(heightUnits[HeightUnit.IN.arrayIndex], false)
        }
    }

    private val weightUnitSelection: WeightUnit
        get() {
            val selectedUnit = weightUnitSpinner?.text.toString()
            val kgUnit =
                getResources().getStringArray(R.array.weight_unit_labels)[WeightUnit.KG.arrayIndex]
            return if (selectedUnit == kgUnit) {
                WeightUnit.KG
            } else {
                WeightUnit.LB
            }
        }

    private val heightUnitSelection: HeightUnit
        get() {
            val selectedUnit = heightUnitSpinner?.text.toString()
            val cmUnit =
                getResources().getStringArray(R.array.height_unit_labels)[HeightUnit.CM.arrayIndex]
            return if (selectedUnit == cmUnit) {
                HeightUnit.CM
            } else {
                HeightUnit.IN
            }
        }

    private fun calculate() {
        // clear any message
        messageTextView?.text == null
        // make sure message white with 2 calculations in row, 1st invalid
        resetResultTextColor()
        val weightText: CharSequence = weightEditText?.text ?: ""
        val heightText: CharSequence = heightEditText?.text ?: ""
        try {
            var weight = weightText.toString().toDouble()
            if (weightUnitSelection == WeightUnit.LB) {
                weight = UnitConverter.lbsToKgs(weight)
            }
            var height = heightText.toString().toDouble()
            if (heightUnitSelection == HeightUnit.IN) {
                height = UnitConverter.insToCms(height)
            }
            val result = BMI.calculateCmRounded(weight, height)
            calculatedResult?.text = getString(R.string.bmi_result, result.toString())
            val message = getMessage(result)
            messageTextView?.text = message
            if (!BMI.isNormalBmi(result)) {
                calculatedResult?.setTextAppearance(R.style.TextAppearance_Calculator_Error)
            }
        } catch (_: NumberFormatException) {
            calculatedResult?.text = getString(R.string.invalid_warning)
            calculatedResult?.setTextAppearance(R.style.TextAppearance_Calculator_Error)
            messageTextView?.text = null
        }
    }

    fun getMessage(bmi: Double): String {
        val classification = BMI.getClassification(bmi)
        return when (classification) {
            BMI.Classification.UNDERWEIGHT_SEVERE -> getString(R.string.underweight_severe_label)
            BMI.Classification.UNDERWEIGHT_MODERATE -> getString(R.string.underweight_moderate_label)
            BMI.Classification.UNDERWEIGHT_MILD -> getString(R.string.underweight_mild_label)
            BMI.Classification.NORMAL -> getString(R.string.normal_label)
            BMI.Classification.OVERWEIGHT_PREOBESE -> getString(R.string.overweight_preobese_label)
            BMI.Classification.OVERWEIGHT_CLASS_1 -> getString(R.string.overweight_class_1_label)
            BMI.Classification.OVERWEIGHT_CLASS_2 -> getString(R.string.overweight_class_2_label)
            BMI.Classification.OVERWEIGHT_CLASS_3 -> getString(R.string.overweight_class_3_label)
        }
    }

    private fun clearEntries() {
        weightEditText?.text = null
        heightEditText?.text = null
        messageTextView?.text = null
        calculatedResult?.text = null
        weightEditText?.requestFocus()
        resetResultTextColor()
    }

    private fun resetResultTextColor() {
        calculatedResult?.setTextAppearance(R.style.TextAppearance_Calculator_Result)
    }

    private fun getPrefs() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        prefs.getString("default_weight_unit", "KG")?.let {
            weightUnitPref ->
            defaultWeightUnitSelection = if (weightUnitPref == "KG") {
                WeightUnit.KG
            } else {
                WeightUnit.LB
            }
        }

        prefs.getString("default_height_unit", "CM")?.let {
            heightUnitPref ->
            defaultHeightUnitSelection = if (heightUnitPref == "CM") {
                HeightUnit.CM
            } else {
                HeightUnit.IN
            }
        }
    }

    override fun hideInstructionsMenuItem(): Boolean {
        return false
    }

    override fun showActivityInstructions() {
        showAlertDialog(
            R.string.bmi_calculator_title,
            R.string.bmi_calculator_instructions
        )
    }

    override fun hideReferenceMenuItem(): Boolean {
        return false
    }

    override fun showActivityReference() {
        showReferenceAlertDialog(
            R.string.bmi_calculator_reference,
            R.string.bmi_calculator_link
        )
    }
}