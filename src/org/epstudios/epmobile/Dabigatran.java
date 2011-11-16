/*  EP Mobile -- Mobile tools for electrophysiologists
    Copyright (C) 2011 EP Studios, Inc.
    www.epstudiossoftware.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */   

package org.epstudios.epmobile;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.AdapterView.OnItemSelectedListener;

public class Dabigatran extends DrugCalculator implements OnClickListener {
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
	private TextView ccTextView;	// cc == Creatinine Clearance
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
	
	@Override
	protected String getMessage(int crCl) {
		String msg = super.getMessage(crCl) + "\n";
		if ((crCl >= 15) && (crCl <= 30)) {
			msg += getString(R.string.dabigatran_warning_severe);
			ccTextView.setTextColor(Color.parseColor("#ffa500"));
		}
		else if ((crCl > 30) && (crCl <= 50)) {
			msg += getString(R.string.dabigatran_warning_mild);
			ccTextView.setTextColor(Color.YELLOW);
		}
		else
			ccTextView.setTextColor(Color.WHITE);
		return msg;
	}
	

	@Override
	protected int getDose(double crClr) {
		if (crClr >= 30)
			return 150;
		if (crClr >= 15)
			return 75;
		return 0;
	}

	private void clearEntries() {
		weightEditText.setText(null);
		creatinineEditText.setText(null);
		ageEditText.setText(null);
		ccTextView.setText(R.string.creatinine_clearance_label);
		ccTextView.setTextColor(Color.WHITE);
		calculatedDoseTextView.setText(getString(R.string.dabigatran_result_label));
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

		

		

}

