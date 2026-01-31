package org.epstudios.epmobile.features.calculators.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.preference.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.epstudios.epmobile.R
import org.epstudios.epmobile.core.data.QtcCalculator
import org.epstudios.epmobile.core.data.QtcCalculator.QtcFormula
import org.epstudios.epmobile.core.data.Reference
import org.epstudios.epmobile.core.ui.base.EpActivity
import org.epstudios.epmobile.core.ui.base.MaterialSpinnerAdapter
import org.epstudios.epmobile.databinding.QtcivcdBinding

class QtcIvcd : EpActivity() {
    private enum class IntervalRate { INTERVAL, RATE }

    private lateinit var binding: QtcivcdBinding

    private var qtcFormula: String = BAZETT
    private var defaultIntervalRateSelection = IntervalRate.INTERVAL

    companion object {
        private const val BAZETT = "BAZETT"
        private const val FRIDERICIA = "FRIDERICIA"
        private const val SAGIE = "SAGIE"
        private const val HODGES = "HODGES"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = QtcivcdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupInsets(binding.qtcivcdRootView)
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
        val labels = resources.getStringArray(R.array.interval_rate_labels)
        val adapter = MaterialSpinnerAdapter(this, R.layout.dropdown_menu_item, labels)

        binding.intervalRateSpinner.setAdapter(adapter)
        val initialPosition = if (defaultIntervalRateSelection == IntervalRate.INTERVAL) 0 else 1
        binding.intervalRateSpinner.setText(adapter.getItem(initialPosition), false)
        updateIntervalRateSelection()

        binding.intervalRateSpinner.setOnItemClickListener { _, _, _, _ ->
            updateIntervalRateSelection()
        }
    }

    private fun setFormulaAdapters() {
        val formulaNames = resources.getStringArray(R.array.formula_names)
        val adapter = MaterialSpinnerAdapter(this, R.layout.dropdown_menu_item, formulaNames)
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

    private fun getQtcFormula(name: String): QtcCalculator.QtcFormula {
        return when (name) {
            FRIDERICIA -> QtcCalculator.QtcFormula.FRIDERICIA
            SAGIE -> QtcCalculator.QtcFormula.SAGIE
            HODGES -> QtcCalculator.QtcFormula.HODGES
            else -> QtcCalculator.QtcFormula.BAZETT
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

    private class ShortQrsException : Exception()

    private fun calculateQtc() {
        val rateIntervalText = binding.rrEditText.text.toString()
        val qtText = binding.qtEditText.text.toString()
        val qrsText = binding.qrsEditText.text.toString()
        val isMale = binding.maleChip.isChecked
        val isLBBB = binding.lbbbCheckBox.isChecked

        try {
            val rateInterval = rateIntervalText.toInt()
            val (interval, rate) = if (getIntervalRateSelection() == IntervalRate.RATE) {
                (60000.0 / rateInterval).toInt() to rateInterval.toDouble()
            } else {
                rateInterval to (60000.0 / rateInterval).toInt().toDouble()
            }

            val qt = qtText.toInt()
            val qrs = qrsText.toInt()

            if (rateInterval <= 0 || qt <= 0 || qrs <= 0 || qrs >= qt) {
                throw NumberFormatException()
            } else if (qrs < 120) {
                throw ShortQrsException()
            }

            val formula = getQtcFormula(qtcFormula)
            val qtc = QtcCalculator.calculate(interval, qt, formula)
            val jt = QtcCalculator.jtInterval(qt, qrs).toInt()
            val jtc = qtc - qrs
            val qtm = QtcCalculator.qtCorrectedForLBBB(qt, qrs).toInt()
            val qtmc = QtcCalculator.calculate(interval, qtm, formula)
            val qtrrqrs = QtcCalculator.qtRrIvcd(qt, rate, qrs, isMale).toInt()
            val preLbbbQtc = QtcCalculator.preLbbbQtc(qt, interval, qrs, isMale, formula).toInt()

            val intent = Intent(this, QtcIvcdResults::class.java).apply {
                putExtra("isLBBB", isLBBB)
                putExtra("isMale", isMale)
                putExtra("QT", qt)
                putExtra("QTc", qtc)
                putExtra("JT", jt)
                putExtra("JTc", jtc)
                putExtra("QTm", qtm)
                putExtra("QTmc", qtmc)
                putExtra("QTrrqrs", qtrrqrs)
                putExtra("preLbbbQtc", preLbbbQtc)
                putExtra("QTcFormula", formula.name)
            }
            startActivity(intent)
        } catch (e: NumberFormatException) {
            showError(getString(R.string.qt_calculator_error))
        } catch (e: ShortQrsException) {
            showError(getString(R.string.short_qrs_error))
        }
    }

    private fun showError(message: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.error_dialog_title))
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok_button_label), null)
            .show()
    }

    private fun clearEntries() {
        binding.rrEditText.text = null
        binding.qtEditText.text = null
        binding.qrsEditText.text = null
        binding.rrEditText.requestFocus()
    }

    private fun getPrefs() {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
        qtcFormula = prefs.getString("qtc_formula", BAZETT) ?: BAZETT
        val intervalRatePreference = prefs.getString("interval_rate", "INTERVAL")
        defaultIntervalRateSelection = if (intervalRatePreference == "INTERVAL") IntervalRate.INTERVAL else IntervalRate.RATE
    }

    override fun hideReferenceMenuItem() = false

    override fun showActivityReference() {
        val referenceBogossian = Reference(
            this,
            R.string.qtc_ivcd_reference_bogossian,
            R.string.qtc_ivcd_link_bogossian
        )
        val referenceRautaharju = Reference(
            this,
            R.string.qtc_ivcd_reference_rautaharju,
            R.string.qtc_ivcd_link_rautaharju
        )
        val referenceYankelson = Reference(
            this,
            R.string.qtc_ivcd_reference_yankelson,
            R.string.qtc_ivcd_link_yankelson
        )
        val referenceQtcLimits = Reference(
            this,
            R.string.qtc_limits_reference,
            R.string.qtc_limits_link
        )
        val references = arrayOf(
            referenceBogossian,
            referenceRautaharju,
            referenceYankelson,
            referenceQtcLimits
        )
        showReferenceAlertDialog(references)
    }

    override fun hideInstructionsMenuItem() = false

    override fun showActivityInstructions() {
        showAlertDialog(
            R.string.qtc_ivcd_calculator_title,
            R.string.qtc_ivcd_calculator_instructions
        )
    }
}
