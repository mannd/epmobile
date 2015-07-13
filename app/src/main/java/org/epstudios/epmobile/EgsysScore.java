package org.epstudios.epmobile;

import android.widget.CheckBox;

public class EgsysScore extends SyncopeRiskScore {
	// These scores are from the validation group in the paper.
	// The derivation cohort had slightly different scores.
	final int points[] = new int[] { 4, 3, 3, 2, -1, -1 };

	@Override
	protected void setContentView() {
		setContentView(R.layout.simplerisk);
	}

	@Override
	protected void calculateResult() {
		int result = 0;
		clearSelectedRisks();
		for (int i = 0; i < checkBox.length; i++) {
			if (checkBox[i].isChecked()) {
				addSelectedRisk(checkBox[i].getText().toString());
				result += points[i];
			}
		}
		displayResult(getResultMessage(result),
				getString(R.string.syncope_egsys_score_title));
	}

	private String getResultMessage(int result) {
		String message;
		int mortalityRisk;
		int syncopeRisk = 0;
		if (result < 3) {
			mortalityRisk = 2;
			syncopeRisk = 2;
		} else
			mortalityRisk = 21;
		if (result == 3)
			syncopeRisk = 13;
		if (result == 4)
			syncopeRisk = 33;
		if (result > 4)
			syncopeRisk = 77;

		message = getRiskLabel() + " score = " + result + "\n"
				+ "2-year total mortality = " + mortalityRisk
				+ "%\nCardiac syncope probability = " + syncopeRisk + "%";
		setResultMessage(message);
		return resultWithShortReference();

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

		checkBox[0].setText(getString(R.string.palps_before_syncope_label));
		checkBox[1]
				.setText(getString(R.string.abnormal_ecg_or_heart_disease_label));
		checkBox[2].setText(getString(R.string.syncope_during_effort_label));
		checkBox[3].setText(getString(R.string.syncope_while_supine_label));
		checkBox[4].setText(getString(R.string.autonomic_prodrome_label));
		checkBox[5].setText(getString(R.string.predisposing_factors_label));
	}

	@Override
	protected String getFullReference() {
		return getString(R.string.syncope_egsys_full_reference);
	}

	@Override
	protected String getRiskLabel() {
		return getString(R.string.syncope_egsys_label);
	}

	@Override
	protected String getShortReference() {
		return getString(R.string.egsys_score_reference);
	}
}
