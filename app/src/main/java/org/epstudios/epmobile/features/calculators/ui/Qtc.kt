package org.epstudios.epmobile.features.calculators.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.preference.PreferenceManager
import org.epstudios.epmobile.R
import org.epstudios.epmobile.core.data.QtcCalculator
import org.epstudios.epmobile.core.data.QtcCalculator.QtcFormula
import org.epstudios.epmobile.core.data.Reference
import org.epstudios.epmobile.core.ui.base.EpActivity
import org.epstudios.epmobile.databinding.QtcBinding

class Qtc : EpActivity() {
    private enum class IntervalRate { INTERVAL, RATE }

    private lateinit var binding: QtcBinding

    private var qtcUpperLimit: Int = QTC_UPPER_LIMIT
    private var defaultIntervalRateSelection = IntervalRate.INTERVAL
    private var qtcFormula: String = BAZETT

    companion object {
        private const val QTC_UPPER_LIMIT = 440
        private const val BAZETT = "BAZETT"
        private const val FRIDERICIA = "FRIDERICIA"
        private const val SAGIE = "SAGIE"
        private const val HODGES = "HODGES"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = QtcBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupInsets(binding.qtcRootView)
        initToolbar()

        binding.calculateButtonsLayout.calculateButton.setOnClickListener { calculateQtc() }
        binding.calculateButtonsLayout.clearButton.setOnClickListener { clearEntries() }

        getPrefs()
        setAdapters()
        setFormulaAdapters()

        clearEntries()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val parentActivityIntent = Intent(this, CalculatorList::class.java)
            parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(parentActivityIntent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setAdapters() {
        val adapter = ArrayAdapter.createFromResource(
            this, R.array.interval_rate_labels,
            R.layout.dropdown_menu_item
        )
        binding.intervalRateSpinner.setAdapter(adapter)
        val initialPosition = if (defaultIntervalRateSelection == IntervalRate.INTERVAL) 0 else 1
        binding.intervalRateSpinner.setText(adapter.getItem(initialPosition), false)
        updateIntervalRateSelection()

        binding.intervalRateSpinner.setOnItemClickListener { _, _, _, _ ->
            updateIntervalRateSelection()
        }
    }

    private fun setFormulaAdapters() {
        val adapter = ArrayAdapter.createFromResource(
            this, R.array.formula_names,
            R.layout.dropdown_menu_item
        )
        binding.qtcFormulaSpinner.setAdapter(adapter)
        val formula = when (getQtcFormula(qtcFormula)) {
            QtcFormula.BAZETT -> 0
            QtcFormula.FRIDERICIA -> 1
            QtcFormula.SAGIE -> 2
            QtcFormula.HODGES -> 3
        }
        binding.qtcFormulaSpinner.setText(adapter.getItem(formula), false)

        binding.qtcFormulaSpinner.setOnItemClickListener { _, _, position, _ ->
            qtcFormula = when (position) {
                0 -> BAZETT
                1 -> FRIDERICIA
                2 -> SAGIE
                else -> HODGES
            }
        }
    }

    private fun updateIntervalRateSelection() {
        val hint = if (getIntervalRateSelection() == IntervalRate.INTERVAL) {
            getString(R.string.rr_hint)
        } else {
            getString(R.string.hr_hint)
        }
        binding.rrInputLayout.hint = hint
    }

    private fun getIntervalRateSelection(): IntervalRate {
        return if (binding.intervalRateSpinner.text.toString().startsWith("RR")) {
            IntervalRate.INTERVAL
        } else {
            IntervalRate.RATE
        }
    }

    private fun showQtcFormula() {
        binding.qtcFormula.text = getString(R.string.qtc_formula_used, qtcFormula)
    }

    private fun calculateQtc() {
        val rrText = binding.rrEditText.text.toString()
        val qtText = binding.qtEditText.text.toString()

        try {
            var rr = rrText.toInt()
            if (getIntervalRateSelection() == IntervalRate.RATE) {
                rr = (60000.0 / rr).toInt()
            }
            val qt = qtText.toInt()

            showQtcFormula()
            val formula = getQtcFormula(qtcFormula)
            Toast.makeText(this, "QTc Formula is $qtcFormula", Toast.LENGTH_LONG).show()
            val qtc = QtcCalculator.calculate(rr, qt, formula)
            binding.calculatedQtc.text = getString(R.string.qtc_result, qtc.toString())

            if (qtc >= qtcUpperLimit) {
                binding.calculatedQtc.setTextAppearance(R.style.TextAppearance_Calculator_Error)
            } else {
                binding.calculatedQtc.setTextAppearance(R.style.TextAppearance_Calculator_Result)
            }
        } catch (e: NumberFormatException) {
            binding.calculatedQtc.text = getString(R.string.invalid_warning)
            binding.calculatedQtc.setTextAppearance(R.style.TextAppearance_Calculator_Error)
        }
    }

    private fun getQtcFormula(name: String): QtcFormula {
        return when (name) {
            FRIDERICIA -> QtcFormula.FRIDERICIA
            SAGIE -> QtcFormula.SAGIE
            HODGES -> QtcFormula.HODGES
            else -> QtcFormula.BAZETT
        }
    }

    private fun clearEntries() {
        binding.rrEditText.text = null
        binding.qtEditText.text = null
        binding.calculatedQtc.text = getString(R.string.qtc_result_label)
        binding.calculatedQtc.setTextAppearance(R.style.TextAppearance_Calculator_Result)
        binding.qtcFormula.text = null
        binding.rrEditText.requestFocus()
    }

    private fun getPrefs() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(baseContext)
        qtcFormula = prefs.getString("qtc_formula", BAZETT) ?: BAZETT
        val intervalRatePreference = prefs.getString("interval_rate", "INTERVAL")
        defaultIntervalRateSelection = if (intervalRatePreference == "INTERVAL") IntervalRate.INTERVAL else IntervalRate.RATE
        val s = prefs.getString("maximum_qtc", "")
        try {
            qtcUpperLimit = s?.toInt() ?: QTC_UPPER_LIMIT
        } catch (e: NumberFormatException) {
            qtcUpperLimit = QTC_UPPER_LIMIT
            val editor = prefs.edit()
            editor.putString("maximum_qtc", QTC_UPPER_LIMIT.toString())
            editor.apply()
        }
    }

    override fun hideReferenceMenuItem() = false

    override fun showActivityReference() {
        val referenceBazett = Reference(this, R.string.qtc_calculator_reference_bazett, R.string.qtc_calculator_link_bazett)
        val referenceFridericia = Reference(this, R.string.qtc_calculator_reference_fridericia, R.string.qtc_calculator_link_fridericia)
        val referenceSagie = Reference(this, R.string.qtc_calculator_reference_sagie, R.string.qtc_calculator_link_sagie)
        val referenceHodges = Reference(getString(R.string.qtc_calculator_reference_hodges), null)
        val referenceQtcLimits = Reference(this, R.string.qtc_limits_reference, R.string.qtc_limits_link)
        val references = arrayOf(referenceBazett, referenceFridericia, referenceSagie, referenceHodges, referenceQtcLimits)
        showReferenceAlertDialog(references)
    }
}
