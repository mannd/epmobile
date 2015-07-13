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

public class MitralAnnularVt extends LocationAlgorithm implements
		OnClickListener {
	private Button yesButton;
	private Button noButton;
	protected Button backButton;
	private Button instructionsButton;
	protected TextView stepTextView;

	protected boolean mitralAnnularVt = false;

	private boolean isNotMitralAnnular = false;
	private boolean isAnteroLateral = false;
	private boolean isAnteroMedial = false;
	private boolean isPosterior = false;
	private boolean isPosteroSeptal = false;

	private final int initialStep = 1;
	private final int positiveQrsInferiorLeadsStep = 2;
	private final int notchingRInferiorLeadsStep = 3;
	private final int notchingQInferiorLeadsStep = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.simplealgorithm);
        super.onCreate(savedInstanceState);

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
				getString(R.string.mitral_annular_vt_instructions));
		Linkify.addLinks(message, Linkify.WEB_URLS);
		dialog.setMessage(message);
		dialog.setTitle(getString(R.string.mitral_annular_vt_title));
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
		case initialStep:
			isNotMitralAnnular = true;
			showResult();
			break;
		case positiveQrsInferiorLeadsStep:
			step = notchingQInferiorLeadsStep;
			break;
		case notchingRInferiorLeadsStep:
			isAnteroMedial = true;
			showResult();
			break;
		case notchingQInferiorLeadsStep:
			isPosteroSeptal = true;
			showResult();
			break;
		}
		gotoStep();
	}

	protected void getYesResult() {
		adjustStepsForward();
		switch (step) {
		case initialStep:
			step = positiveQrsInferiorLeadsStep;
			break;
		case positiveQrsInferiorLeadsStep:
			step = notchingRInferiorLeadsStep;
			break;
		case notchingRInferiorLeadsStep:
			isAnteroLateral = true;
			showResult();
			break;
		case notchingQInferiorLeadsStep:
			isPosterior = true;
			showResult();
			break;
		}
		gotoStep();
	}

	private void resetResult() {
		isAnteroLateral = isAnteroMedial = false;
		isNotMitralAnnular = isPosterior = isPosteroSeptal = false;
	}

	protected void step1() {
		stepTextView.setText(getString(R.string.mavt_initial_step));
		backButton.setEnabled(false);
		instructionsButton.setVisibility(View.VISIBLE);
	}

	protected void gotoStep() {
		if (step > 1)
			instructionsButton.setVisibility(View.GONE);
		switch (step) {
		case initialStep:
			step1();
			break;
		case positiveQrsInferiorLeadsStep:
			stepTextView
					.setText(getString(R.string.mavt_positive_qrs_inferior_leads_step));
			break;
		case notchingRInferiorLeadsStep:
			stepTextView
					.setText(getString(R.string.mavt_notching_r_inferior_leads_step));
			break;
		case notchingQInferiorLeadsStep:
			stepTextView
					.setText(getString(R.string.mavt_notching_q_inferior_leads_step));
			break;
		}
		if (step != initialStep)
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
						resetResult();
						gotoStep();
					}
				});

		dialog.show();
	}

	protected String getMessage() {
		String message;
		if (isNotMitralAnnular)
			message = getString(R.string.mavt_not_mitral_annular_label);
		else if (isAnteroLateral)
			message = getString(R.string.mavt_anterolateral_label);
		else if (isAnteroMedial)
			message = getString(R.string.mavt_anteromedial_label);
		else if (isPosterior)
			message = getString(R.string.mavt_posterior_label);
		else if (isPosteroSeptal)
			message = getString(R.string.mavt_posteroseptal_label);
		else
			message = getString(R.string.indeterminate_location);
		return message;
	}
}
