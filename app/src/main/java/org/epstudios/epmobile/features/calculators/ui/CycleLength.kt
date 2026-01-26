package org.epstudios.epmobile.features.calculators.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import org.epstudios.epmobile.R
import org.epstudios.epmobile.core.ui.base.EpActivity
import org.epstudios.epmobile.databinding.CyclelengthBinding
import kotlin.math.roundToInt

class CycleLength : EpActivity() {

    private lateinit var binding: CyclelengthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CyclelengthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupInsets(binding.cycleLengthRootView)
        initToolbar()

        binding.calculateButtonsLayout.calculateButton.setOnClickListener { calculateResult() }
        binding.calculateButtonsLayout.clearButton.setOnClickListener { clearEntries() }

        binding.intervalRateChipGroup.setOnCheckedChangeListener { _, _ ->
            updateInputHint()
            binding.calculatedResult.setText(R.string.calculated_result_label)
        }
        // Set initial hint
        updateInputHint()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val parentActivityIntent = Intent(this, CalculatorList::class.java)
                parentActivityIntent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                )
                startActivity(parentActivityIntent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateInputHint() {
        val hintResId = if (binding.intervalRateChipGroup.checkedChipId == R.id.cl_chip) {
            R.string.cl_hint
        } else {
            R.string.hr_hint
        }
        binding.inputLayout.hint = getString(hintResId)
    }

    private fun calculateResult() {
        val inputText = binding.inputEditText.text.toString()
        binding.calculatedResult.setTextAppearance(R.style.TextAppearance_Calculator_Result)
        try {
            val input = inputText.toInt()
            if (input == 0) throw NumberFormatException()

            val result = calculate(input)
            val resultStringId = if (binding.intervalRateChipGroup.checkedChipId == R.id.cl_chip) {
                R.string.cl_result_as_rate
            } else {
                R.string.cl_result_as_interval
            }
            binding.calculatedResult.text = getString(resultStringId, result.toString())
        } catch (e: NumberFormatException) {
            binding.calculatedResult.text = getString(R.string.invalid_warning)
            binding.calculatedResult.setTextAppearance(R.style.TextAppearance_Calculator_Error)
        }
    }

    private fun clearEntries() {
        binding.inputEditText.text = null
        binding.calculatedResult.setText(R.string.calculated_result_label)
        binding.inputEditText.requestFocus()
    }

    companion object {
        fun calculate(value: Int): Int {
            require(value != 0)
            return (60000.0 / value).roundToInt()
        }
    }
}
