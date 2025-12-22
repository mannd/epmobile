package org.epstudios.epmobile.core.ui.base

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.preference.PreferenceManager
import org.epstudios.epmobile.R
import org.epstudios.epmobile.core.data.CreatinineUnit
import org.epstudios.epmobile.core.data.Sex
import org.epstudios.epmobile.core.data.UnitConverter
import org.epstudios.epmobile.core.data.WeightUnit
import org.epstudios.epmobile.databinding.DrugcalculatorBinding
import org.epstudios.epmobile.features.calculators.data.CreatinineClearance
import java.text.DecimalFormat
import kotlin.math.roundToInt

abstract class DrugCalculator : EpActivity(), View.OnClickListener {

    private var defaultWeightUnitSelection = WeightUnit.KG
    private var defaultCreatinineUnitSelection = CreatinineUnit.MG

    // return string for Drug Reference CrCl calculator
    protected var creatinineClearanceReturnString: String? = null
        private set

    lateinit var binding: DrugcalculatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DrugcalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupInsets(binding.root)

        initToolbar()

        binding.calculateButtonsLayout.calculateButton.setOnClickListener(this)
        binding.calculateButtonsLayout.clearButton.setOnClickListener(this)

        this.prefs
        setAdapters()
        clearEntries()
    }


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
        binding.weightUnitSpinner.setAdapter(weightUnitAdapter)

        if (defaultWeightUnitSelection == WeightUnit.KG) {
            binding.weightUnitSpinner.setText(weightUnits[WeightUnit.KG.arrayIndex], false)
        } else {
            binding.weightUnitSpinner.setText(weightUnits[WeightUnit.LB.arrayIndex], false)
        }


        // Creatinine Spinner
        val creatinineUnits = getResources().getStringArray(
            R.array.creatinine_unit_labels
        )
        val creatinineUnitAdapter = ArrayAdapter<String?>(
            this, R.layout.dropdown_menu_item, creatinineUnits
        )
        binding.creatinineUnitSpinner.setAdapter(creatinineUnitAdapter)

        if (defaultCreatinineUnitSelection == CreatinineUnit.MG) {
            binding.creatinineUnitSpinner.setText(creatinineUnits[CreatinineUnit.MG.arrayIndex], false)
        } else {
            binding.creatinineUnitSpinner.setText(creatinineUnits[CreatinineUnit.MMOL.arrayIndex], false)
        }
    }

    private val weightUnitSelection: WeightUnit
        get() {
            val selectedUnit = binding.weightUnitSpinner.text.toString()
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
            val selectedUnit = binding.creatinineUnitSpinner.text.toString()
            val mgUnit =
                getResources().getStringArray(R.array.creatinine_unit_labels)[CreatinineUnit.MG.arrayIndex]
            return if (selectedUnit == mgUnit) {
                CreatinineUnit.MG
            } else {
                CreatinineUnit.MMOL
            }
        }

    protected open fun calculateDose() {
        val weightText: CharSequence = binding.weightEditText.text ?: ""
        val creatinineText: CharSequence = binding.creatinineEditText.text ?: ""
        val ageText: CharSequence = binding.ageEditText.text ?: ""
        val isMale = binding.sexChipGroup.checkedChipId == R.id.maleChip
        val sex = if (isMale) Sex.MALE else Sex.FEMALE
        try {
            var weight = weightText.toString().toDouble()
            if (this.weightUnitSelection == WeightUnit.LB) weight = UnitConverter.lbsToKgs(weight)
            val creatinine = creatinineText.toString().toDouble()
            val age = ageText.toString().toDouble()
            if (age < 18 && !pediatricDosingOk()!!) {
                binding.calculatedDose.text = getString(R.string.do_not_use_warning)
                binding.calculatedDose.setTextAppearance(R.style.TextAppearance_Calculator_Error)
                binding.creatinineClearanceTextView.text = getString(R.string.pediatric_use_warning)
                return
            }
            val useMmolUnits = (this.creatinineUnitSelection == CreatinineUnit.MMOL)
            val creatinineUnits = if (useMmolUnits) CreatinineUnit.MMOL else CreatinineUnit.MG
            val cc = CreatinineClearance.calculate(
                isMale, age, weight,
                creatinine, useMmolUnits
            )
            val ccMessage = getMessage(cc, age)
            binding.creatinineClearanceTextView.text = String.format(
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
                binding.creatinineClearanceTextView.text = message
            }
            if (dose < 0) {  // CrCl only
                binding.calculatedDose.setTextAppearance(R.style.TextAppearance_Calculator_Result)
                binding.calculatedDose.text = String.format("%s mL/min", cc)
            } else if (dose == 0.0) {
                binding.calculatedDose.text = getString(R.string.do_not_use_warning)
                binding.calculatedDose.setTextAppearance(R.style.TextAppearance_Calculator_Error)
            } else {
                binding.calculatedDose.setTextAppearance(R.style.TextAppearance_Calculator_Result)
                // format to only show decimal if non-zero
                binding.calculatedDose.text = String.format(
                    "%s%s", DecimalFormat("#.#")
                        .format(dose), doseFrequency(cc)
                )
            }
        } catch (_: NumberFormatException) {
            binding.calculatedDose.text = getString(R.string.invalid_warning)
            binding.calculatedDose.setTextAppearance(R.style.TextAppearance_Calculator_Error)
            binding.creatinineClearanceTextView.setText(R.string.creatinine_clearance_label)
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
        binding.weightEditText.text = null
        binding.creatinineEditText.text = null
        binding.ageEditText.text = null
        binding.sexChipGroup.check(R.id.maleChip)
        binding.creatinineClearanceTextView.setText(R.string.creatinine_clearance_label)
        binding.calculatedDose.text = defaultResultLabel()
        binding.calculatedDose.setTextAppearance(R.style.TextAppearance_Calculator_Result)
        binding.ageEditText.requestFocus()
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
