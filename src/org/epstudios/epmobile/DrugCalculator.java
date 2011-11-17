package org.epstudios.epmobile;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public abstract class DrugCalculator extends EpActivity implements OnClickListener {

	public DrugCalculator() {
		super();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
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
        
        getPrefs();
        setAdapters();
        clearEntries();
	}
	
	private enum WeightUnit {KG, LB};

	
	private TextView calculatedDoseTextView;
	private EditText weightEditText;
	private EditText creatinineEditText;
	private RadioGroup sexRadioGroup;
	private EditText ageEditText;
	protected TextView ccTextView;	// cc == Creatinine Clearance
	private Spinner weightSpinner;
	private OnItemSelectedListener itemListener;
	
	private final static int KG_SELECTION = 0;
	private final static int LB_SELECTION = 1;
	
	private WeightUnit defaultWeightUnitSelection = WeightUnit.KG;
	
	
	
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
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.weight_unit_labels, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		weightSpinner.setAdapter(adapter);
		if (defaultWeightUnitSelection.equals(WeightUnit.KG))
			weightSpinner.setSelection(KG_SELECTION);
		else
			weightSpinner.setSelection(LB_SELECTION);		
		itemListener = new OnItemSelectedListener() {
			public void onItemSelected(AdapterView parent, View v,
					int position, long id) {
				updateWeightUnitSelection();
			}
			public void onNothingSelected(AdapterView parent) {
				// do nothing
			}
		
		};
		
		weightSpinner.setOnItemSelectedListener(itemListener);	
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
		if (result == 0)
			return WeightUnit.KG;
		else
			return WeightUnit.LB;
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
			int cc = CreatinineClearance.calculate(isMale, age, weight, creatinine);
			String ccMessage = getMessage(cc);
			ccTextView.setText(ccMessage);
			int dose = getDose(cc);
			if (dose == 0) {
				calculatedDoseTextView.setText("Do not use!");
				calculatedDoseTextView.setTextColor(Color.RED);
				ccTextView.setTextColor(Color.RED);
			}
			else {
				calculatedDoseTextView.setTextColor(Color.LTGRAY);
				calculatedDoseTextView.setText(String.valueOf(dose) + " mg BID");
			}
		}
		catch (NumberFormatException e) {	
			calculatedDoseTextView.setText("Invalid!");
			calculatedDoseTextView.setTextColor(Color.RED);
		}		
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
		String weightUnitPreference = prefs.getString("default_weight_unit", "KG");
		if (weightUnitPreference.equals("KG"))
			defaultWeightUnitSelection = WeightUnit.KG;
		else
			defaultWeightUnitSelection = WeightUnit.LB;
	}
	
	
	protected String getMessage(int crCl) {
		// returns basic creatinine clearance
		// override for drug-specific message
		return getString(R.string.creatine_clearance_label) +
			" = " + String.valueOf(crCl);
	}
	
	abstract protected int getDose(double crCl);

}