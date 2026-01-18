package org.epstudios.epmobile.features.calculators.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.preference.PreferenceManager
import org.epstudios.epmobile.R
import org.epstudios.epmobile.core.data.UnitConverter
import org.epstudios.epmobile.core.ui.base.EpActivity
import org.epstudios.epmobile.databinding.IbwBinding
import java.text.DecimalFormat

class IbwCalculator : EpActivity() {
    private lateinit var binding: IbwBinding

    private enum class WeightUnit { KG, LB }
    private enum class HeightUnit { CM, IN }

    private var defaultWeightUnitSelection = WeightUnit.KG
    private var defaultHeightUnitSelection = HeightUnit.CM

    private var weightUnitPosition: Int = 0
    private var heightUnitPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = IbwBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupInsets(binding.ibwRootView)
        initToolbar()

        binding.calculateButtonsLayout.calculateButton.setOnClickListener { calculate() }
        binding.calculateButtonsLayout.clearButton.setOnClickListener { clearEntries() }

        getPrefs()
        setAdapters()
        clearEntries()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setAdapters() {
        val weightLabels = resources.getStringArray(R.array.weight_unit_labels)
        val weightAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, weightLabels)
        binding.weightSpinner.setAdapter(weightAdapter)
        val initialWeightPosition = if (defaultWeightUnitSelection == WeightUnit.KG) 0 else 1
        weightUnitPosition = initialWeightPosition
        binding.weightSpinner.setText(weightAdapter.getItem(initialWeightPosition), false)

        binding.weightSpinner.setOnItemClickListener { _, _, position, _ ->
            weightUnitPosition = position
        }

        val heightLabels = resources.getStringArray(R.array.height_unit_labels)
        val heightAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, heightLabels)
        binding.heightSpinner.setAdapter(heightAdapter)
        val initialHeightPosition = if (defaultHeightUnitSelection == HeightUnit.CM) 0 else 1
        heightUnitPosition = initialHeightPosition
        binding.heightSpinner.setText(heightAdapter.getItem(initialHeightPosition), false)

        binding.heightSpinner.setOnItemClickListener { _, _, position, _ ->
            heightUnitPosition = position
        }
    }

    private fun getWeightUnitSelection(): WeightUnit {
        return if (weightUnitPosition == 0) WeightUnit.KG else WeightUnit.LB
    }

    private fun getHeightUnitSelection(): HeightUnit {
        return if (heightUnitPosition == 0) HeightUnit.CM else HeightUnit.IN
    }

    private fun calculate() {
        binding.messageTextView.text = null
        resetResultTextColor()
        val isMale = binding.sexRadioGroup.checkedRadioButtonId == R.id.male
        val weightText = binding.weightEditText.text.toString()
        val heightText = binding.heightEditText.text.toString()

        try {
            var weight = weightText.toDouble()
            val originalWeight = weight
            if (getWeightUnitSelection() == WeightUnit.LB) {
                weight = UnitConverter.lbsToKgs(weight)
            }

            var height = heightText.toDouble()
            if (getHeightUnitSelection() == HeightUnit.CM) {
                height = UnitConverter.cmsToIns(height)
            }

            val ibw = idealBodyWeight(height, isMale)
            val abw = adjustedBodyWeight(ibw, weight)

            val unitsInLbs = getWeightUnitSelection() == WeightUnit.LB
            val weightUnitAbbreviation = if (unitsInLbs) getString(R.string.pound_abbreviation) else getString(R.string.kg_abbreviation)

            val displayIbw = if (unitsInLbs) UnitConverter.kgsToLbs(ibw) else ibw
            val displayAbw = if (unitsInLbs) UnitConverter.kgsToLbs(abw) else abw

            val formattedIbw = DecimalFormat("#.0").format(displayIbw)
            val formattedAbw = DecimalFormat("#.0").format(displayAbw)

            binding.ibwResultTextView.text = "${getString(R.string.ibw_label)} = $formattedIbw $weightUnitAbbreviation"
            binding.abwResultTextView.text = "${getString(R.string.abw_label)} = $formattedAbw $weightUnitAbbreviation"

            val underHeight = isUnderHeight(height)
            val overWeight = isOverweight(ibw, weight)
            val underWeight = isUnderWeight(weight, ibw)

            binding.messageTextView.text = when {
                underHeight -> getString(R.string.underheight_message)
                overWeight -> getString(R.string.overweight_message, "$formattedAbw $weightUnitAbbreviation")
                underWeight -> getString(R.string.underweight_message, "${DecimalFormat("#.0").format(originalWeight)} $weightUnitAbbreviation")
                else -> getString(R.string.normalweight_message, "$formattedIbw $weightUnitAbbreviation")
            }

        } catch (e: NumberFormatException) {
            binding.ibwResultTextView.text = getString(R.string.invalid_warning)
            binding.ibwResultTextView.setTextAppearance(R.style.TextAppearance_Calculator_Error)
            binding.abwResultTextView.text = getString(R.string.invalid_warning)
            binding.abwResultTextView.setTextAppearance(R.style.TextAppearance_Calculator_Error)
        }
    }

    private fun resetResultTextColor() {
        binding.ibwResultTextView.setTextAppearance(R.style.TextAppearance_Calculator_Result)
        binding.abwResultTextView.setTextAppearance(R.style.TextAppearance_Calculator_Result)
    }

    private fun getPrefs() {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val weightUnitPreference = prefs.getString("default_weight_unit", "KG")
        val heightUnitPreference = prefs.getString("default_height_unit", "CM")
        defaultWeightUnitSelection = if (weightUnitPreference == "KG") WeightUnit.KG else WeightUnit.LB
        defaultHeightUnitSelection = if (heightUnitPreference == "CM") HeightUnit.CM else HeightUnit.IN
    }

    private fun clearEntries() {
        binding.weightEditText.text = null
        binding.heightEditText.text = null
        binding.ibwResultTextView.text = null
        binding.abwResultTextView.text = null
        resetResultTextColor()
        binding.messageTextView.text = null
        binding.weightEditText.requestFocus()
    }

    private fun idealBodyWeight(height: Double, isMale: Boolean): Double {
        var weight = if (height > 60.0) (height - 60.0) * 2.3 else 0.0
        weight += if (isMale) 50.0 else 45.5
        return weight
    }

    private fun adjustedBodyWeight(ibw: Double, actualWeight: Double): Double {
        val abw = ibw + 0.4 * (actualWeight - ibw)
        return if (actualWeight > ibw) abw else actualWeight
    }

    private fun isOverweight(ibw: Double, actualWeight: Double) = actualWeight > ibw * 1.3

    private fun isUnderHeight(height: Double) = height <= 60.0

    private fun isUnderWeight(weight: Double, ibw: Double) = weight < ibw

    override fun hideInstructionsMenuItem() = false

    override fun showActivityInstructions() {
        showAlertDialog(R.string.ibw_calculator_title, R.string.ibw_calculator_instructions)
    }

    override fun hideReferenceMenuItem() = false

    override fun showActivityReference() {
        showReferenceAlertDialog(R.string.ibw_calculator_reference, R.string.ibw_calculator_link)
    }
}
