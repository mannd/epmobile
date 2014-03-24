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
	@Override
	protected void setContentView() {
		setContentView(R.layout.chadsvasc);
	}

	@Override
	protected void init() {
		checkBox = new CheckBox[8];

		checkBox[0] = (CheckBox) findViewById(R.id.chf);
		checkBox[1] = (CheckBox) findViewById(R.id.hypertension);
		checkBox[2] = (CheckBox) findViewById(R.id.age75);
		checkBox[3] = (CheckBox) findViewById(R.id.diabetes);
		checkBox[4] = (CheckBox) findViewById(R.id.stroke);
		checkBox[5] = (CheckBox) findViewById(R.id.vascular);
		checkBox[6] = (CheckBox) findViewById(R.id.age65);
		checkBox[7] = (CheckBox) findViewById(R.id.female);
	}

	@Override
	protected void calculateResult() {
		int result = 0;
		// correct checking both age checkboxes
		if (checkBox[2].isChecked() && checkBox[6].isChecked())
			checkBox[6].setChecked(false);
		for (int i = 0; i < checkBox.length; i++) {
			if (checkBox[i].isChecked()) {
				if (i == 4 || i == 2) // stroke, age>75 = 2 points
					result = result + 2;
				else
					result++;
			}
		}
		displayResult(getResultMessage(result),
				getString(R.string.chadsvasc_title));
	}

	private String getResultMessage(int result) {
		String message;
		if (result < 1)
			message = getString(R.string.low_chadsvasc_message);
		else if (result == 1)
			message = getString(R.string.medium_chadsvasc_message);
		else
			message = getString(R.string.high_chadsvasc_message);
		String risk = "";
		switch (result) {
		case 0:
			risk = "0";
			break;
		case 1:
			risk = "1.3";
			break;
		case 2:
			risk = "2.2";
			break;
		case 3:
			risk = "3.2";
			break;
		case 4:
			risk = "4.0";
			break;
		case 5:
			risk = "6.7";
			break;
		case 6:
			risk = "9.8";
			break;
		case 7:
			risk = "9.6";
			break;
		case 8:
			risk = "6.7";
			break;
		case 9:
			risk = "15.2";
			break;
		}
		risk = "Annual stroke risk is " + risk + "%";
		message = "CHA\u2082DS\u2082-VASc score = " + result + "\n" + message
				+ "\n" + risk
				+ "\nReference: Lip GY et al. Stroke 2010 137:263.";
		return message;
	}

}
