package org.epstudios.epmobile;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class CmsIcd extends RiskScore {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cmsicd);

		View calculateButton = findViewById(R.id.calculate_button);
		calculateButton.setOnClickListener(this);
		View clearButton = findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);

		checkBox = new CheckBox[2];

		checkBox[0] = (CheckBox) findViewById(R.id.icd_cardiac_arrest);
		checkBox[1] = (CheckBox) findViewById(R.id.icd_sus_vt);
//	//	checkBox[2] = (CheckBox) findViewById(R.id.age75);
//		checkBox[3] = (CheckBox) findViewById(R.id.diabetes);
//		checkBox[4] = (CheckBox) findViewById(R.id.stroke);
	}

	@Override
	protected void calculateResult() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getResultMessage(int result) {
		return "test";
	}

	@Override
	protected String getDialogTitle() {
		return getString(R.string.icd_calculator_title);
	}

}
