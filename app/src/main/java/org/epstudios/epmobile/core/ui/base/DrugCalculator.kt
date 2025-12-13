package org.epstudios.epmobile.core.ui.base

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.preference.PreferenceManager
import org.epstudios.epmobile.R
import org.epstudios.epmobile.core.data.CreatinineUnit
import org.epstudios.epmobile.core.data.Sex
import org.epstudios.epmobile.core.data.UnitConverter
import org.epstudios.epmobile.core.data.WeightUnit
import org.epstudios.epmobile.features.calculators.data.CreatinineClearance
import java.text.DecimalFormat
import kotlin.math.roundToInt

abstract class DrugCalculator : EpActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drugcalculator)
        setupInsets(R.id.selection_list_root_view)
        initToolbar()

        val calculateDoseButton = findViewById<View>(R.id.calculate_button)
        calculateDoseButton.setOnClickListener(this)
        val clearButton = findViewById<View>(R.id.clear_button)
        clearButton.setOnClickListener(this)

        calculatedDoseTextView = findViewById(R.id.calculated_dose)
        creatinineClearanceTextView = findViewById(R.id.ccTextView)
        weightEditText = findViewById(R.id.weightEditText)
        creatinineEditText = findViewById(R.id.creatinineEditText)
        ageEditText = findViewById(R.id.ageEditText)
        sexRadioGroup = findViewById(R.id.sexRadioGroup)

        weightUnitSpinner = findViewById(R.id.weightUnitSpinner)
        creatinineUnitSpinner = findViewById(R.id.creatinineUnitSpinner)

        this.prefs
        setAdapters()
        clearEntries()
    }

    private var calculatedDoseTextView: TextView? = null
    private var weightEditText: EditText? = null
    private var creatinineEditText: EditText? = null
    private var sexRadioGroup: RadioGroup? = null
    private var ageEditText: EditText? = null
    protected var creatinineClearanceTextView: TextView? = null // cc == Creatinine Clearance

    private var weightUnitSpinner: AutoCompleteTextView? = null
    private var creatinineUnitSpinner: AutoCompleteTextView? = null

    private var defaultWeightUnitSelection = WeightUnit.KG
    private var defaultCreatinineUnitSelection = CreatinineUnit.MG

    // return string for Drug Reference CrCl calculator
    protected var creatinineClearanceReturnString: String? = null
        private set

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.calculate_button) {
            calculateDose()
        } else if (id == R.id.clear_button) {
            clearEntries()
        }
    }

    private fun setAdapters() {
        // Weight Spinner
        val weightUnits = getResources().getStringArray(
            R.array.weight_unit_labels
        )
        val weightUnitAdapter = ArrayAdapter<String?>(
            this, R.layout.dropdown_menu_item, weightUnits
        )
        weightUnitSpinner!!.setAdapter(weightUnitAdapter)

        if (defaultWeightUnitSelection == WeightUnit.KG) {
            weightUnitSpinner!!.setText(weightUnits[WeightUnit.KG.arrayIndex], false)
        } else {
            weightUnitSpinner!!.setText(weightUnits[WeightUnit.LB.arrayIndex], false)
        }


        // Creatinine Spinner
        val creatinineUnits = getResources().getStringArray(
            R.array.creatinine_unit_labels
        )
        val creatinineUnitAdapter = ArrayAdapter<String?>(
            this, R.layout.dropdown_menu_item, creatinineUnits
        )
        creatinineUnitSpinner!!.setAdapter(creatinineUnitAdapter)

        if (defaultCreatinineUnitSelection == CreatinineUnit.MG) {
            creatinineUnitSpinner!!.setText(creatinineUnits[CreatinineUnit.MG.arrayIndex], false)
        } else {
            creatinineUnitSpinner!!.setText(creatinineUnits[CreatinineUnit.MMOL.arrayIndex], false)
        }
    }

    private val weightUnitSelection: WeightUnit
        get() {
            val selectedUnit = weightUnitSpinner?.text?.toString() ?: ""
            val kgUnit =
                getResources().getStringArray(R.array.weight_unit_labels)[WeightUnit.KG.arrayIndex]
            return if (selectedUnit == kgUnit) {
                WeightUnit.KG
            } else {
                WeightUnit.LB
            }
        }

    private val creatinineUnitSelection: CreatinineUnit
        get() {
            val selectedUnit = creatinineUnitSpinner?.text?.toString() ?: ""
            val mgUnit =
                getResources().getStringArray(R.array.creatinine_unit_labels)[CreatinineUnit.MG.arrayIndex]
            return if (selectedUnit == mgUnit) {
                CreatinineUnit.MG
            } else {
                CreatinineUnit.MMOL
            }
        }

    protected open fun calculateDose() {
        val weightText: CharSequence = weightEditText?.text ?: ""
        val creatinineText: CharSequence = creatinineEditText?.text ?: ""
        val ageText: CharSequence = ageEditText?.text ?: ""
        val isMale = (sexRadioGroup?.checkedRadioButtonId ?: R.id.male) == R.id.male
        val sex = if (isMale) Sex.MALE else Sex.FEMALE
        try {
            var weight = weightText.toString().toDouble()
            if (this.weightUnitSelection == WeightUnit.LB) weight = UnitConverter.lbsToKgs(weight)
            val creatinine = creatinineText.toString().toDouble()
            val age = ageText.toString().toDouble()
            if (age < 18 && !pediatricDosingOk()!!) {
                calculatedDoseTextView?.text = getString(R.string.do_not_use_warning)
                calculatedDoseTextView?.setTextAppearance(R.style.TextAppearance_Calculator_Error)
                creatinineClearanceTextView?.text = getString(R.string.pediatric_use_warning)
                return
            }
            val useMmolUnits = (this.creatinineUnitSelection == CreatinineUnit.MMOL)
            val creatinineUnits = if (useMmolUnits) CreatinineUnit.MMOL else CreatinineUnit.MG
            val cc = CreatinineClearance.calculate(
                isMale, age, weight,
                creatinine, useMmolUnits
            )
            val ccMessage = getMessage(cc, age)
            creatinineClearanceTextView?.text = String.format(
                "%s%s", ccMessage,
                this.disclaimer
            )
            creatinineClearanceReturnString = getCrClResultsString(
                cc.toDouble(), sex, age, weight, creatinine,
                creatinineUnits
            )
            var dose = getDose(cc).toDouble()
            if (dose == USE_APIXABAN_DOSING.toDouble()) {
                // special processing here
                val creatinineTooHigh =
                    ((creatinine >= 133 && useMmolUnits) || (creatinine >= 1.5 && !useMmolUnits))
                dose = if ((creatinineTooHigh && (age >= 80 || weight <= 60))
                    || (age >= 80 && weight <= 60)
                ) 2.5
                else 5.0
                // add on CYP/dPg warnings
                var message: String? = ccMessage + "\n"
                message += if (dose == 5.0) {
                    getString(R.string.apixaban_drug_interaction_at_5_mg_message)
                } else {
                    getString(R.string.apixaban_drug_interaction_at_2_5_mg_message)
                }
                message += " " + getString(R.string.apixaban_dual_inhibitors)
                if (cc < 15) {
                    message += getString(R.string.apixaban_esrd_caution)
                }
                message += this.disclaimer
                creatinineClearanceTextView?.text = message
            }
            if (dose < 0) {  // CrCl only
                calculatedDoseTextView?.setTextAppearance(R.style.TextAppearance_Calculator_Result)
                calculatedDoseTextView?.text = String.format("%s mL/min", cc)
            } else if (dose == 0.0) {
                calculatedDoseTextView!!.text = getString(R.string.do_not_use_warning)
                calculatedDoseTextView!!.setTextAppearance(R.style.TextAppearance_Calculator_Error)
            } else {
                calculatedDoseTextView!!.setTextAppearance(R.style.TextAppearance_Calculator_Result)
                // format to only show decimal if non-zero
                calculatedDoseTextView!!.text = String.format(
                    "%s%s", DecimalFormat("#.#")
                        .format(dose), doseFrequency(cc)
                )
            }
        } catch (_: NumberFormatException) {
            calculatedDoseTextView?.text = getString(R.string.invalid_warning)
            calculatedDoseTextView?.setTextAppearance(R.style.TextAppearance_Calculator_Error)
            creatinineClearanceTextView?.setText(R.string.creatinine_clearance_label)
        }
    }

    private fun getCrClResultsString(
        crCl: Double,
        sex: Sex,
        age: Double,
        weight: Double,
        cr: Double,
        creatinineUnit: CreatinineUnit
    ): String {
        val roundedCrCl = crCl.roundToInt()
        val roundedAge = age.roundToInt().toString()
        val roundedWeight = weight.roundToInt().toString()
        val crString = cr.toString()

        val sexArray = resources.getStringArray(R.array.sex_abbrev)
        val sexString = sexArray[sex.arrayIndex]
        val creatinineUnitArray = resources.getStringArray(R.array.creatinine_unit_labels)
        val creatinineUnitString = creatinineUnitArray[creatinineUnit.arrayIndex]

        return getString(
            R.string.crcl_results_summary,
            roundedCrCl,
            roundedAge,
            sexString,
            roundedWeight,
            crString,
            creatinineUnitString
        )

    }

    protected open fun pediatricDosingOk(): Boolean? {
        return false
    }

    protected open fun doseFrequency(crCl: Int): String? {
        return " mg BID"
    }

    private fun clearEntries() {
        weightEditText?.text = null
        creatinineEditText?.text = null
        ageEditText?.text = null
        creatinineClearanceTextView?.setText(R.string.creatinine_clearance_label)
        calculatedDoseTextView?.text = defaultResultLabel()
        calculatedDoseTextView?.setTextAppearance(R.style.TextAppearance_Calculator_Result)
        ageEditText!!.requestFocus()
    }

    protected open fun defaultResultLabel(): String? {
        return "Dose"
    }

    private val prefs: Unit
        get() {
            val prefs = PreferenceManager
                .getDefaultSharedPreferences(baseContext)
            val weightUnitPreference: String = prefs.getString(
                "default_weight_unit",
                "KG"
            )!!
            val creatinineUnitPreference: String = prefs.getString(
                getString(R.string.creatinine_clearance_unit_key), "MG"
            )!!
            defaultWeightUnitSelection = if (weightUnitPreference == "KG") WeightUnit.KG
            else WeightUnit.LB
            defaultCreatinineUnitSelection = if (creatinineUnitPreference == "MG") CreatinineUnit.MG
            else CreatinineUnit.MMOL
        }

    protected open fun getMessage(crCl: Int, age: Double): String {
        // returns basic creatinine clearance
        // override for drug-specific message
        // age is only used in some cases for warnings
        return (getString(R.string.long_creatinine_clearance_label) + " = "
                + crCl + " mL/min")
    }

    protected open val disclaimer: String?
        get() = getString(R.string.drug_dose_disclaimer)

    protected abstract fun getDose(crCl: Int): Int


    override fun hideInstructionsMenuItem(): Boolean {
        return false
    }

    override fun showActivityInstructions() {
        showAlertDialog(
            R.string.drug_dose_calculators_title,
            R.string.drug_calculator_instructions
        )
    }

    companion object {
        // phony result of getDose() to indicate special dosing for apixaban
        const val USE_APIXABAN_DOSING: Int = 9999

        // phony -int dose to indicate CrCl ONLY
        const val CREATININE_CLEARANCE_ONLY: Int = -1
    }
}
