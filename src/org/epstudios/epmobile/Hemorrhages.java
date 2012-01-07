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

public class Hemorrhages extends RiskScore {
	final static private int REBLEED = 5; // this is the only risk worth 2
											// points

	@Override
	protected void setContentView() {
		setContentView(R.layout.hemorrhages);
	}

	@Override
	protected void init() {
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
		displayResult(getResultMessage(result),
				getString(R.string.hemorrhages_title));
	}

	private String getResultMessage(int result) {
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

}
