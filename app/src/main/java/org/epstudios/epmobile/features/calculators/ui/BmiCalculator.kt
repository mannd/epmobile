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

package org.epstudios.epmobile.features.calculators.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.preference.PreferenceManager
import org.epstudios.epmobile.R
import org.epstudios.epmobile.core.data.HeightUnit
import org.epstudios.epmobile.core.data.UnitConverter
import org.epstudios.epmobile.core.data.WeightUnit
import org.epstudios.epmobile.core.ui.base.EpActivity
import org.epstudios.epmobile.databinding.BmiBinding
import org.epstudios.epmobile.features.calculators.data.BMI

class BmiCalculator : EpActivity(), View.OnClickListener {

    private var defaultWeightUnitSelection = WeightUnit.KG
    private var defaultHeightUnitSelection = HeightUnit.CM

    private lateinit var binding : BmiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = BmiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupInsets(binding.root)
        initToolbar()

        binding.calculateButtonsLayout.calculateButton.setOnClickListener(this)
        binding.calculateButtonsLayout.clearButton.setOnClickListener(this)

        getPrefs()
        // The setAdapters() method is no longer needed!
        // We will set the initial state of the chips instead.
        setInitialChipState()
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

    override fun onClick(v: View) {
        when (v.id) {
            R.id.calculate_button -> calculate()
            R.id.clear_button -> clearEntries()
        }
    }

    // This replaces setAdapters()
    private fun setInitialChipState() {
        if (defaultWeightUnitSelection == WeightUnit.KG) {
            binding.weightUnitChipGroup.check(R.id.kg_chip)
        } else {
            binding.weightUnitChipGroup.check(R.id.lb_chip)
        }

        if (defaultHeightUnitSelection == HeightUnit.CM) {
            binding.heightUnitChipGroup.check(R.id.cm_chip)
        } else {
            binding.heightUnitChipGroup.check(R.id.in_chip)
        }
    }

    // We now get the selection directly from the ChipGroup
    private val weightUnitSelection: WeightUnit
        get() {
            return when (binding.weightUnitChipGroup.checkedChipId) {
                R.id.kg_chip -> WeightUnit.KG
                else -> WeightUnit.LB
            }
        }

    // Same for height
    private val heightUnitSelection: HeightUnit
        get() {
            return when (binding.heightUnitChipGroup.checkedChipId) {
                R.id.cm_chip -> HeightUnit.CM
                else -> HeightUnit.IN
            }
        }

    private fun calculate() {
        binding.messageTextView.text = null
        resetResultTextColor()
        val weightText: CharSequence = binding.weightEditText.text ?: ""
        val heightText: CharSequence = binding.heightEditText.text ?: ""
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
            binding.calculatedResult.text = getString(R.string.bmi_result, result.toString())
            val message = getMessage(result)
            binding.messageTextView.text = message
            if (!BMI.isNormalBmi(result)) {
                binding.calculatedResult.setTextAppearance(R.style.TextAppearance_Calculator_Error)
            }
        } catch (_: NumberFormatException) {
            binding.calculatedResult.text = getString(R.string.invalid_warning)
            binding.calculatedResult.setTextAppearance(R.style.TextAppearance_Calculator_Error)
            binding.messageTextView.text = null
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
//        binding.weightEditText.text = null
//        binding.heightEditText.text = null
        binding.messageTextView.text = null
        binding.calculatedResult.text = null
//        binding.weightEditText.requestFocus()
        resetResultTextColor()
    }

    private fun resetResultTextColor() {
        binding.calculatedResult.setTextAppearance(R.style.TextAppearance_Calculator_Result)
    }

    private fun getPrefs() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.getString("default_weight_unit", "KG")?.let { weightUnitPref ->
            defaultWeightUnitSelection = if (weightUnitPref == "KG") WeightUnit.KG else WeightUnit.LB
        }
        prefs.getString("default_height_unit", "CM")?.let { heightUnitPref ->
            defaultHeightUnitSelection = if (heightUnitPref == "CM") HeightUnit.CM else HeightUnit.IN
        }
    }

    override fun hideInstructionsMenuItem(): Boolean = false

    override fun showActivityInstructions() {
        showAlertDialog(
            R.string.bmi_calculator_title,
            R.string.bmi_calculator_instructions
        )
    }

    override fun hideReferenceMenuItem(): Boolean = false

    override fun showActivityReference() {
        showReferenceAlertDialog(
            R.string.bmi_calculator_reference,
            R.string.bmi_calculator_link
        )
    }
}
