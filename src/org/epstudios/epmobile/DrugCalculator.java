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

public abstract class DrugCalculator extends EpDrugCalculatorActivity implements
		OnClickListener {

	public DrugCalculator() {
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drugcalculator);

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

	private WeightUnit defaultWeightUnitSelection = WeightUnit.KG;
	private CreatinineUnit defaultCreatinineUnitSelection = CreatinineUnit.MG;

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
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				updateWeightUnitSelection();
			}

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
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				updateCreatinineUnitSelection();
			}

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

	private void calculateDose() {
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
			if (age < 18) {
				calculatedDoseTextView.setText("Do not use!");
				calculatedDoseTextView.setTextColor(Color.RED);
				ccTextView.setTextColor(Color.RED);
				ccTextView.setText(getString(R.string.pediatric_use_warning));
				return;
			}
			boolean useMmolUnits = (getCreatinineUnitSelection() == CreatinineUnit.MMOL);
			int cc = CreatinineClearance.calculate(isMale, age, weight,
					creatinine, useMmolUnits);
			ccTextView.setTextColor(Color.WHITE); // reset to white here; text
													// colored later
			String ccMessage = getMessage(cc, age);
			ccTextView.setText(ccMessage);
			double dose = getDose(cc);
			if (dose == USE_APIXABAN_DOSING) {
				// special processing here
				ccTextView.setTextColor(Color.WHITE);
				boolean creatinineTooHigh = ((creatinine >= 133 && useMmolUnits) || (creatinine >= 1.5 && !useMmolUnits));
				if ((creatinineTooHigh && (age >= 80 || weight <= 60))
						|| (age >= 80 && weight <= 60))
					dose = 2.5;
				else
					dose = 5;

				// add on CYP/dPg warnings
				if (dose == 5) {
					ccTextView
							.setText(ccMessage
									+ "\n"
									+ getString(R.string.apixaban_drug_interaction_at_5_mg_message)
									+ " "
									+ getString(R.string.apixaban_dual_inhibitors));
				} else if (dose == 2.5) {
					ccTextView
							.setText(ccMessage
									+ "\n"
									+ getString(R.string.apixaban_drug_interaction_at_2_5_mg_message)
									+ " "
									+ getString(R.string.apixaban_dual_inhibitors));

				}
			}
			if (dose == 0) {
				calculatedDoseTextView
						.setText(getString(R.string.do_not_use_warning));
				calculatedDoseTextView.setTextColor(Color.RED);
				ccTextView.setTextColor(Color.RED);
			} else if (dose == USE_APIXABAN_DOSING) {
				calculatedDoseTextView.setTextColor(Color.LTGRAY);
				calculatedDoseTextView
						.setText(getString(R.string.dose_undefined_warning));
			} else {
				calculatedDoseTextView.setTextColor(Color.LTGRAY);
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

	protected String doseFrequency(int crCl) {
		return " mg BID";
	}

	private void clearEntries() {
		weightEditText.setText(null);
		creatinineEditText.setText(null);
		ageEditText.setText(null);
		ccTextView.setText(R.string.creatinine_clearance_label);
		ccTextView.setTextColor(Color.WHITE);
		calculatedDoseTextView.setText(getString(R.string.dose_result_label));
		calculatedDoseTextView.setTextColor(Color.LTGRAY);
		ageEditText.requestFocus();
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
				"default_creatinine_unit", "MG");
		if (creatinineUnitPreference.equals("MG"))
			defaultCreatinineUnitSelection = CreatinineUnit.MG;
		else
			defaultCreatinineUnitSelection = CreatinineUnit.MMOL;
	}

	protected String getMessage(int crCl, double age) {
		// returns basic creatinine clearance
		// override for drug-specific message
		// age is only used in some cases for warnings
		return getString(R.string.creatine_clearance_label) + " = "
				+ String.valueOf(crCl) + " ml/min";
	}

	abstract protected int getDose(int crCl);

}