package org.epstudios.epmobile;

import android.widget.CheckBox;

public class Estes extends RiskScore {
	private final int VOLTAGE = 0;
	private final int STRAIN_WITHOUT_DIG = 1;
	private final int STRAIN_WITH_DIG = 2;
	private final int LAE = 3;
	private final int LAD = 4;
	private final int QRS_DURATION = 5;
	private final int INTRINSICOID = 6;

	@Override
	protected void calculateResult() {
		int result = 0;
		for (int i = 0; i < checkBox.length; i++) {
			if (checkBox[i].isChecked()) {
				switch (i) {
				case VOLTAGE:
				case LAE:
				case STRAIN_WITHOUT_DIG:
					result += 3;
					break;
				case LAD:
					result += 2;
					break;
				case STRAIN_WITH_DIG:
				case QRS_DURATION:
				case INTRINSICOID:
					result++;
				}
			}
		}
		displayResult(getResultMessage(result),
				getString(R.string.estes_criteria_title));
	}

	private String getResultMessage(int result) {
		String message = "Romhilt-Estes Score = " + result + "\n";
		if (result < 4)
			message += getString(R.string.estes_no_lvh_message);
		else if (result == 4)
			message += getString(R.string.estes_probable_lvh_message);
		else if (result > 4)
			message += getString(R.string.estes_definite_lvh_message);
		return message;

	}

	@Override
	protected void setContentView() {
		setContentView(R.layout.estes);

	}

	@Override
	protected void init() {
		checkBox = new CheckBox[7];

		checkBox[0] = (CheckBox) findViewById(R.id.voltage);
		checkBox[1] = (CheckBox) findViewById(R.id.strain_without_dig);
		checkBox[2] = (CheckBox) findViewById(R.id.strain_with_dig);
		checkBox[3] = (CheckBox) findViewById(R.id.lae);
		checkBox[4] = (CheckBox) findViewById(R.id.lad);
		checkBox[5] = (CheckBox) findViewById(R.id.qrs_duration);
		checkBox[6] = (CheckBox) findViewById(R.id.intrinsicoid);

	}

}
