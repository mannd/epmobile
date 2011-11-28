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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class ChadsVasc extends EpActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chadsvasc);

		View calculateButton = findViewById(R.id.calculate_button);
		calculateButton.setOnClickListener(this);
		View clearButton = findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);

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

	private CheckBox[] checkBox;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.calculate_button:
			calculateResult();
			break;
		case R.id.clear_button:
			clearEntries();
			break;
		}
	}

	private void calculateResult() {
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
		displayResult(result);
	}

	private void displayResult(int result) {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
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
			risk = "0.7";
			break;
		case 2:
			risk = "1.9";
			break;
		case 3:
			risk = "4.7";
			break;
		case 4:
			risk = "2.3";
			break;
		case 5:
			risk = "3.9";
			break;
		case 6:
			risk = "4.5";
			break;
		case 7:
			risk = "10.1";
			break;
		case 8:
			risk = "14.2";
			break;
		case 9:
			risk = "100";
			break;
		}
		risk = "Annual stroke risk is " + risk + "%";

		dialog.setMessage("CHA\u2082DS\u2082-VASc score = " + result + "\n"
				+ message + "\n" + risk
				+ "\nReference: Gregory YHL et al. CHEST 2010 137:263");
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Reset",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						clearEntries();
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Don't Reset",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		dialog.setTitle(getString(R.string.chadsvasc_title));
		dialog.show();
	}

	private void clearEntries() {
		for (int i = 0; i < checkBox.length; i++)
			checkBox[i].setChecked(false);
	}

}
