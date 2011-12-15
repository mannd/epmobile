package org.epstudios.epmobile;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class Hemorrhages extends RiskScore {
	final static private int REBLEED = 5; // this is the only risk worth 2
											// points

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hemorrhages);

		View calculateButton = findViewById(R.id.calculate_button);
		calculateButton.setOnClickListener(this);
		View clearButton = findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);

		checkBox = new CheckBox[11];

		checkBox[0] = (CheckBox) findViewById(R.id.hepatic_or_renal_disease);
		checkBox[1] = (CheckBox) findViewById(R.id.etoh);
		checkBox[2] = (CheckBox) findViewById(R.id.malignancy);
		checkBox[3] = (CheckBox) findViewById(R.id.older);
		checkBox[4] = (CheckBox) findViewById(R.id.platelet);
		checkBox[REBLEED] = (CheckBox) findViewById(R.id.rebleeding);
		checkBox[6] = (CheckBox) findViewById(R.id.htn_uncontrolled);
		checkBox[7] = (CheckBox) findViewById(R.id.anemia);
		checkBox[8] = (CheckBox) findViewById(R.id.genetic_factors);
		checkBox[9] = (CheckBox) findViewById(R.id.fall_risk);
		checkBox[10] = (CheckBox) findViewById(R.id.stroke);
	}

	@Override
	protected void calculateResult() {
		int result = 0;
		for (int i = 0; i < checkBox.length; i++) {
			if (checkBox[i].isChecked())
				if (i == REBLEED)
					result += 2;
				else
					result++;
		}
		displayResult(result);
	}

	@Override
	protected String getResultMessage(int result) {
		String message;
		if (result < 2)
			message = getString(R.string.low_risk_hemorrhages);
		else if (result < 4)
			message = getString(R.string.intermediate_risk_hemorrhages);
		else
			// result >= 4
			message = getString(R.string.high_risk_hemorrhages);
		String risk = "";
		switch (result) {
		case 0:
			risk = "1.9";
			break;
		case 1:
			risk = "2.5";
			break;
		case 2:
			risk = "5.3";
			break;
		case 3:
			risk = "8.4";
			break;
		case 4:
			risk = "10.4";
			break;
		}
		if (result >= 5)
			risk = "12.3";
		risk = "Bleeding risk is " + risk + " bleeds per 100 patient-years";
		message = getString(R.string.hemorrhages_title) + " = " + result + "\n"
				+ message + "\n" + risk + "\n"
				+ getString(R.string.hemorrhages_reference);
		return message;
	}

	protected String getDialogTitle() {
		return getString(R.string.hemorrhages_title);
	}

}
