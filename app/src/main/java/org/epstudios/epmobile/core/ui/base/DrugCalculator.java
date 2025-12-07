package org.epstudios.epmobile.core.ui.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.epstudios.epmobile.R;
import org.epstudios.epmobile.core.data.UnitConverter;
import org.epstudios.epmobile.features.calculators.data.CreatinineClearance;

import java.text.DecimalFormat;

import androidx.preference.PreferenceManager;

@SuppressWarnings("SpellCheckingInspection")
public abstract class DrugCalculator extends EpActivity implements
        OnClickListener {

    public DrugCalculator() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drugcalculator);
        setupInsets(R.id.selection_list_root_view);
        initToolbar();

        View calculateDoseButton = findViewById(R.id.calculate_button);
        calculateDoseButton.setOnClickListener(this);
        View clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);

        calculatedDoseTextView = findViewById(R.id.calculated_dose);
        creatinineClearanceTextView = findViewById(R.id.ccTextView);
        weightEditText = findViewById(R.id.weightEditText);
        creatinineEditText = findViewById(R.id.creatinineEditText);
        ageEditText = findViewById(R.id.ageEditText);
        sexRadioGroup = findViewById(R.id.sexRadioGroup);

        weightUnitSpinner = findViewById(R.id.weightUnitSpinner);
        creatinineUnitSpinner = findViewById(R.id.creatinineUnitSpinner);

        getPrefs();
        setAdapters();
        clearEntries();
    }

    private enum WeightUnit {
        KG, LB
    }

    private enum CreatinineUnit {
        MG, MMOL
    }

    private TextView calculatedDoseTextView;
    private EditText weightEditText;
    private EditText creatinineEditText;
    private RadioGroup sexRadioGroup;
    private EditText ageEditText;
    protected TextView creatinineClearanceTextView; // cc == Creatinine Clearance

    private AutoCompleteTextView weightUnitSpinner;
    private AutoCompleteTextView creatinineUnitSpinner;

    private final static int KG_SELECTION = 0;
    private final static int LB_SELECTION = 1;
    private final static int MG_SELECTION = 0;
    private final static int MMOL_SELECTION = 1;

    // phony result of getDose() to indicate special dosing for apixaban
    protected final static int USE_APIXABAN_DOSING = 9999;
    // phony -int dose to indicate CrCl ONLY
    protected final static int CREATININE_CLEARANCE_ONLY = -1;

    private WeightUnit defaultWeightUnitSelection = WeightUnit.KG;
    private CreatinineUnit defaultCreatinineUnitSelection = CreatinineUnit.MG;

    // return string for Drug Reference CrCl calculator
    private String creatinineClearanceReturnString;

    protected String getCreatinineClearanceReturnString() {
        return creatinineClearanceReturnString;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.calculate_button) {
            calculateDose();
        } else if (id == R.id.clear_button) {
            clearEntries();
        }
    }

    private void setAdapters() {
        // Weight Spinner
        String[] weightUnits = getResources().getStringArray(
                R.array.weight_unit_labels);
        ArrayAdapter<String> weightUnitAdapter = new ArrayAdapter<String>(
                this, R.layout.dropdown_menu_item, weightUnits);
        weightUnitSpinner.setAdapter(weightUnitAdapter);

        if (defaultWeightUnitSelection.equals(WeightUnit.KG)) {
            weightUnitSpinner.setText(weightUnits[KG_SELECTION], false);
        } else {
            weightUnitSpinner.setText(weightUnits[LB_SELECTION], false);
        }

        weightUnitSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateWeightUnitSelection();
            }
        });

        // Creatinine Spinner
        String[] creatinineUnits = getResources().getStringArray(
                R.array.creatinine_unit_labels);
        ArrayAdapter<String> creatinineUnitAdapter = new ArrayAdapter<String>(
                this, R.layout.dropdown_menu_item, creatinineUnits);
        creatinineUnitSpinner.setAdapter(creatinineUnitAdapter);

        if (defaultCreatinineUnitSelection.equals(CreatinineUnit.MG)) {
            creatinineUnitSpinner.setText(creatinineUnits[MG_SELECTION], false);
        } else {
            creatinineUnitSpinner.setText(creatinineUnits[MMOL_SELECTION], false);
        }

        creatinineUnitSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateCreatinineUnitSelection();
            }
        });
    }

    private WeightUnit getWeightUnitSelection() {
        String selectedUnit = weightUnitSpinner.getText().toString();
        String kgUnit = getResources().getStringArray(R.array.weight_unit_labels)[KG_SELECTION];
        if (selectedUnit.equals(kgUnit)) {
            return WeightUnit.KG;
        } else {
            return WeightUnit.LB;
        }
    }

    private CreatinineUnit getCreatinineUnitSelection() {
        String selectedUnit = creatinineUnitSpinner.getText().toString();
        String mgUnit = getResources().getStringArray(R.array.creatinine_unit_labels)[MG_SELECTION];
        if (selectedUnit.equals(mgUnit)) {
            return CreatinineUnit.MG;
        } else {
            return CreatinineUnit.MMOL;
        }
    }


    private void updateWeightUnitSelection() {
        WeightUnit weightUnitSelection = getWeightUnitSelection();
        if (weightUnitSelection.equals(WeightUnit.KG))
            weightEditText.setHint(getString(R.string.weight_hint));
        else
            weightEditText.setHint(getString(R.string.weight_lb_hint));
    }
//
//    private WeightUnit getWeightUnitSelection() {
//        int result = weightSpinner.getSelectedItemPosition();
//        if (result == KG_SELECTION)
//            return WeightUnit.KG;
//        else
//            return WeightUnit.LB;
//    }
//
    private void updateCreatinineUnitSelection() {
        CreatinineUnit creatinineUnitSelection = getCreatinineUnitSelection();
        if (creatinineUnitSelection.equals(CreatinineUnit.MG))
            creatinineEditText.setHint(getString(R.string.creatinine_mg_hint));
        else
            creatinineEditText
                    .setHint(getString(R.string.creatinine_mmol_hint));
    }

//    private CreatinineUnit getCreatinineUnitSelection() {
//        int result = creatinineSpinner.getSelectedItemPosition();
//        if (result == MG_SELECTION)
//            return CreatinineUnit.MG;
//        else
//            return CreatinineUnit.MMOL;
//    }

    protected void calculateDose() {
        CharSequence weightText = weightEditText.getText();
        CharSequence creatinineText = creatinineEditText.getText();
        CharSequence ageText = ageEditText.getText();
        boolean isMale = sexRadioGroup.getCheckedRadioButtonId() == R.id.male;
        try {
            double weight = Double.parseDouble(weightText.toString());
            if (getWeightUnitSelection().equals(WeightUnit.LB))
                weight = UnitConverter.lbsToKgs(weight);
            double creatinine = Double.parseDouble(creatinineText.toString());
            double age = Double.parseDouble(ageText.toString());
            if (age < 18 && !pediatricDosingOk()) {
                calculatedDoseTextView.setText(getString(R.string.do_not_use_warning));
                calculatedDoseTextView.setTextAppearance(R.style.TextAppearance_Calculator_Error);
                creatinineClearanceTextView.setText(getString(R.string.pediatric_use_warning));
                return;
            }
            boolean useMmolUnits = (getCreatinineUnitSelection() == CreatinineUnit.MMOL);
            int cc = CreatinineClearance.calculate(isMale, age, weight,
                    creatinine, useMmolUnits);
            String ccMessage = getMessage(cc, age);
            creatinineClearanceTextView.setText(String.format("%s%s", ccMessage, getDisclaimer()));
            creatinineClearanceReturnString = getCrClResultString(cc, isMale, age, weight, creatinine,
                    useMmolUnits);
            double dose = getDose(cc);
            if (dose == USE_APIXABAN_DOSING) {
                // special processing here
                boolean creatinineTooHigh = ((creatinine >= 133 && useMmolUnits) || (creatinine >= 1.5 && !useMmolUnits));
                if ((creatinineTooHigh && (age >= 80 || weight <= 60))
                        || (age >= 80 && weight <= 60))
                    dose = 2.5;
                else
                    dose = 5;
                // add on CYP/dPg warnings
                String message = ccMessage + "\n";
                if (dose == 5) {
                    message += getString(R.string.apixaban_drug_interaction_at_5_mg_message);
                } else {
                    message += getString(R.string.apixaban_drug_interaction_at_2_5_mg_message);
                }
                message += " " + getString(R.string.apixaban_dual_inhibitors);
                if (cc < 15) {
                    message += getString(R.string.apixaban_esrd_caution);
                }
                message += getDisclaimer();
                creatinineClearanceTextView.setText(message);
            }
            if (dose < 0) {  // CrCl only
                calculatedDoseTextView.setTextAppearance(R.style.TextAppearance_Calculator_Result);
                calculatedDoseTextView.setText(String.format("%s mL/min", cc));
            } else if (dose == 0) {
                calculatedDoseTextView
                        .setText(getString(R.string.do_not_use_warning));
                calculatedDoseTextView.setTextAppearance(R.style.TextAppearance_Calculator_Error);
            } else {
                calculatedDoseTextView.setTextAppearance(R.style.TextAppearance_Calculator_Result);
                // format to only show decimal if non-zero
                calculatedDoseTextView.setText(String.format("%s%s", new DecimalFormat("#.#")
                        .format(dose), doseFrequency(cc)));
            }
        } catch (NumberFormatException e) {
            calculatedDoseTextView.setText(getString(R.string.invalid_warning));
            calculatedDoseTextView.setTextAppearance(R.style.TextAppearance_Calculator_Error);
            creatinineClearanceTextView.setText(R.string.creatinine_clearance_label);
        }
    }

    private String getCrClResultString(double crCl, boolean isMale,
                                       double age, double weight, double cr, boolean crIsMmMolUnits) {
        String result = "CrCl = " + Math.round(crCl) + "mL/min (";
        result += Math.round(age) + "y" + (isMale ? "M" : "F") + " ";
        result += Math.round(weight) + "kg Cr ";
        result += cr + (crIsMmMolUnits ? "µmol/L)" : "mg/dL)");
        return result;
    }

    protected Boolean pediatricDosingOk() {
        return false;
    }

    protected String doseFrequency(int crCl) {
        return " mg BID";
    }

    private void clearEntries() {
        weightEditText.setText(null);
        creatinineEditText.setText(null);
        ageEditText.setText(null);
        creatinineClearanceTextView.setText(R.string.creatinine_clearance_label);
        calculatedDoseTextView.setText(defaultResultLabel());
        calculatedDoseTextView.setTextAppearance(R.style.TextAppearance_Calculator_Result);
        ageEditText.requestFocus();
    }

    protected String defaultResultLabel() {
        return "Dose";
    }

    private void getPrefs() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        String weightUnitPreference = prefs.getString("default_weight_unit",
                "KG");
        String creatinineUnitPreference = prefs.getString(
                getString(R.string.creatinine_clearance_unit_key), "MG");
        if (weightUnitPreference.equals("KG"))
            defaultWeightUnitSelection = WeightUnit.KG;
        else
            defaultWeightUnitSelection = WeightUnit.LB;
        if (creatinineUnitPreference.equals("MG"))
            defaultCreatinineUnitSelection = CreatinineUnit.MG;
        else
            defaultCreatinineUnitSelection = CreatinineUnit.MMOL;
    }

    protected String getMessage(int crCl, double age) {
        // returns basic creatinine clearance
        // override for drug-specific message
        // age is only used in some cases for warnings
        return getString(R.string.long_creatinine_clearance_label) + " = "
                + crCl + " mL/min";
    }

    protected String getDisclaimer() {
        return getString(R.string.drug_dose_disclaimer);
    }

    abstract protected int getDose(int crCl);


    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.drug_dose_calculators_title,
                R.string.drug_calculator_instructions);
    }
}
