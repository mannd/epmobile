package org.epstudios.epmobile;

import android.widget.CheckBox;

public class SyncopeSfRule extends SyncopeRiskScore {
	@Override
	protected void setContentView() {
		setContentView(R.layout.syncopesfrule);
	}

	@Override
	protected void calculateResult() {
		int result = 0;
		for (int i = 0; i < checkBox.length; i++) {
			if (checkBox[i].isChecked()) {
				result++;
			}
		}
		displayResult(getResultMessage(result),
				getString(R.string.syncope_sf_rule_title));
	}

	private String getResultMessage(int result) {
		String message;
		if (result < 1)
			message = getString(R.string.no_sf_rule_risk_message);
		else
			message = getString(R.string.high_sf_rule_risk_message);
		message = "SF Rule Score = " + result + "\n" + message + "\n"
				+ getString(R.string.syncope_sf_rule_reference);
		return message;

	}

	@Override
	protected void init() {
		checkBox = new CheckBox[5];

		checkBox[0] = (CheckBox) findViewById(R.id.abnormal_ecg);
		checkBox[1] = (CheckBox) findViewById(R.id.chf);
		checkBox[2] = (CheckBox) findViewById(R.id.sob);
		checkBox[3] = (CheckBox) findViewById(R.id.low_hct);
		checkBox[4] = (CheckBox) findViewById(R.id.low_bp);
	}

}
