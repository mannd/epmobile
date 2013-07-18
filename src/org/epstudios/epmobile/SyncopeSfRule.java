package org.epstudios.epmobile;

import android.view.View;
import android.widget.CheckBox;

public class SyncopeSfRule extends SyncopeRiskScore {
	@Override
	protected void setContentView() {
		setContentView(R.layout.simplerisk);
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
		message = "SF Rule Score " + (result > 0 ? "\u2265 1." : "= 0.") + "\n"
				+ message + "\n"
				+ getString(R.string.syncope_sf_rule_reference);
		return message;

	}

	@Override
	protected void init() {
		checkBox = new CheckBox[6];

		checkBox[0] = (CheckBox) findViewById(R.id.risk_one);
		checkBox[1] = (CheckBox) findViewById(R.id.risk_two);
		checkBox[2] = (CheckBox) findViewById(R.id.risk_three);
		checkBox[3] = (CheckBox) findViewById(R.id.risk_four);
		checkBox[4] = (CheckBox) findViewById(R.id.risk_five);
		checkBox[5] = (CheckBox) findViewById(R.id.risk_six);

		checkBox[5].setVisibility(View.GONE);

		checkBox[0].setText(getString(R.string.abnormal_ecg_label));
		checkBox[1].setText(getString(R.string.chf_label));
		checkBox[2].setText(getString(R.string.sob_label));
		checkBox[3].setText(getString(R.string.low_hct_label));
		checkBox[4].setText(getString(R.string.low_bp_label));
	}

}
