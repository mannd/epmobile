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
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

// Supports both Arruda and modified Arruda algorithms
public class WpwArruda extends LocationAlgorithm implements OnClickListener {
	public final static String AS = "AS";
	public final static String LPL = "LPL";
	public final static String LL = "LL";
	public final static String LAL = "LAL";
	public final static String LP = "LP";
	public final static String PSTA = "PSTA";
	public final static String SUBEPI = "SUBEPI";
	public final static String PSMA = "PSMA";
	public final static String MSTA = "MSTA";
	public final static String RA = "RA";
	public final static String RAL = "RAL";
	public final static String RL = "RL";
	public final static String RP = "RP";
	public final static String RPL = "RPL";

	private Button yesButton;
	private Button noButton;
	protected Button backButton;
	private Button morphologyButton;
	protected TextView stepTextView;
	protected String message;
	protected String location1 = "";
	protected String location2 = "";

	protected boolean modifiedArruda = false;

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

		step1();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(this,
					WpwAlgorithmList.class);
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
		}
	}

	protected void step1() {
		stepTextView.setText(getString(R.string.arruda_step_1));
		backButton.setEnabled(false);
	}

	protected void getYesResult() {
		adjustStepsForward();
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
		adjustStepsForward();
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
		adjustStepsBackward();
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

	protected void showResult() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		message = "";
		location1 = ""; // need to reset locations or they will be "remembered"
						// by map
		location2 = "";
		setMessageAndLocation();
		dialog.setMessage(message);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setTitle(getString(R.string.pathway_location_label));
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
		dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
				getString(R.string.show_map_label),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						showMap();
						resetSteps();
						gotoStep();
					}
				});
		dialog.show();
	}

	protected void setMessageAndLocation() {
		switch (step) {
		case 9:
			message += getString(R.string.lpl_ll_location);
			location1 = LPL;
			location2 = LL;
			break;
		case 10:
			message += getString(R.string.ll_location);
			location1 = LL;
			break;
		case 11:
			message += getString(R.string.lal_location);
			location1 = LAL;
			break;
		case 12:
			message += getString(R.string.lp_psta_location);
			location1 = LP;
			location2 = PSTA;
			break;
		case 4:
			message += getString(R.string.lp_lpl_location);
			location1 = LP;
			location2 = LPL;
			break;
		case 5:
			message += getString(R.string.ll_lal_location);
			location1 = LL;
			location2 = LAL;
			break;
		case 14:
			message += getString(R.string.subepicardial_location);
			location1 = SUBEPI;
			break;
		case 19:
			message += getString(R.string.psta_psma_location);
			location1 = PSTA;
			location2 = PSMA;
			break;
		case 21:
			message += getString(R.string.as_location);
			location1 = AS;
			break;
		case 23:
			message += getString(R.string.psta_location);
			location1 = PSTA;
			break;
		case 22:
			message += getString(R.string.msta_location);
			location1 = MSTA;
			break;
		case 30:
			message += getString(R.string.ra_ral_location);
			location1 = RA;
			location2 = RAL;
			break;
		case 29:
			message += getString(R.string.rl_location);
			location1 = RL;
			break;
		case 28:
			message += getString(R.string.rp_rpl_location);
			location1 = RP;
			location2 = RPL;
			break;
		}
	}

	private void showMap() {
		Intent i = new Intent(this, AvAnnulusMap.class);
		i.putExtra("message", message);
		i.putExtra("location1", location1);
		i.putExtra("location2", location2);
		startActivity(i);
	}
}
