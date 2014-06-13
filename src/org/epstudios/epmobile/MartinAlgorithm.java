package org.epstudios.epmobile;

import android.view.View;
import android.widget.CheckBox;

public class MartinAlgorithm extends SyncopeRiskScore {
	final int risk[] = new int[] { 0, 5, 16, 27, 27 };

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
				result++;
			}
		}
		displayResult(getResultMessage(result),
				getString(R.string.syncope_martin_title));
	}

	private String getResultMessage(int result) {
		String message;

		message = getRiskLabel() + " score = " + result + "\n"
				+ getString(R.string.syncope_martin_result_label) + " = "
				+ risk[result] + "%";
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

		checkBox[4].setVisibility(View.GONE);
		checkBox[5].setVisibility(View.GONE);

		checkBox[0].setText(getString(R.string.abnormal_ecg_label));
		checkBox[1].setText(getString(R.string.history_vt_label));
		checkBox[2].setText(getString(R.string.history_chf_label));
		checkBox[3].setText(getString(R.string.age_over_45_label));
	}

	@Override
	protected String getFullReference() {
		return getString(R.string.syncope_martin_full_reference);
	}

	@Override
	protected String getRiskLabel() {
		return getString(R.string.syncope_martin_title);
	}

	@Override
	protected String getShortReference() {
		return getString(R.string.martin_algorithm_reference);
	}
}
