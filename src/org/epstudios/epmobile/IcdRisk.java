package org.epstudios.epmobile;

import android.widget.CheckBox;

public class IcdRisk extends RiskScore {

	@Override
	protected void setContentView() {
		setContentView(R.layout.icdrisk);
	}

	@Override
	protected void calculateResult() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void init() {
		checkBox = new CheckBox[1];

		checkBox[0] = (CheckBox) findViewById(R.id.female_sex);
		// checkBox[1] = (CheckBox) findViewById(R.id.hypertension);
		// checkBox[2] = (CheckBox) findViewById(R.id.age75);
		// checkBox[3] = (CheckBox) findViewById(R.id.diabetes);
		// checkBox[4] = (CheckBox) findViewById(R.id.stroke);
	}

}
