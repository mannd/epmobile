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

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class Hcm extends RiskScore {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	}

	@Override
	protected void setContentView() {
		setContentView(R.layout.hcm);
	}

	@Override
	protected void init() {
		checkBox = new CheckBox[11];

		// super bad
		checkBox[0] = (CheckBox) findViewById(R.id.cardiac_arrest);
		checkBox[1] = (CheckBox) findViewById(R.id.spontaneous_vt);
		// major risks
		checkBox[2] = (CheckBox) findViewById(R.id.family_hx_sd);
		checkBox[3] = (CheckBox) findViewById(R.id.unexplained_syncope);
		checkBox[4] = (CheckBox) findViewById(R.id.lv_thickness);
		checkBox[5] = (CheckBox) findViewById(R.id.abnormal_exercise_bp);
		checkBox[6] = (CheckBox) findViewById(R.id.nonsustained_vt);
		// minor risks
		checkBox[7] = (CheckBox) findViewById(R.id.afb);
		checkBox[8] = (CheckBox) findViewById(R.id.myocardial_ischemia);
		checkBox[9] = (CheckBox) findViewById(R.id.lv_outflow_obstruction);
		checkBox[10] = (CheckBox) findViewById(R.id.high_risk_mutation);
	}

	final int HIGHEST_RISK_SCORE = 100; // can't happen by calculation
	int minor_risks; // need to keep this at class level
	final int firstMajorRisk = 2;
	final int firstMinorRisk = 7;

	@Override
	protected void calculateResult() {
		int result = 0;
		minor_risks = 0;
		clearSelectedRisks();
		if (checkBox[0].isChecked() || checkBox[1].isChecked()) {
			addSelectedRisk(getString(R.string.hcm_combined_sus_va_risk_label));
			result = HIGHEST_RISK_SCORE; // CA or spont VT
		} else {
			for (int i = firstMajorRisk; i < firstMinorRisk; ++i) {
				if (checkBox[i].isChecked()) {
					addSelectedRisk(checkBox[i].getText().toString());
					result++;
				}
			}
			for (int i = firstMinorRisk; i < checkBox.length; ++i) {
				if (checkBox[i].isChecked()) {
					addSelectedRisk(checkBox[i].getText().toString());
					minor_risks++;
				}
			}
		}
		displayResult(getResultMessage(result), getString(R.string.hcm_title));
	}

	private String getResultMessage(int result) {
		String message;
		if (result == HIGHEST_RISK_SCORE)
			message = getString(R.string.hcm_highest_risk_sd_text);
		else {
			message = "Major risks = " + result + "\n";
			message += "Minor risks = " + minor_risks + "\n";
			if (result >= 2)
				message += getString(R.string.hcm_high_risk_text);
			else if (result == 1)
				message += getString(R.string.hcm_intermediate_risk_text);
			else if (result == 0)
				message += getString(R.string.hcm_low_risk_text);
		}
		setResultMessage(message);
		// no short reference added here
		return message;

	}

	@Override
	protected String getFullReference() {
		return getString(R.string.hcm_full_reference);
	}

	@Override
	protected String getRiskLabel() {
		return getString(R.string.hcm_title);
	}

	@Override
	protected String getShortReference() {
		// no short reference given, since it is in layout
		return null;
	}
}
