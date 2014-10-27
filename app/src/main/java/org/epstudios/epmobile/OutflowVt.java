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
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class OutflowVt extends LocationAlgorithm implements OnClickListener {
	private Button yesButton;
	private Button noButton;
	protected Button backButton;
	private Button instructionsButton;
	protected TextView stepTextView;

	protected boolean mitralAnnularVt = false;

	private boolean isRvot = false;
	private boolean isLvot = false;
	private boolean isIndeterminate = false;
	private boolean isSupraValvular = false;
	private boolean isRvFreeWall = false;
	private boolean isAnterior = false;
	private boolean isCaudal = false;

	private final int lateTransitionStep = 1;
	private final int freeWallStep = 2;
	private final int anteriorLocationStep = 3;
	private final int caudalLocationStep = 4;
	private final int v3TransitionStep = 6;
	private final int indeterminateLocationStep = 7;
	private final int supraValvularStep = 9;

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
		instructionsButton = (Button) findViewById(R.id.morphology_button);
		instructionsButton.setOnClickListener(this);
		instructionsButton.setText(getString(R.string.instructions_label));
		stepTextView = (TextView) findViewById(R.id.stepTextView);
		step1();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(this, VtList.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
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
		case R.id.morphology_button:
			displayInstructions();
			break;
		}
	}

	private void displayInstructions() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		final SpannableString message = new SpannableString(
				getString(R.string.outflow_vt_instructions));
		Linkify.addLinks(message, Linkify.WEB_URLS);
		dialog.setMessage(message);
		dialog.setTitle(getString(R.string.outflow_tract_vt_title));
		dialog.show();
		((TextView) dialog.findViewById(android.R.id.message))
				.setMovementMethod(LinkMovementMethod.getInstance());
	}

	private void getBackResult() {
		adjustStepsBackward();
		gotoStep();
	}

	private void getNoResult() {
		adjustStepsForward();
		switch (step) {
		case lateTransitionStep:
			step = v3TransitionStep;
			break;
		case freeWallStep:
			isRvFreeWall = false;
			step = anteriorLocationStep;
			break;
		case anteriorLocationStep:
			isAnterior = false;
			step = caudalLocationStep;
			break;
		case caudalLocationStep:
			isCaudal = false;
			showResult();
			break;
		case v3TransitionStep:
			isLvot = true;
			isRvot = false;
			isIndeterminate = false;
			step = supraValvularStep;
			break;
		case indeterminateLocationStep:
			isLvot = true;
			isRvot = false;
			isIndeterminate = true;
			step = supraValvularStep;
			break;

		case supraValvularStep:
			isSupraValvular = false;
			showResult();
		}
		gotoStep();
	}

	protected void getYesResult() {
		adjustStepsForward();
		switch (step) {
		case lateTransitionStep:
			isRvot = true;
			isIndeterminate = false;
			isLvot = false;
			step = freeWallStep;
			break;
		case freeWallStep:
			isRvFreeWall = true;
			step = anteriorLocationStep;
			break;
		case anteriorLocationStep:
			isAnterior = true;
			step = caudalLocationStep;
			break;
		case caudalLocationStep:
			isCaudal = true;
			showResult();
			break;
		case v3TransitionStep:
			step = indeterminateLocationStep;
			break;
		case indeterminateLocationStep:
			isRvot = true;
			isLvot = false;
			isIndeterminate = true;
			isRvFreeWall = false;
			step = anteriorLocationStep;
			break;
		case supraValvularStep:
			isSupraValvular = true;
			showResult();
			break;
		}
		gotoStep();
	}

	private void resetButtons() {
		yesButton.setText(getString(R.string.yes));
		noButton.setText(getString(R.string.no));
	}

	protected void step1() {
		stepTextView
				.setText(getString(R.string.outflow_vt_late_transition_step));
		backButton.setEnabled(false);
		instructionsButton.setVisibility(View.VISIBLE);
	}

	protected void gotoStep() {
		if (step != indeterminateLocationStep)
			resetButtons();
		if (step > 1)
			instructionsButton.setVisibility(View.GONE);
		switch (step) {
		case lateTransitionStep:
			step1();
			break;
		case freeWallStep:
			stepTextView.setText(getString(R.string.outflow_vt_free_wall_step));
			break;
		case anteriorLocationStep:
			stepTextView
					.setText(getString(R.string.outflow_vt_anterior_location_step));
			break;
		case caudalLocationStep:
			stepTextView
					.setText(getString(R.string.outflow_vt_caudal_location_step));
			break;
		case v3TransitionStep:
			stepTextView
					.setText(getString(R.string.outflow_vt_v3_transition_step));
			break;
		case indeterminateLocationStep:
			stepTextView
					.setText(getString(R.string.outflow_vt_indeterminate_location_step));
			yesButton.setText(getString(R.string.rv_label));
			noButton.setText(getString(R.string.lv_label));
			break;
		case supraValvularStep:
			stepTextView
					.setText(getString(R.string.outflow_vt_supravalvular_step));
			break;
		}
		if (step != lateTransitionStep)
			backButton.setEnabled(true);
	}

	protected void showResult() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		String message = getMessage();
		dialog.setMessage(message);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
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
		if (isIndeterminate)
			message += "Note: Location (RV vs LV) is indeterminate. "
					+ "Results reflect one possible localization.\n";
		if (isRvot) {
			message += getString(R.string.rvot_label);
			message += isRvFreeWall ? "\nFree wall" : "\nSeptal";
			message += isAnterior ? "\nAnterior" : "\nPosterior";
			message += isCaudal ? "\nCaudal (> 2 cm from pulmonic valve)"
					: "\nCranial (< 2 cm from pulmonic valve)";
		} else if (isLvot) {
			message += getString(R.string.lvot_label);
			message += isSupraValvular ? getString(R.string.cusp_vt_label)
					: getString(R.string.mitral_annular_vt_label);
		} else
			message = getString(R.string.indeterminate_location);
		return message;
	}
}
