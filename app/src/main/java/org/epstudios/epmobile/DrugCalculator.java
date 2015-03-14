package org.epstudios.epmobile;

import java.text.DecimalFormat;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public abstract class DrugCalculator extends EpActivity implements
		OnClickListener {

	public DrugCalculator() {
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.drugcalculator);
        super.onCreate(savedInstanceState);

		View calculateDoseButton = findViewById(R.id.calculate_dose_button);
		calculateDoseButton.setOnClickListener(this);
		View clearButton = findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);

		calculatedDoseTextView = (TextView) findViewById(R.id.calculated_dose);
		ccTextView = (TextView) findViewById(R.id.ccTextView);
		weightEditText = (EditText) findViewById(R.id.weightEditText);
		creatinineEditText = (EditText) findViewById(R.id.creatinineEditText);
		ageEditText = (EditText) findViewById(R.id.ageEditText);
		sexRadioGroup = (RadioGroup) findViewById(R.id.sexRadioGroup);
		weightSpinner = (Spinner) findViewById(R.id.weight_spinner);
		creatinineSpinner = (Spinner) findViewById(R.id.creatinine_spinner);

		getPrefs();
		setAdapters();
		clearEntries();
	}

	private enum WeightUnit {
		KG, LB
	};

	private enum CreatinineUnit {
		MG, MMOL
	};

	private TextView calculatedDoseTextView;
	private EditText weightEditText;
	private EditText creatinineEditText;
	private RadioGroup sexRadioGroup;
	private EditText ageEditText;
	protected TextView ccTextView; // cc == Creatinine Clearance
	private Spinner weightSpinner;
	private Spinner creatinineSpinner;
	private OnItemSelectedListener itemListener;
	private OnItemSelectedListener creatItemListener;

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
		switch (v.getId()) {
		case R.id.calculate_dose_button:
			calculateDose();
			break;
		case R.id.clear_button:
			clearEntries();
			break;
		}
	}

	private void setAdapters() {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.weight_unit_labels,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		weightSpinner.setAdapter(adapter);
		if (defaultWeightUnitSelection.equals(WeightUnit.KG))
			weightSpinner.setSelection(KG_SELECTION);
		else
			weightSpinner.setSelection(LB_SELECTION);
		itemListener = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				updateWeightUnitSelection();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}

		};

		// creatinineSpinner.setOnItemSelectedListener(itemListener);

		ArrayAdapter<CharSequence> creatAdapter = ArrayAdapter
				.createFromResource(this, R.array.creatinine_unit_labels,
						android.R.layout.simple_spinner_item);
		creatAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		creatinineSpinner.setAdapter(creatAdapter);
		if (defaultCreatinineUnitSelection.equals(CreatinineUnit.MG))
			creatinineSpinner.setSelection(MG_SELECTION);
		else
			creatinineSpinner.setSelection(MMOL_SELECTION);
		creatItemListener = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				updateCreatinineUnitSelection();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}

		};

		weightSpinner.setOnItemSelectedListener(itemListener);
		creatinineSpinner.setOnItemSelectedListener(creatItemListener);
	}

	private void updateWeightUnitSelection() {
		WeightUnit weightUnitSelection = getWeightUnitSelection();
		if (weightUnitSelection.equals(WeightUnit.KG))
			weightEditText.setHint(getString(R.string.weight_hint));
		else
			weightEditText.setHint(getString(R.string.weight_lb_hint));
	}

	private WeightUnit getWeightUnitSelection() {
		int result = weightSpinner.getSelectedItemPosition();
		if (result == KG_SELECTION)
			return WeightUnit.KG;
		else
			return WeightUnit.LB;
	}

	private void updateCreatinineUnitSelection() {
		CreatinineUnit creatinineUnitSelection = getCreatinineUnitSelection();
		if (creatinineUnitSelection.equals(CreatinineUnit.MG))
			creatinineEditText.setHint(getString(R.string.creatinine_mg_hint));
		else
			creatinineEditText
					.setHint(getString(R.string.creatinine_mmol_hint));
	}

	private CreatinineUnit getCreatinineUnitSelection() {
		int result = creatinineSpinner.getSelectedItemPosition();
		if (result == MG_SELECTION)
			return CreatinineUnit.MG;
		else
			return CreatinineUnit.MMOL;
	}

	protected void calculateDose() {
		CharSequence weightText = weightEditText.getText();
		CharSequence creatinineText = creatinineEditText.getText();
		CharSequence ageText = ageEditText.getText();
		Boolean isMale = sexRadioGroup.getCheckedRadioButtonId() == R.id.male;
		try {
			double weight = Double.parseDouble(weightText.toString());
			if (getWeightUnitSelection().equals(WeightUnit.LB))
				weight = UnitConverter.lbsToKgs(weight);
			double creatinine = Double.parseDouble(creatinineText.toString());
			double age = Double.parseDouble(ageText.toString());
			if (age < 18 && !pediatricDosingOk()) {
				calculatedDoseTextView.setText("Do not use!");
				calculatedDoseTextView.setTextColor(Color.RED);
				ccTextView.setTextColor(Color.RED);
				ccTextView.setText(getString(R.string.pediatric_use_warning));
				return;
			}
			boolean useMmolUnits = (getCreatinineUnitSelection() == CreatinineUnit.MMOL);
			int cc = CreatinineClearance.calculate(isMale, age, weight,
                    creatinine, useMmolUnits);
			ccTextView.setTextAppearance(this,
					android.R.style.TextAppearance_Medium);
			String ccMessage = getMessage(cc, age);
			ccTextView.setText(ccMessage + getDisclaimer());
            creatinineClearanceReturnString = getCrClResultString(cc, isMale, age, weight, creatinine,
                    useMmolUnits);
            double dose = getDose(cc);
            if (dose == USE_APIXABAN_DOSING) {
				// special processing here
				ccTextView.setTextAppearance(this,
						android.R.style.TextAppearance_Medium);
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
                }
                else if (dose == 2.5) {
                    message += getString(R.string.apixaban_drug_interaction_at_2_5_mg_message);
                }
                message += " " + getString(R.string.apixaban_dual_inhibitors);
                if (cc < 15) {
                    message += getString(R.string.apixaban_esrd_caution);
                }
                message += getDisclaimer();
                ccTextView.setText(message);
			}
            if (dose < 0) {  // CrCl only
                calculatedDoseTextView.setTextAppearance(this,
                        android.R.style.TextAppearance_Large);
                calculatedDoseTextView.setText(String.valueOf(cc) + " mL/min");
            }
			else if (dose == 0) {
				calculatedDoseTextView
						.setText(getString(R.string.do_not_use_warning));
				calculatedDoseTextView.setTextColor(Color.RED);
				ccTextView.setTextColor(Color.RED);
			} else if (dose == USE_APIXABAN_DOSING) {
				calculatedDoseTextView.setTextAppearance(this,
						android.R.style.TextAppearance_Large);
				calculatedDoseTextView
						.setText(getString(R.string.dose_undefined_warning));
			} else {
				calculatedDoseTextView.setTextAppearance(this,
						android.R.style.TextAppearance_Large);
				// format to only show decimal if non-zero
				calculatedDoseTextView.setText(new DecimalFormat("#.#")
						.format(dose) + doseFrequency(cc));
			}
		} catch (NumberFormatException e) {
			calculatedDoseTextView.setText(getString(R.string.invalid_warning));
			calculatedDoseTextView.setTextColor(Color.RED);
			ccTextView.setText(R.string.creatinine_clearance_label);
		}
	}

    private String getCrClResultString(double crCl, boolean isMale,
                                       double age, double weight, double cr, boolean crIsMmMolUnits) {
        String result = "CrCl = " + Math.round(crCl) + "mL/min (";
        result += Math.round(age)+ "y" + (isMale ? "M" : "F") + " ";
        result += Math.round(weight) + "kg Cr ";
        result += cr + (crIsMmMolUnits ? "\u00B5mol/L)" : "mg/dL)");
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
		ccTextView.setText(R.string.creatinine_clearance_label);
		ccTextView.setTextAppearance(this,
				android.R.style.TextAppearance_Medium);
		calculatedDoseTextView.setText(defaultResultLabel());
		calculatedDoseTextView.setTextAppearance(this,
				android.R.style.TextAppearance_Large);
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
		if (weightUnitPreference.equals("KG"))
			defaultWeightUnitSelection = WeightUnit.KG;
		else
			defaultWeightUnitSelection = WeightUnit.LB;
		String creatinineUnitPreference = prefs.getString(
				getString(R.string.creatinine_clearance_unit_key), "MG");
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
				+ String.valueOf(crCl) + " mL/min";
	}

    protected String getDisclaimer() {
        return getString(R.string.drug_dose_disclaimer);
    }

	abstract protected int getDose(int crCl);

}
