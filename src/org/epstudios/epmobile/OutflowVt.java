/*  EP Mobile -- Mobile tools for electrophysiologists
    Copyright (C) 2012 EP Studios, Inc.
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
import android.widget.Button;
import android.widget.TextView;

public class OutflowVt extends EpActivity implements OnClickListener {
	private Button yesButton;
	private Button noButton;
	protected Button backButton;
	private Button morphologyButton;
	protected TextView stepTextView;

	protected int step = 1;
	private int priorStep = 1;
	private int priorStep1 = 1;
	private int priorStep2 = 1;
	private int priorStep3 = 1;
	private int priorStep4 = 1;
	private int priorStep5 = 1;
	private int priorStep6 = 1;
	private int priorStep7 = 1;

	private boolean isRvot = false;
	private boolean isLvot = false;
	private boolean isIndeterminate = false;
	private boolean isSupravalvular = false;
	private boolean isRvFreeWall = false;
	private boolean isAnterior = false;
	private boolean isCaudal = false;

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
		morphologyButton.setVisibility(View.GONE);
		stepTextView = (TextView) findViewById(R.id.stepTextView);
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

	private void getBackResult() {
		adjustStepsBackward();
		gotoStep();
	}

	private void getNoResult() {
		adjustStepsForward();
		switch (step) {
		case 1:
			step = 6;
			break;
		case 2:
			isRvFreeWall = false;
			step = 3;
			break;
		case 3:
			isAnterior = false;
			step = 4;
			break;
		case 4:
			isCaudal = false;
			step = 5;
			break;
		case 6:
			step = 7;
			break;
		case 7:
			isLvot = true;
			isIndeterminate = true;
			break;
		}
		gotoStep();
	}

	protected void getYesResult() {
		adjustStepsForward();
		switch (step) {
		case 1:
			isRvot = true;
			step = 2;
			break;
		case 2:
			isRvFreeWall = true;
			step = 3;
			break;
		case 3:
			isAnterior = true;
			step = 4;
			break;
		case 4:
			isCaudal = true;
			step = 5;
			break;
		case 6:
			step = 7;
			break;
		case 7:
			isRvot = true;
			isIndeterminate = true;
			step = 2;
			break;
		}
		gotoStep();
	}

	protected void adjustStepsForward() {
		priorStep7 = priorStep6;
		priorStep6 = priorStep5;
		priorStep5 = priorStep4;
		priorStep4 = priorStep3;
		priorStep3 = priorStep2;
		priorStep2 = priorStep1;
		priorStep1 = priorStep;
		priorStep = step;
	}

	protected void adjustStepsBackward() {
		step = priorStep;
		priorStep = priorStep1;
		priorStep1 = priorStep2;
		priorStep2 = priorStep3;
		priorStep3 = priorStep4;
		priorStep4 = priorStep5;
		priorStep5 = priorStep6;
		priorStep6 = priorStep7;
	}

	private void resetSteps() {
		priorStep7 = priorStep6 = priorStep5 = priorStep4 = priorStep3 = priorStep2 = priorStep1 = priorStep = step = 1;
	}

	protected void step1() {
		stepTextView.setText(getString(R.string.outflow_vt_step_1));
		backButton.setEnabled(false);
	}

	protected void gotoStep() {
		switch (step) {
		case 1:
			step1();
			break;
		case 2:
			stepTextView.setText(getString(R.string.outflow_vt_step_2));
			break;
		case 3:
			stepTextView.setText(getString(R.string.outflow_vt_step_3));
			break;
		case 4:
			stepTextView.setText(getString(R.string.outflow_vt_step_4));
			break;
		case 5:
			showResult();
			break;
		case 6:
			stepTextView.setText(getString(R.string.outflow_vt_step_6));
			break;
		case 7:
			stepTextView.setText(getString(R.string.outflow_vt_step_7));
			break;

		}
		if (step != 1)
			backButton.setEnabled(true);
	}

	protected void showResult() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		String message = getMessage();
		dialog.setMessage(message);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setTitle(getString(R.string.outflow_vt_location_label));
		dialog.setButton(DialogInterface.BUTTON_POSITIVE,
				getString(R.string.done_label),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
				getString(R.string.reset_label),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						resetSteps();
						gotoStep();
					}
				});

		dialog.show();
	}

	protected String getMessage() {
		String message = new String();
		message += "Note: Location (RV vs LV) is indeterminate. "
				+ "Results reflect one possible localization.\n";
		if (isRvot) {
			message += "Right Ventricular Outflow Tract";
			message += isRvFreeWall ? "\nFree wall" : "\nSeptal";
			message += isAnterior ? "\nAnterior" : "\nPosterior";
			message += isCaudal ? "\nCaudal (> 2 cm from PV)"
					: "\nCranial (< 2 cm from PV)";
		} else
			message = "Not RVOT";
		return message;
	}
}
