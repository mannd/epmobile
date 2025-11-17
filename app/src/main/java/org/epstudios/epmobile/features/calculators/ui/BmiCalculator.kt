package org.epstudios.epmobile.features.calculators.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.preference.PreferenceManager
import org.epstudios.epmobile.core.ui.base.EpActivity
import org.epstudios.epmobile.R
import org.epstudios.epmobile.core.data.UnitConverter
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
    private var weightSpinner: Spinner? = null
    private var heightSpinner: Spinner? = null
    private var messageTextView: TextView? = null
    private var calculatedResult: TextView? = null

    private enum class WeightUnit {
        KG, LB
    }

    private enum class HeightUnit {
        CM, IN
    }

    private val KG_SELECTION: Int = 0
    private val LB_SELECTION: Int = 1
    private val CM_SELECTION: Int = 0
    private val IN_SELECTION: Int = 1

    private var defaultWeightUnitSelection = WeightUnit.KG
    private var defaultHeightUnitSelection = HeightUnit.CM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bmi)
        setupInsets(R.id.bmi_root_view)
        initToolbar()

        val calculateButton = findViewById<View?>(R.id.calculate_button)
        calculateButton.setOnClickListener {
            calculate()
        }
        val clearButton = findViewById<View?>(R.id.clear_button)
        clearButton.setOnClickListener {
            clearEntries()
        }

        weightEditText = findViewById<EditText?>(R.id.weightEditText)
        heightEditText = findViewById<EditText?>(R.id.heightEditText)
        weightSpinner = findViewById<Spinner?>(R.id.weight_spinner)
        heightSpinner = findViewById<Spinner?>(R.id.height_spinner)
        messageTextView = findViewById<TextView?>(R.id.messageTextView)
        calculatedResult = findViewById<TextView?>(R.id.calculated_result)

        getPrefs()
        setAdapters()
        clearEntries()
    }

    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
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
        val adapter = ArrayAdapter.createFromResource(
            this, R.array.weight_unit_labels,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        weightSpinner!!.setAdapter(adapter)
        if (defaultWeightUnitSelection == WeightUnit.KG) weightSpinner!!.setSelection(KG_SELECTION)
        else weightSpinner!!.setSelection(LB_SELECTION)
        // do nothing
        val itemListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, v: View?,
                    position: Int, id: Long
                ) {
                    updateWeightUnitSelection()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // do nothing
                }
            }

        val heightAdapter = ArrayAdapter
            .createFromResource(
                this, R.array.height_unit_labels,
                android.R.layout.simple_spinner_item
            )
        heightAdapter
            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        heightSpinner!!.setAdapter(heightAdapter)
        if (defaultHeightUnitSelection == HeightUnit.CM) heightSpinner!!.setSelection(CM_SELECTION)
        else heightSpinner!!.setSelection(IN_SELECTION)
        // do nothing
        val heightItemListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, v: View?,
                    position: Int, id: Long
                ) {
                    updateHeightUnitSelection()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // do nothing
                }
            }

        weightSpinner!!.setOnItemSelectedListener(itemListener)
        heightSpinner!!.setOnItemSelectedListener(heightItemListener)
    }

    private fun updateWeightUnitSelection() {
        val weightUnitSelection = getWeightUnitSelection()
        if (weightUnitSelection == WeightUnit.KG) {
            weightEditText!!.setHint(getString(R.string.weight_hint))
        } else {
            weightEditText!!.setHint(getString(R.string.weight_lb_hint))
        }
    }

    private fun getWeightUnitSelection(): WeightUnit {
        val result = weightSpinner!!.getSelectedItemPosition()
        if (result == KG_SELECTION) return WeightUnit.KG
        else return WeightUnit.LB
    }

    private fun updateHeightUnitSelection() {
        val heightUnitSelection = getHeightUnitSelection()
        if (heightUnitSelection == HeightUnit.CM) heightEditText!!.setHint(getString(R.string.height_hint))
        else heightEditText!!.setHint(getString(R.string.height_inches_hint))
    }

    private fun getHeightUnitSelection(): HeightUnit {
        val result = heightSpinner!!.getSelectedItemPosition()
        if (result == CM_SELECTION) return HeightUnit.CM
        else return HeightUnit.IN
    }

    private fun calculate() {
        // clear any message
        messageTextView!!.setText(null)
        // make sure message white with 2 calculations in row, 1st invalid
        resetResultTextColor()
        val weightText: CharSequence = weightEditText!!.getText()
        val heightText: CharSequence = heightEditText!!.getText()
        try {
            var unitsInLbs = false
            var weight = weightText.toString().toDouble()
            if (getWeightUnitSelection() == WeightUnit.LB) {
                weight = UnitConverter.lbsToKgs(weight)
            }
            var height = heightText.toString().toDouble()
            if (getHeightUnitSelection() == HeightUnit.IN) {
                height = UnitConverter.insToCms(height)
            }
            val result = BMI.Companion.calculateCmRounded(weight, height)
            calculatedResult?.setText(getString(R.string.bmi_result, result.toString()))
            val message = getMessage(result)
            messageTextView?.setText(message)
            if (!BMI.Companion.isNormalBmi(result)) {
                calculatedResult?.setTextColor(Color.RED)
            }
        } catch (e: NumberFormatException) {
            calculatedResult!!.setText(getString(R.string.invalid_warning))
            calculatedResult!!.setTextColor(Color.RED)
            messageTextView!!.setText(null)
        }
    }

    fun getMessage(bmi: Double): String {
        val classification = BMI.Companion.getClassification(bmi)
        when (classification) {
            BMI.Classification.UNDERWEIGHT_SEVERE -> return getString(R.string.underweight_severe_label)
            BMI.Classification.UNDERWEIGHT_MODERATE -> return getString(R.string.underweight_moderate_label)
            BMI.Classification.UNDERWEIGHT_MILD -> return getString(R.string.underweight_mild_label)
            BMI.Classification.NORMAL -> return getString(R.string.normal_label)
            BMI.Classification.OVERWEIGHT_PREOBESE -> return getString(R.string.overweight_preobese_label)
            BMI.Classification.OVERWEIGHT_CLASS_1 -> return getString(R.string.overweight_class_1_label)
            BMI.Classification.OVERWEIGHT_CLASS_2 -> return getString(R.string.overweight_class_2_label)
            BMI.Classification.OVERWEIGHT_CLASS_3 -> return getString(R.string.overweight_class_3_label)
        }
    }

    private fun formatWeight(weight: String?, units: String?): String {
        return weight + " " + units + ")."
    }

    private fun clearEntries() {
        weightEditText!!.setText(null)
        heightEditText!!.setText(null)
        messageTextView!!.setText(null)
        calculatedResult!!.setText(null)
        weightEditText!!.requestFocus()
        resetResultTextColor()
    }

    private fun resetResultTextColor() {
        calculatedResult!!.setTextAppearance(
            android.R.style.TextAppearance_Medium
        )
    }

    private fun getPrefs() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val weightUnitPreference: String = prefs.getString(
            "default_weight_unit",
            "KG"
        )!!
        val heightUnitPreference: String = prefs.getString(
            "default_height_unit",
            "CM"
        )!!
        if (weightUnitPreference == "KG") {
            defaultWeightUnitSelection = WeightUnit.KG
        } else {
            defaultWeightUnitSelection = WeightUnit.LB
        }
        if (heightUnitPreference == "CM") {
            defaultHeightUnitSelection = HeightUnit.CM
        } else {
            defaultHeightUnitSelection = HeightUnit.IN
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