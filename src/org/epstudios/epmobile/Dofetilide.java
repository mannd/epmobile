package org.epstudios.epmobile;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.RadioGroup;
//import android.text.Editable;

public class Dofetilide extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dofetilide);
		
		View calculateDoseButton = findViewById(R.id.calculate_dose_button);
        calculateDoseButton.setOnClickListener(this);
        View clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);
        
		dofetilideDoseTextView = (TextView) findViewById(R.id.calculated_dose);
		ccTextView = (TextView) findViewById(R.id.ccTextView);
        weightEditText = (EditText) findViewById(R.id.weightEditText);
        creatinineEditText = (EditText) findViewById(R.id.creatinineEditText);
        ageEditText = (EditText) findViewById(R.id.ageEditText);
        sexRadioGroup = (RadioGroup) findViewById(R.id.sexRadioGroup); 
	}
	
	private TextView dofetilideDoseTextView;
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
			int cc = (int) creatinineClearance(isMale, age, weight, creatinine);
			ccTextView.setText("Creatinine Clearance = " + String.valueOf(cc));
			int dose = getDose(cc);
			if (dose == 0) {
				dofetilideDoseTextView.setText("Do not use!");
				dofetilideDoseTextView.setTextColor(Color.RED);
			}
			else {
				dofetilideDoseTextView.setTextColor(Color.LTGRAY);
				dofetilideDoseTextView.setText(String.valueOf(dose) + " mcg BID");
			}
		}
		catch (NumberFormatException e) {	
			dofetilideDoseTextView.setText("Invalid!");
			dofetilideDoseTextView.setTextColor(Color.RED);
		}		
	}		
	

	
	private int getDose(double crClr) {
		if (crClr > 60)
			return 500;
		if (crClr > 40)
			return 250;
		if (crClr > 20)
			return 125;
		return 0;
	}

	private double creatinineClearance(Boolean isMale, double age, double weight,
			double creatinine) {
		double crClr = 0;
		crClr = (140 - age) * weight;
		crClr = crClr / (72 * creatinine);
		if (!isMale)
			crClr = crClr * 0.85;
		return crClr;
	}
	
	
	private void clearEntries() {
		weightEditText.setText(null);
		creatinineEditText.setText(null);
		dofetilideDoseTextView.setText(getString(R.string.dofetilide_result_label));
		dofetilideDoseTextView.setTextColor(Color.LTGRAY);
		weightEditText.requestFocus();
	}
		

}

