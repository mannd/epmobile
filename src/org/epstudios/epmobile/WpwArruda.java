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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

// Supports both Arruda and modified Arruda algorithms
public class WpwArruda extends EpActivity implements OnClickListener {
	private Button yesButton;
	private Button noButton;
	private Button backButton;
	private Button morphologyButton;
	private TextView stepTextView;
	private static int step = 1;

	protected final boolean modifiedArruda = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simplealgorithm);

		yesButton = (Button) findViewById(R.id.yes_button);
		yesButton.setOnClickListener(this);
		noButton = (Button) findViewById(R.id.no_button);
		noButton.setOnClickListener(this);
		backButton = (Button) findViewById(R.id.back_button);
		backButton.setOnClickListener(this);
		morphologyButton = (Button) findViewById(R.id.morphology_button);
		morphologyButton.setVisibility(View.GONE); // maybe need to change this
													// to an instructions button
		stepTextView = (TextView) findViewById(R.id.stepTextView);

		step = 1; // needed to reset this when activity starts
		step1();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.yes_button:
			getYesResult();
			break;
		case R.id.no_button:
			getNoResult();
			break;
		case R.id.back_button:
			getBackResult();
			break;
		}
	}

	private void step1() {
		stepTextView.setText(getString(R.string.arruda_step_1));
		backButton.setEnabled(false);
	}

	protected void getYesResult() {
		switch (step) {
		case 1:
			if (modifiedArruda)
				step = 6;
			else
				step = 2;
			break;
		case 2:
			step = 5;
			break;
		case 6:
			step = 12;
			break;
		case 13:
			step = 14;
			break;
		case 15:
			step = 16;
			break;
		case 16:
			step = 23;
			break;
		case 18:
			step = 19;
			break;
		case 20:
			step = 21;
			break;
		case 24:
			step = 30;
			break;
		case 27:
			step = 29;
			break;
		case 80:
			step = 9;
			break;
		case 81:
			step = 10;
			break;
		}
		gotoStep();
	}

	protected void getNoResult() {
		switch (step) {
		case 1:
			step = 13;
			break;
		case 2:
			step = 4;
			break;
		case 6:
			step = 80; // 8a
			break;
		// handle 8 differently
		case 80:
			step = 81;
			break;
		case 81:
			step = 11;
			break;
		case 13:
			step = 15;
			break;
		case 15:
			step = 24;
			break;
		case 16:
			step = 18;
			break;
		case 18:
			step = 20;
			break;
		case 20:
			step = 22;
			break;
		case 24:
			step = 27;
			break;
		case 27:
			step = 28;
			break;
		}
		gotoStep();
	}

	private void getBackResult() {
		switch (step) {
		case 1:
		case 2:
		case 6:
		case 13:
			step = 1;
			break;
		case 7:
			step = 6;
			break;
		case 8:
			step = 7;
			break;
		case 15:
			step = 13;
			break;
		case 16:
		case 24:
			step = 15;
			break;
		case 18:
			step = 16;
			break;
		case 20:
			step = 18;
			break;
		case 27:
			step = 24;
			break;
		}
		gotoStep();
	}

	protected void gotoStep() {
		switch (step) {
		case 1:
			step1();
			break;
		case 2:
			stepTextView.setText(getString(R.string.arruda_step_2_3));
			break;
		case 4:
		case 5:
		case 9:
		case 10:
		case 11:
		case 12:
		case 14:
		case 19:
		case 21:
		case 22:
		case 23:
		case 28:
		case 30:
		case 29:
			showResult();
			break;
		case 6:
			stepTextView.setText(getString(R.string.arruda_step_6_7));
			break;
		case 13:
			stepTextView.setText(getString(R.string.arruda_step_13));
			break;
		case 15:
			stepTextView.setText(getString(R.string.arruda_step_15));
			break;
		case 16:
			stepTextView.setText(getString(R.string.arruda_step_16_17));
			break;
		case 18:
			stepTextView.setText(getString(R.string.arruda_step_18));
			break;
		case 20:
			stepTextView.setText(getString(R.string.arruda_step_20));
			break;
		case 24:
			stepTextView.setText(getString(R.string.arruda_step_24_25_26));
			break;
		case 27:
			stepTextView.setText(getString(R.string.arruda_step_27));
			break;
		case 80: // 8a
			stepTextView.setText(getString(R.string.arruda_step_8a));
			break;
		case 81:
			stepTextView.setText(getString(R.string.arruda_step_8b));
			break;
		}
		if (step != 1)
			backButton.setEnabled(true);
	}

	private void showResult() {
		// display map
		step = 1;
		gotoStep();
	}
}
