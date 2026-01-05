package org.epstudios.epmobile.features.calculators.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.preference.PreferenceManager
import org.epstudios.epmobile.R
import org.epstudios.epmobile.core.data.Gfr
import org.epstudios.epmobile.core.ui.base.EpActivity
import org.epstudios.epmobile.databinding.GfrBinding

class GfrCalculator : EpActivity() {

    private lateinit var binding: GfrBinding

    private enum class CreatinineUnit { MG, MMOL }

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
        setAdapters()
        clearEntries()
    }

    private fun calculateResult() {
        // Reset to normal appearing text color
        binding.calculatedGfr.setTextAppearance(android.R.style.TextAppearance_Medium)

        val ageText = binding.ageEditText.text.toString()
        val crText = binding.creatinineEditText.text.toString()
        val isMale = binding.sexRadioGroup.checkedRadioButtonId == R.id.male
        val isBlack = binding.raceRadioGroup.checkedRadioButtonId == R.id.black

        try {
            val age = ageText.toDouble()
            if (age > MAX_AGE) {
                displayInvalidInput()
                return
            }
            var cr = crText.toDouble()
            if (getCreatinineUnitSelection() == CreatinineUnit.MMOL) {
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

    private fun setAdapters() {
        val creatinineItems = resources.getStringArray(R.array.creatinine_unit_labels)
        val creatinineAdapter = ArrayAdapter(this, R.layout.dropdown_menu_item, creatinineItems)
        binding.creatinineUnitsSpinner.setAdapter(creatinineAdapter)

        binding.creatinineUnitsSpinner.setOnItemClickListener { _, _, position, _ ->
            creatinineUnitPosition = position
            updateCreatinineUnitSelection(position)
        }
        // Set initial selection
        val initialPosition = if (defaultCreatinineUnitSelection == CreatinineUnit.MG) 0 else 1
        creatinineUnitPosition = initialPosition
        binding.creatinineUnitsSpinner.setText(creatinineAdapter.getItem(initialPosition), false)
        updateCreatinineUnitSelection(initialPosition)
    }

    private fun getPrefs() {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val creatinineUnitPreference = prefs.getString(getString(R.string.creatinine_clearance_unit_key), "MG")
        defaultCreatinineUnitSelection = if (creatinineUnitPreference == "MG") CreatinineUnit.MG else CreatinineUnit.MMOL
    }

    private fun updateCreatinineUnitSelection(position: Int) {
        val hint = if (position == 0) {
            getString(R.string.creatinine_mg_hint)
        } else {
            getString(R.string.creatinine_mmol_hint)
        }
        binding.creatinineInputLayout.hint = hint
    }

    private fun getCreatinineUnitSelection(): CreatinineUnit {
        return if (creatinineUnitPosition == 0) CreatinineUnit.MG else CreatinineUnit.MMOL
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
