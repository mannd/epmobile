package org.epstudios.epmobile.core.ui.base

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.preference.PreferenceManager
import org.epstudios.epmobile.R
import org.epstudios.epmobile.core.data.UnitConverter
import org.epstudios.epmobile.features.calculators.data.CreatinineClearance
import java.text.DecimalFormat

@Suppress("SpellCheckingInspection")
abstract class DrugCalculator : EpActivity(), View.OnClickListener {
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drugcalculator)
        setupInsets(R.id.selection_list_root_view)
        initToolbar()

        val calculateDoseButton = findViewById<View>(R.id.calculate_button)
        calculateDoseButton.setOnClickListener(this)
        val clearButton = findViewById<View>(R.id.clear_button)
        clearButton.setOnClickListener(this)

        calculatedDoseTextView = findViewById<TextView>(R.id.calculated_dose)
        creatinineClearanceTextView = findViewById<TextView>(R.id.ccTextView)
        weightEditText = findViewById<EditText>(R.id.weightEditText)
        creatinineEditText = findViewById<EditText>(R.id.creatinineEditText)
        ageEditText = findViewById<EditText>(R.id.ageEditText)
        sexRadioGroup = findViewById<RadioGroup>(R.id.sexRadioGroup)

        weightUnitSpinner = findViewById<AutoCompleteTextView>(R.id.weightUnitSpinner)
        creatinineUnitSpinner = findViewById<AutoCompleteTextView>(R.id.creatinineUnitSpinner)

        this.prefs
        setAdapters()
        clearEntries()
    }

    private enum class WeightUnit {
        KG, LB
    }

    private enum class CreatinineUnit {
        MG, MMOL
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
        val id = v.getId()
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
        weightUnitSpinner!!.setAdapter<ArrayAdapter<String?>?>(weightUnitAdapter)

        if (defaultWeightUnitSelection == WeightUnit.KG) {
            weightUnitSpinner!!.setText(weightUnits[KG_SELECTION], false)
        } else {
            weightUnitSpinner!!.setText(weightUnits[LB_SELECTION], false)
        }

        weightUnitSpinner!!.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateWeightUnitSelection()
            }
        })

        // Creatinine Spinner
        val creatinineUnits = getResources().getStringArray(
            R.array.creatinine_unit_labels
        )
        val creatinineUnitAdapter = ArrayAdapter<String?>(
            this, R.layout.dropdown_menu_item, creatinineUnits
        )
        creatinineUnitSpinner!!.setAdapter<ArrayAdapter<String?>?>(creatinineUnitAdapter)

        if (defaultCreatinineUnitSelection == CreatinineUnit.MG) {
            creatinineUnitSpinner!!.setText(creatinineUnits[MG_SELECTION], false)
        } else {
            creatinineUnitSpinner!!.setText(creatinineUnits[MMOL_SELECTION], false)
        }

        creatinineUnitSpinner!!.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateCreatinineUnitSelection()
            }
        })
    }

    private val weightUnitSelection: WeightUnit
        get() {
            val selectedUnit = weightUnitSpinner!!.getText().toString()
            val kgUnit =
                getResources().getStringArray(R.array.weight_unit_labels)[KG_SELECTION]
            if (selectedUnit == kgUnit) {
                return WeightUnit.KG
            } else {
                return WeightUnit.LB
            }
        }

    private val creatinineUnitSelection: CreatinineUnit
        get() {
            val selectedUnit = creatinineUnitSpinner!!.getText().toString()
            val mgUnit =
                getResources().getStringArray(R.array.creatinine_unit_labels)[MG_SELECTION]
            if (selectedUnit == mgUnit) {
                return CreatinineUnit.MG
            } else {
                return CreatinineUnit.MMOL
            }
        }


    private fun updateWeightUnitSelection() {
        val weightUnitSelection =
            this.weightUnitSelection
    }

    private fun updateCreatinineUnitSelection() {
        val creatinineUnitSelection =
            this.creatinineUnitSelection
    }

    protected open fun calculateDose() {
        val weightText: CharSequence = weightEditText!!.getText()
        val creatinineText: CharSequence = creatinineEditText!!.getText()
        val ageText: CharSequence = ageEditText!!.getText()
        val isMale = sexRadioGroup!!.getCheckedRadioButtonId() == R.id.male
        try {
            var weight = weightText.toString().toDouble()
            if (this.weightUnitSelection == WeightUnit.LB) weight = UnitConverter.lbsToKgs(weight)
            val creatinine = creatinineText.toString().toDouble()
            val age = ageText.toString().toDouble()
            if (age < 18 && !pediatricDosingOk()!!) {
                calculatedDoseTextView!!.setText(getString(R.string.do_not_use_warning))
                calculatedDoseTextView!!.setTextAppearance(R.style.TextAppearance_Calculator_Error)
                creatinineClearanceTextView!!.setText(getString(R.string.pediatric_use_warning))
                return
            }
            val useMmolUnits = (this.creatinineUnitSelection == CreatinineUnit.MMOL)
            val cc = CreatinineClearance.calculate(
                isMale, age, weight,
                creatinine, useMmolUnits
            )
            val ccMessage = getMessage(cc, age)
            creatinineClearanceTextView!!.setText(
                String.format(
                    "%s%s", ccMessage,
                    this.disclaimer
                )
            )
            creatinineClearanceReturnString = getCrClResultString(
                cc.toDouble(), isMale, age, weight, creatinine,
                useMmolUnits
            )
            var dose = getDose(cc).toDouble()
            if (dose == USE_APIXABAN_DOSING.toDouble()) {
                // special processing here
                val creatinineTooHigh =
                    ((creatinine >= 133 && useMmolUnits) || (creatinine >= 1.5 && !useMmolUnits))
                if ((creatinineTooHigh && (age >= 80 || weight <= 60))
                    || (age >= 80 && weight <= 60)
                ) dose = 2.5
                else dose = 5.0
                // add on CYP/dPg warnings
                var message: String? = ccMessage + "\n"
                if (dose == 5.0) {
                    message += getString(R.string.apixaban_drug_interaction_at_5_mg_message)
                } else {
                    message += getString(R.string.apixaban_drug_interaction_at_2_5_mg_message)
                }
                message += " " + getString(R.string.apixaban_dual_inhibitors)
                if (cc < 15) {
                    message += getString(R.string.apixaban_esrd_caution)
                }
                message += this.disclaimer
                creatinineClearanceTextView!!.setText(message)
            }
            if (dose < 0) {  // CrCl only
                calculatedDoseTextView!!.setTextAppearance(R.style.TextAppearance_Calculator_Result)
                calculatedDoseTextView!!.setText(String.format("%s mL/min", cc))
            } else if (dose == 0.0) {
                calculatedDoseTextView!!
                    .setText(getString(R.string.do_not_use_warning))
                calculatedDoseTextView!!.setTextAppearance(R.style.TextAppearance_Calculator_Error)
            } else {
                calculatedDoseTextView!!.setTextAppearance(R.style.TextAppearance_Calculator_Result)
                // format to only show decimal if non-zero
                calculatedDoseTextView!!.setText(
                    String.format(
                        "%s%s", DecimalFormat("#.#")
                            .format(dose), doseFrequency(cc)
                    )
                )
            }
        } catch (e: NumberFormatException) {
            calculatedDoseTextView!!.setText(getString(R.string.invalid_warning))
            calculatedDoseTextView!!.setTextAppearance(R.style.TextAppearance_Calculator_Error)
            creatinineClearanceTextView!!.setText(R.string.creatinine_clearance_label)
        }
    }

    private fun getCrClResultString(
        crCl: Double, isMale: Boolean,
        age: Double, weight: Double, cr: Double, crIsMmMolUnits: Boolean
    ): String {
        var result = "CrCl = " + Math.round(crCl) + "mL/min ("
        result += Math.round(age).toString() + "y" + (if (isMale) "M" else "F") + " "
        result += Math.round(weight).toString() + "kg Cr "
        result += cr.toString() + (if (crIsMmMolUnits) "µmol/L)" else "mg/dL)")
        return result
    }

    protected open fun pediatricDosingOk(): Boolean? {
        return false
    }

    protected open fun doseFrequency(crCl: Int): String? {
        return " mg BID"
    }

    private fun clearEntries() {
        weightEditText!!.setText(null)
        creatinineEditText!!.setText(null)
        ageEditText!!.setText(null)
        creatinineClearanceTextView!!.setText(R.string.creatinine_clearance_label)
        calculatedDoseTextView!!.setText(defaultResultLabel())
        calculatedDoseTextView!!.setTextAppearance(R.style.TextAppearance_Calculator_Result)
        ageEditText!!.requestFocus()
    }

    protected open fun defaultResultLabel(): String? {
        return "Dose"
    }

    private val prefs: Unit
        get() {
            val prefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext())
            val weightUnitPreference: String = prefs.getString(
                "default_weight_unit",
                "KG"
            )!!
            val creatinineUnitPreference: String = prefs.getString(
                getString(org.epstudios.epmobile.R.string.creatinine_clearance_unit_key), "MG"
            )!!
            if (weightUnitPreference == "KG") defaultWeightUnitSelection =
                WeightUnit.KG
            else defaultWeightUnitSelection =
                WeightUnit.LB
            if (creatinineUnitPreference == "MG") defaultCreatinineUnitSelection =
                CreatinineUnit.MG
            else defaultCreatinineUnitSelection =
                CreatinineUnit.MMOL
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
        private const val KG_SELECTION = 0
        private const val LB_SELECTION = 1
        private const val MG_SELECTION = 0
        private const val MMOL_SELECTION = 1

        // phony result of getDose() to indicate special dosing for apixaban
        protected const val USE_APIXABAN_DOSING: Int = 9999

        // phony -int dose to indicate CrCl ONLY
        @JvmField
        protected val CREATININE_CLEARANCE_ONLY: Int = -1
    }
}
