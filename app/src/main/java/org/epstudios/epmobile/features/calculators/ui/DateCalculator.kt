package org.epstudios.epmobile.features.calculators.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.datepicker.MaterialDatePicker
import org.epstudios.epmobile.R
import org.epstudios.epmobile.core.ui.base.EpActivity
import org.epstudios.epmobile.databinding.DatecalculatorBinding
import java.text.DateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone

class DateCalculator : EpActivity() {
    private lateinit var binding: DatecalculatorBinding
    private var selectedDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DatecalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupInsets(binding.root)
        initToolbar()

        binding.calculateButtonsLayout.calculateButton.setOnClickListener { calculateDays() }
        binding.calculateButtonsLayout.clearButton.setOnClickListener { clearEntries() }
        binding.indexDateButton.setOnClickListener { showDatePicker() }

        binding.numberOfDaysEditText.setText(R.string.dc_default_number_of_days)
        updateDateButtonText()

        binding.dayRadioGroup.setOnCheckedChangeListener {
            _, checkedId ->
            val days = when (checkedId) {
                R.id.ninetyRadio -> "90"
                R.id.fortyRadio -> "40"
                R.id.thirtyRadio -> "30"
                else -> null
            }
            days?.let {
                binding.numberOfDaysEditText.setText(it)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val parentActivityIntent = Intent(this, CalculatorList::class.java)
                parentActivityIntent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                            or Intent.FLAG_ACTIVITY_NEW_TASK
                )
                startActivity(parentActivityIntent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun calculateDays() {
        val numberOfDays = binding.numberOfDaysEditText.text.toString()
        try {
            var number = numberOfDays.toInt()
            if (binding.reverseTimeCheckBox.isChecked) number = -number
            val cal: Calendar = GregorianCalendar(
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
            )
            cal.add(Calendar.DATE, number)
            val message = DateFormat.getDateInstance(DateFormat.MEDIUM).format(cal.time)
            binding.calculatedDate.text = message
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        } catch (_: NumberFormatException) {
            binding.calculatedDate.text = getString(R.string.invalid_warning)
            binding.calculatedDate.setTextAppearance(R.style.TextAppearance_Calculator_Error)
        }
    }

    private fun clearEntries() {
        binding.numberOfDaysEditText.text = null
        binding.calculatedDate.text = getString(R.string.date_result_label)
        binding.dayRadioGroup.check(R.id.ninetyRadio)
        binding.numberOfDaysEditText.setText(getString(R.string.dc_default_number_of_days))
        binding.calculatedDate.setTextAppearance(R.style.TextAppearance_Calculator_Result)
        selectedDate = Calendar.getInstance()
        updateDateButtonText()
    }

    override fun hideInstructionsMenuItem(): Boolean {
        return false
    }

    override fun showActivityInstructions() {
        showAlertDialog(
            R.string.date_calculator_title,
            R.string.date_calculator_instructions
        )
    }

    private fun showDatePicker() {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.select_date_title)
            .setSelection(utcMillisFromCalendar(selectedDate))
            .build()
        picker.addOnPositiveButtonClickListener { selection ->
            selection?.let {
                selectedDate = calendarFromUtc(it)
                updateDateButtonText()
            }
        }
        picker.show(supportFragmentManager, "date-picker")
    }

    private fun updateDateButtonText() {
        val dateText = DateFormat.getDateInstance(DateFormat.MEDIUM).format(selectedDate.time)
        binding.indexDateButton.text = dateText
    }

    private fun calendarFromUtc(utcMillis: Long): Calendar {
        val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utcCalendar.timeInMillis = utcMillis
        val localCalendar = Calendar.getInstance()
        localCalendar.set(
            utcCalendar.get(Calendar.YEAR),
            utcCalendar.get(Calendar.MONTH),
            utcCalendar.get(Calendar.DAY_OF_MONTH)
        )
        return localCalendar
    }

    private fun utcMillisFromCalendar(calendar: Calendar): Long {
        val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utcCalendar.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        return utcCalendar.timeInMillis
    }
}
