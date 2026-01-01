package org.epstudios.epmobile.features.calculators.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.datepicker.MaterialDatePicker
import org.epstudios.epmobile.R
import org.epstudios.epmobile.core.ui.base.EpActivity
import org.epstudios.epmobile.databinding.DatecalculatorBinding
import java.text.DateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone

class DateCalculator : EpActivity(), View.OnClickListener {
    private lateinit var binding: DatecalculatorBinding
    private var selectedDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DatecalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupInsets(binding.root)
        initToolbar()

        binding.calculateButtonsLayout.calculateButton.setOnClickListener(this)
        binding.calculateButtonsLayout.clearButton.setOnClickListener(this)
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

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.calculate_button) {
            calculateDays()
        } else if (id == R.id.clear_button) {
            clearEntries()
        }
    }

    private fun calculateDays() {
        val numberOfDays: CharSequence = binding.numberOfDaysEditText.getText()
        try {
            var number = numberOfDays.toString().toInt()
            if (binding.reverseTimeCheckBox.isChecked()) number = -number
            val cal: Calendar = GregorianCalendar(
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
            )
            cal.add(Calendar.DATE, number)
            // DateFormat =
            // SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            val message = DateFormat.getDateInstance(DateFormat.MEDIUM).format(cal.getTime())
            binding.calculatedDate.setText(message)
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        } catch (_: NumberFormatException) {
            binding.calculatedDate.setText(getString(R.string.invalid_warning))
            binding.calculatedDate.setTextAppearance(R.style.TextAppearance_Calculator_Error)
        }
    }

    private fun clearEntries() {
        binding.numberOfDaysEditText.setText(null)
        binding.calculatedDate.setText(getString(R.string.date_result_label))
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
