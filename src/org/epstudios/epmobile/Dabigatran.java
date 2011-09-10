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

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.RadioGroup;

public class Dabigatran extends EpActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dabigatran);
		
		View calculateDoseButton = findViewById(R.id.calculate_dose_button);
        calculateDoseButton.setOnClickListener(this);
        View clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);
        
		dabigitranDoseTextView = (TextView) findViewById(R.id.calculated_dose);
		ccTextView = (TextView) findViewById(R.id.ccTextView);
        weightEditText = (EditText) findViewById(R.id.weightEditText);
        creatinineEditText = (EditText) findViewById(R.id.creatinineEditText);
        ageEditText = (EditText) findViewById(R.id.ageEditText);
        sexRadioGroup = (RadioGroup) findViewById(R.id.sexRadioGroup); 
	}
	
	private TextView dabigitranDoseTextView;
	private EditText weightEditText;
	private EditText creatinineEditText;
	private RadioGroup sexRadioGroup;
	private EditText ageEditText;
	private TextView ccTextView;	// cc == Creatinine Clearance
	
	
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
	
	private void calculateDose() {
		CharSequence weightText = weightEditText.getText();
		CharSequence creatinineText = creatinineEditText.getText();
		CharSequence ageText = ageEditText.getText();
		Boolean isMale = sexRadioGroup.getCheckedRadioButtonId() == R.id.male;
		try {
			double weight = Double.parseDouble(weightText.toString());
			double creatinine = Double.parseDouble(creatinineText.toString());
			double age = Double.parseDouble(ageText.toString());
			int cc = CreatinineClearance.calculate(isMale, age, weight, creatinine);
			ccTextView.setText("(Creatinine Clearance = " + String.valueOf(cc) + ")");
			int dose = getDose(cc);
			if (dose == 0) {
				dabigitranDoseTextView.setText("Do not use!");
				dabigitranDoseTextView.setTextColor(Color.RED);
			}
			else {
				dabigitranDoseTextView.setTextColor(Color.LTGRAY);
				dabigitranDoseTextView.setText(String.valueOf(dose) + " mg BID");
			}
		}
		catch (NumberFormatException e) {	
			dabigitranDoseTextView.setText("Invalid!");
			dabigitranDoseTextView.setTextColor(Color.RED);
		}		
	}		
	

	
	private int getDose(double crClr) {
		if (crClr > 30)
			return 150;
		if (crClr > 15)
			return 75;
		return 0;
	}

	private void clearEntries() {
		weightEditText.setText(null);
		creatinineEditText.setText(null);
		ageEditText.setText(null);
		ccTextView.setText(R.string.creatinine_clearance_label);
		dabigitranDoseTextView.setText(getString(R.string.dabigatran_result_label));
		dabigitranDoseTextView.setTextColor(Color.LTGRAY);
		weightEditText.requestFocus();
	}
		

}

