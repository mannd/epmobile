package org.epstudios.epmobile.features.calculators.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.preference.PreferenceManager
import org.epstudios.epmobile.R
import org.epstudios.epmobile.core.data.HeightUnit
import org.epstudios.epmobile.core.data.UnitConverter
import org.epstudios.epmobile.core.data.WeightUnit
import org.epstudios.epmobile.core.ui.base.EpActivity
import org.epstudios.epmobile.databinding.IbwBinding
import java.text.DecimalFormat

class IbwCalculator : EpActivity() {
    private lateinit var binding: IbwBinding

    private var defaultWeightUnitSelection = WeightUnit.KG
    private var defaultHeightUnitSelection = HeightUnit.CM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = IbwBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupInsets(binding.ibwRootView)
        initToolbar()

        binding.calculateButtonsLayout.calculateButton.setOnClickListener { calculate() }
        binding.calculateButtonsLayout.clearButton.setOnClickListener { clearEntries() }

        getPrefs()
        setInitialChipState()
        clearEntries()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

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
        val isMale = binding.maleChip.isChecked
        val weightText = binding.weightEditText.text.toString()
        val heightText = binding.heightEditText.text.toString()

        try {
            var weight = weightText.toDouble()
            val originalWeight = weight
            if (weightUnitSelection == WeightUnit.LB) {
                weight = UnitConverter.lbsToKgs(weight)
            }

            var height = heightText.toDouble()
            if (heightUnitSelection == HeightUnit.CM) {
                height = UnitConverter.cmsToIns(height)
            }

            val ibw = idealBodyWeight(height, isMale)
            val abw = adjustedBodyWeight(ibw, weight)

            val unitsInLbs = weightUnitSelection == WeightUnit.LB
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

    companion object {
        @JvmStatic
        public fun idealBodyWeight(height: Double, isMale: Boolean): Double {
            var weight = if (height > 60.0) (height - 60.0) * 2.3 else 0.0
            weight += if (isMale) 50.0 else 45.5
            return weight
        }

        @JvmStatic
        fun adjustedBodyWeight(ibw: Double, actualWeight: Double): Double {
            val abw = ibw + 0.4 * (actualWeight - ibw)
            return if (actualWeight > ibw) abw else actualWeight
        }

        @JvmStatic
        fun isOverweight(ibw: Double, actualWeight: Double) = actualWeight > ibw * 1.3

        @JvmStatic
        fun isUnderHeight(height: Double) = height <= 60.0

        @JvmStatic
        fun isUnderWeight(weight: Double, ibw: Double) = weight < ibw
    }



    override fun hideInstructionsMenuItem() = false

    override fun showActivityInstructions() {
        showAlertDialog(R.string.ibw_calculator_title, R.string.ibw_calculator_instructions)
    }

    override fun hideReferenceMenuItem() = false

    override fun showActivityReference() {
        showReferenceAlertDialog(R.string.ibw_calculator_reference, R.string.ibw_calculator_link)
    }
}
