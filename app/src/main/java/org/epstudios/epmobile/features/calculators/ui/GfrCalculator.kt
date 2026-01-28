package org.epstudios.epmobile.features.calculators.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceManager
import org.epstudios.epmobile.R
import org.epstudios.epmobile.core.data.CreatinineUnit
import org.epstudios.epmobile.core.data.Gfr
import org.epstudios.epmobile.core.ui.base.EpActivity
import org.epstudios.epmobile.databinding.GfrBinding

class GfrCalculator : EpActivity() {

    private lateinit var binding: GfrBinding

    private var defaultCreatinineUnitSelection = CreatinineUnit.MG
    private var creatinineUnitPosition: Int = 0

    companion object {
        private const val MAX_AGE = 120
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GfrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupInsets(binding.gfrRootView)
        initToolbar()

        binding.calculateButtonsLayout.calculateButton.setOnClickListener { calculateResult() }
        binding.calculateButtonsLayout.clearButton.setOnClickListener { clearEntries() }

        getPrefs()
        setInitialChipState()
        clearEntries()
    }


    private fun setInitialChipState() {
        if (defaultCreatinineUnitSelection == CreatinineUnit.MG) {
            binding.creatinineUnitChipGroup.check(R.id.mg_chip)
        } else {
            binding.creatinineUnitChipGroup.check(R.id.mmol_chip)
        }
    }

    private val creatinineUnitSelection: org.epstudios.epmobile.core.data.CreatinineUnit
        get() {
            return when (binding.creatinineUnitChipGroup.checkedChipId) {
                R.id.mg_chip -> org.epstudios.epmobile.core.data.CreatinineUnit.MG
                else -> org.epstudios.epmobile.core.data.CreatinineUnit.MMOL
            }
        }

    private fun calculateResult() {
        // Reset to normal appearing text color
        binding.calculatedGfr.setTextAppearance(android.R.style.TextAppearance_Medium)

        val ageText = binding.ageEditText.text.toString()
        val crText = binding.creatinineEditText.text.toString()
        val isMale = binding.maleChip.isChecked
        val isBlack = binding.blackChip.isChecked

        try {
            val age = ageText.toDouble()
            if (age > MAX_AGE) {
                displayInvalidInput()
                return
            }
            var cr = crText.toDouble()
            if (creatinineUnitSelection == CreatinineUnit.MMOL) {
                cr = Gfr.convertMicroMolPerLiterToMgPerDL(cr)
            }
            val result = Gfr.ckdEpiGfr(cr, age.toInt(), isMale, isBlack)
            val resultString = getString(R.string.gfr_result_string, result.toLong())
            binding.calculatedGfr.text = resultString
            binding.calculatedGfr.setTextAppearance(R.style.TextAppearance_Calculator_Result)
        } catch (_: NumberFormatException) {
            displayInvalidInput()
        }
    }

    private fun displayInvalidInput() {
        binding.calculatedGfr.text = getString(R.string.invalid_warning)
        binding.calculatedGfr.setTextAppearance(R.style.TextAppearance_Calculator_Error)
    }

    private fun clearEntries() {
        binding.ageEditText.text = null
        binding.creatinineEditText.text = null
        binding.calculatedGfr.text = getString(R.string.gfr_result_label)
        binding.calculatedGfr.setTextAppearance(R.style.TextAppearance_Calculator_Result)
        binding.ageEditText.requestFocus()
    }


    private fun getPrefs() {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val creatinineUnitPreference = prefs.getString(getString(R.string.creatinine_clearance_unit_key), "MG")
        defaultCreatinineUnitSelection = if (creatinineUnitPreference == "MG") CreatinineUnit.MG else CreatinineUnit.MMOL
    }

    override fun hideReferenceMenuItem() = false

    override fun showActivityReference() {
        showReferenceAlertDialog(R.string.gfr_reference, R.string.gfr_link)
    }

    override fun hideInstructionsMenuItem() = false

    override fun showActivityInstructions() {
        showAlertDialog(R.string.gfr_calculator_title, R.string.gfr_instructions)
    }
}
