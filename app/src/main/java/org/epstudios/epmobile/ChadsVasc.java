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

import android.widget.CheckBox;

public class ChadsVasc extends RiskScore {
	private boolean isFemale;

	@Override
	protected void setContentView() {
		setContentView(R.layout.chadsvasc);
	}

	@Override
	protected void init() {
		checkBox = new CheckBox[8];

		checkBox[0] = findViewById(R.id.chf);
		checkBox[1] = findViewById(R.id.hypertension);
		checkBox[2] = findViewById(R.id.age75);
		checkBox[3] = findViewById(R.id.diabetes);
		checkBox[4] = findViewById(R.id.stroke);
		checkBox[5] = findViewById(R.id.vascular);
		checkBox[6] = findViewById(R.id.age65);
		checkBox[7] = findViewById(R.id.female);
	}

	@Override
	protected void calculateResult() {
		int result = 0;
		isFemale = false;
		clearSelectedRisks();
		// correct checking both age checkboxes
		if (checkBox[2].isChecked() && checkBox[6].isChecked())
			checkBox[6].setChecked(false);
		for (int i = 0; i < checkBox.length; i++) {
			if (checkBox[i].isChecked()) {
				addSelectedRisk(checkBox[i].getText().toString());
				if (i == 7) {
					isFemale = true;
				}
				if (i == 4 || i == 2) // stroke, age>75 = 2 points
					result = result + 2;
				else
					result++;
			}
		}
		displayResult(getResultMessage(result),
				getString(R.string.chadsvasc_title));
	}

	//  https://www.mdcalc.com/cha2ds2-vasc-score-atrial-fibrillation-stroke-risk
	private String getResultMessage(int result) {
		String message;
		if (result < 1)
			message = getString(R.string.low_chadsvasc_message);
		else if (result == 1) {
            message = getString(R.string.medium_chadsvasc_message);
            if (isFemale) {
                message += " " + getString(R.string.female_only_chadsvasc_message);
            }
			else {
				message += " " + getString(R.string.non_female_chadsvasc_message);
			}
        }
		else
			message = getString(R.string.high_chadsvasc_message);
		String risk = "";
		String neuroRisk = "";
		switch (result) {
		case 0:
			risk = "0.2";
			neuroRisk = "0.3";
			break;
		case 1:
			risk = "0.6";
			neuroRisk = "0.9";
			break;
		case 2:
			risk = "2.2";
			neuroRisk = "2.9";
			break;
		case 3:
			risk = "3.2";
			neuroRisk = "4.6";
			break;
		case 4:
			risk = "4.8";
			neuroRisk = "6.7";
			break;
		case 5:
			risk = "7.2";
			neuroRisk = "10.0";
			break;
		case 6:
			risk = "9.7";
			neuroRisk = "13.6";
			break;
		case 7:
			risk = "11.2";
			neuroRisk = "15.7";
			break;
		case 8:
			risk = "10.8";
			neuroRisk = "15.2";
			break;
		case 9:
			risk = "12.23";
			neuroRisk = "17.4";
			break;
		}
		risk = "Annual ischemic stroke risk is " + risk + "%";
		neuroRisk = "Annual stroke/TIA/peripheral emboli risk is " + neuroRisk + "%";
		message = getRiskLabel() + " score = " + result + "\n" + message + "\n"
				+ risk + "\n" + neuroRisk;
		setResultMessage(message);
		return resultWithShortReference();
	}

	@Override
	protected String getFullReference() {
		return getString(R.string.chads_chadsvasc_full_reference);
	}

	@Override
	protected String getRiskLabel() {
		return getString(R.string.chadsvasc_label);
	}

	@Override
	protected String getShortReference() {
		return getString(R.string.chads_chadsvasc_short_reference);
	}
}
